import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DishDetailComponent } from './dish-detail.component';

describe('Component Tests', () => {
  describe('Dish Management Detail Component', () => {
    let comp: DishDetailComponent;
    let fixture: ComponentFixture<DishDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DishDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dish: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DishDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DishDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dish on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dish).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
