import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalCooperative } from '../local-cooperative.model';
import { LocalCooperativeService } from '../service/local-cooperative.service';

@Component({
  templateUrl: './local-cooperative-delete-dialog.component.html',
})
export class LocalCooperativeDeleteDialogComponent {
  localCooperative?: ILocalCooperative;

  constructor(protected localCooperativeService: LocalCooperativeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localCooperativeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
