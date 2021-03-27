import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliverer } from '../deliverer.model';

@Component({
  selector: 'jhi-deliverer-detail',
  templateUrl: './deliverer-detail.component.html',
})
export class DelivererDetailComponent implements OnInit {
  deliverer: IDeliverer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliverer }) => {
      this.deliverer = deliverer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
