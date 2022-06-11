import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PromotionsAuditComponent } from './list/promotions-audit.component';
import { PromotionsAuditDetailComponent } from './detail/promotions-audit-detail.component';
import { PromotionsAuditUpdateComponent } from './update/promotions-audit-update.component';
import { PromotionsAuditDeleteDialogComponent } from './delete/promotions-audit-delete-dialog.component';
import { PromotionsAuditRoutingModule } from './route/promotions-audit-routing.module';

@NgModule({
  imports: [SharedModule, PromotionsAuditRoutingModule],
  declarations: [
    PromotionsAuditComponent,
    PromotionsAuditDetailComponent,
    PromotionsAuditUpdateComponent,
    PromotionsAuditDeleteDialogComponent,
  ],
  entryComponents: [PromotionsAuditDeleteDialogComponent],
})
export class PromotionsAuditModule {}
