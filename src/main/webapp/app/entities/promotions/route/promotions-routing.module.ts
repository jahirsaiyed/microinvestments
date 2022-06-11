import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PromotionsComponent } from '../list/promotions.component';
import { PromotionsDetailComponent } from '../detail/promotions-detail.component';
import { PromotionsUpdateComponent } from '../update/promotions-update.component';
import { PromotionsRoutingResolveService } from './promotions-routing-resolve.service';

const promotionsRoute: Routes = [
  {
    path: '',
    component: PromotionsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PromotionsDetailComponent,
    resolve: {
      promotions: PromotionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PromotionsUpdateComponent,
    resolve: {
      promotions: PromotionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PromotionsUpdateComponent,
    resolve: {
      promotions: PromotionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(promotionsRoute)],
  exports: [RouterModule],
})
export class PromotionsRoutingModule {}
