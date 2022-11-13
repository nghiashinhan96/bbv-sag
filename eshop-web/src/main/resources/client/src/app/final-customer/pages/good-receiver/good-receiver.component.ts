import { Component, OnInit, OnDestroy } from '@angular/core';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { GoodReceiverModalComponent } from '../good-receiver-modal/good-receiver-modal.component';
import { FinalCustomerModel } from '../../models/final-customer.model';
import { Subscription } from 'rxjs';

@Component({
    selector: 'connect-good-receiver',
    templateUrl: './good-receiver.component.html',
    styleUrls: ['./good-receiver.component.scss']
})
export class GoodReceiverComponent implements OnInit, OnDestroy {

    isShowGoodsReceiverModal;

    customer: FinalCustomerModel = new FinalCustomerModel();
    private subscription: Subscription;
    constructor(
        private appStorage: AppStorageService,
        private modalService: BsModalService
    ) { }

    ngOnInit() {
        this.customer = new FinalCustomerModel(this.appStorage.goodReceiver);
        this.subscription = this.appStorage.observeGoodReceiver().subscribe(res => {
            this.customer = new FinalCustomerModel(res);
        });
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    selectGoodReceiver() {
        this.modalService.show(GoodReceiverModalComponent, {
            initialState: {
                selectedCustomer: this.customer
            },
            class: 'modal-lg',
            ignoreBackdropClick: true
        });
    }
}
