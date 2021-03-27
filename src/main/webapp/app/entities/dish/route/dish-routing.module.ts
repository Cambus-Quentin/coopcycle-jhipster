import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DishComponent } from '../list/dish.component';
import { DishDetailComponent } from '../detail/dish-detail.component';
import { DishUpdateComponent } from '../update/dish-update.component';
import { DishRoutingResolveService } from './dish-routing-resolve.service';

const dishRoute: Routes = [
  {
    path: '',
    component: DishComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DishDetailComponent,
    resolve: {
      dish: DishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DishUpdateComponent,
    resolve: {
      dish: DishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DishUpdateComponent,
    resolve: {
      dish: DishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dishRoute)],
  exports: [RouterModule],
})
export class DishRoutingModule {}
