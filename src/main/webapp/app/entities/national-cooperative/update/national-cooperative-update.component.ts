import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INationalCooperative, NationalCooperative } from '../national-cooperative.model';
import { NationalCooperativeService } from '../service/national-cooperative.service';

@Component({
  selector: 'jhi-national-cooperative-update',
  templateUrl: './national-cooperative-update.component.html',
})
export class NationalCooperativeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(
    protected nationalCooperativeService: NationalCooperativeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nationalCooperative }) => {
      this.updateForm(nationalCooperative);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nationalCooperative = this.createFromForm();
    if (nationalCooperative.id !== undefined) {
      this.subscribeToSaveResponse(this.nationalCooperativeService.update(nationalCooperative));
    } else {
      this.subscribeToSaveResponse(this.nationalCooperativeService.create(nationalCooperative));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INationalCooperative>>): void {
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

  protected updateForm(nationalCooperative: INationalCooperative): void {
    this.editForm.patchValue({
      id: nationalCooperative.id,
      name: nationalCooperative.name,
    });
  }

  protected createFromForm(): INationalCooperative {
    return {
      ...new NationalCooperative(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
