import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INationalCooperative, NationalCooperative } from '../national-cooperative.model';

import { NationalCooperativeService } from './national-cooperative.service';

describe('Service Tests', () => {
  describe('NationalCooperative Service', () => {
    let service: NationalCooperativeService;
    let httpMock: HttpTestingController;
    let elemDefault: INationalCooperative;
    let expectedResult: INationalCooperative | INationalCooperative[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NationalCooperativeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a NationalCooperative', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new NationalCooperative()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a NationalCooperative', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a NationalCooperative', () => {
        const patchObject = Object.assign({}, new NationalCooperative());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of NationalCooperative', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a NationalCooperative', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNationalCooperativeToCollectionIfMissing', () => {
        it('should add a NationalCooperative to an empty array', () => {
          const nationalCooperative: INationalCooperative = { id: 123 };
          expectedResult = service.addNationalCooperativeToCollectionIfMissing([], nationalCooperative);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nationalCooperative);
        });

        it('should not add a NationalCooperative to an array that contains it', () => {
          const nationalCooperative: INationalCooperative = { id: 123 };
          const nationalCooperativeCollection: INationalCooperative[] = [
            {
              ...nationalCooperative,
            },
            { id: 456 },
          ];
          expectedResult = service.addNationalCooperativeToCollectionIfMissing(nationalCooperativeCollection, nationalCooperative);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a NationalCooperative to an array that doesn't contain it", () => {
          const nationalCooperative: INationalCooperative = { id: 123 };
          const nationalCooperativeCollection: INationalCooperative[] = [{ id: 456 }];
          expectedResult = service.addNationalCooperativeToCollectionIfMissing(nationalCooperativeCollection, nationalCooperative);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nationalCooperative);
        });

        it('should add only unique NationalCooperative to an array', () => {
          const nationalCooperativeArray: INationalCooperative[] = [{ id: 123 }, { id: 456 }, { id: 94636 }];
          const nationalCooperativeCollection: INationalCooperative[] = [{ id: 123 }];
          expectedResult = service.addNationalCooperativeToCollectionIfMissing(nationalCooperativeCollection, ...nationalCooperativeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const nationalCooperative: INationalCooperative = { id: 123 };
          const nationalCooperative2: INationalCooperative = { id: 456 };
          expectedResult = service.addNationalCooperativeToCollectionIfMissing([], nationalCooperative, nationalCooperative2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(nationalCooperative);
          expect(expectedResult).toContain(nationalCooperative2);
        });

        it('should accept null and undefined values', () => {
          const nationalCooperative: INationalCooperative = { id: 123 };
          expectedResult = service.addNationalCooperativeToCollectionIfMissing([], null, nationalCooperative, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(nationalCooperative);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
