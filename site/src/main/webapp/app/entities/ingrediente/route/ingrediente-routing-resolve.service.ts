import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIngrediente } from '../ingrediente.model';
import { IngredienteService } from '../service/ingrediente.service';

@Injectable({ providedIn: 'root' })
export class IngredienteRoutingResolveService implements Resolve<IIngrediente | null> {
  constructor(protected service: IngredienteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIngrediente | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ingrediente: HttpResponse<IIngrediente>) => {
          if (ingrediente.body) {
            return of(ingrediente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
