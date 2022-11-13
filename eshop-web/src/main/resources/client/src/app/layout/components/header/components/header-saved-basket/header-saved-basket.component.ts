import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { ShoppingBasketHistoryService } from 'src/app/core/services/shopping-basket-history.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { HeaderSavedBasketModalComponent } from '../header-saved-basket-modal/header-saved-basket-modal.component';
import { ShoppingExportModalComponent } from 'src/app/shared/connect-common/components/shopping-export-modal/shopping-export-modal.component';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { HeaderSaveShoppingBasketModalComponent } from '../header-save-shopping-basket-modal/header-save-shopping-basket-modal.component';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { SubSink } from 'subsink';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { Router } from '@angular/router';
import { SHOPPING_BASKET_ENUM } from 'src/app/core/enums/shopping-basket.enum';

@Component({
    selector: 'connect-header-saved-basket',
    templateUrl: './header-saved-basket.component.html',
    styleUrls: ['./header-saved-basket.component.scss']
})
export class HeaderSavedBasketComponent implements OnInit, OnDestroy {
    @ViewChild('pop', { static: false }) savedBasketPopover: PopoverDirective;

    @Input() isHiddenSymbol: boolean = false;

    basketHistoriesTotals;
    cartQuantity = null;
    shouldDisablePopoverMenu = false;
    private subs = new SubSink();
    private miniBasketData: ShoppingBasketModel;
    constructor(
        private shoppingBasketService: ShoppingBasketService,
        private shoppingBasketHistoryService: ShoppingBasketHistoryService,
        private modalService: BsModalService,
        private appModal: AppModalService,
        private router: Router
    ) { }

    ngOnInit() {
        this.shoppingBasketHistoryService.savedBasketQuantity$.subscribe(quantity => {
            if (quantity <= 99) {
                this.basketHistoriesTotals = quantity || '0';
            } else {
                this.basketHistoriesTotals = '99+';
            }
        });

        this.subs.sink = this.shoppingBasketService.basketQuantity$.subscribe(cartQuantity => {
            this.cartQuantity = cartQuantity;
        });

        this.subs.sink = this.shoppingBasketService.miniBasket$.subscribe((miniBasket: ShoppingBasketModel) => {
            this.miniBasketData = miniBasket;
        });
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    shownBasketHistory() {
        this.savedBasketPopover.hide();
        this.appModal.modals = this.modalService.show(HeaderSavedBasketModalComponent, {
            class: 'modal-xl',
            ignoreBackdropClick: true
        });
    }

    onSaveShoppingBasket() {
        this.appModal.modals = this.modalService.show(HeaderSaveShoppingBasketModalComponent, {
            ignoreBackdropClick: true
        });
    }

    onClickSaveShoppingBasket() {
        this.savedBasketPopover.hide();
        this.onSaveShoppingBasket()
    }

    onClickExportShoppingBasket() {
        this.savedBasketPopover.hide();
        const currentPageIsShoppingBasket = this.router.url.indexOf('/shopping-basket') > -1;
        if (!currentPageIsShoppingBasket && !this.miniBasketData) {
            const spinner = SpinnerService.startApp();
            this.shoppingBasketService.loadMiniBasket(() => {
                SpinnerService.stop(spinner);
                this.showExportModal(currentPageIsShoppingBasket);
            });
            return;
        }
        this.showExportModal(currentPageIsShoppingBasket);
    }


    private showExportModal(currentPageIsShoppingBasket: boolean) {
        let shoppingBasketDataForExport: ShoppingBasketModel = this.getDataForExportModal(currentPageIsShoppingBasket);
        this.appModal.modals = this.modalService.show(ShoppingExportModalComponent, {
            ignoreBackdropClick: true,
            class: 'shopping-export-modal',
            initialState: {
                shoppingBasket: shoppingBasketDataForExport
            }
        });
    }

    private getDataForExportModal(currentPageIsShoppingBasket: boolean) {
        if (!currentPageIsShoppingBasket) {
            return this.miniBasketData;
        }
        const isSubFinalShoppingCart = this.shoppingBasketService.basketType === SHOPPING_BASKET_ENUM.FINAL;
        if (!isSubFinalShoppingCart) {
            return this.miniBasketData;
        }
        return this.shoppingBasketService.currentSubBasket;
    }

    onPopoverShown() {
        this.shouldDisablePopoverMenu = this.getValueForShouldDisablePopoverMenu();
    }

    private getValueForShouldDisablePopoverMenu() {
        const currentPageIsShoppingBasket = this.router.url.indexOf('/shopping-basket') > -1;
        if (!currentPageIsShoppingBasket) {
            return !this.cartQuantity;
        }
        const isSubFinalShoppingCart = this.shoppingBasketService.basketType === SHOPPING_BASKET_ENUM.FINAL;
        if (!isSubFinalShoppingCart) {
            return !this.cartQuantity;
        }
        const currentSubBasket = this.shoppingBasketService.currentSubBasket;
        const currentSubBasketQuantity = currentSubBasket && currentSubBasket.numberOfOrderPos;
        return !currentSubBasketQuantity;
    }
}
