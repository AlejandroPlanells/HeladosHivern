import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFabricante } from '../fabricante.model';
import { FabricanteService } from '../service/fabricante.service';

@Injectable({ providedIn: 'root' })
export class FabricanteRoutingResolveService implements Resolve<IFabricante | null> {
  constructor(protected service: FabricanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFabricante | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fabricante: HttpResponse<IFabricante>) => {
          if (fabricante.body) {
            return of(fabricante.body);
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
