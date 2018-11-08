import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValidationLog } from 'app/shared/model/validation-log.model';

@Component({
    selector: 'jhi-validation-log-detail',
    templateUrl: './validation-log-detail.component.html'
})
export class ValidationLogDetailComponent implements OnInit {
    validationLog: IValidationLog;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ validationLog }) => {
            this.validationLog = validationLog;
        });
    }

    previousState() {
        window.history.back();
    }
}
