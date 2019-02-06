/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UniversitaTestModule } from '../../../test.module';
import { StudenteDetailComponent } from 'app/entities/studente/studente-detail.component';
import { Studente } from 'app/shared/model/studente.model';

describe('Component Tests', () => {
    describe('Studente Management Detail Component', () => {
        let comp: StudenteDetailComponent;
        let fixture: ComponentFixture<StudenteDetailComponent>;
        const route = ({ data: of({ studente: new Studente(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [StudenteDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(StudenteDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StudenteDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.studente).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
