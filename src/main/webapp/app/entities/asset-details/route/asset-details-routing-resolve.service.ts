import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetDetails, AssetDetails } from '../asset-details.model';
import { AssetDetailsService } from '../service/asset-details.service';

@Injectable({ providedIn: 'root' })
export class AssetDetailsRoutingResolveService implements Resolve<IAssetDetails> {
  constructor(protected service: AssetDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetDetails: HttpResponse<AssetDetails>) => {
          if (assetDetails.body) {
            return of(assetDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetDetails());
  }
}
