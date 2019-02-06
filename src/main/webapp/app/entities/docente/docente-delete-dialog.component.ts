import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDocente } from 'app/shared/model/docente.model';
import { DocenteService } from './docente.service';

@Component({
    selector: 'jhi-docente-delete-dialog',
    templateUrl: './docente-delete-dialog.component.html'
})
export class DocenteDeleteDialogComponent {
    docente: IDocente;

    constructor(protected docenteService: DocenteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.docenteService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'docenteListModification',
                content: 'Deleted an docente'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-docente-delete-popup',
    template: ''
})
export class DocenteDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ docente }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DocenteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.docente = docente;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/docente', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/docente', { outlets: { popup: null } }]);
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
