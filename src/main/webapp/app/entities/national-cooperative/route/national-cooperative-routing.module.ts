import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NationalCooperativeComponent } from '../list/national-cooperative.component';
import { NationalCooperativeDetailComponent } from '../detail/national-cooperative-detail.component';
import { NationalCooperativeUpdateComponent } from '../update/national-cooperative-update.component';
import { NationalCooperativeRoutingResolveService } from './national-cooperative-routing-resolve.service';

const nationalCooperativeRoute: Routes = [
  {
    path: '',
    component: NationalCooperativeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NationalCooperativeDetailComponent,
    resolve: {
      nationalCooperative: NationalCooperativeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NationalCooperativeUpdateComponent,
    resolve: {
      nationalCooperative: NationalCooperativeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NationalCooperativeUpdateComponent,
    resolve: {
      nationalCooperative: NationalCooperativeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nationalCooperativeRoute)],
  exports: [RouterModule],
})
export class NationalCooperativeRoutingModule {}
