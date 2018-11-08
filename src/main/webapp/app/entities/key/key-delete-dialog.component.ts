import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKey } from 'app/shared/model/key.model';
import { KeyService } from './key.service';

@Component({
    selector: 'jhi-key-delete-dialog',
    templateUrl: './key-delete-dialog.component.html'
})
export class KeyDeleteDialogComponent {
    key: IKey;

    constructor(private keyService: KeyService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.keyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'keyListModification',
                content: 'Deleted an key'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-key-delete-popup',
    template: ''
})
export class KeyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ key }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(KeyDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.key = key;
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
