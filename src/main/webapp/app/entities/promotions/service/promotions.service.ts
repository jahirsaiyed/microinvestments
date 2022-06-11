import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPromotions, getPromotionsIdentifier } from '../promotions.model';

export type EntityResponseType = HttpResponse<IPromotions>;
export type EntityArrayResponseType = HttpResponse<IPromotions[]>;

@Injectable({ providedIn: 'root' })
export class PromotionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/promotions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(promotions: IPromotions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promotions);
    return this.http
      .post<IPromotions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(promotions: IPromotions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promotions);
    return this.http
      .put<IPromotions>(`${this.resourceUrl}/${getPromotionsIdentifier(promotions) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(promotions: IPromotions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promotions);
    return this.http
      .patch<IPromotions>(`${this.resourceUrl}/${getPromotionsIdentifier(promotions) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPromotions>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPromotions[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPromotionsToCollectionIfMissing(
    promotionsCollection: IPromotions[],
    ...promotionsToCheck: (IPromotions | null | undefined)[]
  ): IPromotions[] {
    const promotions: IPromotions[] = promotionsToCheck.filter(isPresent);
    if (promotions.length > 0) {
      const promotionsCollectionIdentifiers = promotionsCollection.map(promotionsItem => getPromotionsIdentifier(promotionsItem)!);
      const promotionsToAdd = promotions.filter(promotionsItem => {
        const promotionsIdentifier = getPromotionsIdentifier(promotionsItem);
        if (promotionsIdentifier == null || promotionsCollectionIdentifiers.includes(promotionsIdentifier)) {
          return false;
        }
        promotionsCollectionIdentifiers.push(promotionsIdentifier);
        return true;
      });
      return [...promotionsToAdd, ...promotionsCollection];
    }
    return promotionsCollection;
  }

  protected convertDateFromClient(promotions: IPromotions): IPromotions {
    return Object.assign({}, promotions, {
      createdOn: promotions.createdOn?.isValid() ? promotions.createdOn.toJSON() : undefined,
      updatedOn: promotions.updatedOn?.isValid() ? promotions.updatedOn.toJSON() : undefined,
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
      res.body.forEach((promotions: IPromotions) => {
        promotions.createdOn = promotions.createdOn ? dayjs(promotions.createdOn) : undefined;
        promotions.updatedOn = promotions.updatedOn ? dayjs(promotions.updatedOn) : undefined;
      });
    }
    return res;
  }
}
