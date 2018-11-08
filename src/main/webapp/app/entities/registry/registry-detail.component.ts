import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegistry } from 'app/shared/model/registry.model';

@Component({
    selector: 'jhi-registry-detail',
    templateUrl: './registry-detail.component.html'
})
export class RegistryDetailComponent implements OnInit {
    registry: IRegistry;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ registry }) => {
            this.registry = registry;
        });
    }

    previousState() {
        window.history.back();
    }
}
