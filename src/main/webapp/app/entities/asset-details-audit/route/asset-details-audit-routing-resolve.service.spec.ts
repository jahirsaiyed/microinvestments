import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAssetDetailsAudit, AssetDetailsAudit } from '../asset-details-audit.model';
import { AssetDetailsAuditService } from '../service/asset-details-audit.service';

import { AssetDetailsAuditRoutingResolveService } from './asset-details-audit-routing-resolve.service';

describe('AssetDetailsAudit routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetDetailsAuditRoutingResolveService;
  let service: AssetDetailsAuditService;
  let resultAssetDetailsAudit: IAssetDetailsAudit | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(AssetDetailsAuditRoutingResolveService);
    service = TestBed.inject(AssetDetailsAuditService);
    resultAssetDetailsAudit = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetDetailsAudit returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetDetailsAudit = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetDetailsAudit).toEqual({ id: 123 });
    });

    it('should return new IAssetDetailsAudit if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetDetailsAudit = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetDetailsAudit).toEqual(new AssetDetailsAudit());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetDetailsAudit })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetDetailsAudit = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetDetailsAudit).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
