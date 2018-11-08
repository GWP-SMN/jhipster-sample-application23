import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IValidationLog } from 'app/shared/model/validation-log.model';

type EntityResponseType = HttpResponse<IValidationLog>;
type EntityArrayResponseType = HttpResponse<IValidationLog[]>;

@Injectable({ providedIn: 'root' })
export class ValidationLogService {
    public resourceUrl = SERVER_API_URL + 'api/validation-logs';

    constructor(private http: HttpClient) {}

    create(validationLog: IValidationLog): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(validationLog);
        return this.http
            .post<IValidationLog>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(validationLog: IValidationLog): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(validationLog);
        return this.http
            .put<IValidationLog>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IValidationLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IValidationLog[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(validationLog: IValidationLog): IValidationLog {
        const copy: IValidationLog = Object.assign({}, validationLog, {
            dateTime: validationLog.dateTime != null && validationLog.dateTime.isValid() ? validationLog.dateTime.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateTime = res.body.dateTime != null ? moment(res.body.dateTime) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((validationLog: IValidationLog) => {
                validationLog.dateTime = validationLog.dateTime != null ? moment(validationLog.dateTime) : null;
            });
        }
        return res;
    }
}
