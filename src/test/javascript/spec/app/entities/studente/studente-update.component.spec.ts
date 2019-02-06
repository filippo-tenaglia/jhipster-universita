/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UniversitaTestModule } from '../../../test.module';
import { StudenteUpdateComponent } from 'app/entities/studente/studente-update.component';
import { StudenteService } from 'app/entities/studente/studente.service';
import { Studente } from 'app/shared/model/studente.model';

describe('Component Tests', () => {
    describe('Studente Management Update Component', () => {
        let comp: StudenteUpdateComponent;
        let fixture: ComponentFixture<StudenteUpdateComponent>;
        let service: StudenteService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [StudenteUpdateComponent]
            })
                .overrideTemplate(StudenteUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StudenteUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudenteService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Studente(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.studente = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Studente();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.studente = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
