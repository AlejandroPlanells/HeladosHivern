import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHelado } from '../helado.model';
import { HeladoService } from '../service/helado.service';

@Injectable({ providedIn: 'root' })
export class HeladoRoutingResolveService implements Resolve<IHelado | null> {
  constructor(protected service: HeladoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHelado | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((helado: HttpResponse<IHelado>) => {
          if (helado.body) {
            return of(helado.body);
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
