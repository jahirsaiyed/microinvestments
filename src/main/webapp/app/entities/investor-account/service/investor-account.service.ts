import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvestorAccount, getInvestorAccountIdentifier } from '../investor-account.model';

export type EntityResponseType = HttpResponse<IInvestorAccount>;
export type EntityArrayResponseType = HttpResponse<IInvestorAccount[]>;

@Injectable({ providedIn: 'root' })
export class InvestorAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/investor-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(investorAccount: IInvestorAccount): Observable<EntityResponseType> {
    return this.http.post<IInvestorAccount>(this.resourceUrl, investorAccount, { observe: 'response' });
  }

  update(investorAccount: IInvestorAccount): Observable<EntityResponseType> {
    return this.http.put<IInvestorAccount>(
      `${this.resourceUrl}/${getInvestorAccountIdentifier(investorAccount) as number}`,
      investorAccount,
      { observe: 'response' }
    );
  }

  partialUpdate(investorAccount: IInvestorAccount): Observable<EntityResponseType> {
    return this.http.patch<IInvestorAccount>(
      `${this.resourceUrl}/${getInvestorAccountIdentifier(investorAccount) as number}`,
      investorAccount,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInvestorAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInvestorAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvestorAccountToCollectionIfMissing(
    investorAccountCollection: IInvestorAccount[],
    ...investorAccountsToCheck: (IInvestorAccount | null | undefined)[]
  ): IInvestorAccount[] {
    const investorAccounts: IInvestorAccount[] = investorAccountsToCheck.filter(isPresent);
    if (investorAccounts.length > 0) {
      const investorAccountCollectionIdentifiers = investorAccountCollection.map(
        investorAccountItem => getInvestorAccountIdentifier(investorAccountItem)!
      );
      const investorAccountsToAdd = investorAccounts.filter(investorAccountItem => {
        const investorAccountIdentifier = getInvestorAccountIdentifier(investorAccountItem);
        if (investorAccountIdentifier == null || investorAccountCollectionIdentifiers.includes(investorAccountIdentifier)) {
          return false;
        }
        investorAccountCollectionIdentifiers.push(investorAccountIdentifier);
        return true;
      });
      return [...investorAccountsToAdd, ...investorAccountCollection];
    }
    return investorAccountCollection;
  }
}
