import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFacolta } from 'app/shared/model/facolta.model';

@Component({
    selector: 'jhi-facolta-detail',
    templateUrl: './facolta-detail.component.html'
})
export class FacoltaDetailComponent implements OnInit {
    facolta: IFacolta;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ facolta }) => {
            this.facolta = facolta;
        });
    }

    previousState() {
        window.history.back();
    }
}
