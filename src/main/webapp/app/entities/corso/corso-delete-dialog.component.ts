import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICorso } from 'app/shared/model/corso.model';
import { CorsoService } from './corso.service';

@Component({
    selector: 'jhi-corso-delete-dialog',
    templateUrl: './corso-delete-dialog.component.html'
})
export class CorsoDeleteDialogComponent {
    corso: ICorso;

    constructor(protected corsoService: CorsoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.corsoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'corsoListModification',
                content: 'Deleted an corso'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-corso-delete-popup',
    template: ''
})
export class CorsoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ corso }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CorsoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.corso = corso;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/corso', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/corso', { outlets: { popup: null } }]);
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
