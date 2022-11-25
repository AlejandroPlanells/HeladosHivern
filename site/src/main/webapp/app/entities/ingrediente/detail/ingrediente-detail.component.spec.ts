import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IngredienteDetailComponent } from './ingrediente-detail.component';

describe('Ingrediente Management Detail Component', () => {
  let comp: IngredienteDetailComponent;
  let fixture: ComponentFixture<IngredienteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IngredienteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ingrediente: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IngredienteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IngredienteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ingrediente on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ingrediente).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
