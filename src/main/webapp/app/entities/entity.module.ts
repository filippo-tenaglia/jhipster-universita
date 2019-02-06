import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'facolta',
                loadChildren: './facolta/facolta.module#UniversitaFacoltaModule'
            },
            {
                path: 'corso',
                loadChildren: './corso/corso.module#UniversitaCorsoModule'
            },
            {
                path: 'studente',
                loadChildren: './studente/studente.module#UniversitaStudenteModule'
            },
            {
                path: 'docente',
                loadChildren: './docente/docente.module#UniversitaDocenteModule'
            },
            {
                path: 'docente',
                loadChildren: './docente/docente.module#UniversitaDocenteModule'
            },
            {
                path: 'facolta',
                loadChildren: './facolta/facolta.module#UniversitaFacoltaModule'
            },
            {
                path: 'facolta',
                loadChildren: './facolta/facolta.module#UniversitaFacoltaModule'
            },
            {
                path: 'corso',
                loadChildren: './corso/corso.module#UniversitaCorsoModule'
            },
            {
                path: 'studente',
                loadChildren: './studente/studente.module#UniversitaStudenteModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UniversitaEntityModule {}
