import { Component, OnInit, Input, OnDestroy, AfterViewInit, ElementRef, Inject } from '@angular/core';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { UserService } from 'src/app/core/services/user.service';
import { Subscription } from 'rxjs/internal/Subscription';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { pipe, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { PageScrollService } from 'ngx-page-scroll-core';
import { DOCUMENT } from '@angular/common';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';


@Component({
    selector: 'connect-header-shopping-basket-item',
    templateUrl: './header-shopping-basket-item.component.html',
    styleUrls: ['./header-shopping-basket-item.component.scss']
})
export class HeaderShoppingBasketItemComponent implements OnInit, OnDestroy, AfterViewInit {
    @Input() index;
    @Input() basketItem: ShoppingBasketItemModel;
    @Input() disabled: boolean;
    @Input() isAnimation: boolean;

    productText = '';
    supplier = '';
    artnrDisplayed = '';
    articlePrice;
    isOrdered;

    flashing = false;
    private priceSubscription: Subscription;
    isSubItem = false;
    constructor(
        private gaService: GoogleAnalyticsService,
        private userService: UserService,
        private shoppingBasketService: ShoppingBasketService,
        private el: ElementRef,
        private pageScrollService: PageScrollService,
        @Inject(DOCUMENT) private document: any
    ) { }

    ngOnInit() {
        this.productText = this.basketItem.getProductText();
        this.supplier = this.basketItem.articleItem.supplier;
        this.artnrDisplayed = this.basketItem.articleItem.artnrDisplay;
        this.priceSubscription = this.userService.userPrice$.subscribe((userPrice: any) => {
            this.articlePrice = this.basketItem.getPrice(userPrice);
        });
        this.isSubItem = this.basketItem.depot || this.basketItem.voc || this.basketItem.vrg || this.basketItem.pfand || this.basketItem.recycle;
        if (this.isSubItem) {
            const subItemTitle = this.productText;
            this.productText = '';
            this.artnrDisplayed = subItemTitle;
        }
    }

    ngAfterViewInit(): void {
        if (this.isAnimation) {
            this.pageScrollService.scroll({
                document: this.document,
                scrollTarget: this.el.nativeElement,
                scrollInView: true,
                scrollViews: [document.querySelector('.mini-basket-container')],
                duration: 400
            });
            this.flashing = true;
        }
    }

    ngOnDestroy(): void {
        if (this.priceSubscription) {
            this.priceSubscription.unsubscribe();
        }
    }

    removeFromCart() {
        if (this.isOrdered || this.basketItem.attachedCartItem) {
            return;
        }

        const spinnner = SpinnerService.start(`connect-header-shopping-basket-item .mini-basket-content.item-${this.index}`, {
            class: 'sm',
            containerMinHeight: 0,
            withoutText: true
        });
        this.shoppingBasketService.removeBasketItem({ cartKeys: [this.basketItem.cartKey], reloadAvail: true })
            .pipe(
                catchError(err => {
                    return of(null);
                })
            ).subscribe(res => {
                SpinnerService.stop(spinnner);
            });
    }
}
