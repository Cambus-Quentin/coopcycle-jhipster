import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDish, getDishIdentifier } from '../dish.model';

export type EntityResponseType = HttpResponse<IDish>;
export type EntityArrayResponseType = HttpResponse<IDish[]>;

@Injectable({ providedIn: 'root' })
export class DishService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/dishes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(dish: IDish): Observable<EntityResponseType> {
    return this.http.post<IDish>(this.resourceUrl, dish, { observe: 'response' });
  }

  update(dish: IDish): Observable<EntityResponseType> {
    return this.http.put<IDish>(`${this.resourceUrl}/${getDishIdentifier(dish) as number}`, dish, { observe: 'response' });
  }

  partialUpdate(dish: IDish): Observable<EntityResponseType> {
    return this.http.patch<IDish>(`${this.resourceUrl}/${getDishIdentifier(dish) as number}`, dish, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDish>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDish[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDishToCollectionIfMissing(dishCollection: IDish[], ...dishesToCheck: (IDish | null | undefined)[]): IDish[] {
    const dishes: IDish[] = dishesToCheck.filter(isPresent);
    if (dishes.length > 0) {
      const dishCollectionIdentifiers = dishCollection.map(dishItem => getDishIdentifier(dishItem)!);
      const dishesToAdd = dishes.filter(dishItem => {
        const dishIdentifier = getDishIdentifier(dishItem);
        if (dishIdentifier == null || dishCollectionIdentifiers.includes(dishIdentifier)) {
          return false;
        }
        dishCollectionIdentifiers.push(dishIdentifier);
        return true;
      });
      return [...dishesToAdd, ...dishCollection];
    }
    return dishCollection;
  }
}
