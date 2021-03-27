import { IUser } from 'app/entities/user/user.model';
import { ILocalCooperative } from 'app/entities/local-cooperative/local-cooperative.model';
import { IDish } from 'app/entities/dish/dish.model';
import { ICommand } from 'app/entities/command/command.model';

export interface IRestaurant {
  id?: number;
  name?: string;
  address?: string;
  user?: IUser | null;
  localCooperatives?: ILocalCooperative[] | null;
  dishes?: IDish[] | null;
  commands?: ICommand[] | null;
}

export class Restaurant implements IRestaurant {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string,
    public user?: IUser | null,
    public localCooperatives?: ILocalCooperative[] | null,
    public dishes?: IDish[] | null,
    public commands?: ICommand[] | null
  ) {}
}

export function getRestaurantIdentifier(restaurant: IRestaurant): number | undefined {
  return restaurant.id;
}
