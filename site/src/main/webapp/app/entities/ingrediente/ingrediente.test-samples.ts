import { IIngrediente, NewIngrediente } from './ingrediente.model';

export const sampleWithRequiredData: IIngrediente = {
  id: 15222,
  nombre: 'Algodón payment',
  descripcion: 'Pasaje Acero primary',
  gr: 11517,
  cal: 'morph haptic',
};

export const sampleWithPartialData: IIngrediente = {
  id: 67530,
  nombre: 'markets',
  descripcion: 'Salchichas Joyería empower',
  gr: 43182,
  cal: 'Coordinador',
};

export const sampleWithFullData: IIngrediente = {
  id: 66612,
  nombre: 'Rial',
  descripcion: 'éxito Guinea Rioja',
  gr: 57334,
  cal: 'Panamá',
};

export const sampleWithNewData: NewIngrediente = {
  nombre: 'Reactivo',
  descripcion: 'Martinica FTP',
  gr: 75518,
  cal: 'Borders coherente',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
