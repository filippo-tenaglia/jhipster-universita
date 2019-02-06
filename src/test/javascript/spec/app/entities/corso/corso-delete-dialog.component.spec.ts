/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { UniversitaTestModule } from '../../../test.module';
import { CorsoDeleteDialogComponent } from 'app/entities/corso/corso-delete-dialog.component';
import { CorsoService } from 'app/entities/corso/corso.service';

describe('Component Tests', () => {
    describe('Corso Management Delete Component', () => {
        let comp: CorsoDeleteDialogComponent;
        let fixture: ComponentFixture<CorsoDeleteDialogComponent>;
        let service: CorsoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [UniversitaTestModule],
                declarations: [CorsoDeleteDialogComponent]
            })
                .overrideTemplate(CorsoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CorsoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorsoService);
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
