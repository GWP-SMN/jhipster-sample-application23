import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IRegistry } from 'app/shared/model/registry.model';
import { RegistryService } from './registry.service';
import { IApp } from 'app/shared/model/app.model';
import { AppService } from 'app/entities/app';
import { ISubcategory } from 'app/shared/model/subcategory.model';
import { SubcategoryService } from 'app/entities/subcategory';

@Component({
    selector: 'jhi-registry-update',
    templateUrl: './registry-update.component.html'
})
export class RegistryUpdateComponent implements OnInit {
    registry: IRegistry;
    isSaving: boolean;

    apps: IApp[];

    subcategories: ISubcategory[];
    birthdateDp: any;
    resetDate: string;
    activationDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private registryService: RegistryService,
        private appService: AppService,
        private subcategoryService: SubcategoryService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ registry }) => {
            this.registry = registry;
            this.resetDate = this.registry.resetDate != null ? this.registry.resetDate.format(DATE_TIME_FORMAT) : null;
            this.activationDate = this.registry.activationDate != null ? this.registry.activationDate.format(DATE_TIME_FORMAT) : null;
        });
        this.appService.query().subscribe(
            (res: HttpResponse<IApp[]>) => {
                this.apps = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.subcategoryService.query().subscribe(
            (res: HttpResponse<ISubcategory[]>) => {
                this.subcategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.registry.resetDate = this.resetDate != null ? moment(this.resetDate, DATE_TIME_FORMAT) : null;
        this.registry.activationDate = this.activationDate != null ? moment(this.activationDate, DATE_TIME_FORMAT) : null;
        if (this.registry.id !== undefined) {
            this.subscribeToSaveResponse(this.registryService.update(this.registry));
        } else {
            this.subscribeToSaveResponse(this.registryService.create(this.registry));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRegistry>>) {
        result.subscribe((res: HttpResponse<IRegistry>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAppById(index: number, item: IApp) {
        return item.id;
    }

    trackSubcategoryById(index: number, item: ISubcategory) {
        return item.id;
    }
}
