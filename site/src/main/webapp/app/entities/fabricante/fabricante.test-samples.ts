import { IFabricante, NewFabricante } from './fabricante.model';

export const sampleWithRequiredData: IFabricante = {
  id: 47959,
  nombre: 'Krone Ejecutivo',
  domicilio: 'benchmark Algodón capacitor',
};

export const sampleWithPartialData: IFabricante = {
  id: 25633,
  nombre: 'Avon Consultor',
  domicilio: 'Factores',
};

export const sampleWithFullData: IFabricante = {
  id: 85693,
  nombre: 'Metal Facilitador Granito',
  domicilio: 'EXE',
};

export const sampleWithNewData: NewFabricante = {
  nombre: 'cultivate Andalucía Música',
  domicilio: 'Rojo communities drive',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
