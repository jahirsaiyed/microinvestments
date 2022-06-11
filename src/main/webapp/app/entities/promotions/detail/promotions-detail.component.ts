import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPromotions } from '../promotions.model';

@Component({
  selector: 'jhi-promotions-detail',
  templateUrl: './promotions-detail.component.html',
})
export class PromotionsDetailComponent implements OnInit {
  promotions: IPromotions | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promotions }) => {
      this.promotions = promotions;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
