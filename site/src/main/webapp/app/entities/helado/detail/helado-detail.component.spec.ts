import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HeladoDetailComponent } from './helado-detail.component';

describe('Helado Management Detail Component', () => {
  let comp: HeladoDetailComponent;
  let fixture: ComponentFixture<HeladoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeladoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ helado: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HeladoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HeladoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load helado on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.helado).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
