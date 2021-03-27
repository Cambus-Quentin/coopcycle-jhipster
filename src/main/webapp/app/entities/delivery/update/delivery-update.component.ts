import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDelivery, Delivery } from '../delivery.model';
import { DeliveryService } from '../service/delivery.service';
import { IDeliverer } from 'app/entities/deliverer/deliverer.model';
import { DelivererService } from 'app/entities/deliverer/service/deliverer.service';

@Component({
  selector: 'jhi-delivery-update',
  templateUrl: './delivery-update.component.html',
})
export class DeliveryUpdateComponent implements OnInit {
  isSaving = false;

  deliverersSharedCollection: IDeliverer[] = [];

  editForm = this.fb.group({
    id: [],
    deliveryAddr: [null, [Validators.required, Validators.minLength(3)]],
    distance: [null, [Validators.required]],
    price: [null, [Validators.required]],
    deliverer: [],
  });

  constructor(
    protected deliveryService: DeliveryService,
    protected delivererService: DelivererService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ delivery }) => {
      this.updateForm(delivery);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const delivery = this.createFromForm();
    if (delivery.id !== undefined) {
      this.subscribeToSaveResponse(this.deliveryService.update(delivery));
    } else {
      this.subscribeToSaveResponse(this.deliveryService.create(delivery));
    }
  }

  trackDelivererById(index: number, item: IDeliverer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDelivery>>): void {
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

  protected updateForm(delivery: IDelivery): void {
    this.editForm.patchValue({
      id: delivery.id,
      deliveryAddr: delivery.deliveryAddr,
      distance: delivery.distance,
      price: delivery.price,
      deliverer: delivery.deliverer,
    });

    this.deliverersSharedCollection = this.delivererService.addDelivererToCollectionIfMissing(
      this.deliverersSharedCollection,
      delivery.deliverer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.delivererService
      .query()
      .pipe(map((res: HttpResponse<IDeliverer[]>) => res.body ?? []))
      .pipe(
        map((deliverers: IDeliverer[]) =>
          this.delivererService.addDelivererToCollectionIfMissing(deliverers, this.editForm.get('deliverer')!.value)
        )
      )
      .subscribe((deliverers: IDeliverer[]) => (this.deliverersSharedCollection = deliverers));
  }

  protected createFromForm(): IDelivery {
    return {
      ...new Delivery(),
      id: this.editForm.get(['id'])!.value,
      deliveryAddr: this.editForm.get(['deliveryAddr'])!.value,
      distance: this.editForm.get(['distance'])!.value,
      price: this.editForm.get(['price'])!.value,
      deliverer: this.editForm.get(['deliverer'])!.value,
    };
  }
}
