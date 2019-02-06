import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudente } from 'app/shared/model/studente.model';
import { StudenteService } from './studente.service';

@Component({
    selector: 'jhi-studente-delete-dialog',
    templateUrl: './studente-delete-dialog.component.html'
})
export class StudenteDeleteDialogComponent {
    studente: IStudente;

    constructor(protected studenteService: StudenteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.studenteService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'studenteListModification',
                content: 'Deleted an studente'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-studente-delete-popup',
    template: ''
})
export class StudenteDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ studente }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StudenteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.studente = studente;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/studente', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/studente', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
