import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIngrediente, NewIngrediente } from '../ingrediente.model';

export type PartialUpdateIngrediente = Partial<IIngrediente> & Pick<IIngrediente, 'id'>;

export type EntityResponseType = HttpResponse<IIngrediente>;
export type EntityArrayResponseType = HttpResponse<IIngrediente[]>;

@Injectable({ providedIn: 'root' })
export class IngredienteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ingredientes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ingrediente: NewIngrediente): Observable<EntityResponseType> {
    return this.http.post<IIngrediente>(this.resourceUrl, ingrediente, { observe: 'response' });
  }

  update(ingrediente: IIngrediente): Observable<EntityResponseType> {
    return this.http.put<IIngrediente>(`${this.resourceUrl}/${this.getIngredienteIdentifier(ingrediente)}`, ingrediente, {
      observe: 'response',
    });
  }

  partialUpdate(ingrediente: PartialUpdateIngrediente): Observable<EntityResponseType> {
    return this.http.patch<IIngrediente>(`${this.resourceUrl}/${this.getIngredienteIdentifier(ingrediente)}`, ingrediente, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIngrediente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIngrediente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIngredienteIdentifier(ingrediente: Pick<IIngrediente, 'id'>): number {
    return ingrediente.id;
  }

  compareIngrediente(o1: Pick<IIngrediente, 'id'> | null, o2: Pick<IIngrediente, 'id'> | null): boolean {
    return o1 && o2 ? this.getIngredienteIdentifier(o1) === this.getIngredienteIdentifier(o2) : o1 === o2;
  }

  addIngredienteToCollectionIfMissing<Type extends Pick<IIngrediente, 'id'>>(
    ingredienteCollection: Type[],
    ...ingredientesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ingredientes: Type[] = ingredientesToCheck.filter(isPresent);
    if (ingredientes.length > 0) {
      const ingredienteCollectionIdentifiers = ingredienteCollection.map(
        ingredienteItem => this.getIngredienteIdentifier(ingredienteItem)!
      );
      const ingredientesToAdd = ingredientes.filter(ingredienteItem => {
        const ingredienteIdentifier = this.getIngredienteIdentifier(ingredienteItem);
        if (ingredienteCollectionIdentifiers.includes(ingredienteIdentifier)) {
          return false;
        }
        ingredienteCollectionIdentifiers.push(ingredienteIdentifier);
        return true;
      });
      return [...ingredientesToAdd, ...ingredienteCollection];
    }
    return ingredienteCollection;
  }
}
