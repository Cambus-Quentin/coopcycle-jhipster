jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDeliverer, Deliverer } from '../deliverer.model';
import { DelivererService } from '../service/deliverer.service';

import { DelivererRoutingResolveService } from './deliverer-routing-resolve.service';

describe('Service Tests', () => {
  describe('Deliverer routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DelivererRoutingResolveService;
    let service: DelivererService;
    let resultDeliverer: IDeliverer | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DelivererRoutingResolveService);
      service = TestBed.inject(DelivererService);
      resultDeliverer = undefined;
    });

    describe('resolve', () => {
      it('should return IDeliverer returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeliverer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDeliverer).toEqual({ id: 123 });
      });

      it('should return new IDeliverer if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeliverer = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDeliverer).toEqual(new Deliverer());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeliverer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDeliverer).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
