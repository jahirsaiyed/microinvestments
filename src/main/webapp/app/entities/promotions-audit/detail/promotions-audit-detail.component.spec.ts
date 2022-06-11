import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PromotionsAuditDetailComponent } from './promotions-audit-detail.component';

describe('PromotionsAudit Management Detail Component', () => {
  let comp: PromotionsAuditDetailComponent;
  let fixture: ComponentFixture<PromotionsAuditDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PromotionsAuditDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ promotionsAudit: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PromotionsAuditDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PromotionsAuditDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load promotionsAudit on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.promotionsAudit).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
