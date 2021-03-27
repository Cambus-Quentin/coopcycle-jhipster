import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliverer, getDelivererIdentifier } from '../deliverer.model';

export type EntityResponseType = HttpResponse<IDeliverer>;
export type EntityArrayResponseType = HttpResponse<IDeliverer[]>;

@Injectable({ providedIn: 'root' })
export class DelivererService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/deliverers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(deliverer: IDeliverer): Observable<EntityResponseType> {
    return this.http.post<IDeliverer>(this.resourceUrl, deliverer, { observe: 'response' });
  }

  update(deliverer: IDeliverer): Observable<EntityResponseType> {
    return this.http.put<IDeliverer>(`${this.resourceUrl}/${getDelivererIdentifier(deliverer) as number}`, deliverer, {
      observe: 'response',
    });
  }

  partialUpdate(deliverer: IDeliverer): Observable<EntityResponseType> {
    return this.http.patch<IDeliverer>(`${this.resourceUrl}/${getDelivererIdentifier(deliverer) as number}`, deliverer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeliverer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeliverer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDelivererToCollectionIfMissing(
    delivererCollection: IDeliverer[],
    ...deliverersToCheck: (IDeliverer | null | undefined)[]
  ): IDeliverer[] {
    const deliverers: IDeliverer[] = deliverersToCheck.filter(isPresent);
    if (deliverers.length > 0) {
      const delivererCollectionIdentifiers = delivererCollection.map(delivererItem => getDelivererIdentifier(delivererItem)!);
      const deliverersToAdd = deliverers.filter(delivererItem => {
        const delivererIdentifier = getDelivererIdentifier(delivererItem);
        if (delivererIdentifier == null || delivererCollectionIdentifiers.includes(delivererIdentifier)) {
          return false;
        }
        delivererCollectionIdentifiers.push(delivererIdentifier);
        return true;
      });
      return [...deliverersToAdd, ...delivererCollection];
    }
    return delivererCollection;
  }
}
