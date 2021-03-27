import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommand, Command } from '../command.model';
import { CommandService } from '../service/command.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IDelivery } from 'app/entities/delivery/delivery.model';
import { DeliveryService } from 'app/entities/delivery/service/delivery.service';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';
import { IDish } from 'app/entities/dish/dish.model';
import { DishService } from 'app/entities/dish/service/dish.service';

@Component({
  selector: 'jhi-command-update',
  templateUrl: './command-update.component.html',
})
export class CommandUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];
  deliveriesSharedCollection: IDelivery[] = [];
  restaurantsSharedCollection: IRestaurant[] = [];
  dishesSharedCollection: IDish[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    price: [null, [Validators.required]],
    state: [],
    client: [],
    delivery: [],
    restaurant: [],
    dishes: [],
  });

  constructor(
    protected commandService: CommandService,
    protected clientService: ClientService,
    protected deliveryService: DeliveryService,
    protected restaurantService: RestaurantService,
    protected dishService: DishService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ command }) => {
      this.updateForm(command);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const command = this.createFromForm();
    if (command.id !== undefined) {
      this.subscribeToSaveResponse(this.commandService.update(command));
    } else {
      this.subscribeToSaveResponse(this.commandService.create(command));
    }
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  trackDeliveryById(index: number, item: IDelivery): number {
    return item.id!;
  }

  trackRestaurantById(index: number, item: IRestaurant): number {
    return item.id!;
  }

  trackDishById(index: number, item: IDish): number {
    return item.id!;
  }

  getSelectedDish(option: IDish, selectedVals?: IDish[]): IDish {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommand>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(command: ICommand): void {
    this.editForm.patchValue({
      id: command.id,
      date: command.date,
      price: command.price,
      state: command.state,
      client: command.client,
      delivery: command.delivery,
      restaurant: command.restaurant,
      dishes: command.dishes,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, command.client);
    this.deliveriesSharedCollection = this.deliveryService.addDeliveryToCollectionIfMissing(
      this.deliveriesSharedCollection,
      command.delivery
    );
    this.restaurantsSharedCollection = this.restaurantService.addRestaurantToCollectionIfMissing(
      this.restaurantsSharedCollection,
      command.restaurant
    );
    this.dishesSharedCollection = this.dishService.addDishToCollectionIfMissing(this.dishesSharedCollection, ...(command.dishes ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.deliveryService
      .query()
      .pipe(map((res: HttpResponse<IDelivery[]>) => res.body ?? []))
      .pipe(
        map((deliveries: IDelivery[]) =>
          this.deliveryService.addDeliveryToCollectionIfMissing(deliveries, this.editForm.get('delivery')!.value)
        )
      )
      .subscribe((deliveries: IDelivery[]) => (this.deliveriesSharedCollection = deliveries));

    this.restaurantService
      .query()
      .pipe(map((res: HttpResponse<IRestaurant[]>) => res.body ?? []))
      .pipe(
        map((restaurants: IRestaurant[]) =>
          this.restaurantService.addRestaurantToCollectionIfMissing(restaurants, this.editForm.get('restaurant')!.value)
        )
      )
      .subscribe((restaurants: IRestaurant[]) => (this.restaurantsSharedCollection = restaurants));

    this.dishService
      .query()
      .pipe(map((res: HttpResponse<IDish[]>) => res.body ?? []))
      .pipe(map((dishes: IDish[]) => this.dishService.addDishToCollectionIfMissing(dishes, ...(this.editForm.get('dishes')!.value ?? []))))
      .subscribe((dishes: IDish[]) => (this.dishesSharedCollection = dishes));
  }

  protected createFromForm(): ICommand {
    return {
      ...new Command(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      price: this.editForm.get(['price'])!.value,
      state: this.editForm.get(['state'])!.value,
      client: this.editForm.get(['client'])!.value,
      delivery: this.editForm.get(['delivery'])!.value,
      restaurant: this.editForm.get(['restaurant'])!.value,
      dishes: this.editForm.get(['dishes'])!.value,
    };
  }
}
