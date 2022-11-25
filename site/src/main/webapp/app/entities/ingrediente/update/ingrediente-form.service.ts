import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIngrediente, NewIngrediente } from '../ingrediente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIngrediente for edit and NewIngredienteFormGroupInput for create.
 */
type IngredienteFormGroupInput = IIngrediente | PartialWithRequiredKeyOf<NewIngrediente>;

type IngredienteFormDefaults = Pick<NewIngrediente, 'id' | 'helados'>;

type IngredienteFormGroupContent = {
  id: FormControl<IIngrediente['id'] | NewIngrediente['id']>;
  nombre: FormControl<IIngrediente['nombre']>;
  descripcion: FormControl<IIngrediente['descripcion']>;
  gr: FormControl<IIngrediente['gr']>;
  cal: FormControl<IIngrediente['cal']>;
  helados: FormControl<IIngrediente['helados']>;
};

export type IngredienteFormGroup = FormGroup<IngredienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IngredienteFormService {
  createIngredienteFormGroup(ingrediente: IngredienteFormGroupInput = { id: null }): IngredienteFormGroup {
    const ingredienteRawValue = {
      ...this.getFormDefaults(),
      ...ingrediente,
    };
    return new FormGroup<IngredienteFormGroupContent>({
      id: new FormControl(
        { value: ingredienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(ingredienteRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(300)],
      }),
      descripcion: new FormControl(ingredienteRawValue.descripcion, {
        validators: [Validators.required, Validators.maxLength(300)],
      }),
      gr: new FormControl(ingredienteRawValue.gr, {
        validators: [Validators.required],
      }),
      cal: new FormControl(ingredienteRawValue.cal, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      helados: new FormControl(ingredienteRawValue.helados ?? []),
    });
  }

  getIngrediente(form: IngredienteFormGroup): IIngrediente | NewIngrediente {
    return form.getRawValue() as IIngrediente | NewIngrediente;
  }

  resetForm(form: IngredienteFormGroup, ingrediente: IngredienteFormGroupInput): void {
    const ingredienteRawValue = { ...this.getFormDefaults(), ...ingrediente };
    form.reset(
      {
        ...ingredienteRawValue,
        id: { value: ingredienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IngredienteFormDefaults {
    return {
      id: null,
      helados: [],
    };
  }
}
