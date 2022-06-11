import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPromotionsAudit, PromotionsAudit } from '../promotions-audit.model';
import { PromotionsAuditService } from '../service/promotions-audit.service';

import { PromotionsAuditRoutingResolveService } from './promotions-audit-routing-resolve.service';

describe('PromotionsAudit routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PromotionsAuditRoutingResolveService;
  let service: PromotionsAuditService;
  let resultPromotionsAudit: IPromotionsAudit | undefined;

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
    routingResolveService = TestBed.inject(PromotionsAuditRoutingResolveService);
    service = TestBed.inject(PromotionsAuditService);
    resultPromotionsAudit = undefined;
  });

  describe('resolve', () => {
    it('should return IPromotionsAudit returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPromotionsAudit = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPromotionsAudit).toEqual({ id: 123 });
    });

    it('should return new IPromotionsAudit if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPromotionsAudit = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPromotionsAudit).toEqual(new PromotionsAudit());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PromotionsAudit })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPromotionsAudit = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPromotionsAudit).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
