import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPromotions } from '../promotions.model';
import { PromotionsService } from '../service/promotions.service';

@Component({
  templateUrl: './promotions-delete-dialog.component.html',
})
export class PromotionsDeleteDialogComponent {
  promotions?: IPromotions;

  constructor(protected promotionsService: PromotionsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.promotionsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
