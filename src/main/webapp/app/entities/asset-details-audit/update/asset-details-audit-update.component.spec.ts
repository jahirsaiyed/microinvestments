import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssetDetailsAuditService } from '../service/asset-details-audit.service';
import { IAssetDetailsAudit, AssetDetailsAudit } from '../asset-details-audit.model';

import { AssetDetailsAuditUpdateComponent } from './asset-details-audit-update.component';

describe('AssetDetailsAudit Management Update Component', () => {
  let comp: AssetDetailsAuditUpdateComponent;
  let fixture: ComponentFixture<AssetDetailsAuditUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetDetailsAuditService: AssetDetailsAuditService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AssetDetailsAuditUpdateComponent],
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
      .overrideTemplate(AssetDetailsAuditUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetDetailsAuditUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetDetailsAuditService = TestBed.inject(AssetDetailsAuditService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const assetDetailsAudit: IAssetDetailsAudit = { id: 456 };

      activatedRoute.data = of({ assetDetailsAudit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetDetailsAudit));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDetailsAudit>>();
      const assetDetailsAudit = { id: 123 };
      jest.spyOn(assetDetailsAuditService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDetailsAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetDetailsAudit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetDetailsAuditService.update).toHaveBeenCalledWith(assetDetailsAudit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDetailsAudit>>();
      const assetDetailsAudit = new AssetDetailsAudit();
      jest.spyOn(assetDetailsAuditService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDetailsAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetDetailsAudit }));
      saveSubject.complete();

      // THEN
      expect(assetDetailsAuditService.create).toHaveBeenCalledWith(assetDetailsAudit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetDetailsAudit>>();
      const assetDetailsAudit = { id: 123 };
      jest.spyOn(assetDetailsAuditService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetDetailsAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetDetailsAuditService.update).toHaveBeenCalledWith(assetDetailsAudit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
