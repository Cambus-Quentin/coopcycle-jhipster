jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocalCooperativeService } from '../service/local-cooperative.service';
import { ILocalCooperative, LocalCooperative } from '../local-cooperative.model';
import { INationalCooperative } from 'app/entities/national-cooperative/national-cooperative.model';
import { NationalCooperativeService } from 'app/entities/national-cooperative/service/national-cooperative.service';

import { LocalCooperativeUpdateComponent } from './local-cooperative-update.component';

describe('Component Tests', () => {
  describe('LocalCooperative Management Update Component', () => {
    let comp: LocalCooperativeUpdateComponent;
    let fixture: ComponentFixture<LocalCooperativeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let localCooperativeService: LocalCooperativeService;
    let nationalCooperativeService: NationalCooperativeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocalCooperativeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LocalCooperativeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocalCooperativeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      localCooperativeService = TestBed.inject(LocalCooperativeService);
      nationalCooperativeService = TestBed.inject(NationalCooperativeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call NationalCooperative query and add missing value', () => {
        const localCooperative: ILocalCooperative = { id: 456 };
        const nationalCooperative: INationalCooperative = { id: 96293 };
        localCooperative.nationalCooperative = nationalCooperative;

        const nationalCooperativeCollection: INationalCooperative[] = [{ id: 85186 }];
        spyOn(nationalCooperativeService, 'query').and.returnValue(of(new HttpResponse({ body: nationalCooperativeCollection })));
        const additionalNationalCooperatives = [nationalCooperative];
        const expectedCollection: INationalCooperative[] = [...additionalNationalCooperatives, ...nationalCooperativeCollection];
        spyOn(nationalCooperativeService, 'addNationalCooperativeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ localCooperative });
        comp.ngOnInit();

        expect(nationalCooperativeService.query).toHaveBeenCalled();
        expect(nationalCooperativeService.addNationalCooperativeToCollectionIfMissing).toHaveBeenCalledWith(
          nationalCooperativeCollection,
          ...additionalNationalCooperatives
        );
        expect(comp.nationalCooperativesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const localCooperative: ILocalCooperative = { id: 456 };
        const nationalCooperative: INationalCooperative = { id: 15364 };
        localCooperative.nationalCooperative = nationalCooperative;

        activatedRoute.data = of({ localCooperative });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(localCooperative));
        expect(comp.nationalCooperativesSharedCollection).toContain(nationalCooperative);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const localCooperative = { id: 123 };
        spyOn(localCooperativeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ localCooperative });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: localCooperative }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(localCooperativeService.update).toHaveBeenCalledWith(localCooperative);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const localCooperative = new LocalCooperative();
        spyOn(localCooperativeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ localCooperative });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: localCooperative }));
        saveSubject.complete();

        // THEN
        expect(localCooperativeService.create).toHaveBeenCalledWith(localCooperative);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const localCooperative = { id: 123 };
        spyOn(localCooperativeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ localCooperative });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(localCooperativeService.update).toHaveBeenCalledWith(localCooperative);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackNationalCooperativeById', () => {
        it('Should return tracked NationalCooperative primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackNationalCooperativeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
