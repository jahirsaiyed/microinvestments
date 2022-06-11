import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssetDetails, AssetDetails } from '../asset-details.model';

import { AssetDetailsService } from './asset-details.service';

describe('AssetDetails Service', () => {
  let service: AssetDetailsService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetDetails;
  let expectedResult: IAssetDetails | IAssetDetails[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetDetailsService);
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

    it('should create a AssetDetails', () => {
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

      service.create(new AssetDetails()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetDetails', () => {
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

    it('should partial update a AssetDetails', () => {
      const patchObject = Object.assign(
        {
          units: 1,
          unitPrice: 1,
          balance: 1,
          createdOn: currentDate.format(DATE_TIME_FORMAT),
          updatedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        new AssetDetails()
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

    it('should return a list of AssetDetails', () => {
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

    it('should delete a AssetDetails', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetDetailsToCollectionIfMissing', () => {
      it('should add a AssetDetails to an empty array', () => {
        const assetDetails: IAssetDetails = { id: 123 };
        expectedResult = service.addAssetDetailsToCollectionIfMissing([], assetDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetDetails);
      });

      it('should not add a AssetDetails to an array that contains it', () => {
        const assetDetails: IAssetDetails = { id: 123 };
        const assetDetailsCollection: IAssetDetails[] = [
          {
            ...assetDetails,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetDetailsToCollectionIfMissing(assetDetailsCollection, assetDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetDetails to an array that doesn't contain it", () => {
        const assetDetails: IAssetDetails = { id: 123 };
        const assetDetailsCollection: IAssetDetails[] = [{ id: 456 }];
        expectedResult = service.addAssetDetailsToCollectionIfMissing(assetDetailsCollection, assetDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetDetails);
      });

      it('should add only unique AssetDetails to an array', () => {
        const assetDetailsArray: IAssetDetails[] = [{ id: 123 }, { id: 456 }, { id: 58833 }];
        const assetDetailsCollection: IAssetDetails[] = [{ id: 123 }];
        expectedResult = service.addAssetDetailsToCollectionIfMissing(assetDetailsCollection, ...assetDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetDetails: IAssetDetails = { id: 123 };
        const assetDetails2: IAssetDetails = { id: 456 };
        expectedResult = service.addAssetDetailsToCollectionIfMissing([], assetDetails, assetDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetDetails);
        expect(expectedResult).toContain(assetDetails2);
      });

      it('should accept null and undefined values', () => {
        const assetDetails: IAssetDetails = { id: 123 };
        expectedResult = service.addAssetDetailsToCollectionIfMissing([], null, assetDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetDetails);
      });

      it('should return initial array if no AssetDetails is added', () => {
        const assetDetailsCollection: IAssetDetails[] = [{ id: 123 }];
        expectedResult = service.addAssetDetailsToCollectionIfMissing(assetDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(assetDetailsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
