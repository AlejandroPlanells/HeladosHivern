import { IHelado } from 'app/entities/helado/helado.model';

export interface IIngrediente {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  gr?: number | null;
  cal?: string | null;
  helados?: Pick<IHelado, 'id'>[] | null;
}

export type NewIngrediente = Omit<IIngrediente, 'id'> & { id: null };
