import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetDetailsComponent } from './list/asset-details.component';
import { AssetDetailsDetailComponent } from './detail/asset-details-detail.component';
import { AssetDetailsUpdateComponent } from './update/asset-details-update.component';
import { AssetDetailsDeleteDialogComponent } from './delete/asset-details-delete-dialog.component';
import { AssetDetailsRoutingModule } from './route/asset-details-routing.module';

@NgModule({
  imports: [SharedModule, AssetDetailsRoutingModule],
  declarations: [AssetDetailsComponent, AssetDetailsDetailComponent, AssetDetailsUpdateComponent, AssetDetailsDeleteDialogComponent],
  entryComponents: [AssetDetailsDeleteDialogComponent],
})
export class AssetDetailsModule {}
