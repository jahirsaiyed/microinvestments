import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetDetailsAuditComponent } from './list/asset-details-audit.component';
import { AssetDetailsAuditDetailComponent } from './detail/asset-details-audit-detail.component';
import { AssetDetailsAuditUpdateComponent } from './update/asset-details-audit-update.component';
import { AssetDetailsAuditDeleteDialogComponent } from './delete/asset-details-audit-delete-dialog.component';
import { AssetDetailsAuditRoutingModule } from './route/asset-details-audit-routing.module';

@NgModule({
  imports: [SharedModule, AssetDetailsAuditRoutingModule],
  declarations: [
    AssetDetailsAuditComponent,
    AssetDetailsAuditDetailComponent,
    AssetDetailsAuditUpdateComponent,
    AssetDetailsAuditDeleteDialogComponent,
  ],
  entryComponents: [AssetDetailsAuditDeleteDialogComponent],
})
export class AssetDetailsAuditModule {}
