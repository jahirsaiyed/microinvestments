import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPromotionsAudit, getPromotionsAuditIdentifier } from '../promotions-audit.model';

export type EntityResponseType = HttpResponse<IPromotionsAudit>;
export type EntityArrayResponseType = HttpResponse<IPromotionsAudit[]>;

@Injectable({ providedIn: 'root' })
export class PromotionsAuditService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/promotions-audits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(promotionsAudit: IPromotionsAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promotionsAudit);
    return this.http
      .post<IPromotionsAudit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(promotionsAudit: IPromotionsAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promotionsAudit);
    return this.http
      .put<IPromotionsAudit>(`${this.resourceUrl}/${getPromotionsAuditIdentifier(promotionsAudit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(promotionsAudit: IPromotionsAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promotionsAudit);
    return this.http
      .patch<IPromotionsAudit>(`${this.resourceUrl}/${getPromotionsAuditIdentifier(promotionsAudit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPromotionsAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPromotionsAudit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPromotionsAuditToCollectionIfMissing(
    promotionsAuditCollection: IPromotionsAudit[],
    ...promotionsAuditsToCheck: (IPromotionsAudit | null | undefined)[]
  ): IPromotionsAudit[] {
    const promotionsAudits: IPromotionsAudit[] = promotionsAuditsToCheck.filter(isPresent);
    if (promotionsAudits.length > 0) {
      const promotionsAuditCollectionIdentifiers = promotionsAuditCollection.map(
        promotionsAuditItem => getPromotionsAuditIdentifier(promotionsAuditItem)!
      );
      const promotionsAuditsToAdd = promotionsAudits.filter(promotionsAuditItem => {
        const promotionsAuditIdentifier = getPromotionsAuditIdentifier(promotionsAuditItem);
        if (promotionsAuditIdentifier == null || promotionsAuditCollectionIdentifiers.includes(promotionsAuditIdentifier)) {
          return false;
        }
        promotionsAuditCollectionIdentifiers.push(promotionsAuditIdentifier);
        return true;
      });
      return [...promotionsAuditsToAdd, ...promotionsAuditCollection];
    }
    return promotionsAuditCollection;
  }

  protected convertDateFromClient(promotionsAudit: IPromotionsAudit): IPromotionsAudit {
    return Object.assign({}, promotionsAudit, {
      createdOn: promotionsAudit.createdOn?.isValid() ? promotionsAudit.createdOn.toJSON() : undefined,
      updatedOn: promotionsAudit.updatedOn?.isValid() ? promotionsAudit.updatedOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? dayjs(res.body.createdOn) : undefined;
      res.body.updatedOn = res.body.updatedOn ? dayjs(res.body.updatedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((promotionsAudit: IPromotionsAudit) => {
        promotionsAudit.createdOn = promotionsAudit.createdOn ? dayjs(promotionsAudit.createdOn) : undefined;
        promotionsAudit.updatedOn = promotionsAudit.updatedOn ? dayjs(promotionsAudit.updatedOn) : undefined;
      });
    }
    return res;
  }
}
