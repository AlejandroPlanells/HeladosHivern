import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIngrediente } from '../ingrediente.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ingrediente.test-samples';

import { IngredienteService } from './ingrediente.service';

const requireRestSample: IIngrediente = {
  ...sampleWithRequiredData,
};

describe('Ingrediente Service', () => {
  let service: IngredienteService;
  let httpMock: HttpTestingController;
  let expectedResult: IIngrediente | IIngrediente[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IngredienteService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Ingrediente', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ingrediente = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ingrediente).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ingrediente', () => {
      const ingrediente = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ingrediente).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ingrediente', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ingrediente', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ingrediente', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIngredienteToCollectionIfMissing', () => {
      it('should add a Ingrediente to an empty array', () => {
        const ingrediente: IIngrediente = sampleWithRequiredData;
        expectedResult = service.addIngredienteToCollectionIfMissing([], ingrediente);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ingrediente);
      });

      it('should not add a Ingrediente to an array that contains it', () => {
        const ingrediente: IIngrediente = sampleWithRequiredData;
        const ingredienteCollection: IIngrediente[] = [
          {
            ...ingrediente,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIngredienteToCollectionIfMissing(ingredienteCollection, ingrediente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ingrediente to an array that doesn't contain it", () => {
        const ingrediente: IIngrediente = sampleWithRequiredData;
        const ingredienteCollection: IIngrediente[] = [sampleWithPartialData];
        expectedResult = service.addIngredienteToCollectionIfMissing(ingredienteCollection, ingrediente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ingrediente);
      });

      it('should add only unique Ingrediente to an array', () => {
        const ingredienteArray: IIngrediente[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ingredienteCollection: IIngrediente[] = [sampleWithRequiredData];
        expectedResult = service.addIngredienteToCollectionIfMissing(ingredienteCollection, ...ingredienteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ingrediente: IIngrediente = sampleWithRequiredData;
        const ingrediente2: IIngrediente = sampleWithPartialData;
        expectedResult = service.addIngredienteToCollectionIfMissing([], ingrediente, ingrediente2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ingrediente);
        expect(expectedResult).toContain(ingrediente2);
      });

      it('should accept null and undefined values', () => {
        const ingrediente: IIngrediente = sampleWithRequiredData;
        expectedResult = service.addIngredienteToCollectionIfMissing([], null, ingrediente, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ingrediente);
      });

      it('should return initial array if no Ingrediente is added', () => {
        const ingredienteCollection: IIngrediente[] = [sampleWithRequiredData];
        expectedResult = service.addIngredienteToCollectionIfMissing(ingredienteCollection, undefined, null);
        expect(expectedResult).toEqual(ingredienteCollection);
      });
    });

    describe('compareIngrediente', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIngrediente(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIngrediente(entity1, entity2);
        const compareResult2 = service.compareIngrediente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIngrediente(entity1, entity2);
        const compareResult2 = service.compareIngrediente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIngrediente(entity1, entity2);
        const compareResult2 = service.compareIngrediente(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
