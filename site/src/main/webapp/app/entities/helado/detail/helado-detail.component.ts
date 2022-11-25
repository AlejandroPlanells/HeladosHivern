import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHelado } from '../helado.model';

@Component({
  selector: 'jhi-helado-detail',
  templateUrl: './helado-detail.component.html',
})
export class HeladoDetailComponent implements OnInit {
  helado: IHelado | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ helado }) => {
      this.helado = helado;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
