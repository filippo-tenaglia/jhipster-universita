import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStudente } from 'app/shared/model/studente.model';

type EntityResponseType = HttpResponse<IStudente>;
type EntityArrayResponseType = HttpResponse<IStudente[]>;

@Injectable({ providedIn: 'root' })
export class StudenteService {
    public resourceUrl = SERVER_API_URL + 'api/studenti';

    constructor(protected http: HttpClient) {}

    create(studente: IStudente): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(studente);
        return this.http
            .post<IStudente>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(studente: IStudente): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(studente);
        return this.http
            .put<IStudente>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IStudente>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStudente[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(studente: IStudente): IStudente {
        const copy: IStudente = Object.assign({}, studente, {
            data_nascita:
                studente.data_nascita != null && studente.data_nascita.isValid() ? studente.data_nascita.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.data_nascita = res.body.data_nascita != null ? moment(res.body.data_nascita) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((studente: IStudente) => {
                studente.data_nascita = studente.data_nascita != null ? moment(studente.data_nascita) : null;
            });
        }
        return res;
    }
}
