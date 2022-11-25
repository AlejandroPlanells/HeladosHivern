import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FabricanteFormService, FabricanteFormGroup } from './fabricante-form.service';
import { IFabricante } from '../fabricante.model';
import { FabricanteService } from '../service/fabricante.service';

@Component({
  selector: 'jhi-fabricante-update',
  templateUrl: './fabricante-update.component.html',
})
export class FabricanteUpdateComponent implements OnInit {
  isSaving = false;
  fabricante: IFabricante | null = null;

  editForm: FabricanteFormGroup = this.fabricanteFormService.createFabricanteFormGroup();

  constructor(
    protected fabricanteService: FabricanteService,
    protected fabricanteFormService: FabricanteFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fabricante }) => {
      this.fabricante = fabricante;
      if (fabricante) {
        this.updateForm(fabricante);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fabricante = this.fabricanteFormService.getFabricante(this.editForm);
    if (fabricante.id !== null) {
      this.subscribeToSaveResponse(this.fabricanteService.update(fabricante));
    } else {
      this.subscribeToSaveResponse(this.fabricanteService.create(fabricante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFabricante>>): void {
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

  protected updateForm(fabricante: IFabricante): void {
    this.fabricante = fabricante;
    this.fabricanteFormService.resetForm(this.editForm, fabricante);
  }
}
