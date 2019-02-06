import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICorso } from 'app/shared/model/corso.model';
import { CorsoService } from './corso.service';
import { IFacolta } from 'app/shared/model/facolta.model';
import { FacoltaService } from 'app/entities/facolta';
import { IDocente } from 'app/shared/model/docente.model';
import { DocenteService } from 'app/entities/docente';
import { IStudente } from 'app/shared/model/studente.model';
import { StudenteService } from 'app/entities/studente';

@Component({
    selector: 'jhi-corso-update',
    templateUrl: './corso-update.component.html'
})
export class CorsoUpdateComponent implements OnInit {
    corso: ICorso;
    isSaving: boolean;

    facoltas: IFacolta[];

    docentes: IDocente[];

    studenti: IStudente[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected corsoService: CorsoService,
        protected facoltaService: FacoltaService,
        protected docenteService: DocenteService,
        protected studenteService: StudenteService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ corso }) => {
            this.corso = corso;
        });
        this.facoltaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IFacolta[]>) => mayBeOk.ok),
                map((response: HttpResponse<IFacolta[]>) => response.body)
            )
            .subscribe((res: IFacolta[]) => (this.facoltas = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.docenteService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDocente[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDocente[]>) => response.body)
            )
            .subscribe((res: IDocente[]) => (this.docentes = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.studenteService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IStudente[]>) => mayBeOk.ok),
                map((response: HttpResponse<IStudente[]>) => response.body)
            )
            .subscribe((res: IStudente[]) => (this.studenti = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.corso.id !== undefined) {
            this.subscribeToSaveResponse(this.corsoService.update(this.corso));
        } else {
            this.subscribeToSaveResponse(this.corsoService.create(this.corso));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorso>>) {
        result.subscribe((res: HttpResponse<ICorso>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackDocenteById(index: number, item: IDocente) {
        return item.id;
    }

    trackStudenteById(index: number, item: IStudente) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
