import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INationalCooperative } from '../national-cooperative.model';

@Component({
  selector: 'jhi-national-cooperative-detail',
  templateUrl: './national-cooperative-detail.component.html',
})
export class NationalCooperativeDetailComponent implements OnInit {
  nationalCooperative: INationalCooperative | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nationalCooperative }) => {
      this.nationalCooperative = nationalCooperative;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
