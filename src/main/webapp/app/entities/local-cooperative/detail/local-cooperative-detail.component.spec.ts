import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocalCooperativeDetailComponent } from './local-cooperative-detail.component';

describe('Component Tests', () => {
  describe('LocalCooperative Management Detail Component', () => {
    let comp: LocalCooperativeDetailComponent;
    let fixture: ComponentFixture<LocalCooperativeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LocalCooperativeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ localCooperative: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LocalCooperativeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LocalCooperativeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load localCooperative on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.localCooperative).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
