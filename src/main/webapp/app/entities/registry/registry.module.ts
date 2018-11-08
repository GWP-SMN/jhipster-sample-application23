import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationWithGradleSharedModule } from 'app/shared';
import {
    RegistryComponent,
    RegistryDetailComponent,
    RegistryUpdateComponent,
    RegistryDeletePopupComponent,
    RegistryDeleteDialogComponent,
    registryRoute,
    registryPopupRoute
} from './';

const ENTITY_STATES = [...registryRoute, ...registryPopupRoute];

@NgModule({
    imports: [JhipsterSampleApplicationWithGradleSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RegistryComponent,
        RegistryDetailComponent,
        RegistryUpdateComponent,
        RegistryDeleteDialogComponent,
        RegistryDeletePopupComponent
    ],
    entryComponents: [RegistryComponent, RegistryUpdateComponent, RegistryDeleteDialogComponent, RegistryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterSampleApplicationWithGradleRegistryModule {}
