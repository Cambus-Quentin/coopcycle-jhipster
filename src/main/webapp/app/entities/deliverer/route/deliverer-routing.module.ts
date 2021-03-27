import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DelivererComponent } from '../list/deliverer.component';
import { DelivererDetailComponent } from '../detail/deliverer-detail.component';
import { DelivererUpdateComponent } from '../update/deliverer-update.component';
import { DelivererRoutingResolveService } from './deliverer-routing-resolve.service';

const delivererRoute: Routes = [
  {
    path: '',
    component: DelivererComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DelivererDetailComponent,
    resolve: {
      deliverer: DelivererRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DelivererUpdateComponent,
    resolve: {
      deliverer: DelivererRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DelivererUpdateComponent,
    resolve: {
      deliverer: DelivererRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(delivererRoute)],
  exports: [RouterModule],
})
export class DelivererRoutingModule {}
