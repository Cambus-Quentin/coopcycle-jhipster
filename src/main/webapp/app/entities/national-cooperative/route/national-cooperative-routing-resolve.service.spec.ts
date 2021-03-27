jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INationalCooperative, NationalCooperative } from '../national-cooperative.model';
import { NationalCooperativeService } from '../service/national-cooperative.service';

import { NationalCooperativeRoutingResolveService } from './national-cooperative-routing-resolve.service';

describe('Service Tests', () => {
  describe('NationalCooperative routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NationalCooperativeRoutingResolveService;
    let service: NationalCooperativeService;
    let resultNationalCooperative: INationalCooperative | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NationalCooperativeRoutingResolveService);
      service = TestBed.inject(NationalCooperativeService);
      resultNationalCooperative = undefined;
    });

    describe('resolve', () => {
      it('should return INationalCooperative returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNationalCooperative = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNationalCooperative).toEqual({ id: 123 });
      });

      it('should return new INationalCooperative if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNationalCooperative = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNationalCooperative).toEqual(new NationalCooperative());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNationalCooperative = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNationalCooperative).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
