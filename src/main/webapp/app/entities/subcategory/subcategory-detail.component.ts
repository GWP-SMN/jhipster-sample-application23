import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubcategory } from 'app/shared/model/subcategory.model';

@Component({
    selector: 'jhi-subcategory-detail',
    templateUrl: './subcategory-detail.component.html'
})
export class SubcategoryDetailComponent implements OnInit {
    subcategory: ISubcategory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ subcategory }) => {
            this.subcategory = subcategory;
        });
    }

    previousState() {
        window.history.back();
    }
}
