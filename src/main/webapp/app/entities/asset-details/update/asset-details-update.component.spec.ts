import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssetDetailsService } from '../service/asset-details.service';
import { IAssetDetails, AssetDetails } from '../asset-details.model';

import { AssetDetailsUpdateComponent } from './asset-details-update.component';

describe('AssetDetails Management Update Component', () => {
  let comp: AssetDetailsUpdateComponent;
  let fixture: ComponentFixture<AssetDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetDetailsService: AssetDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssetDetailsUpdateComponent],
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
      .overrideTemplate(AssetDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetDetailsService = TestBed.inject(AssetDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const assetDetails: IAssetDetails = { id: 456 };

      activatedRoute.data = of({ assetDetails });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetDetails));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDetails>>();
      const assetDetails = { id: 123 };
      jest.spyOn(assetDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetDetails }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetDetailsService.update).toHaveBeenCalledWith(assetDetails);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDetails>>();
      const assetDetails = new AssetDetails();
      jest.spyOn(assetDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetDetails }));
      saveSubject.complete();

      // THEN
      expect(assetDetailsService.create).toHaveBeenCalledWith(assetDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDetails>>();
      const assetDetails = { id: 123 };
      jest.spyOn(assetDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetDetailsService.update).toHaveBeenCalledWith(assetDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
