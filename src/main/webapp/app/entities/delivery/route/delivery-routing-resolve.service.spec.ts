jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDelivery, Delivery } from '../delivery.model';
import { DeliveryService } from '../service/delivery.service';

import { DeliveryRoutingResolveService } from './delivery-routing-resolve.service';

describe('Service Tests', () => {
  describe('Delivery routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DeliveryRoutingResolveService;
    let service: DeliveryService;
    let resultDelivery: IDelivery | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DeliveryRoutingResolveService);
      service = TestBed.inject(DeliveryService);
      resultDelivery = undefined;
    });

    describe('resolve', () => {
      it('should return IDelivery returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDelivery = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDelivery).toEqual({ id: 123 });
      });

      it('should return new IDelivery if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDelivery = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDelivery).toEqual(new Delivery());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDelivery = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDelivery).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
