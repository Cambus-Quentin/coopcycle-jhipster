import { ILocalCooperative } from 'app/entities/local-cooperative/local-cooperative.model';

export interface INationalCooperative {
  id?: number;
  name?: string;
  localCooperatives?: ILocalCooperative[] | null;
}

export class NationalCooperative implements INationalCooperative {
  constructor(public id?: number, public name?: string, public localCooperatives?: ILocalCooperative[] | null) {}
}

export function getNationalCooperativeIdentifier(nationalCooperative: INationalCooperative): number | undefined {
  return nationalCooperative.id;
}
