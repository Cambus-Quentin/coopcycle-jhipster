import { IUser } from 'app/entities/user/user.model';
import { ICommand } from 'app/entities/command/command.model';

export interface IClient {
  id?: number;
  name?: string;
  surname?: string;
  address?: string | null;
  phoneNumber?: string | null;
  email?: string;
  profilContentType?: string | null;
  profil?: string | null;
  user?: IUser | null;
  commands?: ICommand[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public address?: string | null,
    public phoneNumber?: string | null,
    public email?: string,
    public profilContentType?: string | null,
    public profil?: string | null,
    public user?: IUser | null,
    public commands?: ICommand[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
