import { NgModule } from '@angular/core';

import { JhipsterSampleApplicationWithGradleSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [JhipsterSampleApplicationWithGradleSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [JhipsterSampleApplicationWithGradleSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class JhipsterSampleApplicationWithGradleSharedCommonModule {}
