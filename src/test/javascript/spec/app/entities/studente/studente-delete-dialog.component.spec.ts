/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UniversitaTestModule } from '../../../test.module';
import { StudenteDeleteDialogComponent } from 'app/entities/studente/studente-delete-dialog.component';
import { StudenteService } from 'app/entities/studente/studente.service';

describe('Component Tests', () => {
    describe('Studente Management Delete Component', () => {
        let comp: StudenteDeleteDialogComponent;
        let fixture: ComponentFixture<StudenteDeleteDialogComponent>;
        let service: StudenteService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [StudenteDeleteDialogComponent]
            })
                .overrideTemplate(StudenteDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StudenteDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudenteService);
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
