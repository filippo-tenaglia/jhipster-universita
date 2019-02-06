/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UniversitaTestModule } from '../../../test.module';
import { DocenteDeleteDialogComponent } from 'app/entities/docente/docente-delete-dialog.component';
import { DocenteService } from 'app/entities/docente/docente.service';

describe('Component Tests', () => {
    describe('Docente Management Delete Component', () => {
        let comp: DocenteDeleteDialogComponent;
        let fixture: ComponentFixture<DocenteDeleteDialogComponent>;
        let service: DocenteService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [DocenteDeleteDialogComponent]
            })
                .overrideTemplate(DocenteDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DocenteDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DocenteService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
