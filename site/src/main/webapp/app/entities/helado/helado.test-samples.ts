import dayjs from 'dayjs/esm';

import { IHelado, NewHelado } from './helado.model';

export const sampleWithRequiredData: IHelado = {
  id: 66181,
  nombre: 'Sorprendente SMTP',
  enOferta: false,
  precio: 57998,
  fechaCreacion: dayjs('2022-11-23T07:46'),
};

export const sampleWithPartialData: IHelado = {
  id: 23172,
  nombre: 'world-class paradigms',
  enOferta: true,
  precioOferta: 45757,
  precio: 97999,
  fechaCreacion: dayjs('2022-11-22T18:04'),
};

export const sampleWithFullData: IHelado = {
  id: 52912,
  nombre: 'deliverables Supervisor',
  enOferta: true,
  precioOferta: 73060,
  precio: 69898,
  fechaCreacion: dayjs('2022-11-22T15:34'),
};

export const sampleWithNewData: NewHelado = {
  nombre: 'Azul override',
  enOferta: false,
  precio: 92956,
  fechaCreacion: dayjs('2022-11-23T02:10'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
