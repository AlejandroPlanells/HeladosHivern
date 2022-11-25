import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IngredienteFormService } from './ingrediente-form.service';
import { IngredienteService } from '../service/ingrediente.service';
import { IIngrediente } from '../ingrediente.model';

import { IngredienteUpdateComponent } from './ingrediente-update.component';

describe('Ingrediente Management Update Component', () => {
  let comp: IngredienteUpdateComponent;
  let fixture: ComponentFixture<IngredienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ingredienteFormService: IngredienteFormService;
  let ingredienteService: IngredienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IngredienteUpdateComponent],
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
      .overrideTemplate(IngredienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IngredienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ingredienteFormService = TestBed.inject(IngredienteFormService);
    ingredienteService = TestBed.inject(IngredienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ingrediente: IIngrediente = { id: 456 };

      activatedRoute.data = of({ ingrediente });
      comp.ngOnInit();

      expect(comp.ingrediente).toEqual(ingrediente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngrediente>>();
      const ingrediente = { id: 123 };
      jest.spyOn(ingredienteFormService, 'getIngrediente').mockReturnValue(ingrediente);
      jest.spyOn(ingredienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingrediente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ingrediente }));
      saveSubject.complete();

      // THEN
      expect(ingredienteFormService.getIngrediente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ingredienteService.update).toHaveBeenCalledWith(expect.objectContaining(ingrediente));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngrediente>>();
      const ingrediente = { id: 123 };
      jest.spyOn(ingredienteFormService, 'getIngrediente').mockReturnValue({ id: null });
      jest.spyOn(ingredienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingrediente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ingrediente }));
      saveSubject.complete();

      // THEN
      expect(ingredienteFormService.getIngrediente).toHaveBeenCalled();
      expect(ingredienteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngrediente>>();
      const ingrediente = { id: 123 };
      jest.spyOn(ingredienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingrediente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ingredienteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
