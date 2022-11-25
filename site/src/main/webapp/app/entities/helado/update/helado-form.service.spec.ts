import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../helado.test-samples';

import { HeladoFormService } from './helado-form.service';

describe('Helado Form Service', () => {
  let service: HeladoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HeladoFormService);
  });

  describe('Service methods', () => {
    describe('createHeladoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHeladoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            enOferta: expect.any(Object),
            precioOferta: expect.any(Object),
            precio: expect.any(Object),
            fechaCreacion: expect.any(Object),
            fabricante: expect.any(Object),
            ingredientes: expect.any(Object),
          })
        );
      });

      it('passing IHelado should create a new form with FormGroup', () => {
        const formGroup = service.createHeladoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            enOferta: expect.any(Object),
            precioOferta: expect.any(Object),
            precio: expect.any(Object),
            fechaCreacion: expect.any(Object),
            fabricante: expect.any(Object),
            ingredientes: expect.any(Object),
          })
        );
      });
    });

    describe('getHelado', () => {
      it('should return NewHelado for default Helado initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHeladoFormGroup(sampleWithNewData);

        const helado = service.getHelado(formGroup) as any;

        expect(helado).toMatchObject(sampleWithNewData);
      });

      it('should return NewHelado for empty Helado initial value', () => {
        const formGroup = service.createHeladoFormGroup();

        const helado = service.getHelado(formGroup) as any;

        expect(helado).toMatchObject({});
      });

      it('should return IHelado', () => {
        const formGroup = service.createHeladoFormGroup(sampleWithRequiredData);

        const helado = service.getHelado(formGroup) as any;

        expect(helado).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHelado should not enable id FormControl', () => {
        const formGroup = service.createHeladoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHelado should disable id FormControl', () => {
        const formGroup = service.createHeladoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
