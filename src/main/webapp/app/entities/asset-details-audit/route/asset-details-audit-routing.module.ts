import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetDetailsAuditComponent } from '../list/asset-details-audit.component';
import { AssetDetailsAuditDetailComponent } from '../detail/asset-details-audit-detail.component';
import { AssetDetailsAuditUpdateComponent } from '../update/asset-details-audit-update.component';
import { AssetDetailsAuditRoutingResolveService } from './asset-details-audit-routing-resolve.service';

const assetDetailsAuditRoute: Routes = [
  {
    path: '',
    component: AssetDetailsAuditComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetDetailsAuditDetailComponent,
    resolve: {
      assetDetailsAudit: AssetDetailsAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetDetailsAuditUpdateComponent,
    resolve: {
      assetDetailsAudit: AssetDetailsAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetDetailsAuditUpdateComponent,
    resolve: {
      assetDetailsAudit: AssetDetailsAuditRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetDetailsAuditRoute)],
  exports: [RouterModule],
})
export class AssetDetailsAuditRoutingModule {}
