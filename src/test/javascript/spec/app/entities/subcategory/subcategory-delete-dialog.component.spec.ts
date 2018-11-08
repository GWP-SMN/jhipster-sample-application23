/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhipsterSampleApplicationWithGradleTestModule } from '../../../test.module';
import { SubcategoryDeleteDialogComponent } from 'app/entities/subcategory/subcategory-delete-dialog.component';
import { SubcategoryService } from 'app/entities/subcategory/subcategory.service';

describe('Component Tests', () => {
    describe('Subcategory Management Delete Component', () => {
        let comp: SubcategoryDeleteDialogComponent;
        let fixture: ComponentFixture<SubcategoryDeleteDialogComponent>;
        let service: SubcategoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterSampleApplicationWithGradleTestModule],
                declarations: [SubcategoryDeleteDialogComponent]
            })
                .overrideTemplate(SubcategoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SubcategoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubcategoryService);
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
