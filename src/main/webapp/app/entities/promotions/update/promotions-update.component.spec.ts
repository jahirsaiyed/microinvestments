import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PromotionsService } from '../service/promotions.service';
import { IPromotions, Promotions } from '../promotions.model';

import { PromotionsUpdateComponent } from './promotions-update.component';

describe('Promotions Management Update Component', () => {
  let comp: PromotionsUpdateComponent;
  let fixture: ComponentFixture<PromotionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let promotionsService: PromotionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PromotionsUpdateComponent],
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
      .overrideTemplate(PromotionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PromotionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    promotionsService = TestBed.inject(PromotionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const promotions: IPromotions = { id: 456 };

      activatedRoute.data = of({ promotions });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(promotions));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promotions>>();
      const promotions = { id: 123 };
      jest.spyOn(promotionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promotions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promotions }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(promotionsService.update).toHaveBeenCalledWith(promotions);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promotions>>();
      const promotions = new Promotions();
      jest.spyOn(promotionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promotions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promotions }));
      saveSubject.complete();

      // THEN
      expect(promotionsService.create).toHaveBeenCalledWith(promotions);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promotions>>();
      const promotions = { id: 123 };
      jest.spyOn(promotionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promotions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(promotionsService.update).toHaveBeenCalledWith(promotions);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
