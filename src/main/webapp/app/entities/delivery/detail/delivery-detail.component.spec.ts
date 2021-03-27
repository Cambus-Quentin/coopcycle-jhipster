import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliveryDetailComponent } from './delivery-detail.component';

describe('Component Tests', () => {
  describe('Delivery Management Detail Component', () => {
    let comp: DeliveryDetailComponent;
    let fixture: ComponentFixture<DeliveryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DeliveryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ delivery: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DeliveryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeliveryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load delivery on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.delivery).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
