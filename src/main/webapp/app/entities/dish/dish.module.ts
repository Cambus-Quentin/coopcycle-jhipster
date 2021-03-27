import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DishComponent } from './list/dish.component';
import { DishDetailComponent } from './detail/dish-detail.component';
import { DishUpdateComponent } from './update/dish-update.component';
import { DishDeleteDialogComponent } from './delete/dish-delete-dialog.component';
import { DishRoutingModule } from './route/dish-routing.module';

@NgModule({
  imports: [SharedModule, DishRoutingModule],
  declarations: [DishComponent, DishDetailComponent, DishUpdateComponent, DishDeleteDialogComponent],
  entryComponents: [DishDeleteDialogComponent],
})
export class DishModule {}
