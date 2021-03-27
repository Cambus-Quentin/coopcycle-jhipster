import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDelivery, Delivery } from '../delivery.model';

import { DeliveryService } from './delivery.service';

describe('Service Tests', () => {
  describe('Delivery Service', () => {
    let service: DeliveryService;
    let httpMock: HttpTestingController;
    let elemDefault: IDelivery;
    let expectedResult: IDelivery | IDelivery[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DeliveryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        deliveryAddr: 'AAAAAAA',
        distance: 0,
        price: 0,
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

      it('should create a Delivery', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Delivery()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Delivery', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            deliveryAddr: 'BBBBBB',
            distance: 1,
            price: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Delivery', () => {
        const patchObject = Object.assign(
          {
            deliveryAddr: 'BBBBBB',
          },
          new Delivery()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Delivery', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            deliveryAddr: 'BBBBBB',
            distance: 1,
            price: 1,
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

      it('should delete a Delivery', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDeliveryToCollectionIfMissing', () => {
        it('should add a Delivery to an empty array', () => {
          const delivery: IDelivery = { id: 123 };
          expectedResult = service.addDeliveryToCollectionIfMissing([], delivery);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(delivery);
        });

        it('should not add a Delivery to an array that contains it', () => {
          const delivery: IDelivery = { id: 123 };
          const deliveryCollection: IDelivery[] = [
            {
              ...delivery,
            },
            { id: 456 },
          ];
          expectedResult = service.addDeliveryToCollectionIfMissing(deliveryCollection, delivery);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Delivery to an array that doesn't contain it", () => {
          const delivery: IDelivery = { id: 123 };
          const deliveryCollection: IDelivery[] = [{ id: 456 }];
          expectedResult = service.addDeliveryToCollectionIfMissing(deliveryCollection, delivery);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(delivery);
        });

        it('should add only unique Delivery to an array', () => {
          const deliveryArray: IDelivery[] = [{ id: 123 }, { id: 456 }, { id: 37566 }];
          const deliveryCollection: IDelivery[] = [{ id: 123 }];
          expectedResult = service.addDeliveryToCollectionIfMissing(deliveryCollection, ...deliveryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const delivery: IDelivery = { id: 123 };
          const delivery2: IDelivery = { id: 456 };
          expectedResult = service.addDeliveryToCollectionIfMissing([], delivery, delivery2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(delivery);
          expect(expectedResult).toContain(delivery2);
        });

        it('should accept null and undefined values', () => {
          const delivery: IDelivery = { id: 123 };
          expectedResult = service.addDeliveryToCollectionIfMissing([], null, delivery, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(delivery);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
