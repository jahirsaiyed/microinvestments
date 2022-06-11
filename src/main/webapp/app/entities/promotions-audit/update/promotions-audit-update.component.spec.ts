import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PromotionsAuditService } from '../service/promotions-audit.service';
import { IPromotionsAudit, PromotionsAudit } from '../promotions-audit.model';

import { PromotionsAuditUpdateComponent } from './promotions-audit-update.component';

describe('PromotionsAudit Management Update Component', () => {
  let comp: PromotionsAuditUpdateComponent;
  let fixture: ComponentFixture<PromotionsAuditUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let promotionsAuditService: PromotionsAuditService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PromotionsAuditUpdateComponent],
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
      .overrideTemplate(PromotionsAuditUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PromotionsAuditUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    promotionsAuditService = TestBed.inject(PromotionsAuditService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const promotionsAudit: IPromotionsAudit = { id: 456 };

      activatedRoute.data = of({ promotionsAudit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(promotionsAudit));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PromotionsAudit>>();
      const promotionsAudit = { id: 123 };
      jest.spyOn(promotionsAuditService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promotionsAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promotionsAudit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(promotionsAuditService.update).toHaveBeenCalledWith(promotionsAudit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PromotionsAudit>>();
      const promotionsAudit = new PromotionsAudit();
      jest.spyOn(promotionsAuditService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promotionsAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promotionsAudit }));
      saveSubject.complete();

      // THEN
      expect(promotionsAuditService.create).toHaveBeenCalledWith(promotionsAudit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PromotionsAudit>>();
      const promotionsAudit = { id: 123 };
      jest.spyOn(promotionsAuditService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promotionsAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(promotionsAuditService.update).toHaveBeenCalledWith(promotionsAudit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
