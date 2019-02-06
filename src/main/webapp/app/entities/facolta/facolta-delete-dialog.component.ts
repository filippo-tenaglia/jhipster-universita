import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFacolta } from 'app/shared/model/facolta.model';
import { FacoltaService } from './facolta.service';

@Component({
    selector: 'jhi-facolta-delete-dialog',
    templateUrl: './facolta-delete-dialog.component.html'
})
export class FacoltaDeleteDialogComponent {
    facolta: IFacolta;

    constructor(protected facoltaService: FacoltaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.facoltaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'facoltaListModification',
                content: 'Deleted an facolta'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-facolta-delete-popup',
    template: ''
})
export class FacoltaDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ facolta }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FacoltaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.facolta = facolta;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/facolta', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/facolta', { outlets: { popup: null } }]);
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
