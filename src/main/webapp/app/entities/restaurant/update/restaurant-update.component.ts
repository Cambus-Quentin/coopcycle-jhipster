import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRestaurant, Restaurant } from '../restaurant.model';
import { RestaurantService } from '../service/restaurant.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILocalCooperative } from 'app/entities/local-cooperative/local-cooperative.model';
import { LocalCooperativeService } from 'app/entities/local-cooperative/service/local-cooperative.service';

@Component({
  selector: 'jhi-restaurant-update',
  templateUrl: './restaurant-update.component.html',
})
export class RestaurantUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  localCooperativesSharedCollection: ILocalCooperative[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(3)]],
    address: [null, [Validators.required, Validators.minLength(3)]],
    user: [],
    localCooperatives: [],
  });

  constructor(
    protected restaurantService: RestaurantService,
    protected userService: UserService,
    protected localCooperativeService: LocalCooperativeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurant }) => {
      this.updateForm(restaurant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurant = this.createFromForm();
    if (restaurant.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantService.update(restaurant));
    } else {
      this.subscribeToSaveResponse(this.restaurantService.create(restaurant));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackLocalCooperativeById(index: number, item: ILocalCooperative): number {
    return item.id!;
  }

  getSelectedLocalCooperative(option: ILocalCooperative, selectedVals?: ILocalCooperative[]): ILocalCooperative {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurant>>): void {
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

  protected updateForm(restaurant: IRestaurant): void {
    this.editForm.patchValue({
      id: restaurant.id,
      name: restaurant.name,
      address: restaurant.address,
      user: restaurant.user,
      localCooperatives: restaurant.localCooperatives,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, restaurant.user);
    this.localCooperativesSharedCollection = this.localCooperativeService.addLocalCooperativeToCollectionIfMissing(
      this.localCooperativesSharedCollection,
      ...(restaurant.localCooperatives ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.localCooperativeService
      .query()
      .pipe(map((res: HttpResponse<ILocalCooperative[]>) => res.body ?? []))
      .pipe(
        map((localCooperatives: ILocalCooperative[]) =>
          this.localCooperativeService.addLocalCooperativeToCollectionIfMissing(
            localCooperatives,
            ...(this.editForm.get('localCooperatives')!.value ?? [])
          )
        )
      )
      .subscribe((localCooperatives: ILocalCooperative[]) => (this.localCooperativesSharedCollection = localCooperatives));
  }

  protected createFromForm(): IRestaurant {
    return {
      ...new Restaurant(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      user: this.editForm.get(['user'])!.value,
      localCooperatives: this.editForm.get(['localCooperatives'])!.value,
    };
  }
}
