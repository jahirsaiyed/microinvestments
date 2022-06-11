import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvestorAccount } from '../investor-account.model';
import { InvestorAccountService } from '../service/investor-account.service';

@Component({
  templateUrl: './investor-account-delete-dialog.component.html',
})
export class InvestorAccountDeleteDialogComponent {
  investorAccount?: IInvestorAccount;

  constructor(protected investorAccountService: InvestorAccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.investorAccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
