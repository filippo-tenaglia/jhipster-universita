import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFacolta } from 'app/shared/model/facolta.model';

type EntityResponseType = HttpResponse<IFacolta>;
type EntityArrayResponseType = HttpResponse<IFacolta[]>;

@Injectable({ providedIn: 'root' })
export class FacoltaService {
    public resourceUrl = SERVER_API_URL + 'api/facoltas';

    constructor(protected http: HttpClient) {}

    create(facolta: IFacolta): Observable<EntityResponseType> {
        return this.http.post<IFacolta>(this.resourceUrl, facolta, { observe: 'response' });
    }

    update(facolta: IFacolta): Observable<EntityResponseType> {
        return this.http.put<IFacolta>(this.resourceUrl, facolta, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFacolta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFacolta[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
