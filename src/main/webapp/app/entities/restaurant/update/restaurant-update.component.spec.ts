jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RestaurantService } from '../service/restaurant.service';
import { IRestaurant, Restaurant } from '../restaurant.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILocalCooperative } from 'app/entities/local-cooperative/local-cooperative.model';
import { LocalCooperativeService } from 'app/entities/local-cooperative/service/local-cooperative.service';

import { RestaurantUpdateComponent } from './restaurant-update.component';

describe('Component Tests', () => {
  describe('Restaurant Management Update Component', () => {
    let comp: RestaurantUpdateComponent;
    let fixture: ComponentFixture<RestaurantUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let restaurantService: RestaurantService;
    let userService: UserService;
    let localCooperativeService: LocalCooperativeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RestaurantUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RestaurantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RestaurantUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      restaurantService = TestBed.inject(RestaurantService);
      userService = TestBed.inject(UserService);
      localCooperativeService = TestBed.inject(LocalCooperativeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const restaurant: IRestaurant = { id: 456 };
        const user: IUser = { id: 5866 };
        restaurant.user = user;

        const userCollection: IUser[] = [{ id: 85868 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ restaurant });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LocalCooperative query and add missing value', () => {
        const restaurant: IRestaurant = { id: 456 };
        const localCooperatives: ILocalCooperative[] = [{ id: 67008 }];
        restaurant.localCooperatives = localCooperatives;

        const localCooperativeCollection: ILocalCooperative[] = [{ id: 42003 }];
        spyOn(localCooperativeService, 'query').and.returnValue(of(new HttpResponse({ body: localCooperativeCollection })));
        const additionalLocalCooperatives = [...localCooperatives];
        const expectedCollection: ILocalCooperative[] = [...additionalLocalCooperatives, ...localCooperativeCollection];
        spyOn(localCooperativeService, 'addLocalCooperativeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ restaurant });
        comp.ngOnInit();

        expect(localCooperativeService.query).toHaveBeenCalled();
        expect(localCooperativeService.addLocalCooperativeToCollectionIfMissing).toHaveBeenCalledWith(
          localCooperativeCollection,
          ...additionalLocalCooperatives
        );
        expect(comp.localCooperativesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const restaurant: IRestaurant = { id: 456 };
        const user: IUser = { id: 12855 };
        restaurant.user = user;
        const localCooperatives: ILocalCooperative = { id: 31271 };
        restaurant.localCooperatives = [localCooperatives];

        activatedRoute.data = of({ restaurant });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(restaurant));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.localCooperativesSharedCollection).toContain(localCooperatives);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const restaurant = { id: 123 };
        spyOn(restaurantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: restaurant }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(restaurantService.update).toHaveBeenCalledWith(restaurant);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const restaurant = new Restaurant();
        spyOn(restaurantService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: restaurant }));
        saveSubject.complete();

        // THEN
        expect(restaurantService.create).toHaveBeenCalledWith(restaurant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const restaurant = { id: 123 };
        spyOn(restaurantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ restaurant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(restaurantService.update).toHaveBeenCalledWith(restaurant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLocalCooperativeById', () => {
        it('Should return tracked LocalCooperative primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLocalCooperativeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedLocalCooperative', () => {
        it('Should return option if no LocalCooperative is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedLocalCooperative(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected LocalCooperative for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedLocalCooperative(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this LocalCooperative is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedLocalCooperative(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
