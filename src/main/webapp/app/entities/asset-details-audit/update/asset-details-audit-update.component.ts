import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAssetDetailsAudit, AssetDetailsAudit } from '../asset-details-audit.model';
import { AssetDetailsAuditService } from '../service/asset-details-audit.service';

@Component({
  selector: 'jhi-asset-details-audit-update',
  templateUrl: './asset-details-audit-update.component.html',
})
export class AssetDetailsAuditUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    units: [],
    unitPrice: [],
    balance: [],
    currentInvestedAmount: [],
    profitLoss: [],
    createdOn: [],
    updatedOn: [],
  });

  constructor(
    protected assetDetailsAuditService: AssetDetailsAuditService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetDetailsAudit }) => {
      if (assetDetailsAudit.id === undefined) {
        const today = dayjs().startOf('day');
        assetDetailsAudit.createdOn = today;
        assetDetailsAudit.updatedOn = today;
      }

      this.updateForm(assetDetailsAudit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetDetailsAudit = this.createFromForm();
    if (assetDetailsAudit.id !== undefined) {
      this.subscribeToSaveResponse(this.assetDetailsAuditService.update(assetDetailsAudit));
    } else {
      this.subscribeToSaveResponse(this.assetDetailsAuditService.create(assetDetailsAudit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetDetailsAudit>>): void {
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

  protected updateForm(assetDetailsAudit: IAssetDetailsAudit): void {
    this.editForm.patchValue({
      id: assetDetailsAudit.id,
      units: assetDetailsAudit.units,
      unitPrice: assetDetailsAudit.unitPrice,
      balance: assetDetailsAudit.balance,
      currentInvestedAmount: assetDetailsAudit.currentInvestedAmount,
      profitLoss: assetDetailsAudit.profitLoss,
      createdOn: assetDetailsAudit.createdOn ? assetDetailsAudit.createdOn.format(DATE_TIME_FORMAT) : null,
      updatedOn: assetDetailsAudit.updatedOn ? assetDetailsAudit.updatedOn.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IAssetDetailsAudit {
    return {
      ...new AssetDetailsAudit(),
      id: this.editForm.get(['id'])!.value,
      units: this.editForm.get(['units'])!.value,
      unitPrice: this.editForm.get(['unitPrice'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      currentInvestedAmount: this.editForm.get(['currentInvestedAmount'])!.value,
      profitLoss: this.editForm.get(['profitLoss'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedOn: this.editForm.get(['updatedOn'])!.value ? dayjs(this.editForm.get(['updatedOn'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
