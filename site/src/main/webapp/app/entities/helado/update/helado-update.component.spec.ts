import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HeladoFormService } from './helado-form.service';
import { HeladoService } from '../service/helado.service';
import { IHelado } from '../helado.model';
import { IFabricante } from 'app/entities/fabricante/fabricante.model';
import { FabricanteService } from 'app/entities/fabricante/service/fabricante.service';
import { IIngrediente } from 'app/entities/ingrediente/ingrediente.model';
import { IngredienteService } from 'app/entities/ingrediente/service/ingrediente.service';

import { HeladoUpdateComponent } from './helado-update.component';

describe('Helado Management Update Component', () => {
  let comp: HeladoUpdateComponent;
  let fixture: ComponentFixture<HeladoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let heladoFormService: HeladoFormService;
  let heladoService: HeladoService;
  let fabricanteService: FabricanteService;
  let ingredienteService: IngredienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HeladoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(HeladoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HeladoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    heladoFormService = TestBed.inject(HeladoFormService);
    heladoService = TestBed.inject(HeladoService);
    fabricanteService = TestBed.inject(FabricanteService);
    ingredienteService = TestBed.inject(IngredienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fabricante query and add missing value', () => {
      const helado: IHelado = { id: 456 };
      const fabricante: IFabricante = { id: 39280 };
      helado.fabricante = fabricante;

      const fabricanteCollection: IFabricante[] = [{ id: 19142 }];
      jest.spyOn(fabricanteService, 'query').mockReturnValue(of(new HttpResponse({ body: fabricanteCollection })));
      const additionalFabricantes = [fabricante];
      const expectedCollection: IFabricante[] = [...additionalFabricantes, ...fabricanteCollection];
      jest.spyOn(fabricanteService, 'addFabricanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ helado });
      comp.ngOnInit();

      expect(fabricanteService.query).toHaveBeenCalled();
      expect(fabricanteService.addFabricanteToCollectionIfMissing).toHaveBeenCalledWith(
        fabricanteCollection,
        ...additionalFabricantes.map(expect.objectContaining)
      );
      expect(comp.fabricantesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ingrediente query and add missing value', () => {
      const helado: IHelado = { id: 456 };
      const ingredientes: IIngrediente[] = [{ id: 48379 }];
      helado.ingredientes = ingredientes;

      const ingredienteCollection: IIngrediente[] = [{ id: 92840 }];
      jest.spyOn(ingredienteService, 'query').mockReturnValue(of(new HttpResponse({ body: ingredienteCollection })));
      const additionalIngredientes = [...ingredientes];
      const expectedCollection: IIngrediente[] = [...additionalIngredientes, ...ingredienteCollection];
      jest.spyOn(ingredienteService, 'addIngredienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ helado });
      comp.ngOnInit();

      expect(ingredienteService.query).toHaveBeenCalled();
      expect(ingredienteService.addIngredienteToCollectionIfMissing).toHaveBeenCalledWith(
        ingredienteCollection,
        ...additionalIngredientes.map(expect.objectContaining)
      );
      expect(comp.ingredientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const helado: IHelado = { id: 456 };
      const fabricante: IFabricante = { id: 73921 };
      helado.fabricante = fabricante;
      const ingredientes: IIngrediente = { id: 38922 };
      helado.ingredientes = [ingredientes];

      activatedRoute.data = of({ helado });
      comp.ngOnInit();

      expect(comp.fabricantesSharedCollection).toContain(fabricante);
      expect(comp.ingredientesSharedCollection).toContain(ingredientes);
      expect(comp.helado).toEqual(helado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHelado>>();
      const helado = { id: 123 };
      jest.spyOn(heladoFormService, 'getHelado').mockReturnValue(helado);
      jest.spyOn(heladoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ helado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: helado }));
      saveSubject.complete();

      // THEN
      expect(heladoFormService.getHelado).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(heladoService.update).toHaveBeenCalledWith(expect.objectContaining(helado));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHelado>>();
      const helado = { id: 123 };
      jest.spyOn(heladoFormService, 'getHelado').mockReturnValue({ id: null });
      jest.spyOn(heladoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ helado: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: helado }));
      saveSubject.complete();

      // THEN
      expect(heladoFormService.getHelado).toHaveBeenCalled();
      expect(heladoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHelado>>();
      const helado = { id: 123 };
      jest.spyOn(heladoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ helado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(heladoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFabricante', () => {
      it('Should forward to fabricanteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fabricanteService, 'compareFabricante');
        comp.compareFabricante(entity, entity2);
        expect(fabricanteService.compareFabricante).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIngrediente', () => {
      it('Should forward to ingredienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ingredienteService, 'compareIngrediente');
        comp.compareIngrediente(entity, entity2);
        expect(ingredienteService.compareIngrediente).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
