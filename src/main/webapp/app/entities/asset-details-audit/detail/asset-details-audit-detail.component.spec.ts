import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetDetailsAuditDetailComponent } from './asset-details-audit-detail.component';

describe('AssetDetailsAudit Management Detail Component', () => {
  let comp: AssetDetailsAuditDetailComponent;
  let fixture: ComponentFixture<AssetDetailsAuditDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetDetailsAuditDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetDetailsAudit: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetDetailsAuditDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetDetailsAuditDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetDetailsAudit on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetDetailsAudit).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
