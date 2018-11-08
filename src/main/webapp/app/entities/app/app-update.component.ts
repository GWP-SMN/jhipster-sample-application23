import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IApp } from 'app/shared/model/app.model';
import { AppService } from './app.service';
import { ISubcategory } from 'app/shared/model/subcategory.model';
import { SubcategoryService } from 'app/entities/subcategory';

@Component({
    selector: 'jhi-app-update',
    templateUrl: './app-update.component.html'
})
export class AppUpdateComponent implements OnInit {
    app: IApp;
    isSaving: boolean;

    subcategories: ISubcategory[];

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private appService: AppService,
        private subcategoryService: SubcategoryService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ app }) => {
            this.app = app;
        });
        this.subcategoryService.query().subscribe(
            (res: HttpResponse<ISubcategory[]>) => {
                this.subcategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.app, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.app.id !== undefined) {
            this.subscribeToSaveResponse(this.appService.update(this.app));
        } else {
            this.subscribeToSaveResponse(this.appService.create(this.app));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IApp>>) {
        result.subscribe((res: HttpResponse<IApp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSubcategoryById(index: number, item: ISubcategory) {
        return item.id;
    }
}
