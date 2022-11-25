import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IngredienteFormService, IngredienteFormGroup } from './ingrediente-form.service';
import { IIngrediente } from '../ingrediente.model';
import { IngredienteService } from '../service/ingrediente.service';

@Component({
  selector: 'jhi-ingrediente-update',
  templateUrl: './ingrediente-update.component.html',
})
export class IngredienteUpdateComponent implements OnInit {
  isSaving = false;
  ingrediente: IIngrediente | null = null;

  editForm: IngredienteFormGroup = this.ingredienteFormService.createIngredienteFormGroup();

  constructor(
    protected ingredienteService: IngredienteService,
    protected ingredienteFormService: IngredienteFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ingrediente }) => {
      this.ingrediente = ingrediente;
      if (ingrediente) {
        this.updateForm(ingrediente);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ingrediente = this.ingredienteFormService.getIngrediente(this.editForm);
    if (ingrediente.id !== null) {
      this.subscribeToSaveResponse(this.ingredienteService.update(ingrediente));
    } else {
      this.subscribeToSaveResponse(this.ingredienteService.create(ingrediente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIngrediente>>): void {
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

  protected updateForm(ingrediente: IIngrediente): void {
    this.ingrediente = ingrediente;
    this.ingredienteFormService.resetForm(this.editForm, ingrediente);
  }
}
