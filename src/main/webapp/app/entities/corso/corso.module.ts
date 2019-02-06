import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { UniversitaSharedModule } from 'app/shared';
import {
    CorsoComponent,
    CorsoDetailComponent,
    CorsoUpdateComponent,
    CorsoDeletePopupComponent,
    CorsoDeleteDialogComponent,
    corsoRoute,
    corsoPopupRoute
} from './';

const ENTITY_STATES = [...corsoRoute, ...corsoPopupRoute];

@NgModule({
    imports: [UniversitaSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CorsoComponent, CorsoDetailComponent, CorsoUpdateComponent, CorsoDeleteDialogComponent, CorsoDeletePopupComponent],
    entryComponents: [CorsoComponent, CorsoUpdateComponent, CorsoDeleteDialogComponent, CorsoDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UniversitaCorsoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
