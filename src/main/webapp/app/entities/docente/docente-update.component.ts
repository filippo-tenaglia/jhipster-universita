import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDocente } from 'app/shared/model/docente.model';
import { DocenteService } from './docente.service';
import { IFacolta } from 'app/shared/model/facolta.model';
import { FacoltaService } from 'app/entities/facolta';

@Component({
    selector: 'jhi-docente-update',
    templateUrl: './docente-update.component.html'
})
export class DocenteUpdateComponent implements OnInit {
    docente: IDocente;
    isSaving: boolean;

    facoltas: IFacolta[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected docenteService: DocenteService,
        protected facoltaService: FacoltaService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ docente }) => {
            this.docente = docente;
        });
        this.facoltaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IFacolta[]>) => mayBeOk.ok),
                map((response: HttpResponse<IFacolta[]>) => response.body)
            )
            .subscribe((res: IFacolta[]) => (this.facoltas = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.docente.id !== undefined) {
            this.subscribeToSaveResponse(this.docenteService.update(this.docente));
        } else {
            this.subscribeToSaveResponse(this.docenteService.create(this.docente));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocente>>) {
        result.subscribe((res: HttpResponse<IDocente>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackFacoltaById(index: number, item: IFacolta) {
        return item.id;
    }
}
