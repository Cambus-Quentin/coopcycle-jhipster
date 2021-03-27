import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommand, getCommandIdentifier } from '../command.model';

export type EntityResponseType = HttpResponse<ICommand>;
export type EntityArrayResponseType = HttpResponse<ICommand[]>;

@Injectable({ providedIn: 'root' })
export class CommandService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/commands');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(command: ICommand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(command);
    return this.http
      .post<ICommand>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(command: ICommand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(command);
    return this.http
      .put<ICommand>(`${this.resourceUrl}/${getCommandIdentifier(command) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(command: ICommand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(command);
    return this.http
      .patch<ICommand>(`${this.resourceUrl}/${getCommandIdentifier(command) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommand>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommand[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommandToCollectionIfMissing(commandCollection: ICommand[], ...commandsToCheck: (ICommand | null | undefined)[]): ICommand[] {
    const commands: ICommand[] = commandsToCheck.filter(isPresent);
    if (commands.length > 0) {
      const commandCollectionIdentifiers = commandCollection.map(commandItem => getCommandIdentifier(commandItem)!);
      const commandsToAdd = commands.filter(commandItem => {
        const commandIdentifier = getCommandIdentifier(commandItem);
        if (commandIdentifier == null || commandCollectionIdentifiers.includes(commandIdentifier)) {
          return false;
        }
        commandCollectionIdentifiers.push(commandIdentifier);
        return true;
      });
      return [...commandsToAdd, ...commandCollection];
    }
    return commandCollection;
  }

  protected convertDateFromClient(command: ICommand): ICommand {
    return Object.assign({}, command, {
      date: command.date?.isValid() ? command.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((command: ICommand) => {
        command.date = command.date ? dayjs(command.date) : undefined;
      });
    }
    return res;
  }
}
