jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILocalCooperative, LocalCooperative } from '../local-cooperative.model';
import { LocalCooperativeService } from '../service/local-cooperative.service';

import { LocalCooperativeRoutingResolveService } from './local-cooperative-routing-resolve.service';

describe('Service Tests', () => {
  describe('LocalCooperative routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LocalCooperativeRoutingResolveService;
    let service: LocalCooperativeService;
    let resultLocalCooperative: ILocalCooperative | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LocalCooperativeRoutingResolveService);
      service = TestBed.inject(LocalCooperativeService);
      resultLocalCooperative = undefined;
    });

    describe('resolve', () => {
      it('should return ILocalCooperative returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocalCooperative = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocalCooperative).toEqual({ id: 123 });
      });

      it('should return new ILocalCooperative if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocalCooperative = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLocalCooperative).toEqual(new LocalCooperative());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocalCooperative = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocalCooperative).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
