/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhipsterSampleApplicationWithGradleTestModule } from '../../../test.module';
import { RegistryUpdateComponent } from 'app/entities/registry/registry-update.component';
import { RegistryService } from 'app/entities/registry/registry.service';
import { Registry } from 'app/shared/model/registry.model';

describe('Component Tests', () => {
    describe('Registry Management Update Component', () => {
        let comp: RegistryUpdateComponent;
        let fixture: ComponentFixture<RegistryUpdateComponent>;
        let service: RegistryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterSampleApplicationWithGradleTestModule],
                declarations: [RegistryUpdateComponent]
            })
                .overrideTemplate(RegistryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RegistryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RegistryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Registry(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.registry = entity;
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
                    const entity = new Registry();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.registry = entity;
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
