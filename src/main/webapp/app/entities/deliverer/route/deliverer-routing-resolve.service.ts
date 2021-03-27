import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliverer, Deliverer } from '../deliverer.model';
import { DelivererService } from '../service/deliverer.service';

@Injectable({ providedIn: 'root' })
export class DelivererRoutingResolveService implements Resolve<IDeliverer> {
  constructor(protected service: DelivererService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliverer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliverer: HttpResponse<Deliverer>) => {
          if (deliverer.body) {
            return of(deliverer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Deliverer());
  }
}
