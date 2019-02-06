import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Studente } from 'app/shared/model/studente.model';
import { StudenteService } from './studente.service';
import { StudenteComponent } from './studente.component';
import { StudenteDetailComponent } from './studente-detail.component';
import { StudenteUpdateComponent } from './studente-update.component';
import { StudenteDeletePopupComponent } from './studente-delete-dialog.component';
import { IStudente } from 'app/shared/model/studente.model';

@Injectable({ providedIn: 'root' })
export class StudenteResolve implements Resolve<IStudente> {
    constructor(private service: StudenteService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStudente> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Studente>) => response.ok),
                map((studente: HttpResponse<Studente>) => studente.body)
            );
        }
        return of(new Studente());
    }
}

export const studenteRoute: Routes = [
    {
        path: '',
        component: StudenteComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'universitaApp.studente.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: StudenteDetailComponent,
        resolve: {
            studente: StudenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.studente.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: StudenteUpdateComponent,
        resolve: {
            studente: StudenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.studente.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: StudenteUpdateComponent,
        resolve: {
            studente: StudenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.studente.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: StudenteDeletePopupComponent,
        resolve: {
            studente: StudenteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.studente.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
