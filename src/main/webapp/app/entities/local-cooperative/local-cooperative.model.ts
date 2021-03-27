import { INationalCooperative } from 'app/entities/national-cooperative/national-cooperative.model';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';

export interface ILocalCooperative {
  id?: number;
  geoZone?: string;
  nationalCooperative?: INationalCooperative | null;
  restaurants?: IRestaurant[] | null;
}

export class LocalCooperative implements ILocalCooperative {
  constructor(
    public id?: number,
    public geoZone?: string,
    public nationalCooperative?: INationalCooperative | null,
    public restaurants?: IRestaurant[] | null
  ) {}
}

export function getLocalCooperativeIdentifier(localCooperative: ILocalCooperative): number | undefined {
  return localCooperative.id;
}
