import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocalCooperative, LocalCooperative } from '../local-cooperative.model';
import { LocalCooperativeService } from '../service/local-cooperative.service';

@Injectable({ providedIn: 'root' })
export class LocalCooperativeRoutingResolveService implements Resolve<ILocalCooperative> {
  constructor(protected service: LocalCooperativeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocalCooperative> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((localCooperative: HttpResponse<LocalCooperative>) => {
          if (localCooperative.body) {
            return of(localCooperative.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LocalCooperative());
  }
}
