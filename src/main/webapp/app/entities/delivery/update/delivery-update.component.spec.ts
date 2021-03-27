jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeliveryService } from '../service/delivery.service';
import { IDelivery, Delivery } from '../delivery.model';
import { IDeliverer } from 'app/entities/deliverer/deliverer.model';
import { DelivererService } from 'app/entities/deliverer/service/deliverer.service';

import { DeliveryUpdateComponent } from './delivery-update.component';

describe('Component Tests', () => {
  describe('Delivery Management Update Component', () => {
    let comp: DeliveryUpdateComponent;
    let fixture: ComponentFixture<DeliveryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let deliveryService: DeliveryService;
    let delivererService: DelivererService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DeliveryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DeliveryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeliveryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      deliveryService = TestBed.inject(DeliveryService);
      delivererService = TestBed.inject(DelivererService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Deliverer query and add missing value', () => {
        const delivery: IDelivery = { id: 456 };
        const deliverer: IDeliverer = { id: 92618 };
        delivery.deliverer = deliverer;

        const delivererCollection: IDeliverer[] = [{ id: 27501 }];
        spyOn(delivererService, 'query').and.returnValue(of(new HttpResponse({ body: delivererCollection })));
        const additionalDeliverers = [deliverer];
        const expectedCollection: IDeliverer[] = [...additionalDeliverers, ...delivererCollection];
        spyOn(delivererService, 'addDelivererToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ delivery });
        comp.ngOnInit();

        expect(delivererService.query).toHaveBeenCalled();
        expect(delivererService.addDelivererToCollectionIfMissing).toHaveBeenCalledWith(delivererCollection, ...additionalDeliverers);
        expect(comp.deliverersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const delivery: IDelivery = { id: 456 };
        const deliverer: IDeliverer = { id: 45664 };
        delivery.deliverer = deliverer;

        activatedRoute.data = of({ delivery });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(delivery));
        expect(comp.deliverersSharedCollection).toContain(deliverer);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const delivery = { id: 123 };
        spyOn(deliveryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ delivery });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: delivery }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(deliveryService.update).toHaveBeenCalledWith(delivery);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const delivery = new Delivery();
        spyOn(deliveryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ delivery });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: delivery }));
        saveSubject.complete();

        // THEN
        expect(deliveryService.create).toHaveBeenCalledWith(delivery);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const delivery = { id: 123 };
        spyOn(deliveryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ delivery });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(deliveryService.update).toHaveBeenCalledWith(delivery);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDelivererById', () => {
        it('Should return tracked Deliverer primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDelivererById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
