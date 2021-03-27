jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommand, Command } from '../command.model';
import { CommandService } from '../service/command.service';

import { CommandRoutingResolveService } from './command-routing-resolve.service';

describe('Service Tests', () => {
  describe('Command routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommandRoutingResolveService;
    let service: CommandService;
    let resultCommand: ICommand | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommandRoutingResolveService);
      service = TestBed.inject(CommandService);
      resultCommand = undefined;
    });

    describe('resolve', () => {
      it('should return ICommand returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommand = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommand).toEqual({ id: 123 });
      });

      it('should return new ICommand if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommand = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCommand).toEqual(new Command());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommand = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommand).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
