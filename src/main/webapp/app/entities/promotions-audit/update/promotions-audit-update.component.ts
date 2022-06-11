import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPromotionsAudit, PromotionsAudit } from '../promotions-audit.model';
import { PromotionsAuditService } from '../service/promotions-audit.service';
import { PROMOTIONTYPE } from 'app/entities/enumerations/promotiontype.model';

@Component({
  selector: 'jhi-promotions-audit-update',
  templateUrl: './promotions-audit-update.component.html',
})
export class PromotionsAuditUpdateComponent implements OnInit {
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

  constructor(
    protected promotionsAuditService: PromotionsAuditService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promotionsAudit }) => {
      if (promotionsAudit.id === undefined) {
        const today = dayjs().startOf('day');
        promotionsAudit.createdOn = today;
        promotionsAudit.updatedOn = today;
      }

      this.updateForm(promotionsAudit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const promotionsAudit = this.createFromForm();
    if (promotionsAudit.id !== undefined) {
      this.subscribeToSaveResponse(this.promotionsAuditService.update(promotionsAudit));
    } else {
      this.subscribeToSaveResponse(this.promotionsAuditService.create(promotionsAudit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPromotionsAudit>>): void {
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

  protected updateForm(promotionsAudit: IPromotionsAudit): void {
    this.editForm.patchValue({
      id: promotionsAudit.id,
      description: promotionsAudit.description,
      type: promotionsAudit.type,
      amount: promotionsAudit.amount,
      createdOn: promotionsAudit.createdOn ? promotionsAudit.createdOn.format(DATE_TIME_FORMAT) : null,
      updatedOn: promotionsAudit.updatedOn ? promotionsAudit.updatedOn.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IPromotionsAudit {
    return {
      ...new PromotionsAudit(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      type: this.editForm.get(['type'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedOn: this.editForm.get(['updatedOn'])!.value ? dayjs(this.editForm.get(['updatedOn'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
