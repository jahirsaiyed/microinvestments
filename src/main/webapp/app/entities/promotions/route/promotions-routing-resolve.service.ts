import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPromotions, Promotions } from '../promotions.model';
import { PromotionsService } from '../service/promotions.service';

@Injectable({ providedIn: 'root' })
export class PromotionsRoutingResolveService implements Resolve<IPromotions> {
  constructor(protected service: PromotionsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPromotions> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((promotions: HttpResponse<Promotions>) => {
          if (promotions.body) {
            return of(promotions.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Promotions());
  }
}
