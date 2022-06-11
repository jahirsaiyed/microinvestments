import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvestorAccountService } from '../service/investor-account.service';
import { IInvestorAccount, InvestorAccount } from '../investor-account.model';
import { IInvestor } from 'app/entities/investor/investor.model';
import { InvestorService } from 'app/entities/investor/service/investor.service';

import { InvestorAccountUpdateComponent } from './investor-account-update.component';

describe('InvestorAccount Management Update Component', () => {
  let comp: InvestorAccountUpdateComponent;
  let fixture: ComponentFixture<InvestorAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let investorAccountService: InvestorAccountService;
  let investorService: InvestorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvestorAccountUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InvestorAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvestorAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    investorAccountService = TestBed.inject(InvestorAccountService);
    investorService = TestBed.inject(InvestorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Investor query and add missing value', () => {
      const investorAccount: IInvestorAccount = { id: 456 };
      const investor: IInvestor = { id: 897 };
      investorAccount.investor = investor;

      const investorCollection: IInvestor[] = [{ id: 16522 }];
      jest.spyOn(investorService, 'query').mockReturnValue(of(new HttpResponse({ body: investorCollection })));
      const additionalInvestors = [investor];
      const expectedCollection: IInvestor[] = [...additionalInvestors, ...investorCollection];
      jest.spyOn(investorService, 'addInvestorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ investorAccount });
      comp.ngOnInit();

      expect(investorService.query).toHaveBeenCalled();
      expect(investorService.addInvestorToCollectionIfMissing).toHaveBeenCalledWith(investorCollection, ...additionalInvestors);
      expect(comp.investorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const investorAccount: IInvestorAccount = { id: 456 };
      const investor: IInvestor = { id: 42252 };
      investorAccount.investor = investor;

      activatedRoute.data = of({ investorAccount });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(investorAccount));
      expect(comp.investorsSharedCollection).toContain(investor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvestorAccount>>();
      const investorAccount = { id: 123 };
      jest.spyOn(investorAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investorAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: investorAccount }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(investorAccountService.update).toHaveBeenCalledWith(investorAccount);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvestorAccount>>();
      const investorAccount = new InvestorAccount();
      jest.spyOn(investorAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investorAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: investorAccount }));
      saveSubject.complete();

      // THEN
      expect(investorAccountService.create).toHaveBeenCalledWith(investorAccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvestorAccount>>();
      const investorAccount = { id: 123 };
      jest.spyOn(investorAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investorAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(investorAccountService.update).toHaveBeenCalledWith(investorAccount);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInvestorById', () => {
      it('Should return tracked Investor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInvestorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
