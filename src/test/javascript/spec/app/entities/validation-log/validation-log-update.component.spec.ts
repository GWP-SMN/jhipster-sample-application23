/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhipsterSampleApplicationWithGradleTestModule } from '../../../test.module';
import { ValidationLogUpdateComponent } from 'app/entities/validation-log/validation-log-update.component';
import { ValidationLogService } from 'app/entities/validation-log/validation-log.service';
import { ValidationLog } from 'app/shared/model/validation-log.model';

describe('Component Tests', () => {
    describe('ValidationLog Management Update Component', () => {
        let comp: ValidationLogUpdateComponent;
        let fixture: ComponentFixture<ValidationLogUpdateComponent>;
        let service: ValidationLogService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterSampleApplicationWithGradleTestModule],
                declarations: [ValidationLogUpdateComponent]
            })
                .overrideTemplate(ValidationLogUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ValidationLogUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ValidationLogService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ValidationLog(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.validationLog = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ValidationLog();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.validationLog = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
