import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInvestorAccount, InvestorAccount } from '../investor-account.model';
import { InvestorAccountService } from '../service/investor-account.service';

import { InvestorAccountRoutingResolveService } from './investor-account-routing-resolve.service';

describe('InvestorAccount routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InvestorAccountRoutingResolveService;
  let service: InvestorAccountService;
  let resultInvestorAccount: IInvestorAccount | undefined;

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
    routingResolveService = TestBed.inject(InvestorAccountRoutingResolveService);
    service = TestBed.inject(InvestorAccountService);
    resultInvestorAccount = undefined;
  });

  describe('resolve', () => {
    it('should return IInvestorAccount returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvestorAccount = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvestorAccount).toEqual({ id: 123 });
    });

    it('should return new IInvestorAccount if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvestorAccount = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInvestorAccount).toEqual(new InvestorAccount());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InvestorAccount })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvestorAccount = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvestorAccount).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
