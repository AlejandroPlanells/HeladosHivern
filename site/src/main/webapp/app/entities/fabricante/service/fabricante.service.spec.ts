import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFabricante } from '../fabricante.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fabricante.test-samples';

import { FabricanteService } from './fabricante.service';

const requireRestSample: IFabricante = {
  ...sampleWithRequiredData,
};

describe('Fabricante Service', () => {
  let service: FabricanteService;
  let httpMock: HttpTestingController;
  let expectedResult: IFabricante | IFabricante[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FabricanteService);
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

    it('should create a Fabricante', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const fabricante = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fabricante).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Fabricante', () => {
      const fabricante = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fabricante).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Fabricante', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Fabricante', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Fabricante', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFabricanteToCollectionIfMissing', () => {
      it('should add a Fabricante to an empty array', () => {
        const fabricante: IFabricante = sampleWithRequiredData;
        expectedResult = service.addFabricanteToCollectionIfMissing([], fabricante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fabricante);
      });

      it('should not add a Fabricante to an array that contains it', () => {
        const fabricante: IFabricante = sampleWithRequiredData;
        const fabricanteCollection: IFabricante[] = [
          {
            ...fabricante,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFabricanteToCollectionIfMissing(fabricanteCollection, fabricante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Fabricante to an array that doesn't contain it", () => {
        const fabricante: IFabricante = sampleWithRequiredData;
        const fabricanteCollection: IFabricante[] = [sampleWithPartialData];
        expectedResult = service.addFabricanteToCollectionIfMissing(fabricanteCollection, fabricante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fabricante);
      });

      it('should add only unique Fabricante to an array', () => {
        const fabricanteArray: IFabricante[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fabricanteCollection: IFabricante[] = [sampleWithRequiredData];
        expectedResult = service.addFabricanteToCollectionIfMissing(fabricanteCollection, ...fabricanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fabricante: IFabricante = sampleWithRequiredData;
        const fabricante2: IFabricante = sampleWithPartialData;
        expectedResult = service.addFabricanteToCollectionIfMissing([], fabricante, fabricante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fabricante);
        expect(expectedResult).toContain(fabricante2);
      });

      it('should accept null and undefined values', () => {
        const fabricante: IFabricante = sampleWithRequiredData;
        expectedResult = service.addFabricanteToCollectionIfMissing([], null, fabricante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fabricante);
      });

      it('should return initial array if no Fabricante is added', () => {
        const fabricanteCollection: IFabricante[] = [sampleWithRequiredData];
        expectedResult = service.addFabricanteToCollectionIfMissing(fabricanteCollection, undefined, null);
        expect(expectedResult).toEqual(fabricanteCollection);
      });
    });

    describe('compareFabricante', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFabricante(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFabricante(entity1, entity2);
        const compareResult2 = service.compareFabricante(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFabricante(entity1, entity2);
        const compareResult2 = service.compareFabricante(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFabricante(entity1, entity2);
        const compareResult2 = service.compareFabricante(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
