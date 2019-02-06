import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { UniversitaSharedModule } from 'app/shared';
import {
    FacoltaComponent,
    FacoltaDetailComponent,
    FacoltaUpdateComponent,
    FacoltaDeletePopupComponent,
    FacoltaDeleteDialogComponent,
    facoltaRoute,
    facoltaPopupRoute
} from './';

const ENTITY_STATES = [...facoltaRoute, ...facoltaPopupRoute];

@NgModule({
    imports: [UniversitaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FacoltaComponent,
        FacoltaDetailComponent,
        FacoltaUpdateComponent,
        FacoltaDeleteDialogComponent,
        FacoltaDeletePopupComponent
    ],
    entryComponents: [FacoltaComponent, FacoltaUpdateComponent, FacoltaDeleteDialogComponent, FacoltaDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UniversitaFacoltaModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
