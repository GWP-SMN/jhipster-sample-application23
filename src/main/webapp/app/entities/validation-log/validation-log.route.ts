import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ValidationLog } from 'app/shared/model/validation-log.model';
import { ValidationLogService } from './validation-log.service';
import { ValidationLogComponent } from './validation-log.component';
import { ValidationLogDetailComponent } from './validation-log-detail.component';
import { ValidationLogUpdateComponent } from './validation-log-update.component';
import { ValidationLogDeletePopupComponent } from './validation-log-delete-dialog.component';
import { IValidationLog } from 'app/shared/model/validation-log.model';

@Injectable({ providedIn: 'root' })
export class ValidationLogResolve implements Resolve<IValidationLog> {
    constructor(private service: ValidationLogService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ValidationLog> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ValidationLog>) => response.ok),
                map((validationLog: HttpResponse<ValidationLog>) => validationLog.body)
            );
        }
        return of(new ValidationLog());
    }
}

export const validationLogRoute: Routes = [
    {
        path: 'validation-log',
        component: ValidationLogComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ValidationLogs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'validation-log/:id/view',
        component: ValidationLogDetailComponent,
        resolve: {
            validationLog: ValidationLogResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ValidationLogs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'validation-log/new',
        component: ValidationLogUpdateComponent,
        resolve: {
            validationLog: ValidationLogResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ValidationLogs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'validation-log/:id/edit',
        component: ValidationLogUpdateComponent,
        resolve: {
            validationLog: ValidationLogResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ValidationLogs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const validationLogPopupRoute: Routes = [
    {
        path: 'validation-log/:id/delete',
        component: ValidationLogDeletePopupComponent,
        resolve: {
            validationLog: ValidationLogResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ValidationLogs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
