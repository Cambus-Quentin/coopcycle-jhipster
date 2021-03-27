import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDelivery, getDeliveryIdentifier } from '../delivery.model';

export type EntityResponseType = HttpResponse<IDelivery>;
export type EntityArrayResponseType = HttpResponse<IDelivery[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/deliveries');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(delivery: IDelivery): Observable<EntityResponseType> {
    return this.http.post<IDelivery>(this.resourceUrl, delivery, { observe: 'response' });
  }

  update(delivery: IDelivery): Observable<EntityResponseType> {
    return this.http.put<IDelivery>(`${this.resourceUrl}/${getDeliveryIdentifier(delivery) as number}`, delivery, { observe: 'response' });
  }

  partialUpdate(delivery: IDelivery): Observable<EntityResponseType> {
    return this.http.patch<IDelivery>(`${this.resourceUrl}/${getDeliveryIdentifier(delivery) as number}`, delivery, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDelivery>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDelivery[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeliveryToCollectionIfMissing(deliveryCollection: IDelivery[], ...deliveriesToCheck: (IDelivery | null | undefined)[]): IDelivery[] {
    const deliveries: IDelivery[] = deliveriesToCheck.filter(isPresent);
    if (deliveries.length > 0) {
      const deliveryCollectionIdentifiers = deliveryCollection.map(deliveryItem => getDeliveryIdentifier(deliveryItem)!);
      const deliveriesToAdd = deliveries.filter(deliveryItem => {
        const deliveryIdentifier = getDeliveryIdentifier(deliveryItem);
        if (deliveryIdentifier == null || deliveryCollectionIdentifiers.includes(deliveryIdentifier)) {
          return false;
        }
        deliveryCollectionIdentifiers.push(deliveryIdentifier);
        return true;
      });
      return [...deliveriesToAdd, ...deliveryCollection];
    }
    return deliveryCollection;
  }
}
