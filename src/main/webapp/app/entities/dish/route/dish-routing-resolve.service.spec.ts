jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDish, Dish } from '../dish.model';
import { DishService } from '../service/dish.service';

import { DishRoutingResolveService } from './dish-routing-resolve.service';

describe('Service Tests', () => {
  describe('Dish routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DishRoutingResolveService;
    let service: DishService;
    let resultDish: IDish | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DishRoutingResolveService);
      service = TestBed.inject(DishService);
      resultDish = undefined;
    });

    describe('resolve', () => {
      it('should return IDish returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDish = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDish).toEqual({ id: 123 });
      });

      it('should return new IDish if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDish = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDish).toEqual(new Dish());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDish = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDish).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
