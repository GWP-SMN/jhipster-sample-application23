import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationWithGradleSharedModule } from 'app/shared';
import {
    KeyComponent,
    KeyDetailComponent,
    KeyUpdateComponent,
    KeyDeletePopupComponent,
    KeyDeleteDialogComponent,
    keyRoute,
    keyPopupRoute
} from './';

const ENTITY_STATES = [...keyRoute, ...keyPopupRoute];

@NgModule({
    imports: [JhipsterSampleApplicationWithGradleSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [KeyComponent, KeyDetailComponent, KeyUpdateComponent, KeyDeleteDialogComponent, KeyDeletePopupComponent],
    entryComponents: [KeyComponent, KeyUpdateComponent, KeyDeleteDialogComponent, KeyDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterSampleApplicationWithGradleKeyModule {}
