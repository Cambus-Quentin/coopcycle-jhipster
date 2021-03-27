import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DelivererDetailComponent } from './deliverer-detail.component';

describe('Component Tests', () => {
  describe('Deliverer Management Detail Component', () => {
    let comp: DelivererDetailComponent;
    let fixture: ComponentFixture<DelivererDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DelivererDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ deliverer: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DelivererDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DelivererDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load deliverer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.deliverer).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
