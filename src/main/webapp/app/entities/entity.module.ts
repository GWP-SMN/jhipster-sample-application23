import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JhipsterSampleApplicationWithGradleAppModule } from './app/app.module';
import { JhipsterSampleApplicationWithGradleRegistryModule } from './registry/registry.module';
import { JhipsterSampleApplicationWithGradleKeyModule } from './key/key.module';
import { JhipsterSampleApplicationWithGradleCategoryModule } from './category/category.module';
import { JhipsterSampleApplicationWithGradleSubcategoryModule } from './subcategory/subcategory.module';
import { JhipsterSampleApplicationWithGradleValidationLogModule } from './validation-log/validation-log.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        JhipsterSampleApplicationWithGradleAppModule,
        JhipsterSampleApplicationWithGradleRegistryModule,
        JhipsterSampleApplicationWithGradleKeyModule,
        JhipsterSampleApplicationWithGradleCategoryModule,
        JhipsterSampleApplicationWithGradleSubcategoryModule,
        JhipsterSampleApplicationWithGradleValidationLogModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterSampleApplicationWithGradleEntityModule {}
