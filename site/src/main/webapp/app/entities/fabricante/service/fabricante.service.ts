import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFabricante, NewFabricante } from '../fabricante.model';

export type PartialUpdateFabricante = Partial<IFabricante> & Pick<IFabricante, 'id'>;

export type EntityResponseType = HttpResponse<IFabricante>;
export type EntityArrayResponseType = HttpResponse<IFabricante[]>;

@Injectable({ providedIn: 'root' })
export class FabricanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fabricantes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fabricante: NewFabricante): Observable<EntityResponseType> {
    return this.http.post<IFabricante>(this.resourceUrl, fabricante, { observe: 'response' });
  }

  update(fabricante: IFabricante): Observable<EntityResponseType> {
    return this.http.put<IFabricante>(`${this.resourceUrl}/${this.getFabricanteIdentifier(fabricante)}`, fabricante, {
      observe: 'response',
    });
  }

  partialUpdate(fabricante: PartialUpdateFabricante): Observable<EntityResponseType> {
    return this.http.patch<IFabricante>(`${this.resourceUrl}/${this.getFabricanteIdentifier(fabricante)}`, fabricante, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFabricante>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFabricante[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFabricanteIdentifier(fabricante: Pick<IFabricante, 'id'>): number {
    return fabricante.id;
  }

  compareFabricante(o1: Pick<IFabricante, 'id'> | null, o2: Pick<IFabricante, 'id'> | null): boolean {
    return o1 && o2 ? this.getFabricanteIdentifier(o1) === this.getFabricanteIdentifier(o2) : o1 === o2;
  }

  addFabricanteToCollectionIfMissing<Type extends Pick<IFabricante, 'id'>>(
    fabricanteCollection: Type[],
    ...fabricantesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fabricantes: Type[] = fabricantesToCheck.filter(isPresent);
    if (fabricantes.length > 0) {
      const fabricanteCollectionIdentifiers = fabricanteCollection.map(fabricanteItem => this.getFabricanteIdentifier(fabricanteItem)!);
      const fabricantesToAdd = fabricantes.filter(fabricanteItem => {
        const fabricanteIdentifier = this.getFabricanteIdentifier(fabricanteItem);
        if (fabricanteCollectionIdentifiers.includes(fabricanteIdentifier)) {
          return false;
        }
        fabricanteCollectionIdentifiers.push(fabricanteIdentifier);
        return true;
      });
      return [...fabricantesToAdd, ...fabricanteCollection];
    }
    return fabricanteCollection;
  }
}
