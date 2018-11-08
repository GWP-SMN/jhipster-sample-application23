import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKey } from 'app/shared/model/key.model';

@Component({
    selector: 'jhi-key-detail',
    templateUrl: './key-detail.component.html'
})
export class KeyDetailComponent implements OnInit {
    key: IKey;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ key }) => {
            this.key = key;
        });
    }

    previousState() {
        window.history.back();
    }
}
