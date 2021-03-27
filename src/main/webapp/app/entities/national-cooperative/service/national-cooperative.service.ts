import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INationalCooperative, getNationalCooperativeIdentifier } from '../national-cooperative.model';

export type EntityResponseType = HttpResponse<INationalCooperative>;
export type EntityArrayResponseType = HttpResponse<INationalCooperative[]>;

@Injectable({ providedIn: 'root' })
export class NationalCooperativeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/national-cooperatives');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(nationalCooperative: INationalCooperative): Observable<EntityResponseType> {
    return this.http.post<INationalCooperative>(this.resourceUrl, nationalCooperative, { observe: 'response' });
  }

  update(nationalCooperative: INationalCooperative): Observable<EntityResponseType> {
    return this.http.put<INationalCooperative>(
      `${this.resourceUrl}/${getNationalCooperativeIdentifier(nationalCooperative) as number}`,
      nationalCooperative,
      { observe: 'response' }
    );
  }

  partialUpdate(nationalCooperative: INationalCooperative): Observable<EntityResponseType> {
    return this.http.patch<INationalCooperative>(
      `${this.resourceUrl}/${getNationalCooperativeIdentifier(nationalCooperative) as number}`,
      nationalCooperative,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INationalCooperative>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INationalCooperative[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNationalCooperativeToCollectionIfMissing(
    nationalCooperativeCollection: INationalCooperative[],
    ...nationalCooperativesToCheck: (INationalCooperative | null | undefined)[]
  ): INationalCooperative[] {
    const nationalCooperatives: INationalCooperative[] = nationalCooperativesToCheck.filter(isPresent);
    if (nationalCooperatives.length > 0) {
      const nationalCooperativeCollectionIdentifiers = nationalCooperativeCollection.map(
        nationalCooperativeItem => getNationalCooperativeIdentifier(nationalCooperativeItem)!
      );
      const nationalCooperativesToAdd = nationalCooperatives.filter(nationalCooperativeItem => {
        const nationalCooperativeIdentifier = getNationalCooperativeIdentifier(nationalCooperativeItem);
        if (nationalCooperativeIdentifier == null || nationalCooperativeCollectionIdentifiers.includes(nationalCooperativeIdentifier)) {
          return false;
        }
        nationalCooperativeCollectionIdentifiers.push(nationalCooperativeIdentifier);
        return true;
      });
      return [...nationalCooperativesToAdd, ...nationalCooperativeCollection];
    }
    return nationalCooperativeCollection;
  }
}
