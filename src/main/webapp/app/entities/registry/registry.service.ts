import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRegistry } from 'app/shared/model/registry.model';

type EntityResponseType = HttpResponse<IRegistry>;
type EntityArrayResponseType = HttpResponse<IRegistry[]>;

@Injectable({ providedIn: 'root' })
export class RegistryService {
    public resourceUrl = SERVER_API_URL + 'api/registries';

    constructor(private http: HttpClient) {}

    create(registry: IRegistry): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(registry);
        return this.http
            .post<IRegistry>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(registry: IRegistry): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(registry);
        return this.http
            .put<IRegistry>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRegistry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRegistry[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(registry: IRegistry): IRegistry {
        const copy: IRegistry = Object.assign({}, registry, {
            birthdate: registry.birthdate != null && registry.birthdate.isValid() ? registry.birthdate.format(DATE_FORMAT) : null,
            resetDate: registry.resetDate != null && registry.resetDate.isValid() ? registry.resetDate.toJSON() : null,
            activationDate: registry.activationDate != null && registry.activationDate.isValid() ? registry.activationDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.birthdate = res.body.birthdate != null ? moment(res.body.birthdate) : null;
            res.body.resetDate = res.body.resetDate != null ? moment(res.body.resetDate) : null;
            res.body.activationDate = res.body.activationDate != null ? moment(res.body.activationDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((registry: IRegistry) => {
                registry.birthdate = registry.birthdate != null ? moment(registry.birthdate) : null;
                registry.resetDate = registry.resetDate != null ? moment(registry.resetDate) : null;
                registry.activationDate = registry.activationDate != null ? moment(registry.activationDate) : null;
            });
        }
        return res;
    }
}
