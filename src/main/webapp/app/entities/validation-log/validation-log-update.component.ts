import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IValidationLog } from 'app/shared/model/validation-log.model';
import { ValidationLogService } from './validation-log.service';
import { IRegistry } from 'app/shared/model/registry.model';
import { RegistryService } from 'app/entities/registry';

@Component({
    selector: 'jhi-validation-log-update',
    templateUrl: './validation-log-update.component.html'
})
export class ValidationLogUpdateComponent implements OnInit {
    validationLog: IValidationLog;
    isSaving: boolean;

    registries: IRegistry[];
    dateTime: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private validationLogService: ValidationLogService,
        private registryService: RegistryService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ validationLog }) => {
            this.validationLog = validationLog;
            this.dateTime = this.validationLog.dateTime != null ? this.validationLog.dateTime.format(DATE_TIME_FORMAT) : null;
        });
        this.registryService.query().subscribe(
            (res: HttpResponse<IRegistry[]>) => {
                this.registries = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.validationLog.dateTime = this.dateTime != null ? moment(this.dateTime, DATE_TIME_FORMAT) : null;
        if (this.validationLog.id !== undefined) {
            this.subscribeToSaveResponse(this.validationLogService.update(this.validationLog));
        } else {
            this.subscribeToSaveResponse(this.validationLogService.create(this.validationLog));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IValidationLog>>) {
        result.subscribe((res: HttpResponse<IValidationLog>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackRegistryById(index: number, item: IRegistry) {
        return item.id;
    }
}
