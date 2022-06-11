import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITransaction, Transaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { IInvestor } from 'app/entities/investor/investor.model';
import { InvestorService } from 'app/entities/investor/service/investor.service';
import { TRANSACTIONTYPE } from 'app/entities/enumerations/transactiontype.model';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  tRANSACTIONTYPEValues = Object.keys(TRANSACTIONTYPE);

  investorsSharedCollection: IInvestor[] = [];

  editForm = this.fb.group({
    id: [],
    type: [],
    amount: [],
    units: [],
    unitPrice: [],
    createdOn: [],
    updatedOn: [],
    investor: [],
  });

  constructor(
    protected transactionService: TransactionService,
    protected investorService: InvestorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      if (transaction.id === undefined) {
        const today = dayjs().startOf('day');
        transaction.createdOn = today;
        transaction.updatedOn = today;
      }

      this.updateForm(transaction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  trackInvestorById(_index: number, item: IInvestor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      type: transaction.type,
      amount: transaction.amount,
      units: transaction.units,
      unitPrice: transaction.unitPrice,
      createdOn: transaction.createdOn ? transaction.createdOn.format(DATE_TIME_FORMAT) : null,
      updatedOn: transaction.updatedOn ? transaction.updatedOn.format(DATE_TIME_FORMAT) : null,
      investor: transaction.investor,
    });

    this.investorsSharedCollection = this.investorService.addInvestorToCollectionIfMissing(
      this.investorsSharedCollection,
      transaction.investor
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

  protected createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      units: this.editForm.get(['units'])!.value,
      unitPrice: this.editForm.get(['unitPrice'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedOn: this.editForm.get(['updatedOn'])!.value ? dayjs(this.editForm.get(['updatedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      investor: this.editForm.get(['investor'])!.value,
    };
  }
}
