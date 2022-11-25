import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFabricante, NewFabricante } from '../fabricante.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFabricante for edit and NewFabricanteFormGroupInput for create.
 */
type FabricanteFormGroupInput = IFabricante | PartialWithRequiredKeyOf<NewFabricante>;

type FabricanteFormDefaults = Pick<NewFabricante, 'id'>;

type FabricanteFormGroupContent = {
  id: FormControl<IFabricante['id'] | NewFabricante['id']>;
  nombre: FormControl<IFabricante['nombre']>;
  domicilio: FormControl<IFabricante['domicilio']>;
};

export type FabricanteFormGroup = FormGroup<FabricanteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FabricanteFormService {
  createFabricanteFormGroup(fabricante: FabricanteFormGroupInput = { id: null }): FabricanteFormGroup {
    const fabricanteRawValue = {
      ...this.getFormDefaults(),
      ...fabricante,
    };
    return new FormGroup<FabricanteFormGroupContent>({
      id: new FormControl(
        { value: fabricanteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(fabricanteRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(300)],
      }),
      domicilio: new FormControl(fabricanteRawValue.domicilio, {
        validators: [Validators.required, Validators.maxLength(300)],
      }),
    });
  }

  getFabricante(form: FabricanteFormGroup): IFabricante | NewFabricante {
    return form.getRawValue() as IFabricante | NewFabricante;
  }

  resetForm(form: FabricanteFormGroup, fabricante: FabricanteFormGroupInput): void {
    const fabricanteRawValue = { ...this.getFormDefaults(), ...fabricante };
    form.reset(
      {
        ...fabricanteRawValue,
        id: { value: fabricanteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FabricanteFormDefaults {
    return {
      id: null,
    };
  }
}
