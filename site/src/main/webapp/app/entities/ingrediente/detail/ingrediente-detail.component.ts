import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIngrediente } from '../ingrediente.model';

@Component({
  selector: 'jhi-ingrediente-detail',
  templateUrl: './ingrediente-detail.component.html',
})
export class IngredienteDetailComponent implements OnInit {
  ingrediente: IIngrediente | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ingrediente }) => {
      this.ingrediente = ingrediente;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
