import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICorso } from 'app/shared/model/corso.model';

type EntityResponseType = HttpResponse<ICorso>;
type EntityArrayResponseType = HttpResponse<ICorso[]>;

@Injectable({ providedIn: 'root' })
export class CorsoService {
    public resourceUrl = SERVER_API_URL + 'api/corsi';

    constructor(protected http: HttpClient) {}

    create(corso: ICorso): Observable<EntityResponseType> {
        return this.http.post<ICorso>(this.resourceUrl, corso, { observe: 'response' });
    }

    update(corso: ICorso): Observable<EntityResponseType> {
        return this.http.put<ICorso>(this.resourceUrl, corso, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICorso>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICorso[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
