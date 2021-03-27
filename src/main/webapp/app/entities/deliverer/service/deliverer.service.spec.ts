import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeliverer, Deliverer } from '../deliverer.model';

import { DelivererService } from './deliverer.service';

describe('Service Tests', () => {
  describe('Deliverer Service', () => {
    let service: DelivererService;
    let httpMock: HttpTestingController;
    let elemDefault: IDeliverer;
    let expectedResult: IDeliverer | IDeliverer[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DelivererService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        firstname: 'AAAAAAA',
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

      it('should create a Deliverer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Deliverer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Deliverer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            firstname: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Deliverer', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Deliverer()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Deliverer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            firstname: 'BBBBBB',
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

      it('should delete a Deliverer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDelivererToCollectionIfMissing', () => {
        it('should add a Deliverer to an empty array', () => {
          const deliverer: IDeliverer = { id: 123 };
          expectedResult = service.addDelivererToCollectionIfMissing([], deliverer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(deliverer);
        });

        it('should not add a Deliverer to an array that contains it', () => {
          const deliverer: IDeliverer = { id: 123 };
          const delivererCollection: IDeliverer[] = [
            {
              ...deliverer,
            },
            { id: 456 },
          ];
          expectedResult = service.addDelivererToCollectionIfMissing(delivererCollection, deliverer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Deliverer to an array that doesn't contain it", () => {
          const deliverer: IDeliverer = { id: 123 };
          const delivererCollection: IDeliverer[] = [{ id: 456 }];
          expectedResult = service.addDelivererToCollectionIfMissing(delivererCollection, deliverer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(deliverer);
        });

        it('should add only unique Deliverer to an array', () => {
          const delivererArray: IDeliverer[] = [{ id: 123 }, { id: 456 }, { id: 96869 }];
          const delivererCollection: IDeliverer[] = [{ id: 123 }];
          expectedResult = service.addDelivererToCollectionIfMissing(delivererCollection, ...delivererArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const deliverer: IDeliverer = { id: 123 };
          const deliverer2: IDeliverer = { id: 456 };
          expectedResult = service.addDelivererToCollectionIfMissing([], deliverer, deliverer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(deliverer);
          expect(expectedResult).toContain(deliverer2);
        });

        it('should accept null and undefined values', () => {
          const deliverer: IDeliverer = { id: 123 };
          expectedResult = service.addDelivererToCollectionIfMissing([], null, deliverer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(deliverer);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
