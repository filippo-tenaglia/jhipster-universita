import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStudente } from 'app/shared/model/studente.model';

@Component({
    selector: 'jhi-studente-detail',
    templateUrl: './studente-detail.component.html'
})
export class StudenteDetailComponent implements OnInit {
    studente: IStudente;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ studente }) => {
            this.studente = studente;
        });
    }

    previousState() {
        window.history.back();
    }
}
