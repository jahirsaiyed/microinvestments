import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { PROMOTIONTYPE } from 'app/entities/enumerations/promotiontype.model';
import { IPromotions, Promotions } from '../promotions.model';

import { PromotionsService } from './promotions.service';

describe('Promotions Service', () => {
  let service: PromotionsService;
  let httpMock: HttpTestingController;
  let elemDefault: IPromotions;
  let expectedResult: IPromotions | IPromotions[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PromotionsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      type: PROMOTIONTYPE.CASHBACK,
      amount: 0,
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

    it('should create a Promotions', () => {
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

      service.create(new Promotions()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Promotions', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          type: 'BBBBBB',
          amount: 1,
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

    it('should partial update a Promotions', () => {
      const patchObject = Object.assign(
        {
          updatedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        new Promotions()
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

    it('should return a list of Promotions', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          type: 'BBBBBB',
          amount: 1,
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

    it('should delete a Promotions', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPromotionsToCollectionIfMissing', () => {
      it('should add a Promotions to an empty array', () => {
        const promotions: IPromotions = { id: 123 };
        expectedResult = service.addPromotionsToCollectionIfMissing([], promotions);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promotions);
      });

      it('should not add a Promotions to an array that contains it', () => {
        const promotions: IPromotions = { id: 123 };
        const promotionsCollection: IPromotions[] = [
          {
            ...promotions,
          },
          { id: 456 },
        ];
        expectedResult = service.addPromotionsToCollectionIfMissing(promotionsCollection, promotions);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Promotions to an array that doesn't contain it", () => {
        const promotions: IPromotions = { id: 123 };
        const promotionsCollection: IPromotions[] = [{ id: 456 }];
        expectedResult = service.addPromotionsToCollectionIfMissing(promotionsCollection, promotions);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promotions);
      });

      it('should add only unique Promotions to an array', () => {
        const promotionsArray: IPromotions[] = [{ id: 123 }, { id: 456 }, { id: 23388 }];
        const promotionsCollection: IPromotions[] = [{ id: 123 }];
        expectedResult = service.addPromotionsToCollectionIfMissing(promotionsCollection, ...promotionsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const promotions: IPromotions = { id: 123 };
        const promotions2: IPromotions = { id: 456 };
        expectedResult = service.addPromotionsToCollectionIfMissing([], promotions, promotions2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promotions);
        expect(expectedResult).toContain(promotions2);
      });

      it('should accept null and undefined values', () => {
        const promotions: IPromotions = { id: 123 };
        expectedResult = service.addPromotionsToCollectionIfMissing([], null, promotions, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promotions);
      });

      it('should return initial array if no Promotions is added', () => {
        const promotionsCollection: IPromotions[] = [{ id: 123 }];
        expectedResult = service.addPromotionsToCollectionIfMissing(promotionsCollection, undefined, null);
        expect(expectedResult).toEqual(promotionsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
