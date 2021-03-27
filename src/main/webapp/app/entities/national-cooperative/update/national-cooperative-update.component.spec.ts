jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NationalCooperativeService } from '../service/national-cooperative.service';
import { INationalCooperative, NationalCooperative } from '../national-cooperative.model';

import { NationalCooperativeUpdateComponent } from './national-cooperative-update.component';

describe('Component Tests', () => {
  describe('NationalCooperative Management Update Component', () => {
    let comp: NationalCooperativeUpdateComponent;
    let fixture: ComponentFixture<NationalCooperativeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let nationalCooperativeService: NationalCooperativeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NationalCooperativeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NationalCooperativeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NationalCooperativeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      nationalCooperativeService = TestBed.inject(NationalCooperativeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const nationalCooperative: INationalCooperative = { id: 456 };

        activatedRoute.data = of({ nationalCooperative });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(nationalCooperative));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nationalCooperative = { id: 123 };
        spyOn(nationalCooperativeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nationalCooperative });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nationalCooperative }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(nationalCooperativeService.update).toHaveBeenCalledWith(nationalCooperative);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nationalCooperative = new NationalCooperative();
        spyOn(nationalCooperativeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nationalCooperative });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nationalCooperative }));
        saveSubject.complete();

        // THEN
        expect(nationalCooperativeService.create).toHaveBeenCalledWith(nationalCooperative);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const nationalCooperative = { id: 123 };
        spyOn(nationalCooperativeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ nationalCooperative });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(nationalCooperativeService.update).toHaveBeenCalledWith(nationalCooperative);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
