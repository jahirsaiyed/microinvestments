import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetDetailsAudit, AssetDetailsAudit } from '../asset-details-audit.model';
import { AssetDetailsAuditService } from '../service/asset-details-audit.service';

@Injectable({ providedIn: 'root' })
export class AssetDetailsAuditRoutingResolveService implements Resolve<IAssetDetailsAudit> {
  constructor(protected service: AssetDetailsAuditService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetDetailsAudit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetDetailsAudit: HttpResponse<AssetDetailsAudit>) => {
          if (assetDetailsAudit.body) {
            return of(assetDetailsAudit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetDetailsAudit());
  }
}
