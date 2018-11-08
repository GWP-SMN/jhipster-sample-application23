import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IKey } from 'app/shared/model/key.model';
import { KeyService } from './key.service';
import { IRegistry } from 'app/shared/model/registry.model';
import { RegistryService } from 'app/entities/registry';
import { IApp } from 'app/shared/model/app.model';
import { AppService } from 'app/entities/app';

@Component({
    selector: 'jhi-key-update',
    templateUrl: './key-update.component.html'
})
export class KeyUpdateComponent implements OnInit {
    key: IKey;
    isSaving: boolean;

    registries: IRegistry[];

    apps: IApp[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private keyService: KeyService,
        private registryService: RegistryService,
        private appService: AppService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ key }) => {
            this.key = key;
        });
        this.registryService.query({ 'keyId.specified': 'false' }).subscribe(
            (res: HttpResponse<IRegistry[]>) => {
                if (!this.key.registryId) {
                    this.registries = res.body;
                } else {
                    this.registryService.find(this.key.registryId).subscribe(
                        (subRes: HttpResponse<IRegistry>) => {
                            this.registries = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.appService.query().subscribe(
            (res: HttpResponse<IApp[]>) => {
                this.apps = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.key.id !== undefined) {
            this.subscribeToSaveResponse(this.keyService.update(this.key));
        } else {
            this.subscribeToSaveResponse(this.keyService.create(this.key));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IKey>>) {
        result.subscribe((res: HttpResponse<IKey>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAppById(index: number, item: IApp) {
        return item.id;
    }
}
