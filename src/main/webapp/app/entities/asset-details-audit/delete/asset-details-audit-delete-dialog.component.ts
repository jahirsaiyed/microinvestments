import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetDetailsAudit } from '../asset-details-audit.model';
import { AssetDetailsAuditService } from '../service/asset-details-audit.service';

@Component({
  templateUrl: './asset-details-audit-delete-dialog.component.html',
})
export class AssetDetailsAuditDeleteDialogComponent {
  assetDetailsAudit?: IAssetDetailsAudit;

  constructor(protected assetDetailsAuditService: AssetDetailsAuditService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetDetailsAuditService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
