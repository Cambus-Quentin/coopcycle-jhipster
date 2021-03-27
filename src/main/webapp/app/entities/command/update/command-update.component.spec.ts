jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommandService } from '../service/command.service';
import { ICommand, Command } from '../command.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IDelivery } from 'app/entities/delivery/delivery.model';
import { DeliveryService } from 'app/entities/delivery/service/delivery.service';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';
import { IDish } from 'app/entities/dish/dish.model';
import { DishService } from 'app/entities/dish/service/dish.service';

import { CommandUpdateComponent } from './command-update.component';

describe('Component Tests', () => {
  describe('Command Management Update Component', () => {
    let comp: CommandUpdateComponent;
    let fixture: ComponentFixture<CommandUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let commandService: CommandService;
    let clientService: ClientService;
    let deliveryService: DeliveryService;
    let restaurantService: RestaurantService;
    let dishService: DishService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommandUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommandUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommandUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      commandService = TestBed.inject(CommandService);
      clientService = TestBed.inject(ClientService);
      deliveryService = TestBed.inject(DeliveryService);
      restaurantService = TestBed.inject(RestaurantService);
      dishService = TestBed.inject(DishService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const command: ICommand = { id: 456 };
        const client: IClient = { id: 8706 };
        command.client = client;

        const clientCollection: IClient[] = [{ id: 35713 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ command });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Delivery query and add missing value', () => {
        const command: ICommand = { id: 456 };
        const delivery: IDelivery = { id: 92791 };
        command.delivery = delivery;

        const deliveryCollection: IDelivery[] = [{ id: 68061 }];
        spyOn(deliveryService, 'query').and.returnValue(of(new HttpResponse({ body: deliveryCollection })));
        const additionalDeliveries = [delivery];
        const expectedCollection: IDelivery[] = [...additionalDeliveries, ...deliveryCollection];
        spyOn(deliveryService, 'addDeliveryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ command });
        comp.ngOnInit();

        expect(deliveryService.query).toHaveBeenCalled();
        expect(deliveryService.addDeliveryToCollectionIfMissing).toHaveBeenCalledWith(deliveryCollection, ...additionalDeliveries);
        expect(comp.deliveriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Restaurant query and add missing value', () => {
        const command: ICommand = { id: 456 };
        const restaurant: IRestaurant = { id: 4615 };
        command.restaurant = restaurant;

        const restaurantCollection: IRestaurant[] = [{ id: 98476 }];
        spyOn(restaurantService, 'query').and.returnValue(of(new HttpResponse({ body: restaurantCollection })));
        const additionalRestaurants = [restaurant];
        const expectedCollection: IRestaurant[] = [...additionalRestaurants, ...restaurantCollection];
        spyOn(restaurantService, 'addRestaurantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ command });
        comp.ngOnInit();

        expect(restaurantService.query).toHaveBeenCalled();
        expect(restaurantService.addRestaurantToCollectionIfMissing).toHaveBeenCalledWith(restaurantCollection, ...additionalRestaurants);
        expect(comp.restaurantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Dish query and add missing value', () => {
        const command: ICommand = { id: 456 };
        const dishes: IDish[] = [{ id: 12943 }];
        command.dishes = dishes;

        const dishCollection: IDish[] = [{ id: 69817 }];
        spyOn(dishService, 'query').and.returnValue(of(new HttpResponse({ body: dishCollection })));
        const additionalDishes = [...dishes];
        const expectedCollection: IDish[] = [...additionalDishes, ...dishCollection];
        spyOn(dishService, 'addDishToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ command });
        comp.ngOnInit();

        expect(dishService.query).toHaveBeenCalled();
        expect(dishService.addDishToCollectionIfMissing).toHaveBeenCalledWith(dishCollection, ...additionalDishes);
        expect(comp.dishesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const command: ICommand = { id: 456 };
        const client: IClient = { id: 36882 };
        command.client = client;
        const delivery: IDelivery = { id: 18252 };
        command.delivery = delivery;
        const restaurant: IRestaurant = { id: 89477 };
        command.restaurant = restaurant;
        const dishes: IDish = { id: 65337 };
        command.dishes = [dishes];

        activatedRoute.data = of({ command });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(command));
        expect(comp.clientsSharedCollection).toContain(client);
        expect(comp.deliveriesSharedCollection).toContain(delivery);
        expect(comp.restaurantsSharedCollection).toContain(restaurant);
        expect(comp.dishesSharedCollection).toContain(dishes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const command = { id: 123 };
        spyOn(commandService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ command });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: command }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(commandService.update).toHaveBeenCalledWith(command);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const command = new Command();
        spyOn(commandService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ command });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: command }));
        saveSubject.complete();

        // THEN
        expect(commandService.create).toHaveBeenCalledWith(command);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const command = { id: 123 };
        spyOn(commandService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ command });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(commandService.update).toHaveBeenCalledWith(command);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDeliveryById', () => {
        it('Should return tracked Delivery primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDeliveryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackRestaurantById', () => {
        it('Should return tracked Restaurant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRestaurantById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDishById', () => {
        it('Should return tracked Dish primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDishById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedDish', () => {
        it('Should return option if no Dish is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedDish(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Dish for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedDish(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Dish is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedDish(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
