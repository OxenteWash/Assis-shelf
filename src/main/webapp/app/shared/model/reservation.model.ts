import dayjs from 'dayjs';
import { IBook } from 'app/shared/model/book.model';
import { IUser } from 'app/shared/model/user.model';

export interface IReservation {
  id?: number;
  dateReservation?: dayjs.Dayjs | null;
  book?: IBook | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IReservation> = {};
