import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDelivery, Delivery } from '../delivery.model';
import { DeliveryService } from '../service/delivery.service';

@Injectable({ providedIn: 'root' })
export class DeliveryRoutingResolveService implements Resolve<IDelivery> {
  constructor(protected service: DeliveryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDelivery> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((delivery: HttpResponse<Delivery>) => {
          if (delivery.body) {
            return of(delivery.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Delivery());
  }
}
