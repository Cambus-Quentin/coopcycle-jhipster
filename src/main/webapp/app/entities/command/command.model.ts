import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';
import { IDelivery } from 'app/entities/delivery/delivery.model';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { IDish } from 'app/entities/dish/dish.model';
import { STATECMD } from 'app/entities/enumerations/statecmd.model';

export interface ICommand {
  id?: number;
  date?: dayjs.Dayjs;
  price?: number;
  state?: STATECMD | null;
  client?: IClient | null;
  delivery?: IDelivery | null;
  restaurant?: IRestaurant | null;
  dishes?: IDish[] | null;
}

export class Command implements ICommand {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public price?: number,
    public state?: STATECMD | null,
    public client?: IClient | null,
    public delivery?: IDelivery | null,
    public restaurant?: IRestaurant | null,
    public dishes?: IDish[] | null
  ) {}
}

export function getCommandIdentifier(command: ICommand): number | undefined {
  return command.id;
}
