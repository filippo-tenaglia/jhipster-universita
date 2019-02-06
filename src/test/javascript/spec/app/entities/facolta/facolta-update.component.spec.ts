/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UniversitaTestModule } from '../../../test.module';
import { FacoltaUpdateComponent } from 'app/entities/facolta/facolta-update.component';
import { FacoltaService } from 'app/entities/facolta/facolta.service';
import { Facolta } from 'app/shared/model/facolta.model';

describe('Component Tests', () => {
    describe('Facolta Management Update Component', () => {
        let comp: FacoltaUpdateComponent;
        let fixture: ComponentFixture<FacoltaUpdateComponent>;
        let service: FacoltaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [FacoltaUpdateComponent]
            })
                .overrideTemplate(FacoltaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FacoltaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FacoltaService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Facolta(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.facolta = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Facolta();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.facolta = entity;
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
