import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvestorAccount } from '../investor-account.model';

@Component({
  selector: 'jhi-investor-account-detail',
  templateUrl: './investor-account-detail.component.html',
})
export class InvestorAccountDetailComponent implements OnInit {
  investorAccount: IInvestorAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ investorAccount }) => {
      this.investorAccount = investorAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
