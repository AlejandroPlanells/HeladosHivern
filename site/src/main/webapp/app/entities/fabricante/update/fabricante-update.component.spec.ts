import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FabricanteFormService } from './fabricante-form.service';
import { FabricanteService } from '../service/fabricante.service';
import { IFabricante } from '../fabricante.model';

import { FabricanteUpdateComponent } from './fabricante-update.component';

describe('Fabricante Management Update Component', () => {
  let comp: FabricanteUpdateComponent;
  let fixture: ComponentFixture<FabricanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fabricanteFormService: FabricanteFormService;
  let fabricanteService: FabricanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FabricanteUpdateComponent],
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
      .overrideTemplate(FabricanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FabricanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fabricanteFormService = TestBed.inject(FabricanteFormService);
    fabricanteService = TestBed.inject(FabricanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fabricante: IFabricante = { id: 456 };

      activatedRoute.data = of({ fabricante });
      comp.ngOnInit();

      expect(comp.fabricante).toEqual(fabricante);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFabricante>>();
      const fabricante = { id: 123 };
      jest.spyOn(fabricanteFormService, 'getFabricante').mockReturnValue(fabricante);
      jest.spyOn(fabricanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fabricante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fabricante }));
      saveSubject.complete();

      // THEN
      expect(fabricanteFormService.getFabricante).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fabricanteService.update).toHaveBeenCalledWith(expect.objectContaining(fabricante));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFabricante>>();
      const fabricante = { id: 123 };
      jest.spyOn(fabricanteFormService, 'getFabricante').mockReturnValue({ id: null });
      jest.spyOn(fabricanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fabricante: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fabricante }));
      saveSubject.complete();

      // THEN
      expect(fabricanteFormService.getFabricante).toHaveBeenCalled();
      expect(fabricanteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFabricante>>();
      const fabricante = { id: 123 };
      jest.spyOn(fabricanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fabricante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fabricanteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
