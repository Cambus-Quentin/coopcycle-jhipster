import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        data: { pageTitle: 'coopcycleApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'command',
        data: { pageTitle: 'coopcycleApp.command.home.title' },
        loadChildren: () => import('./command/command.module').then(m => m.CommandModule),
      },
      {
        path: 'dish',
        data: { pageTitle: 'coopcycleApp.dish.home.title' },
        loadChildren: () => import('./dish/dish.module').then(m => m.DishModule),
      },
      {
        path: 'restaurant',
        data: { pageTitle: 'coopcycleApp.restaurant.home.title' },
        loadChildren: () => import('./restaurant/restaurant.module').then(m => m.RestaurantModule),
      },
      {
        path: 'delivery',
        data: { pageTitle: 'coopcycleApp.delivery.home.title' },
        loadChildren: () => import('./delivery/delivery.module').then(m => m.DeliveryModule),
      },
      {
        path: 'deliverer',
        data: { pageTitle: 'coopcycleApp.deliverer.home.title' },
        loadChildren: () => import('./deliverer/deliverer.module').then(m => m.DelivererModule),
      },
      {
        path: 'national-cooperative',
        data: { pageTitle: 'coopcycleApp.nationalCooperative.home.title' },
        loadChildren: () => import('./national-cooperative/national-cooperative.module').then(m => m.NationalCooperativeModule),
      },
      {
        path: 'local-cooperative',
        data: { pageTitle: 'coopcycleApp.localCooperative.home.title' },
        loadChildren: () => import('./local-cooperative/local-cooperative.module').then(m => m.LocalCooperativeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
