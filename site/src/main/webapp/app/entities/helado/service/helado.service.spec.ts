import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHelado } from '../helado.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../helado.test-samples';

import { HeladoService, RestHelado } from './helado.service';

const requireRestSample: RestHelado = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
};

describe('Helado Service', () => {
  let service: HeladoService;
  let httpMock: HttpTestingController;
  let expectedResult: IHelado | IHelado[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HeladoService);
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

    it('should create a Helado', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const helado = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(helado).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Helado', () => {
      const helado = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(helado).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Helado', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Helado', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Helado', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHeladoToCollectionIfMissing', () => {
      it('should add a Helado to an empty array', () => {
        const helado: IHelado = sampleWithRequiredData;
        expectedResult = service.addHeladoToCollectionIfMissing([], helado);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(helado);
      });

      it('should not add a Helado to an array that contains it', () => {
        const helado: IHelado = sampleWithRequiredData;
        const heladoCollection: IHelado[] = [
          {
            ...helado,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHeladoToCollectionIfMissing(heladoCollection, helado);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Helado to an array that doesn't contain it", () => {
        const helado: IHelado = sampleWithRequiredData;
        const heladoCollection: IHelado[] = [sampleWithPartialData];
        expectedResult = service.addHeladoToCollectionIfMissing(heladoCollection, helado);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(helado);
      });

      it('should add only unique Helado to an array', () => {
        const heladoArray: IHelado[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const heladoCollection: IHelado[] = [sampleWithRequiredData];
        expectedResult = service.addHeladoToCollectionIfMissing(heladoCollection, ...heladoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const helado: IHelado = sampleWithRequiredData;
        const helado2: IHelado = sampleWithPartialData;
        expectedResult = service.addHeladoToCollectionIfMissing([], helado, helado2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(helado);
        expect(expectedResult).toContain(helado2);
      });

      it('should accept null and undefined values', () => {
        const helado: IHelado = sampleWithRequiredData;
        expectedResult = service.addHeladoToCollectionIfMissing([], null, helado, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(helado);
      });

      it('should return initial array if no Helado is added', () => {
        const heladoCollection: IHelado[] = [sampleWithRequiredData];
        expectedResult = service.addHeladoToCollectionIfMissing(heladoCollection, undefined, null);
        expect(expectedResult).toEqual(heladoCollection);
      });
    });

    describe('compareHelado', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHelado(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHelado(entity1, entity2);
        const compareResult2 = service.compareHelado(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHelado(entity1, entity2);
        const compareResult2 = service.compareHelado(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHelado(entity1, entity2);
        const compareResult2 = service.compareHelado(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
