import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILocalCooperative, LocalCooperative } from '../local-cooperative.model';

import { LocalCooperativeService } from './local-cooperative.service';

describe('Service Tests', () => {
  describe('LocalCooperative Service', () => {
    let service: LocalCooperativeService;
    let httpMock: HttpTestingController;
    let elemDefault: ILocalCooperative;
    let expectedResult: ILocalCooperative | ILocalCooperative[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LocalCooperativeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        geoZone: 'AAAAAAA',
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

      it('should create a LocalCooperative', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LocalCooperative()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LocalCooperative', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            geoZone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LocalCooperative', () => {
        const patchObject = Object.assign({}, new LocalCooperative());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LocalCooperative', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            geoZone: 'BBBBBB',
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

      it('should delete a LocalCooperative', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLocalCooperativeToCollectionIfMissing', () => {
        it('should add a LocalCooperative to an empty array', () => {
          const localCooperative: ILocalCooperative = { id: 123 };
          expectedResult = service.addLocalCooperativeToCollectionIfMissing([], localCooperative);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(localCooperative);
        });

        it('should not add a LocalCooperative to an array that contains it', () => {
          const localCooperative: ILocalCooperative = { id: 123 };
          const localCooperativeCollection: ILocalCooperative[] = [
            {
              ...localCooperative,
            },
            { id: 456 },
          ];
          expectedResult = service.addLocalCooperativeToCollectionIfMissing(localCooperativeCollection, localCooperative);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LocalCooperative to an array that doesn't contain it", () => {
          const localCooperative: ILocalCooperative = { id: 123 };
          const localCooperativeCollection: ILocalCooperative[] = [{ id: 456 }];
          expectedResult = service.addLocalCooperativeToCollectionIfMissing(localCooperativeCollection, localCooperative);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(localCooperative);
        });

        it('should add only unique LocalCooperative to an array', () => {
          const localCooperativeArray: ILocalCooperative[] = [{ id: 123 }, { id: 456 }, { id: 36590 }];
          const localCooperativeCollection: ILocalCooperative[] = [{ id: 123 }];
          expectedResult = service.addLocalCooperativeToCollectionIfMissing(localCooperativeCollection, ...localCooperativeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const localCooperative: ILocalCooperative = { id: 123 };
          const localCooperative2: ILocalCooperative = { id: 456 };
          expectedResult = service.addLocalCooperativeToCollectionIfMissing([], localCooperative, localCooperative2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(localCooperative);
          expect(expectedResult).toContain(localCooperative2);
        });

        it('should accept null and undefined values', () => {
          const localCooperative: ILocalCooperative = { id: 123 };
          expectedResult = service.addLocalCooperativeToCollectionIfMissing([], null, localCooperative, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(localCooperative);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
