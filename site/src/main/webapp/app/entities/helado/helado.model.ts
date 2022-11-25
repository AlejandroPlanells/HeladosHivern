import dayjs from 'dayjs/esm';
import { IFabricante } from 'app/entities/fabricante/fabricante.model';
import { IIngrediente } from 'app/entities/ingrediente/ingrediente.model';

export interface IHelado {
  id: number;
  nombre?: string | null;
  enOferta?: boolean | null;
  precioOferta?: number | null;
  precio?: number | null;
  fechaCreacion?: dayjs.Dayjs | null;
  fabricante?: Pick<IFabricante, 'id'> | null;
  ingredientes?: Pick<IIngrediente, 'id'>[] | null;
}

export type NewHelado = Omit<IHelado, 'id'> & { id: null };
