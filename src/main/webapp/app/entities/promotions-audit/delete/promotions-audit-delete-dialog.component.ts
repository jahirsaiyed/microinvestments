import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPromotionsAudit } from '../promotions-audit.model';
import { PromotionsAuditService } from '../service/promotions-audit.service';

@Component({
  templateUrl: './promotions-audit-delete-dialog.component.html',
})
export class PromotionsAuditDeleteDialogComponent {
  promotionsAudit?: IPromotionsAudit;

  constructor(protected promotionsAuditService: PromotionsAuditService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.promotionsAuditService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
