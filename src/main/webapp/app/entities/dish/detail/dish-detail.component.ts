import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDish } from '../dish.model';

@Component({
  selector: 'jhi-dish-detail',
  templateUrl: './dish-detail.component.html',
})
export class DishDetailComponent implements OnInit {
  dish: IDish | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dish }) => {
      this.dish = dish;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
