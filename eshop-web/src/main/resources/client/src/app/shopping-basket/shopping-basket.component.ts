import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';

import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { SubSink } from 'subsink';

import { ShoppingBasketService } from '../core/services/shopping-basket.service';
import { SHOPPING_BASKET_ENUM } from '../core/enums/shopping-basket.enum';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { ShoppingOrderService } from './services/shopping-order.service';
import { UserService } from 'src/app/core/services/user.service';
import { SagConfirmationBoxComponent, SagMessageData } from 'sag-common';

@Component({
    selector: 'connect-shopping-basket',
    templateUrl: './shopping-basket.component.html',
    styleUrls: ['./shopping-basket.component.scss']
})
export class ShoppingBasketComponent implements OnInit, OnDestroy {
    private modalRef: BsModalRef;
    private subs = new SubSink();
    private redirectUrl;
    isConfirmed = false;

    constructor(
        private shoppingBasket: ShoppingBasketService,
        private route: Router,
        private appStorage: AppStorageService,
        private shoppingOrderService: ShoppingOrderService,
        private modalService: BsModalService,
        private userService: UserService
    ) { }

    ngOnInit() {
        this.route.events.subscribe(event => {
            if (event instanceof NavigationStart) {
                this.redirectUrl = event.url;
            }
        });
    }

    ngOnDestroy(): void {
        if (this.shoppingBasket.basketType === SHOPPING_BASKET_ENUM.FINAL) {
            this.shoppingBasket.clearBasket();
        }

        this.shoppingBasket.setBasketType(SHOPPING_BASKET_ENUM.DEFAULT);
        this.shoppingBasket.toggleMiniBasket(true);

        this.subs.unsubscribe();
    }

    canDeactivate() {
        if (this.shoppingBasket.basketType === SHOPPING_BASKET_ENUM.DEFAULT || this.isConfirmed) {
            return true;
        }

        if (this.shoppingOrderService.isSubBasketNotChanged()) {
            return true;
        }

        this.showConfirmModal();
        return false;
    }

    private showConfirmModal() {

        this.modalRef = this.modalService.show(SagConfirmationBoxComponent, {
            ignoreBackdropClick: true,
            class: 'modal-sm',
            initialState: {
                message: 'SHOPPING_BASKET.CONFIRM_SAVE_CHANGES',
                close: () => new Promise((resolve) => {
                    this.isConfirmed = true;
                    this.handleConfirm(resolve);
                }),
                cancel: () => new Promise((resolve) => {
                    this.isConfirmed = true;
                    this.goToRedirectUrl();
                })
            }
        });
    }

    private handleConfirm(resolve) {
        const orderRefText = this.userService.userDetail.custNr;
        const customerMsg = '';
        const orderRequest = this.shoppingOrderService.getOrderRequest(orderRefText, customerMsg);

        if (Object.keys(this.appStorage.subOrderBasket).length === 0) {
            delete orderRequest.finalCustomerOrderId;
        }

        this.shoppingOrderService.saveOrderChange(orderRequest)
            .subscribe(
                response => {
                    resolve({
                        message: '',
                        type: 'SUCCESS',
                    } as SagMessageData);

                    this.modalRef.hide();

                    this.goToRedirectUrl();
                },
                error => {
                    resolve({
                        message: 'MESSAGES.GENERAL_ERROR',
                        type: 'ERROR',
                    } as SagMessageData);
                }
            );
    }

    private goToRedirectUrl() {
        this.route.navigateByUrl(this.redirectUrl);
    }
}
