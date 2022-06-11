import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssetDetailsAudit, getAssetDetailsAuditIdentifier } from '../asset-details-audit.model';

export type EntityResponseType = HttpResponse<IAssetDetailsAudit>;
export type EntityArrayResponseType = HttpResponse<IAssetDetailsAudit[]>;

@Injectable({ providedIn: 'root' })
export class AssetDetailsAuditService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-details-audits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetDetailsAudit: IAssetDetailsAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDetailsAudit);
    return this.http
      .post<IAssetDetailsAudit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetDetailsAudit: IAssetDetailsAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDetailsAudit);
    return this.http
      .put<IAssetDetailsAudit>(`${this.resourceUrl}/${getAssetDetailsAuditIdentifier(assetDetailsAudit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetDetailsAudit: IAssetDetailsAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDetailsAudit);
    return this.http
      .patch<IAssetDetailsAudit>(`${this.resourceUrl}/${getAssetDetailsAuditIdentifier(assetDetailsAudit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetDetailsAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetDetailsAudit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssetDetailsAuditToCollectionIfMissing(
    assetDetailsAuditCollection: IAssetDetailsAudit[],
    ...assetDetailsAuditsToCheck: (IAssetDetailsAudit | null | undefined)[]
  ): IAssetDetailsAudit[] {
    const assetDetailsAudits: IAssetDetailsAudit[] = assetDetailsAuditsToCheck.filter(isPresent);
    if (assetDetailsAudits.length > 0) {
      const assetDetailsAuditCollectionIdentifiers = assetDetailsAuditCollection.map(
        assetDetailsAuditItem => getAssetDetailsAuditIdentifier(assetDetailsAuditItem)!
      );
      const assetDetailsAuditsToAdd = assetDetailsAudits.filter(assetDetailsAuditItem => {
        const assetDetailsAuditIdentifier = getAssetDetailsAuditIdentifier(assetDetailsAuditItem);
        if (assetDetailsAuditIdentifier == null || assetDetailsAuditCollectionIdentifiers.includes(assetDetailsAuditIdentifier)) {
          return false;
        }
        assetDetailsAuditCollectionIdentifiers.push(assetDetailsAuditIdentifier);
        return true;
      });
      return [...assetDetailsAuditsToAdd, ...assetDetailsAuditCollection];
    }
    return assetDetailsAuditCollection;
  }

  protected convertDateFromClient(assetDetailsAudit: IAssetDetailsAudit): IAssetDetailsAudit {
    return Object.assign({}, assetDetailsAudit, {
      createdOn: assetDetailsAudit.createdOn?.isValid() ? assetDetailsAudit.createdOn.toJSON() : undefined,
      updatedOn: assetDetailsAudit.updatedOn?.isValid() ? assetDetailsAudit.updatedOn.toJSON() : undefined,
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
      res.body.forEach((assetDetailsAudit: IAssetDetailsAudit) => {
        assetDetailsAudit.createdOn = assetDetailsAudit.createdOn ? dayjs(assetDetailsAudit.createdOn) : undefined;
        assetDetailsAudit.updatedOn = assetDetailsAudit.updatedOn ? dayjs(assetDetailsAudit.updatedOn) : undefined;
      });
    }
    return res;
  }
}
