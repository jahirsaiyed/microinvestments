import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPromotions, Promotions } from '../promotions.model';
import { PromotionsService } from '../service/promotions.service';
import { PROMOTIONTYPE } from 'app/entities/enumerations/promotiontype.model';

@Component({
  selector: 'jhi-promotions-update',
  templateUrl: './promotions-update.component.html',
})
export class PromotionsUpdateComponent implements OnInit {
  isSaving = false;
  pROMOTIONTYPEValues = Object.keys(PROMOTIONTYPE);

  editForm = this.fb.group({
    id: [],
    description: [],
    type: [],
    amount: [],
    createdOn: [],
    updatedOn: [],
  });

  constructor(protected promotionsService: PromotionsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promotions }) => {
      if (promotions.id === undefined) {
        const today = dayjs().startOf('day');
        promotions.createdOn = today;
        promotions.updatedOn = today;
      }

      this.updateForm(promotions);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const promotions = this.createFromForm();
    if (promotions.id !== undefined) {
      this.subscribeToSaveResponse(this.promotionsService.update(promotions));
    } else {
      this.subscribeToSaveResponse(this.promotionsService.create(promotions));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPromotions>>): void {
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

  protected updateForm(promotions: IPromotions): void {
    this.editForm.patchValue({
      id: promotions.id,
      description: promotions.description,
      type: promotions.type,
      amount: promotions.amount,
      createdOn: promotions.createdOn ? promotions.createdOn.format(DATE_TIME_FORMAT) : null,
      updatedOn: promotions.updatedOn ? promotions.updatedOn.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IPromotions {
    return {
      ...new Promotions(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      type: this.editForm.get(['type'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedOn: this.editForm.get(['updatedOn'])!.value ? dayjs(this.editForm.get(['updatedOn'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
