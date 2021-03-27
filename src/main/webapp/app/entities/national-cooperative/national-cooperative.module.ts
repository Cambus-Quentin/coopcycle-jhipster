import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { NationalCooperativeComponent } from './list/national-cooperative.component';
import { NationalCooperativeDetailComponent } from './detail/national-cooperative-detail.component';
import { NationalCooperativeUpdateComponent } from './update/national-cooperative-update.component';
import { NationalCooperativeDeleteDialogComponent } from './delete/national-cooperative-delete-dialog.component';
import { NationalCooperativeRoutingModule } from './route/national-cooperative-routing.module';

@NgModule({
  imports: [SharedModule, NationalCooperativeRoutingModule],
  declarations: [
    NationalCooperativeComponent,
    NationalCooperativeDetailComponent,
    NationalCooperativeUpdateComponent,
    NationalCooperativeDeleteDialogComponent,
  ],
  entryComponents: [NationalCooperativeDeleteDialogComponent],
})
export class NationalCooperativeModule {}
