import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IFacolta } from 'app/shared/model/facolta.model';
import { FacoltaService } from './facolta.service';

@Component({
    selector: 'jhi-facolta-update',
    templateUrl: './facolta-update.component.html'
})
export class FacoltaUpdateComponent implements OnInit {
    facolta: IFacolta;
    isSaving: boolean;

    constructor(protected facoltaService: FacoltaService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ facolta }) => {
            this.facolta = facolta;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.facolta.id !== undefined) {
            this.subscribeToSaveResponse(this.facoltaService.update(this.facolta));
        } else {
            this.subscribeToSaveResponse(this.facoltaService.create(this.facolta));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacolta>>) {
        result.subscribe((res: HttpResponse<IFacolta>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
