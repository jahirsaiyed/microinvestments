import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvestorAccountComponent } from '../list/investor-account.component';
import { InvestorAccountDetailComponent } from '../detail/investor-account-detail.component';
import { InvestorAccountUpdateComponent } from '../update/investor-account-update.component';
import { InvestorAccountRoutingResolveService } from './investor-account-routing-resolve.service';

const investorAccountRoute: Routes = [
  {
    path: '',
    component: InvestorAccountComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvestorAccountDetailComponent,
    resolve: {
      investorAccount: InvestorAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvestorAccountUpdateComponent,
    resolve: {
      investorAccount: InvestorAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvestorAccountUpdateComponent,
    resolve: {
      investorAccount: InvestorAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(investorAccountRoute)],
  exports: [RouterModule],
})
export class InvestorAccountRoutingModule {}
