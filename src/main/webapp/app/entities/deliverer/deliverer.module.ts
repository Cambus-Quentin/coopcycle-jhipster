import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DelivererComponent } from './list/deliverer.component';
import { DelivererDetailComponent } from './detail/deliverer-detail.component';
import { DelivererUpdateComponent } from './update/deliverer-update.component';
import { DelivererDeleteDialogComponent } from './delete/deliverer-delete-dialog.component';
import { DelivererRoutingModule } from './route/deliverer-routing.module';

@NgModule({
  imports: [SharedModule, DelivererRoutingModule],
  declarations: [DelivererComponent, DelivererDetailComponent, DelivererUpdateComponent, DelivererDeleteDialogComponent],
  entryComponents: [DelivererDeleteDialogComponent],
})
export class DelivererModule {}
