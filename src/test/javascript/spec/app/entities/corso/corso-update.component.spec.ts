/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { UniversitaTestModule } from '../../../test.module';
import { CorsoUpdateComponent } from 'app/entities/corso/corso-update.component';
import { CorsoService } from 'app/entities/corso/corso.service';
import { Corso } from 'app/shared/model/corso.model';

describe('Component Tests', () => {
    describe('Corso Management Update Component', () => {
        let comp: CorsoUpdateComponent;
        let fixture: ComponentFixture<CorsoUpdateComponent>;
        let service: CorsoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [CorsoUpdateComponent]
            })
                .overrideTemplate(CorsoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CorsoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorsoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Corso(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.corso = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Corso();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.corso = entity;
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
