import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICorso } from 'app/shared/model/corso.model';

@Component({
    selector: 'jhi-corso-detail',
    templateUrl: './corso-detail.component.html'
})
export class CorsoDetailComponent implements OnInit {
    corso: ICorso;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ corso }) => {
            this.corso = corso;
        });
    }

    previousState() {
        window.history.back();
    }
}
