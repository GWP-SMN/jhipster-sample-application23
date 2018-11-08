/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhipsterSampleApplicationWithGradleTestModule } from '../../../test.module';
import { ValidationLogDeleteDialogComponent } from 'app/entities/validation-log/validation-log-delete-dialog.component';
import { ValidationLogService } from 'app/entities/validation-log/validation-log.service';

describe('Component Tests', () => {
    describe('ValidationLog Management Delete Component', () => {
        let comp: ValidationLogDeleteDialogComponent;
        let fixture: ComponentFixture<ValidationLogDeleteDialogComponent>;
        let service: ValidationLogService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterSampleApplicationWithGradleTestModule],
                declarations: [ValidationLogDeleteDialogComponent]
            })
                .overrideTemplate(ValidationLogDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ValidationLogDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ValidationLogService);
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
