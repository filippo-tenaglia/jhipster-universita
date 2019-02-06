import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IStudente } from 'app/shared/model/studente.model';
import { StudenteService } from './studente.service';
import { IFacolta } from 'app/shared/model/facolta.model';
import { FacoltaService } from 'app/entities/facolta';
import { ICorso } from 'app/shared/model/corso.model';
import { CorsoService } from 'app/entities/corso';

@Component({
    selector: 'jhi-studente-update',
    templateUrl: './studente-update.component.html'
})
export class StudenteUpdateComponent implements OnInit {
    studente: IStudente;
    isSaving: boolean;

    facoltas: IFacolta[];

    corsi: ICorso[];
    data_nascitaDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected studenteService: StudenteService,
        protected facoltaService: FacoltaService,
        protected corsoService: CorsoService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ studente }) => {
            this.studente = studente;
        });
        this.facoltaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IFacolta[]>) => mayBeOk.ok),
                map((response: HttpResponse<IFacolta[]>) => response.body)
            )
            .subscribe((res: IFacolta[]) => (this.facoltas = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.corsoService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICorso[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICorso[]>) => response.body)
            )
            .subscribe((res: ICorso[]) => (this.corsi = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.studente.id !== undefined) {
            this.subscribeToSaveResponse(this.studenteService.update(this.studente));
        } else {
            this.subscribeToSaveResponse(this.studenteService.create(this.studente));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudente>>) {
        result.subscribe((res: HttpResponse<IStudente>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCorsoById(index: number, item: ICorso) {
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
