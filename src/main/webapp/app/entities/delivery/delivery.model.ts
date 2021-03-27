import { IDeliverer } from 'app/entities/deliverer/deliverer.model';
import { ICommand } from 'app/entities/command/command.model';

export interface IDelivery {
  id?: number;
  deliveryAddr?: string;
  distance?: number;
  price?: number;
  deliverer?: IDeliverer | null;
  commands?: ICommand[] | null;
}

export class Delivery implements IDelivery {
  constructor(
    public id?: number,
    public deliveryAddr?: string,
    public distance?: number,
    public price?: number,
    public deliverer?: IDeliverer | null,
    public commands?: ICommand[] | null
  ) {}
}

export function getDeliveryIdentifier(delivery: IDelivery): number | undefined {
  return delivery.id;
}
