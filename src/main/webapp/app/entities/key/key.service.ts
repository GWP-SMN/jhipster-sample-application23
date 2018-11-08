import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IKey } from 'app/shared/model/key.model';

type EntityResponseType = HttpResponse<IKey>;
type EntityArrayResponseType = HttpResponse<IKey[]>;

@Injectable({ providedIn: 'root' })
export class KeyService {
    public resourceUrl = SERVER_API_URL + 'api/keys';

    constructor(private http: HttpClient) {}

    create(key: IKey): Observable<EntityResponseType> {
        return this.http.post<IKey>(this.resourceUrl, key, { observe: 'response' });
    }

    update(key: IKey): Observable<EntityResponseType> {
        return this.http.put<IKey>(this.resourceUrl, key, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IKey>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IKey[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
