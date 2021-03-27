import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDish } from '../dish.model';
import { DishService } from '../service/dish.service';

@Component({
  templateUrl: './dish-delete-dialog.component.html',
})
export class DishDeleteDialogComponent {
  dish?: IDish;

  constructor(protected dishService: DishService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dishService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
