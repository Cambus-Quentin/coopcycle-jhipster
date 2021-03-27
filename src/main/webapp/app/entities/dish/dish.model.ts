import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { ICommand } from 'app/entities/command/command.model';

export interface IDish {
  id?: number;
  name?: string;
  price?: number;
  restaurant?: IRestaurant | null;
  commands?: ICommand[] | null;
}

export class Dish implements IDish {
  constructor(
    public id?: number,
    public name?: string,
    public price?: number,
    public restaurant?: IRestaurant | null,
    public commands?: ICommand[] | null
  ) {}
}

export function getDishIdentifier(dish: IDish): number | undefined {
  return dish.id;
}
