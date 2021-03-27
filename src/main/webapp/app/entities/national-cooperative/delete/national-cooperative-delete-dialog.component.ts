import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INationalCooperative } from '../national-cooperative.model';
import { NationalCooperativeService } from '../service/national-cooperative.service';

@Component({
  templateUrl: './national-cooperative-delete-dialog.component.html',
})
export class NationalCooperativeDeleteDialogComponent {
  nationalCooperative?: INationalCooperative;

  constructor(protected nationalCooperativeService: NationalCooperativeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nationalCooperativeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
