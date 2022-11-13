import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { AppStorageService } from 'src/app/core/services/app-storage.service';

@Component({
    selector: 'connect-customer-memo',
    templateUrl: './customer-memo.component.html',
    styleUrls: ['./customer-memo.component.scss']
})
export class CustomerMemoComponent implements OnInit, OnDestroy {
    @ViewChild('pop', { static: false }) pop: any;

    @Input() set userDetail(user: UserDetail) {
        this.memoInfo = user.customer && user.customer.comment || '';
        this.customerNr = user.custNr || '';
    }

    @Input() set isOpenInfoBox(val: boolean) {
        if(this.memoInfo && this.pop && val) {
            this.clickedMemo(this.pop);
        }
    }

    @Input() isAnimation = false;
    @Input() containerClass: string;

    memoInfo = '';
    customerNr = '';
    private sticked = false;
    constructor(
        private appStore: AppStorageService
    ) { }

    ngOnInit() {
    }

    ngOnDestroy() {
        if(this.pop && this.sticked && this.pop.isOpen) {
            this.closeMemo(this.pop);
        }
    }

    showPop(pop) {
        if (!this.sticked) {
            pop.show();
        }
    }

    leavePop(pop) {
        if (!this.sticked) {
            pop.hide();
        }
    }

    clickedMemo(pop) {
        this.sticked = true;
        pop.show();
    }

    closeMemo(pop) {
        this.sticked = false;
        pop.hide();
        this.appStore.resetOpenInfoBox(this.customerNr);
    }
}
