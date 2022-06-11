import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetDetailsDetailComponent } from './asset-details-detail.component';

describe('AssetDetails Management Detail Component', () => {
  let comp: AssetDetailsDetailComponent;
  let fixture: ComponentFixture<AssetDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
