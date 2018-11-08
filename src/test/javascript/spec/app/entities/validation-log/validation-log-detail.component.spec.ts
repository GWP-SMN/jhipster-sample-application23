/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationWithGradleTestModule } from '../../../test.module';
import { ValidationLogDetailComponent } from 'app/entities/validation-log/validation-log-detail.component';
import { ValidationLog } from 'app/shared/model/validation-log.model';

describe('Component Tests', () => {
    describe('ValidationLog Management Detail Component', () => {
        let comp: ValidationLogDetailComponent;
        let fixture: ComponentFixture<ValidationLogDetailComponent>;
        const route = ({ data: of({ validationLog: new ValidationLog(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterSampleApplicationWithGradleTestModule],
                declarations: [ValidationLogDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ValidationLogDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ValidationLogDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.validationLog).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
