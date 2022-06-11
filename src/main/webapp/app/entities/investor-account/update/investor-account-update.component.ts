import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInvestorAccount, InvestorAccount } from '../investor-account.model';
import { InvestorAccountService } from '../service/investor-account.service';
import { IInvestor } from 'app/entities/investor/investor.model';
import { InvestorService } from 'app/entities/investor/service/investor.service';

@Component({
  selector: 'jhi-investor-account-update',
  templateUrl: './investor-account-update.component.html',
})
export class InvestorAccountUpdateComponent implements OnInit {
  isSaving = false;

  investorsSharedCollection: IInvestor[] = [];

  editForm = this.fb.group({
    id: [],
    accountNo: [],
    iBAN: [],
    type: [],
    walletAddress: [],
    walletNetwork: [],
    investor: [],
  });

  constructor(
    protected investorAccountService: InvestorAccountService,
    protected investorService: InvestorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ investorAccount }) => {
      this.updateForm(investorAccount);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const investorAccount = this.createFromForm();
    if (investorAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.investorAccountService.update(investorAccount));
    } else {
      this.subscribeToSaveResponse(this.investorAccountService.create(investorAccount));
    }
  }

  trackInvestorById(_index: number, item: IInvestor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvestorAccount>>): void {
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

  protected updateForm(investorAccount: IInvestorAccount): void {
    this.editForm.patchValue({
      id: investorAccount.id,
      accountNo: investorAccount.accountNo,
      iBAN: investorAccount.iBAN,
      type: investorAccount.type,
      walletAddress: investorAccount.walletAddress,
      walletNetwork: investorAccount.walletNetwork,
      investor: investorAccount.investor,
    });

    this.investorsSharedCollection = this.investorService.addInvestorToCollectionIfMissing(
      this.investorsSharedCollection,
      investorAccount.investor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.investorService
      .query()
      .pipe(map((res: HttpResponse<IInvestor[]>) => res.body ?? []))
      .pipe(
        map((investors: IInvestor[]) =>
          this.investorService.addInvestorToCollectionIfMissing(investors, this.editForm.get('investor')!.value)
        )
      )
      .subscribe((investors: IInvestor[]) => (this.investorsSharedCollection = investors));
  }

  protected createFromForm(): IInvestorAccount {
    return {
      ...new InvestorAccount(),
      id: this.editForm.get(['id'])!.value,
      accountNo: this.editForm.get(['accountNo'])!.value,
      iBAN: this.editForm.get(['iBAN'])!.value,
      type: this.editForm.get(['type'])!.value,
      walletAddress: this.editForm.get(['walletAddress'])!.value,
      walletNetwork: this.editForm.get(['walletNetwork'])!.value,
      investor: this.editForm.get(['investor'])!.value,
    };
  }
}
