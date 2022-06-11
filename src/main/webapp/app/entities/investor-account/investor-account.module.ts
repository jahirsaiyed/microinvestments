import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvestorAccountComponent } from './list/investor-account.component';
import { InvestorAccountDetailComponent } from './detail/investor-account-detail.component';
import { InvestorAccountUpdateComponent } from './update/investor-account-update.component';
import { InvestorAccountDeleteDialogComponent } from './delete/investor-account-delete-dialog.component';
import { InvestorAccountRoutingModule } from './route/investor-account-routing.module';

@NgModule({
  imports: [SharedModule, InvestorAccountRoutingModule],
  declarations: [
    InvestorAccountComponent,
    InvestorAccountDetailComponent,
    InvestorAccountUpdateComponent,
    InvestorAccountDeleteDialogComponent,
  ],
  entryComponents: [InvestorAccountDeleteDialogComponent],
})
export class InvestorAccountModule {}
