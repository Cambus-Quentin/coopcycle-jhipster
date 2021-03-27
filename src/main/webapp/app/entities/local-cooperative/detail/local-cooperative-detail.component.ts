import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocalCooperative } from '../local-cooperative.model';

@Component({
  selector: 'jhi-local-cooperative-detail',
  templateUrl: './local-cooperative-detail.component.html',
})
export class LocalCooperativeDetailComponent implements OnInit {
  localCooperative: ILocalCooperative | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localCooperative }) => {
      this.localCooperative = localCooperative;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
