import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Corso } from 'app/shared/model/corso.model';
import { CorsoService } from './corso.service';
import { CorsoComponent } from './corso.component';
import { CorsoDetailComponent } from './corso-detail.component';
import { CorsoUpdateComponent } from './corso-update.component';
import { CorsoDeletePopupComponent } from './corso-delete-dialog.component';
import { ICorso } from 'app/shared/model/corso.model';

@Injectable({ providedIn: 'root' })
export class CorsoResolve implements Resolve<ICorso> {
    constructor(private service: CorsoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICorso> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Corso>) => response.ok),
                map((corso: HttpResponse<Corso>) => corso.body)
            );
        }
        return of(new Corso());
    }
}

export const corsoRoute: Routes = [
    {
        path: '',
        component: CorsoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'universitaApp.corso.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: CorsoDetailComponent,
        resolve: {
            corso: CorsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.corso.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CorsoUpdateComponent,
        resolve: {
            corso: CorsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.corso.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CorsoUpdateComponent,
        resolve: {
            corso: CorsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.corso.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const corsoPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CorsoDeletePopupComponent,
        resolve: {
            corso: CorsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'universitaApp.corso.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
