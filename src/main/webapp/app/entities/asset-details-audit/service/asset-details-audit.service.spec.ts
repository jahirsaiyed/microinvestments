import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssetDetailsAudit, AssetDetailsAudit } from '../asset-details-audit.model';

import { AssetDetailsAuditService } from './asset-details-audit.service';

describe('AssetDetailsAudit Service', () => {
  let service: AssetDetailsAuditService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetDetailsAudit;
  let expectedResult: IAssetDetailsAudit | IAssetDetailsAudit[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetDetailsAuditService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      units: 0,
      unitPrice: 0,
      balance: 0,
      currentInvestedAmount: 0,
      profitLoss: 0,
      createdOn: currentDate,
      updatedOn: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdOn: currentDate.format(DATE_TIME_FORMAT),
          updatedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssetDetailsAudit', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdOn: currentDate.format(DATE_TIME_FORMAT),
          updatedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdOn: currentDate,
          updatedOn: currentDate,
        },
        returnedFromService
      );

      service.create(new AssetDetailsAudit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetDetailsAudit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          units: 1,
          unitPrice: 1,
          balance: 1,
          currentInvestedAmount: 1,
          profitLoss: 1,
          createdOn: currentDate.format(DATE_TIME_FORMAT),
          updatedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdOn: currentDate,
          updatedOn: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetDetailsAudit', () => {
      const patchObject = Object.assign(
        {
          balance: 1,
          currentInvestedAmount: 1,
          createdOn: currentDate.format(DATE_TIME_FORMAT),
          updatedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        new AssetDetailsAudit()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdOn: currentDate,
          updatedOn: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetDetailsAudit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          units: 1,
          unitPrice: 1,
          balance: 1,
          currentInvestedAmount: 1,
          profitLoss: 1,
          createdOn: currentDate.format(DATE_TIME_FORMAT),
          updatedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdOn: currentDate,
          updatedOn: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AssetDetailsAudit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetDetailsAuditToCollectionIfMissing', () => {
      it('should add a AssetDetailsAudit to an empty array', () => {
        const assetDetailsAudit: IAssetDetailsAudit = { id: 123 };
        expectedResult = service.addAssetDetailsAuditToCollectionIfMissing([], assetDetailsAudit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetDetailsAudit);
      });

      it('should not add a AssetDetailsAudit to an array that contains it', () => {
        const assetDetailsAudit: IAssetDetailsAudit = { id: 123 };
        const assetDetailsAuditCollection: IAssetDetailsAudit[] = [
          {
            ...assetDetailsAudit,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetDetailsAuditToCollectionIfMissing(assetDetailsAuditCollection, assetDetailsAudit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetDetailsAudit to an array that doesn't contain it", () => {
        const assetDetailsAudit: IAssetDetailsAudit = { id: 123 };
        const assetDetailsAuditCollection: IAssetDetailsAudit[] = [{ id: 456 }];
        expectedResult = service.addAssetDetailsAuditToCollectionIfMissing(assetDetailsAuditCollection, assetDetailsAudit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetDetailsAudit);
      });

      it('should add only unique AssetDetailsAudit to an array', () => {
        const assetDetailsAuditArray: IAssetDetailsAudit[] = [{ id: 123 }, { id: 456 }, { id: 68150 }];
        const assetDetailsAuditCollection: IAssetDetailsAudit[] = [{ id: 123 }];
        expectedResult = service.addAssetDetailsAuditToCollectionIfMissing(assetDetailsAuditCollection, ...assetDetailsAuditArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetDetailsAudit: IAssetDetailsAudit = { id: 123 };
        const assetDetailsAudit2: IAssetDetailsAudit = { id: 456 };
        expectedResult = service.addAssetDetailsAuditToCollectionIfMissing([], assetDetailsAudit, assetDetailsAudit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetDetailsAudit);
        expect(expectedResult).toContain(assetDetailsAudit2);
      });

      it('should accept null and undefined values', () => {
        const assetDetailsAudit: IAssetDetailsAudit = { id: 123 };
        expectedResult = service.addAssetDetailsAuditToCollectionIfMissing([], null, assetDetailsAudit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetDetailsAudit);
      });

      it('should return initial array if no AssetDetailsAudit is added', () => {
        const assetDetailsAuditCollection: IAssetDetailsAudit[] = [{ id: 123 }];
        expectedResult = service.addAssetDetailsAuditToCollectionIfMissing(assetDetailsAuditCollection, undefined, null);
        expect(expectedResult).toEqual(assetDetailsAuditCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
