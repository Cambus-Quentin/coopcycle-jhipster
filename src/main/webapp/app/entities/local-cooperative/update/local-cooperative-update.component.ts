import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILocalCooperative, LocalCooperative } from '../local-cooperative.model';
import { LocalCooperativeService } from '../service/local-cooperative.service';
import { INationalCooperative } from 'app/entities/national-cooperative/national-cooperative.model';
import { NationalCooperativeService } from 'app/entities/national-cooperative/service/national-cooperative.service';

@Component({
  selector: 'jhi-local-cooperative-update',
  templateUrl: './local-cooperative-update.component.html',
})
export class LocalCooperativeUpdateComponent implements OnInit {
  isSaving = false;

  nationalCooperativesSharedCollection: INationalCooperative[] = [];

  editForm = this.fb.group({
    id: [],
    geoZone: [null, [Validators.required]],
    nationalCooperative: [],
  });

  constructor(
    protected localCooperativeService: LocalCooperativeService,
    protected nationalCooperativeService: NationalCooperativeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localCooperative }) => {
      this.updateForm(localCooperative);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const localCooperative = this.createFromForm();
    if (localCooperative.id !== undefined) {
      this.subscribeToSaveResponse(this.localCooperativeService.update(localCooperative));
    } else {
      this.subscribeToSaveResponse(this.localCooperativeService.create(localCooperative));
    }
  }

  trackNationalCooperativeById(index: number, item: INationalCooperative): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalCooperative>>): void {
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

  protected updateForm(localCooperative: ILocalCooperative): void {
    this.editForm.patchValue({
      id: localCooperative.id,
      geoZone: localCooperative.geoZone,
      nationalCooperative: localCooperative.nationalCooperative,
    });

    this.nationalCooperativesSharedCollection = this.nationalCooperativeService.addNationalCooperativeToCollectionIfMissing(
      this.nationalCooperativesSharedCollection,
      localCooperative.nationalCooperative
    );
  }

  protected loadRelationshipsOptions(): void {
    this.nationalCooperativeService
      .query()
      .pipe(map((res: HttpResponse<INationalCooperative[]>) => res.body ?? []))
      .pipe(
        map((nationalCooperatives: INationalCooperative[]) =>
          this.nationalCooperativeService.addNationalCooperativeToCollectionIfMissing(
            nationalCooperatives,
            this.editForm.get('nationalCooperative')!.value
          )
        )
      )
      .subscribe((nationalCooperatives: INationalCooperative[]) => (this.nationalCooperativesSharedCollection = nationalCooperatives));
  }

  protected createFromForm(): ILocalCooperative {
    return {
      ...new LocalCooperative(),
      id: this.editForm.get(['id'])!.value,
      geoZone: this.editForm.get(['geoZone'])!.value,
      nationalCooperative: this.editForm.get(['nationalCooperative'])!.value,
    };
  }
}
