import {
    Component,
    Input,
    OnInit,
    OnDestroy,
    ViewEncapsulation,
} from '@angular/core';

import { Subject } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { takeUntil } from 'rxjs/operators';

import { CustomerNrPageModel } from 'src/app/home/models/customer-group/customer-nr-page.model';
import { CustomerGroupService } from 'src/app/home/services/customer-group/customer-group.service';

@Component({
    selector: 'backoffice-customer-popup',
    templateUrl: './customer-popup.component.html',
    styleUrls: ['./customer-popup.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class CustomerPopupComponent implements OnDestroy, OnInit {
    @Input() collectionShortName: string = null;
    public isLoaded = false;
    public customerNrs: string[] = [];
    public totalElements = 0;
    public size = 0;
    public page = 0;
    public totalPages = 0;

    private isDestroyed$ = new Subject<boolean>();

    constructor(
        private customerGroupService: CustomerGroupService,
        private bsModalRef: BsModalRef
    ) { }

    ngOnInit() {
        if (this.collectionShortName) {
            this.getCustomerNrs();
        }
    }

    ngOnDestroy() {
        this.isDestroyed$.next(true);
        this.isDestroyed$.complete();
    }

    closeModal() {
        this.bsModalRef.hide();
    }

    showMore() {
        if (this.page < this.totalPages - 1) {
            this.page = this.page + 1;
            this.getCustomerNrs();
        }
    }

    private getCustomerNrs() {
        this.isLoaded = false;

        this.customerGroupService
            .getCustomersByCollectionShortName(this.collectionShortName, this.page).pipe(
                takeUntil(this.isDestroyed$)
            )
            .subscribe(
                (x: CustomerNrPageModel) => {
                    if (x) {
                        this.customerNrs = this.customerNrs.concat(x.content);
                        this.totalElements = x.totalElements;
                        this.size = x.size;
                        this.page = x.number;
                        this.totalPages = x.totalPages;

                        this.isLoaded = true;
                    }
                },
                (err) => {
                    this.customerNrs = null;
                    this.isLoaded = true;
                }
            );
    }
}
