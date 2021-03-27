import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NationalCooperativeDetailComponent } from './national-cooperative-detail.component';

describe('Component Tests', () => {
  describe('NationalCooperative Management Detail Component', () => {
    let comp: NationalCooperativeDetailComponent;
    let fixture: ComponentFixture<NationalCooperativeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NationalCooperativeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ nationalCooperative: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NationalCooperativeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NationalCooperativeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load nationalCooperative on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nationalCooperative).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
