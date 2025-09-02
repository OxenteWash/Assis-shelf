import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ILoan {
  id?: number;
  loanTime?: dayjs.Dayjs | null;
  returnTimeExpected?: dayjs.Dayjs | null;
  realReturnTime?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ILoan> = {};
