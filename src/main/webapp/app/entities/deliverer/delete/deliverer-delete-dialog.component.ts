import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliverer } from '../deliverer.model';
import { DelivererService } from '../service/deliverer.service';

@Component({
  templateUrl: './deliverer-delete-dialog.component.html',
})
export class DelivererDeleteDialogComponent {
  deliverer?: IDeliverer;

  constructor(protected delivererService: DelivererService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.delivererService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
