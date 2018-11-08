import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISubcategory } from 'app/shared/model/subcategory.model';
import { SubcategoryService } from './subcategory.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';

@Component({
    selector: 'jhi-subcategory-update',
    templateUrl: './subcategory-update.component.html'
})
export class SubcategoryUpdateComponent implements OnInit {
    subcategory: ISubcategory;
    isSaving: boolean;

    categories: ICategory[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private subcategoryService: SubcategoryService,
        private categoryService: CategoryService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ subcategory }) => {
            this.subcategory = subcategory;
        });
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.subcategory.id !== undefined) {
            this.subscribeToSaveResponse(this.subcategoryService.update(this.subcategory));
        } else {
            this.subscribeToSaveResponse(this.subcategoryService.create(this.subcategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISubcategory>>) {
        result.subscribe((res: HttpResponse<ISubcategory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }
}
