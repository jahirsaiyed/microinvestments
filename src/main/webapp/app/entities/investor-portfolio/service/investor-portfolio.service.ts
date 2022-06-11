import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvestorPortfolio, getInvestorPortfolioIdentifier } from '../investor-portfolio.model';

export type EntityResponseType = HttpResponse<IInvestorPortfolio>;
export type EntityArrayResponseType = HttpResponse<IInvestorPortfolio[]>;

@Injectable({ providedIn: 'root' })
export class InvestorPortfolioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/investor-portfolios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(investorPortfolio: IInvestorPortfolio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investorPortfolio);
    return this.http
      .post<IInvestorPortfolio>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(investorPortfolio: IInvestorPortfolio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investorPortfolio);
    return this.http
      .put<IInvestorPortfolio>(`${this.resourceUrl}/${getInvestorPortfolioIdentifier(investorPortfolio) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(investorPortfolio: IInvestorPortfolio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investorPortfolio);
    return this.http
      .patch<IInvestorPortfolio>(`${this.resourceUrl}/${getInvestorPortfolioIdentifier(investorPortfolio) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvestorPortfolio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvestorPortfolio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvestorPortfolioToCollectionIfMissing(
    investorPortfolioCollection: IInvestorPortfolio[],
    ...investorPortfoliosToCheck: (IInvestorPortfolio | null | undefined)[]
  ): IInvestorPortfolio[] {
    const investorPortfolios: IInvestorPortfolio[] = investorPortfoliosToCheck.filter(isPresent);
    if (investorPortfolios.length > 0) {
      const investorPortfolioCollectionIdentifiers = investorPortfolioCollection.map(
        investorPortfolioItem => getInvestorPortfolioIdentifier(investorPortfolioItem)!
      );
      const investorPortfoliosToAdd = investorPortfolios.filter(investorPortfolioItem => {
        const investorPortfolioIdentifier = getInvestorPortfolioIdentifier(investorPortfolioItem);
        if (investorPortfolioIdentifier == null || investorPortfolioCollectionIdentifiers.includes(investorPortfolioIdentifier)) {
          return false;
        }
        investorPortfolioCollectionIdentifiers.push(investorPortfolioIdentifier);
        return true;
      });
      return [...investorPortfoliosToAdd, ...investorPortfolioCollection];
    }
    return investorPortfolioCollection;
  }

  protected convertDateFromClient(investorPortfolio: IInvestorPortfolio): IInvestorPortfolio {
    return Object.assign({}, investorPortfolio, {
      createdOn: investorPortfolio.createdOn?.isValid() ? investorPortfolio.createdOn.toJSON() : undefined,
      updatedOn: investorPortfolio.updatedOn?.isValid() ? investorPortfolio.updatedOn.toJSON() : undefined,
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
      res.body.forEach((investorPortfolio: IInvestorPortfolio) => {
        investorPortfolio.createdOn = investorPortfolio.createdOn ? dayjs(investorPortfolio.createdOn) : undefined;
        investorPortfolio.updatedOn = investorPortfolio.updatedOn ? dayjs(investorPortfolio.updatedOn) : undefined;
      });
    }
    return res;
  }
}
