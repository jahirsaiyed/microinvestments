import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPromotionsAudit } from '../promotions-audit.model';

@Component({
  selector: 'jhi-promotions-audit-detail',
  templateUrl: './promotions-audit-detail.component.html',
})
export class PromotionsAuditDetailComponent implements OnInit {
  promotionsAudit: IPromotionsAudit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promotionsAudit }) => {
      this.promotionsAudit = promotionsAudit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
