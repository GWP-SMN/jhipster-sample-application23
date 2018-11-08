/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhipsterSampleApplicationWithGradleTestModule } from '../../../test.module';
import { RegistryDeleteDialogComponent } from 'app/entities/registry/registry-delete-dialog.component';
import { RegistryService } from 'app/entities/registry/registry.service';

describe('Component Tests', () => {
    describe('Registry Management Delete Component', () => {
        let comp: RegistryDeleteDialogComponent;
        let fixture: ComponentFixture<RegistryDeleteDialogComponent>;
        let service: RegistryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterSampleApplicationWithGradleTestModule],
                declarations: [RegistryDeleteDialogComponent]
            })
                .overrideTemplate(RegistryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RegistryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RegistryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
