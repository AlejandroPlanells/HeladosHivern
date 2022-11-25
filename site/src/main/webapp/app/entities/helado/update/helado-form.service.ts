import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHelado, NewHelado } from '../helado.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHelado for edit and NewHeladoFormGroupInput for create.
 */
type HeladoFormGroupInput = IHelado | PartialWithRequiredKeyOf<NewHelado>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHelado | NewHelado> = Omit<T, 'fechaCreacion'> & {
  fechaCreacion?: string | null;
};

type HeladoFormRawValue = FormValueOf<IHelado>;

type NewHeladoFormRawValue = FormValueOf<NewHelado>;

type HeladoFormDefaults = Pick<NewHelado, 'id' | 'enOferta' | 'fechaCreacion' | 'ingredientes'>;

type HeladoFormGroupContent = {
  id: FormControl<HeladoFormRawValue['id'] | NewHelado['id']>;
  nombre: FormControl<HeladoFormRawValue['nombre']>;
  enOferta: FormControl<HeladoFormRawValue['enOferta']>;
  precioOferta: FormControl<HeladoFormRawValue['precioOferta']>;
  precio: FormControl<HeladoFormRawValue['precio']>;
  fechaCreacion: FormControl<HeladoFormRawValue['fechaCreacion']>;
  fabricante: FormControl<HeladoFormRawValue['fabricante']>;
  ingredientes: FormControl<HeladoFormRawValue['ingredientes']>;
};

export type HeladoFormGroup = FormGroup<HeladoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HeladoFormService {
  createHeladoFormGroup(helado: HeladoFormGroupInput = { id: null }): HeladoFormGroup {
    const heladoRawValue = this.convertHeladoToHeladoRawValue({
      ...this.getFormDefaults(),
      ...helado,
    });
    return new FormGroup<HeladoFormGroupContent>({
      id: new FormControl(
        { value: heladoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(heladoRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(300)],
      }),
      enOferta: new FormControl(heladoRawValue.enOferta, {
        validators: [Validators.required],
      }),
      precioOferta: new FormControl(heladoRawValue.precioOferta),
      precio: new FormControl(heladoRawValue.precio, {
        validators: [Validators.required],
      }),
      fechaCreacion: new FormControl(heladoRawValue.fechaCreacion, {
        validators: [Validators.required],
      }),
      fabricante: new FormControl(heladoRawValue.fabricante, {
        validators: [Validators.required],
      }),
      ingredientes: new FormControl(heladoRawValue.ingredientes ?? []),
    });
  }

  getHelado(form: HeladoFormGroup): IHelado | NewHelado {
    return this.convertHeladoRawValueToHelado(form.getRawValue() as HeladoFormRawValue | NewHeladoFormRawValue);
  }

  resetForm(form: HeladoFormGroup, helado: HeladoFormGroupInput): void {
    const heladoRawValue = this.convertHeladoToHeladoRawValue({ ...this.getFormDefaults(), ...helado });
    form.reset(
      {
        ...heladoRawValue,
        id: { value: heladoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HeladoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      enOferta: false,
      fechaCreacion: currentTime,
      ingredientes: [],
    };
  }

  private convertHeladoRawValueToHelado(rawHelado: HeladoFormRawValue | NewHeladoFormRawValue): IHelado | NewHelado {
    return {
      ...rawHelado,
      fechaCreacion: dayjs(rawHelado.fechaCreacion, DATE_TIME_FORMAT),
    };
  }

  private convertHeladoToHeladoRawValue(
    helado: IHelado | (Partial<NewHelado> & HeladoFormDefaults)
  ): HeladoFormRawValue | PartialWithRequiredKeyOf<NewHeladoFormRawValue> {
    return {
      ...helado,
      fechaCreacion: helado.fechaCreacion ? helado.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      ingredientes: helado.ingredientes ?? [],
    };
  }
}
