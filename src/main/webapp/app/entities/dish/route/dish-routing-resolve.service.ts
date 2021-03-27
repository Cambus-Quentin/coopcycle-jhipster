import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDish, Dish } from '../dish.model';
import { DishService } from '../service/dish.service';

@Injectable({ providedIn: 'root' })
export class DishRoutingResolveService implements Resolve<IDish> {
  constructor(protected service: DishService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDish> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dish: HttpResponse<Dish>) => {
          if (dish.body) {
            return of(dish.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dish());
  }
}
