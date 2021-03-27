import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INationalCooperative, NationalCooperative } from '../national-cooperative.model';
import { NationalCooperativeService } from '../service/national-cooperative.service';

@Injectable({ providedIn: 'root' })
export class NationalCooperativeRoutingResolveService implements Resolve<INationalCooperative> {
  constructor(protected service: NationalCooperativeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INationalCooperative> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nationalCooperative: HttpResponse<NationalCooperative>) => {
          if (nationalCooperative.body) {
            return of(nationalCooperative.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NationalCooperative());
  }
}
