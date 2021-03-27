import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocalCooperative, getLocalCooperativeIdentifier } from '../local-cooperative.model';

export type EntityResponseType = HttpResponse<ILocalCooperative>;
export type EntityArrayResponseType = HttpResponse<ILocalCooperative[]>;

@Injectable({ providedIn: 'root' })
export class LocalCooperativeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/local-cooperatives');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(localCooperative: ILocalCooperative): Observable<EntityResponseType> {
    return this.http.post<ILocalCooperative>(this.resourceUrl, localCooperative, { observe: 'response' });
  }

  update(localCooperative: ILocalCooperative): Observable<EntityResponseType> {
    return this.http.put<ILocalCooperative>(
      `${this.resourceUrl}/${getLocalCooperativeIdentifier(localCooperative) as number}`,
      localCooperative,
      { observe: 'response' }
    );
  }

  partialUpdate(localCooperative: ILocalCooperative): Observable<EntityResponseType> {
    return this.http.patch<ILocalCooperative>(
      `${this.resourceUrl}/${getLocalCooperativeIdentifier(localCooperative) as number}`,
      localCooperative,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocalCooperative>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocalCooperative[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocalCooperativeToCollectionIfMissing(
    localCooperativeCollection: ILocalCooperative[],
    ...localCooperativesToCheck: (ILocalCooperative | null | undefined)[]
  ): ILocalCooperative[] {
    const localCooperatives: ILocalCooperative[] = localCooperativesToCheck.filter(isPresent);
    if (localCooperatives.length > 0) {
      const localCooperativeCollectionIdentifiers = localCooperativeCollection.map(
        localCooperativeItem => getLocalCooperativeIdentifier(localCooperativeItem)!
      );
      const localCooperativesToAdd = localCooperatives.filter(localCooperativeItem => {
        const localCooperativeIdentifier = getLocalCooperativeIdentifier(localCooperativeItem);
        if (localCooperativeIdentifier == null || localCooperativeCollectionIdentifiers.includes(localCooperativeIdentifier)) {
          return false;
        }
        localCooperativeCollectionIdentifiers.push(localCooperativeIdentifier);
        return true;
      });
      return [...localCooperativesToAdd, ...localCooperativeCollection];
    }
    return localCooperativeCollection;
  }
}
