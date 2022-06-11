import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'investor',
        data: { pageTitle: 'microApp.investor.home.title' },
        loadChildren: () => import('./investor/investor.module').then(m => m.InvestorModule),
      },
      {
        path: 'investor-account',
        data: { pageTitle: 'microApp.investorAccount.home.title' },
        loadChildren: () => import('./investor-account/investor-account.module').then(m => m.InvestorAccountModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'microApp.transaction.home.title' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      {
        path: 'investor-portfolio',
        data: { pageTitle: 'microApp.investorPortfolio.home.title' },
        loadChildren: () => import('./investor-portfolio/investor-portfolio.module').then(m => m.InvestorPortfolioModule),
      },
      {
        path: 'asset-details',
        data: { pageTitle: 'microApp.assetDetails.home.title' },
        loadChildren: () => import('./asset-details/asset-details.module').then(m => m.AssetDetailsModule),
      },
      {
        path: 'promotions',
        data: { pageTitle: 'microApp.promotions.home.title' },
        loadChildren: () => import('./promotions/promotions.module').then(m => m.PromotionsModule),
      },
      {
        path: 'promotions-audit',
        data: { pageTitle: 'microApp.promotionsAudit.home.title' },
        loadChildren: () => import('./promotions-audit/promotions-audit.module').then(m => m.PromotionsAuditModule),
      },
      {
        path: 'asset-details-audit',
        data: { pageTitle: 'microApp.assetDetailsAudit.home.title' },
        loadChildren: () => import('./asset-details-audit/asset-details-audit.module').then(m => m.AssetDetailsAuditModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
