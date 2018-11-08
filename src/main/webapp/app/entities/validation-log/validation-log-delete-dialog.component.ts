import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IValidationLog } from 'app/shared/model/validation-log.model';
import { ValidationLogService } from './validation-log.service';

@Component({
    selector: 'jhi-validation-log-delete-dialog',
    templateUrl: './validation-log-delete-dialog.component.html'
})
export class ValidationLogDeleteDialogComponent {
    validationLog: IValidationLog;

    constructor(
        private validationLogService: ValidationLogService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.validationLogService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'validationLogListModification',
                content: 'Deleted an validationLog'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-validation-log-delete-popup',
    template: ''
})
export class ValidationLogDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ validationLog }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ValidationLogDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.validationLog = validationLog;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
