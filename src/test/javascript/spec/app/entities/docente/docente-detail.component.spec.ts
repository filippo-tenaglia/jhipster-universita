/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UniversitaTestModule } from '../../../test.module';
import { DocenteDetailComponent } from 'app/entities/docente/docente-detail.component';
import { Docente } from 'app/shared/model/docente.model';

describe('Component Tests', () => {
    describe('Docente Management Detail Component', () => {
        let comp: DocenteDetailComponent;
        let fixture: ComponentFixture<DocenteDetailComponent>;
        const route = ({ data: of({ docente: new Docente(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [DocenteDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DocenteDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DocenteDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.docente).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
