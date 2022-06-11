import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PromotionsComponent } from './list/promotions.component';
import { PromotionsDetailComponent } from './detail/promotions-detail.component';
import { PromotionsUpdateComponent } from './update/promotions-update.component';
import { PromotionsDeleteDialogComponent } from './delete/promotions-delete-dialog.component';
import { PromotionsRoutingModule } from './route/promotions-routing.module';

@NgModule({
  imports: [SharedModule, PromotionsRoutingModule],
  declarations: [PromotionsComponent, PromotionsDetailComponent, PromotionsUpdateComponent, PromotionsDeleteDialogComponent],
  entryComponents: [PromotionsDeleteDialogComponent],
})
export class PromotionsModule {}
