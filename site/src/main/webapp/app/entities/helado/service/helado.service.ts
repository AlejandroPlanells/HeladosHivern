import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHelado, NewHelado } from '../helado.model';

export type PartialUpdateHelado = Partial<IHelado> & Pick<IHelado, 'id'>;

type RestOf<T extends IHelado | NewHelado> = Omit<T, 'fechaCreacion'> & {
  fechaCreacion?: string | null;
};

export type RestHelado = RestOf<IHelado>;

export type NewRestHelado = RestOf<NewHelado>;

export type PartialUpdateRestHelado = RestOf<PartialUpdateHelado>;

export type EntityResponseType = HttpResponse<IHelado>;
export type EntityArrayResponseType = HttpResponse<IHelado[]>;

@Injectable({ providedIn: 'root' })
export class HeladoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/helados');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(helado: NewHelado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(helado);
    return this.http
      .post<RestHelado>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(helado: IHelado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(helado);
    return this.http
      .put<RestHelado>(`${this.resourceUrl}/${this.getHeladoIdentifier(helado)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(helado: PartialUpdateHelado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(helado);
    return this.http
      .patch<RestHelado>(`${this.resourceUrl}/${this.getHeladoIdentifier(helado)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHelado>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHelado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHeladoIdentifier(helado: Pick<IHelado, 'id'>): number {
    return helado.id;
  }

  compareHelado(o1: Pick<IHelado, 'id'> | null, o2: Pick<IHelado, 'id'> | null): boolean {
    return o1 && o2 ? this.getHeladoIdentifier(o1) === this.getHeladoIdentifier(o2) : o1 === o2;
  }

  addHeladoToCollectionIfMissing<Type extends Pick<IHelado, 'id'>>(
    heladoCollection: Type[],
    ...heladosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const helados: Type[] = heladosToCheck.filter(isPresent);
    if (helados.length > 0) {
      const heladoCollectionIdentifiers = heladoCollection.map(heladoItem => this.getHeladoIdentifier(heladoItem)!);
      const heladosToAdd = helados.filter(heladoItem => {
        const heladoIdentifier = this.getHeladoIdentifier(heladoItem);
        if (heladoCollectionIdentifiers.includes(heladoIdentifier)) {
          return false;
        }
        heladoCollectionIdentifiers.push(heladoIdentifier);
        return true;
      });
      return [...heladosToAdd, ...heladoCollection];
    }
    return heladoCollection;
  }

  protected convertDateFromClient<T extends IHelado | NewHelado | PartialUpdateHelado>(helado: T): RestOf<T> {
    return {
      ...helado,
      fechaCreacion: helado.fechaCreacion?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHelado: RestHelado): IHelado {
    return {
      ...restHelado,
      fechaCreacion: restHelado.fechaCreacion ? dayjs(restHelado.fechaCreacion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHelado>): HttpResponse<IHelado> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHelado[]>): HttpResponse<IHelado[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
