import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetDetails } from '../asset-details.model';
import { AssetDetailsService } from '../service/asset-details.service';

@Component({
  templateUrl: './asset-details-delete-dialog.component.html',
})
export class AssetDetailsDeleteDialogComponent {
  assetDetails?: IAssetDetails;

  constructor(protected assetDetailsService: AssetDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
