import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetDetailsAudit } from '../asset-details-audit.model';

@Component({
  selector: 'jhi-asset-details-audit-detail',
  templateUrl: './asset-details-audit-detail.component.html',
})
export class AssetDetailsAuditDetailComponent implements OnInit {
  assetDetailsAudit: IAssetDetailsAudit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetDetailsAudit }) => {
      this.assetDetailsAudit = assetDetailsAudit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
