import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAssetDetails, AssetDetails } from '../asset-details.model';
import { AssetDetailsService } from '../service/asset-details.service';

@Component({
  selector: 'jhi-asset-details-update',
  templateUrl: './asset-details-update.component.html',
})
export class AssetDetailsUpdateComponent implements OnInit {
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

  constructor(protected assetDetailsService: AssetDetailsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetDetails }) => {
      if (assetDetails.id === undefined) {
        const today = dayjs().startOf('day');
        assetDetails.createdOn = today;
        assetDetails.updatedOn = today;
      }

      this.updateForm(assetDetails);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetDetails = this.createFromForm();
    if (assetDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.assetDetailsService.update(assetDetails));
    } else {
      this.subscribeToSaveResponse(this.assetDetailsService.create(assetDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetDetails>>): void {
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

  protected updateForm(assetDetails: IAssetDetails): void {
    this.editForm.patchValue({
      id: assetDetails.id,
      units: assetDetails.units,
      unitPrice: assetDetails.unitPrice,
      balance: assetDetails.balance,
      currentInvestedAmount: assetDetails.currentInvestedAmount,
      profitLoss: assetDetails.profitLoss,
      createdOn: assetDetails.createdOn ? assetDetails.createdOn.format(DATE_TIME_FORMAT) : null,
      updatedOn: assetDetails.updatedOn ? assetDetails.updatedOn.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IAssetDetails {
    return {
      ...new AssetDetails(),
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
