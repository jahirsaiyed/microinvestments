import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PromotionsAuditComponent } from '../list/promotions-audit.component';
import { PromotionsAuditDetailComponent } from '../detail/promotions-audit-detail.component';
import { PromotionsAuditUpdateComponent } from '../update/promotions-audit-update.component';
import { PromotionsAuditRoutingResolveService } from './promotions-audit-routing-resolve.service';

const promotionsAuditRoute: Routes = [
  {
    path: '',
    component: PromotionsAuditComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PromotionsAuditDetailComponent,
    resolve: {
      promotionsAudit: PromotionsAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PromotionsAuditUpdateComponent,
    resolve: {
      promotionsAudit: PromotionsAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PromotionsAuditUpdateComponent,
    resolve: {
      promotionsAudit: PromotionsAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(promotionsAuditRoute)],
  exports: [RouterModule],
})
export class PromotionsAuditRoutingModule {}
