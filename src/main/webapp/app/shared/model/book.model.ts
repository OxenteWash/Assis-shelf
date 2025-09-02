import { ICategory } from 'app/shared/model/category.model';

export interface IBook {
  id?: number;
  title?: string | null;
  isbn?: string | null;
  avaliable?: boolean | null;
  categories?: ICategory[] | null;
}

export const defaultValue: Readonly<IBook> = {
  avaliable: false,
};
