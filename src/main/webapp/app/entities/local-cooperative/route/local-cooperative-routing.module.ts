import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocalCooperativeComponent } from '../list/local-cooperative.component';
import { LocalCooperativeDetailComponent } from '../detail/local-cooperative-detail.component';
import { LocalCooperativeUpdateComponent } from '../update/local-cooperative-update.component';
import { LocalCooperativeRoutingResolveService } from './local-cooperative-routing-resolve.service';

const localCooperativeRoute: Routes = [
  {
    path: '',
    component: LocalCooperativeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocalCooperativeDetailComponent,
    resolve: {
      localCooperative: LocalCooperativeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocalCooperativeUpdateComponent,
    resolve: {
      localCooperative: LocalCooperativeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocalCooperativeUpdateComponent,
    resolve: {
      localCooperative: LocalCooperativeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(localCooperativeRoute)],
  exports: [RouterModule],
})
export class LocalCooperativeRoutingModule {}
