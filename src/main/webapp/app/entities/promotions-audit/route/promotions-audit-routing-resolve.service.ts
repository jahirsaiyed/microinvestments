import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPromotionsAudit, PromotionsAudit } from '../promotions-audit.model';
import { PromotionsAuditService } from '../service/promotions-audit.service';

@Injectable({ providedIn: 'root' })
export class PromotionsAuditRoutingResolveService implements Resolve<IPromotionsAudit> {
  constructor(protected service: PromotionsAuditService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPromotionsAudit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((promotionsAudit: HttpResponse<PromotionsAudit>) => {
          if (promotionsAudit.body) {
            return of(promotionsAudit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PromotionsAudit());
  }
}
