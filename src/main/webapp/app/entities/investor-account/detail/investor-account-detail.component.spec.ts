import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvestorAccountDetailComponent } from './investor-account-detail.component';

describe('InvestorAccount Management Detail Component', () => {
  let comp: InvestorAccountDetailComponent;
  let fixture: ComponentFixture<InvestorAccountDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvestorAccountDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ investorAccount: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InvestorAccountDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InvestorAccountDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load investorAccount on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.investorAccount).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
