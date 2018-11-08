import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Subcategory } from 'app/shared/model/subcategory.model';
import { SubcategoryService } from './subcategory.service';
import { SubcategoryComponent } from './subcategory.component';
import { SubcategoryDetailComponent } from './subcategory-detail.component';
import { SubcategoryUpdateComponent } from './subcategory-update.component';
import { SubcategoryDeletePopupComponent } from './subcategory-delete-dialog.component';
import { ISubcategory } from 'app/shared/model/subcategory.model';

@Injectable({ providedIn: 'root' })
export class SubcategoryResolve implements Resolve<ISubcategory> {
    constructor(private service: SubcategoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Subcategory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Subcategory>) => response.ok),
                map((subcategory: HttpResponse<Subcategory>) => subcategory.body)
            );
        }
        return of(new Subcategory());
    }
}

export const subcategoryRoute: Routes = [
    {
        path: 'subcategory',
        component: SubcategoryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Subcategories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subcategory/:id/view',
        component: SubcategoryDetailComponent,
        resolve: {
            subcategory: SubcategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subcategories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subcategory/new',
        component: SubcategoryUpdateComponent,
        resolve: {
            subcategory: SubcategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subcategories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subcategory/:id/edit',
        component: SubcategoryUpdateComponent,
        resolve: {
            subcategory: SubcategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subcategories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subcategoryPopupRoute: Routes = [
    {
        path: 'subcategory/:id/delete',
        component: SubcategoryDeletePopupComponent,
        resolve: {
            subcategory: SubcategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subcategories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
