import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { JhipsterSampleApplicationWithGradleSharedModule } from 'app/shared';
import { JhipsterSampleApplicationWithGradleCoreModule } from 'app/core';
import { JhipsterSampleApplicationWithGradleAppRoutingModule } from './app-routing.module';
import { JhipsterSampleApplicationWithGradleHomeModule } from './home/home.module';
import { JhipsterSampleApplicationWithGradleAccountModule } from './account/account.module';
import { JhipsterSampleApplicationWithGradleEntityModule } from './entities/entity.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ErrorComponent } from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        JhipsterSampleApplicationWithGradleAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        JhipsterSampleApplicationWithGradleSharedModule,
        JhipsterSampleApplicationWithGradleCoreModule,
        JhipsterSampleApplicationWithGradleHomeModule,
        JhipsterSampleApplicationWithGradleAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        JhipsterSampleApplicationWithGradleEntityModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class JhipsterSampleApplicationWithGradleAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
