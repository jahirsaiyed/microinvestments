import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssetDetails, getAssetDetailsIdentifier } from '../asset-details.model';

export type EntityResponseType = HttpResponse<IAssetDetails>;
export type EntityArrayResponseType = HttpResponse<IAssetDetails[]>;

@Injectable({ providedIn: 'root' })
export class AssetDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetDetails: IAssetDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDetails);
    return this.http
      .post<IAssetDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetDetails: IAssetDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDetails);
    return this.http
      .put<IAssetDetails>(`${this.resourceUrl}/${getAssetDetailsIdentifier(assetDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetDetails: IAssetDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDetails);
    return this.http
      .patch<IAssetDetails>(`${this.resourceUrl}/${getAssetDetailsIdentifier(assetDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssetDetailsToCollectionIfMissing(
    assetDetailsCollection: IAssetDetails[],
    ...assetDetailsToCheck: (IAssetDetails | null | undefined)[]
  ): IAssetDetails[] {
    const assetDetails: IAssetDetails[] = assetDetailsToCheck.filter(isPresent);
    if (assetDetails.length > 0) {
      const assetDetailsCollectionIdentifiers = assetDetailsCollection.map(
        assetDetailsItem => getAssetDetailsIdentifier(assetDetailsItem)!
      );
      const assetDetailsToAdd = assetDetails.filter(assetDetailsItem => {
        const assetDetailsIdentifier = getAssetDetailsIdentifier(assetDetailsItem);
        if (assetDetailsIdentifier == null || assetDetailsCollectionIdentifiers.includes(assetDetailsIdentifier)) {
          return false;
        }
        assetDetailsCollectionIdentifiers.push(assetDetailsIdentifier);
        return true;
      });
      return [...assetDetailsToAdd, ...assetDetailsCollection];
    }
    return assetDetailsCollection;
  }

  protected convertDateFromClient(assetDetails: IAssetDetails): IAssetDetails {
    return Object.assign({}, assetDetails, {
      createdOn: assetDetails.createdOn?.isValid() ? assetDetails.createdOn.toJSON() : undefined,
      updatedOn: assetDetails.updatedOn?.isValid() ? assetDetails.updatedOn.toJSON() : undefined,
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
      res.body.forEach((assetDetails: IAssetDetails) => {
        assetDetails.createdOn = assetDetails.createdOn ? dayjs(assetDetails.createdOn) : undefined;
        assetDetails.updatedOn = assetDetails.updatedOn ? dayjs(assetDetails.updatedOn) : undefined;
      });
    }
    return res;
  }
}
