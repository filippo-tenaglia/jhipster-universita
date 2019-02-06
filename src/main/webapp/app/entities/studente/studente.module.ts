import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { UniversitaSharedModule } from 'app/shared';
import {
    StudenteComponent,
    StudenteDetailComponent,
    StudenteUpdateComponent,
    StudenteDeletePopupComponent,
    StudenteDeleteDialogComponent,
    studenteRoute,
    studentePopupRoute
} from './';

const ENTITY_STATES = [...studenteRoute, ...studentePopupRoute];

@NgModule({
    imports: [UniversitaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StudenteComponent,
        StudenteDetailComponent,
        StudenteUpdateComponent,
        StudenteDeleteDialogComponent,
        StudenteDeletePopupComponent
    ],
    entryComponents: [StudenteComponent, StudenteUpdateComponent, StudenteDeleteDialogComponent, StudenteDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UniversitaStudenteModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
