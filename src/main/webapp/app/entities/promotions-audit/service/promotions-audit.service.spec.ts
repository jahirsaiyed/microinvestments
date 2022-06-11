import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { PROMOTIONTYPE } from 'app/entities/enumerations/promotiontype.model';
import { IPromotionsAudit, PromotionsAudit } from '../promotions-audit.model';

import { PromotionsAuditService } from './promotions-audit.service';

describe('PromotionsAudit Service', () => {
  let service: PromotionsAuditService;
  let httpMock: HttpTestingController;
  let elemDefault: IPromotionsAudit;
  let expectedResult: IPromotionsAudit | IPromotionsAudit[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PromotionsAuditService);
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

    it('should create a PromotionsAudit', () => {
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

      service.create(new PromotionsAudit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PromotionsAudit', () => {
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

    it('should partial update a PromotionsAudit', () => {
      const patchObject = Object.assign(
        {
          type: 'BBBBBB',
        },
        new PromotionsAudit()
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

    it('should return a list of PromotionsAudit', () => {
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

    it('should delete a PromotionsAudit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPromotionsAuditToCollectionIfMissing', () => {
      it('should add a PromotionsAudit to an empty array', () => {
        const promotionsAudit: IPromotionsAudit = { id: 123 };
        expectedResult = service.addPromotionsAuditToCollectionIfMissing([], promotionsAudit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promotionsAudit);
      });

      it('should not add a PromotionsAudit to an array that contains it', () => {
        const promotionsAudit: IPromotionsAudit = { id: 123 };
        const promotionsAuditCollection: IPromotionsAudit[] = [
          {
            ...promotionsAudit,
          },
          { id: 456 },
        ];
        expectedResult = service.addPromotionsAuditToCollectionIfMissing(promotionsAuditCollection, promotionsAudit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PromotionsAudit to an array that doesn't contain it", () => {
        const promotionsAudit: IPromotionsAudit = { id: 123 };
        const promotionsAuditCollection: IPromotionsAudit[] = [{ id: 456 }];
        expectedResult = service.addPromotionsAuditToCollectionIfMissing(promotionsAuditCollection, promotionsAudit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promotionsAudit);
      });

      it('should add only unique PromotionsAudit to an array', () => {
        const promotionsAuditArray: IPromotionsAudit[] = [{ id: 123 }, { id: 456 }, { id: 88574 }];
        const promotionsAuditCollection: IPromotionsAudit[] = [{ id: 123 }];
        expectedResult = service.addPromotionsAuditToCollectionIfMissing(promotionsAuditCollection, ...promotionsAuditArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const promotionsAudit: IPromotionsAudit = { id: 123 };
        const promotionsAudit2: IPromotionsAudit = { id: 456 };
        expectedResult = service.addPromotionsAuditToCollectionIfMissing([], promotionsAudit, promotionsAudit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promotionsAudit);
        expect(expectedResult).toContain(promotionsAudit2);
      });

      it('should accept null and undefined values', () => {
        const promotionsAudit: IPromotionsAudit = { id: 123 };
        expectedResult = service.addPromotionsAuditToCollectionIfMissing([], null, promotionsAudit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promotionsAudit);
      });

      it('should return initial array if no PromotionsAudit is added', () => {
        const promotionsAuditCollection: IPromotionsAudit[] = [{ id: 123 }];
        expectedResult = service.addPromotionsAuditToCollectionIfMissing(promotionsAuditCollection, undefined, null);
        expect(expectedResult).toEqual(promotionsAuditCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
