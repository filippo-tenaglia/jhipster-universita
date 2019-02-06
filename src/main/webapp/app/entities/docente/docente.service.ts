import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDocente } from 'app/shared/model/docente.model';

type EntityResponseType = HttpResponse<IDocente>;
type EntityArrayResponseType = HttpResponse<IDocente[]>;

@Injectable({ providedIn: 'root' })
export class DocenteService {
    public resourceUrl = SERVER_API_URL + 'api/docentes';

    constructor(protected http: HttpClient) {}

    create(docente: IDocente): Observable<EntityResponseType> {
        return this.http.post<IDocente>(this.resourceUrl, docente, { observe: 'response' });
    }

    update(docente: IDocente): Observable<EntityResponseType> {
        return this.http.put<IDocente>(this.resourceUrl, docente, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDocente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDocente[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
