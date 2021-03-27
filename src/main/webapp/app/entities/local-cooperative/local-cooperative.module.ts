import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LocalCooperativeComponent } from './list/local-cooperative.component';
import { LocalCooperativeDetailComponent } from './detail/local-cooperative-detail.component';
import { LocalCooperativeUpdateComponent } from './update/local-cooperative-update.component';
import { LocalCooperativeDeleteDialogComponent } from './delete/local-cooperative-delete-dialog.component';
import { LocalCooperativeRoutingModule } from './route/local-cooperative-routing.module';

@NgModule({
  imports: [SharedModule, LocalCooperativeRoutingModule],
  declarations: [
    LocalCooperativeComponent,
    LocalCooperativeDetailComponent,
    LocalCooperativeUpdateComponent,
    LocalCooperativeDeleteDialogComponent,
  ],
  entryComponents: [LocalCooperativeDeleteDialogComponent],
})
export class LocalCooperativeModule {}
