import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationWithGradleSharedModule } from 'app/shared';
import {
    ValidationLogComponent,
    ValidationLogDetailComponent,
    ValidationLogUpdateComponent,
    ValidationLogDeletePopupComponent,
    ValidationLogDeleteDialogComponent,
    validationLogRoute,
    validationLogPopupRoute
} from './';

const ENTITY_STATES = [...validationLogRoute, ...validationLogPopupRoute];

@NgModule({
    imports: [JhipsterSampleApplicationWithGradleSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ValidationLogComponent,
        ValidationLogDetailComponent,
        ValidationLogUpdateComponent,
        ValidationLogDeleteDialogComponent,
        ValidationLogDeletePopupComponent
    ],
    entryComponents: [
        ValidationLogComponent,
        ValidationLogUpdateComponent,
        ValidationLogDeleteDialogComponent,
        ValidationLogDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterSampleApplicationWithGradleValidationLogModule {}
