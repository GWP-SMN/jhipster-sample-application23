import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Registry } from 'app/shared/model/registry.model';
import { RegistryService } from './registry.service';
import { RegistryComponent } from './registry.component';
import { RegistryDetailComponent } from './registry-detail.component';
import { RegistryUpdateComponent } from './registry-update.component';
import { RegistryDeletePopupComponent } from './registry-delete-dialog.component';
import { IRegistry } from 'app/shared/model/registry.model';

@Injectable({ providedIn: 'root' })
export class RegistryResolve implements Resolve<IRegistry> {
    constructor(private service: RegistryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Registry> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Registry>) => response.ok),
                map((registry: HttpResponse<Registry>) => registry.body)
            );
        }
        return of(new Registry());
    }
}

export const registryRoute: Routes = [
    {
        path: 'registry',
        component: RegistryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Registries'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'registry/:id/view',
        component: RegistryDetailComponent,
        resolve: {
            registry: RegistryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Registries'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'registry/new',
        component: RegistryUpdateComponent,
        resolve: {
            registry: RegistryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Registries'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'registry/:id/edit',
        component: RegistryUpdateComponent,
        resolve: {
            registry: RegistryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Registries'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const registryPopupRoute: Routes = [
    {
        path: 'registry/:id/delete',
        component: RegistryDeletePopupComponent,
        resolve: {
            registry: RegistryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Registries'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
