import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInvestorPortfolio, InvestorPortfolio } from '../investor-portfolio.model';
import { InvestorPortfolioService } from '../service/investor-portfolio.service';

@Component({
  selector: 'jhi-investor-portfolio-update',
  templateUrl: './investor-portfolio-update.component.html',
})
export class InvestorPortfolioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    units: [],
    currentUnitPrice: [],
    balance: [],
    currentInvestedAmount: [],
    createdOn: [],
    updatedOn: [],
  });

  constructor(
    protected investorPortfolioService: InvestorPortfolioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ investorPortfolio }) => {
      if (investorPortfolio.id === undefined) {
        const today = dayjs().startOf('day');
        investorPortfolio.createdOn = today;
        investorPortfolio.updatedOn = today;
      }

      this.updateForm(investorPortfolio);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const investorPortfolio = this.createFromForm();
    if (investorPortfolio.id !== undefined) {
      this.subscribeToSaveResponse(this.investorPortfolioService.update(investorPortfolio));
    } else {
      this.subscribeToSaveResponse(this.investorPortfolioService.create(investorPortfolio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvestorPortfolio>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(investorPortfolio: IInvestorPortfolio): void {
    this.editForm.patchValue({
      id: investorPortfolio.id,
      units: investorPortfolio.units,
      currentUnitPrice: investorPortfolio.currentUnitPrice,
      balance: investorPortfolio.balance,
      currentInvestedAmount: investorPortfolio.currentInvestedAmount,
      createdOn: investorPortfolio.createdOn ? investorPortfolio.createdOn.format(DATE_TIME_FORMAT) : null,
      updatedOn: investorPortfolio.updatedOn ? investorPortfolio.updatedOn.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IInvestorPortfolio {
    return {
      ...new InvestorPortfolio(),
      id: this.editForm.get(['id'])!.value,
      units: this.editForm.get(['units'])!.value,
      currentUnitPrice: this.editForm.get(['currentUnitPrice'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      currentInvestedAmount: this.editForm.get(['currentInvestedAmount'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedOn: this.editForm.get(['updatedOn'])!.value ? dayjs(this.editForm.get(['updatedOn'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
