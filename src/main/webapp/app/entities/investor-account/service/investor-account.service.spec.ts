import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInvestorAccount, InvestorAccount } from '../investor-account.model';

import { InvestorAccountService } from './investor-account.service';

describe('InvestorAccount Service', () => {
  let service: InvestorAccountService;
  let httpMock: HttpTestingController;
  let elemDefault: IInvestorAccount;
  let expectedResult: IInvestorAccount | IInvestorAccount[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvestorAccountService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountNo: 'AAAAAAA',
      iBAN: 'AAAAAAA',
      type: 'AAAAAAA',
      walletAddress: 'AAAAAAA',
      walletNetwork: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a InvestorAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InvestorAccount()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvestorAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountNo: 'BBBBBB',
          iBAN: 'BBBBBB',
          type: 'BBBBBB',
          walletAddress: 'BBBBBB',
          walletNetwork: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvestorAccount', () => {
      const patchObject = Object.assign(
        {
          iBAN: 'BBBBBB',
          type: 'BBBBBB',
          walletAddress: 'BBBBBB',
          walletNetwork: 'BBBBBB',
        },
        new InvestorAccount()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvestorAccount', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountNo: 'BBBBBB',
          iBAN: 'BBBBBB',
          type: 'BBBBBB',
          walletAddress: 'BBBBBB',
          walletNetwork: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a InvestorAccount', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInvestorAccountToCollectionIfMissing', () => {
      it('should add a InvestorAccount to an empty array', () => {
        const investorAccount: IInvestorAccount = { id: 123 };
        expectedResult = service.addInvestorAccountToCollectionIfMissing([], investorAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(investorAccount);
      });

      it('should not add a InvestorAccount to an array that contains it', () => {
        const investorAccount: IInvestorAccount = { id: 123 };
        const investorAccountCollection: IInvestorAccount[] = [
          {
            ...investorAccount,
          },
          { id: 456 },
        ];
        expectedResult = service.addInvestorAccountToCollectionIfMissing(investorAccountCollection, investorAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvestorAccount to an array that doesn't contain it", () => {
        const investorAccount: IInvestorAccount = { id: 123 };
        const investorAccountCollection: IInvestorAccount[] = [{ id: 456 }];
        expectedResult = service.addInvestorAccountToCollectionIfMissing(investorAccountCollection, investorAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(investorAccount);
      });

      it('should add only unique InvestorAccount to an array', () => {
        const investorAccountArray: IInvestorAccount[] = [{ id: 123 }, { id: 456 }, { id: 18459 }];
        const investorAccountCollection: IInvestorAccount[] = [{ id: 123 }];
        expectedResult = service.addInvestorAccountToCollectionIfMissing(investorAccountCollection, ...investorAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const investorAccount: IInvestorAccount = { id: 123 };
        const investorAccount2: IInvestorAccount = { id: 456 };
        expectedResult = service.addInvestorAccountToCollectionIfMissing([], investorAccount, investorAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(investorAccount);
        expect(expectedResult).toContain(investorAccount2);
      });

      it('should accept null and undefined values', () => {
        const investorAccount: IInvestorAccount = { id: 123 };
        expectedResult = service.addInvestorAccountToCollectionIfMissing([], null, investorAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(investorAccount);
      });

      it('should return initial array if no InvestorAccount is added', () => {
        const investorAccountCollection: IInvestorAccount[] = [{ id: 123 }];
        expectedResult = service.addInvestorAccountToCollectionIfMissing(investorAccountCollection, undefined, null);
        expect(expectedResult).toEqual(investorAccountCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
