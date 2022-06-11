import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssetDetails } from '../asset-details.model';

@Component({
  selector: 'jhi-asset-details-detail',
  templateUrl: './asset-details-detail.component.html',
})
export class AssetDetailsDetailComponent implements OnInit {
  assetDetails: IAssetDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetDetails }) => {
      this.assetDetails = assetDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
