import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PromotionsDetailComponent } from './promotions-detail.component';

describe('Promotions Management Detail Component', () => {
  let comp: PromotionsDetailComponent;
  let fixture: ComponentFixture<PromotionsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PromotionsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ promotions: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PromotionsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PromotionsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load promotions on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.promotions).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
