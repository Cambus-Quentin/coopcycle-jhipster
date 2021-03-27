import { IUser } from 'app/entities/user/user.model';
import { IDelivery } from 'app/entities/delivery/delivery.model';

export interface IDeliverer {
  id?: number;
  name?: string;
  firstname?: string;
  user?: IUser | null;
  deliveries?: IDelivery[] | null;
}

export class Deliverer implements IDeliverer {
  constructor(
    public id?: number,
    public name?: string,
    public firstname?: string,
    public user?: IUser | null,
    public deliveries?: IDelivery[] | null
  ) {}
}

export function getDelivererIdentifier(deliverer: IDeliverer): number | undefined {
  return deliverer.id;
}
