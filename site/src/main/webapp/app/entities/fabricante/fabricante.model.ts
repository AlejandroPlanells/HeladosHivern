export interface IFabricante {
  id: number;
  nombre?: string | null;
  domicilio?: string | null;
}

export type NewFabricante = Omit<IFabricante, 'id'> & { id: null };
