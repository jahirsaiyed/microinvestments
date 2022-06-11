import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetDetailsComponent } from '../list/asset-details.component';
import { AssetDetailsDetailComponent } from '../detail/asset-details-detail.component';
import { AssetDetailsUpdateComponent } from '../update/asset-details-update.component';
import { AssetDetailsRoutingResolveService } from './asset-details-routing-resolve.service';

const assetDetailsRoute: Routes = [
  {
    path: '',
    component: AssetDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetDetailsDetailComponent,
    resolve: {
      assetDetails: AssetDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetDetailsUpdateComponent,
    resolve: {
      assetDetails: AssetDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetDetailsUpdateComponent,
    resolve: {
      assetDetails: AssetDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetDetailsRoute)],
  exports: [RouterModule],
})
export class AssetDetailsRoutingModule {}
