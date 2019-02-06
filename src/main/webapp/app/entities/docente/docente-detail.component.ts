import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocente } from 'app/shared/model/docente.model';

@Component({
    selector: 'jhi-docente-detail',
    templateUrl: './docente-detail.component.html'
})
export class DocenteDetailComponent implements OnInit {
    docente: IDocente;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ docente }) => {
            this.docente = docente;
        });
    }

    previousState() {
        window.history.back();
    }
}
