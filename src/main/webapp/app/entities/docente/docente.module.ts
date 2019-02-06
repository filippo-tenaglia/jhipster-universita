import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { UniversitaSharedModule } from 'app/shared';
import {
    DocenteComponent,
    DocenteDetailComponent,
    DocenteUpdateComponent,
    DocenteDeletePopupComponent,
    DocenteDeleteDialogComponent,
    docenteRoute,
    docentePopupRoute
} from './';

const ENTITY_STATES = [...docenteRoute, ...docentePopupRoute];

@NgModule({
    imports: [UniversitaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DocenteComponent,
        DocenteDetailComponent,
        DocenteUpdateComponent,
        DocenteDeleteDialogComponent,
        DocenteDeletePopupComponent
    ],
    entryComponents: [DocenteComponent, DocenteUpdateComponent, DocenteDeleteDialogComponent, DocenteDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UniversitaDocenteModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
