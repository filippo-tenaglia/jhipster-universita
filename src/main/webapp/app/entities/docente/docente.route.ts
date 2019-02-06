import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Docente } from 'app/shared/model/docente.model';
import { DocenteService } from './docente.service';
import { DocenteComponent } from './docente.component';
import { DocenteDetailComponent } from './docente-detail.component';
import { DocenteUpdateComponent } from './docente-update.component';
import { DocenteDeletePopupComponent } from './docente-delete-dialog.component';
import { IDocente } from 'app/shared/model/docente.model';

@Injectable({ providedIn: 'root' })
export class DocenteResolve implements Resolve<IDocente> {
    constructor(private service: DocenteService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDocente> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Docente>) => response.ok),
                map((docente: HttpResponse<Docente>) => docente.body)
            );
        }
        return of(new Docente());
    }
}

export const docenteRoute: Routes = [
    {
        path: '',
        component: DocenteComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'universitaApp.docente.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DocenteDetailComponent,
        resolve: {
            docente: DocenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.docente.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DocenteUpdateComponent,
        resolve: {
            docente: DocenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.docente.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DocenteUpdateComponent,
        resolve: {
            docente: DocenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.docente.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const docentePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DocenteDeletePopupComponent,
        resolve: {
            docente: DocenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.docente.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
