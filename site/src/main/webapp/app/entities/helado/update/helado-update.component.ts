import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { HeladoFormService, HeladoFormGroup } from './helado-form.service';
import { IHelado } from '../helado.model';
import { HeladoService } from '../service/helado.service';
import { IFabricante } from 'app/entities/fabricante/fabricante.model';
import { FabricanteService } from 'app/entities/fabricante/service/fabricante.service';
import { IIngrediente } from 'app/entities/ingrediente/ingrediente.model';
import { IngredienteService } from 'app/entities/ingrediente/service/ingrediente.service';

@Component({
  selector: 'jhi-helado-update',
  templateUrl: './helado-update.component.html',
})
export class HeladoUpdateComponent implements OnInit {
  isSaving = false;
  helado: IHelado | null = null;

  fabricantesSharedCollection: IFabricante[] = [];
  ingredientesSharedCollection: IIngrediente[] = [];

  editForm: HeladoFormGroup = this.heladoFormService.createHeladoFormGroup();

  constructor(
    protected heladoService: HeladoService,
    protected heladoFormService: HeladoFormService,
    protected fabricanteService: FabricanteService,
    protected ingredienteService: IngredienteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFabricante = (o1: IFabricante | null, o2: IFabricante | null): boolean => this.fabricanteService.compareFabricante(o1, o2);

  compareIngrediente = (o1: IIngrediente | null, o2: IIngrediente | null): boolean => this.ingredienteService.compareIngrediente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ helado }) => {
      this.helado = helado;
      if (helado) {
        this.updateForm(helado);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const helado = this.heladoFormService.getHelado(this.editForm);
    if (helado.id !== null) {
      this.subscribeToSaveResponse(this.heladoService.update(helado));
    } else {
      this.subscribeToSaveResponse(this.heladoService.create(helado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHelado>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(helado: IHelado): void {
    this.helado = helado;
    this.heladoFormService.resetForm(this.editForm, helado);

    this.fabricantesSharedCollection = this.fabricanteService.addFabricanteToCollectionIfMissing<IFabricante>(
      this.fabricantesSharedCollection,
      helado.fabricante
    );
    this.ingredientesSharedCollection = this.ingredienteService.addIngredienteToCollectionIfMissing<IIngrediente>(
      this.ingredientesSharedCollection,
      ...(helado.ingredientes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fabricanteService
      .query()
      .pipe(map((res: HttpResponse<IFabricante[]>) => res.body ?? []))
      .pipe(
        map((fabricantes: IFabricante[]) =>
          this.fabricanteService.addFabricanteToCollectionIfMissing<IFabricante>(fabricantes, this.helado?.fabricante)
        )
      )
      .subscribe((fabricantes: IFabricante[]) => (this.fabricantesSharedCollection = fabricantes));

    this.ingredienteService
      .query()
      .pipe(map((res: HttpResponse<IIngrediente[]>) => res.body ?? []))
      .pipe(
        map((ingredientes: IIngrediente[]) =>
          this.ingredienteService.addIngredienteToCollectionIfMissing<IIngrediente>(ingredientes, ...(this.helado?.ingredientes ?? []))
        )
      )
      .subscribe((ingredientes: IIngrediente[]) => (this.ingredientesSharedCollection = ingredientes));
  }
}
