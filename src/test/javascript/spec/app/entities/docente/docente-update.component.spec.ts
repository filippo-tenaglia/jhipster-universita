/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UniversitaTestModule } from '../../../test.module';
import { DocenteUpdateComponent } from 'app/entities/docente/docente-update.component';
import { DocenteService } from 'app/entities/docente/docente.service';
import { Docente } from 'app/shared/model/docente.model';

describe('Component Tests', () => {
    describe('Docente Management Update Component', () => {
        let comp: DocenteUpdateComponent;
        let fixture: ComponentFixture<DocenteUpdateComponent>;
        let service: DocenteService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [DocenteUpdateComponent]
            })
                .overrideTemplate(DocenteUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DocenteUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DocenteService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Docente(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.docente = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Docente();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.docente = entity;
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
