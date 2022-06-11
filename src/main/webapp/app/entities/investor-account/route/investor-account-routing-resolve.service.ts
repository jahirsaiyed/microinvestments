import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvestorAccount, InvestorAccount } from '../investor-account.model';
import { InvestorAccountService } from '../service/investor-account.service';

@Injectable({ providedIn: 'root' })
export class InvestorAccountRoutingResolveService implements Resolve<IInvestorAccount> {
  constructor(protected service: InvestorAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvestorAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((investorAccount: HttpResponse<InvestorAccount>) => {
          if (investorAccount.body) {
            return of(investorAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InvestorAccount());
  }
}
