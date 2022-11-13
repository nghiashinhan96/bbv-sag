import { DOCUMENT } from '@angular/common';
import { AfterViewInit, ElementRef, Renderer2, ViewChild, Inject } from '@angular/core';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { SubSink } from 'subsink';
import { Constant } from '../core/conts/app.constant';

import { ShoppingBasketModel } from '../core/models/shopping-basket.model';

import { UserDetailOwnSetting } from '../core/models/user-detail-own-setting.mode';
import { UserService } from '../core/services/user.service';
import { ShoppingBasketService } from './../core/services/shopping-basket.service';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { ArticlesAnalyticService } from 'src/app/analytic-logging/services/articles-analytic.service';
import { GrossPriceType } from 'sag-custom-pricing';
import { ARTICLE_EVENT_SOURCE } from 'sag-common';
import { finalize } from 'rxjs/operators';

@Component({
    selector: 'connect-dvse-catalog',
    templateUrl: './dvse-catalog.component.html',
    styleUrls: ['./dvse-catalog.component.scss']
})
export class DVSECatalogComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('dvseIframe', { static: true }) dvseIframe: ElementRef;
    @ViewChild('dvseSection', { static: true }) dvseSection: ElementRef;

    userSettings: UserDetailOwnSetting;
    isMaxSize: boolean = false;
    footerIsShowed = false;
    mainMenuIsShowed = true;

    currentTotalCartItems: number = 0;
    subs = new SubSink();
    pollSubject: Observable<any>;
    intervalUpdateTop: any;
    initHeight = 170;
    currentBasketItems: ShoppingBasketItemModel[] = [];

    constructor(
        private userService: UserService,
        private renderer: Renderer2,
        private shoppingBasketService: ShoppingBasketService,
        @Inject(DOCUMENT) private document: any,
        private articlesAnalyticService: ArticlesAnalyticService
    ) { }

    ngOnInit() {
        this.userSettings = this.userService.userDetail && this.userService.userDetail.settings || null;

        this.updateDVSELayout();
        this.loadDVSEUrl();
    }

    ngAfterViewInit() {
        let timeout = Constant.POLL_INTERVAL;

        this.pollSubject = new Observable(observer => {
            let count = 0;
            let interval = setInterval(() => {
                observer.next(count++);
            }, timeout);

            return () => {
                clearInterval(interval);
            };
        });

        this.subs.sink = this.pollSubject.subscribe(value => {
            this.getCurrentTotalCartItemsOfDvseCatalog();
        });

        this.intervalUpdateTop = setInterval(() => {
            this.updateDvseCatelogTop();
        }, 1000);
    }

    ngOnDestroy() {
        this.destroyCustomLayout();
        this.subs.unsubscribe();
        document.body.classList.remove('dvse-openned');
        clearInterval(this.intervalUpdateTop);
    }

    toggleIframeSize() {
        const dvseSection = this.dvseSection.nativeElement;

        if (dvseSection.classList.contains('container-lg')) {
            this.removeDimension(dvseSection);
            this.isMaxSize = false;
        } else {
            this.setDimension(dvseSection);
            const hasFooterClass = document.body.classList.contains('footer-hide');

            if (!hasFooterClass) {
                this.toogleFooter();
            }

            this.isMaxSize = true;
        }
    }

    toogleFooter() {
        this.footerIsShowed = !this.footerIsShowed;
        document.body.classList.toggle('footer-hide', !this.footerIsShowed);
    }

    private loadDVSEUrl() {
        if (this.dvseIframe && this.userSettings.dvseCatalogUri) {
            this.renderer.setAttribute(this.dvseIframe.nativeElement, 'src', this.userSettings.dvseCatalogUri);
        }
    }

    private updateDVSELayout() {
        document.body.classList.add('dvse-openned');
        document.body.classList.toggle('menu-hide', !this.mainMenuIsShowed);
        document.body.classList.toggle('footer-hide', !this.footerIsShowed);
        this.renderer.removeClass(this.dvseSection.nativeElement, 'container-lg');
    }

    private removeDimension(outerEl: ElementRef) {
        if (outerEl) {
            this.renderer.removeClass(outerEl, 'container-lg');
        }
    }

    private setDimension(outerEl) {
        if (outerEl) {
            this.renderer.addClass(outerEl, 'container-lg');
        }
    }

    private destroyCustomLayout() {
        document.body.classList.remove('menu-hide', 'footer-hide', 'dvse-openned');
    }

    private getCurrentTotalCartItemsInBasket() {
        this.subs.sink = this.shoppingBasketService.miniBasket$.subscribe((basket: ShoppingBasketModel) => {
            if(!basket || !basket.items) return;

            let newItem;

            // pre add article into SB
            if(this.currentBasketItems.length === basket.items.length) {
                newItem = (basket.items || []).find(item => {
                    if(this.currentBasketItems.find(it => it.articleItem.artid === item.articleItem.artid && it.quantity !== item.quantity)) {
                        return item;
                    }
                });
            } else {
                // check the first time an article added to SB
                if(this.currentBasketItems.length === 0 && this.currentTotalCartItems === 0) {
                    newItem = basket.items[0];
                } else {
                    newItem = (basket.items || []).find(item => {
                        if(!this.currentBasketItems.find(it => it.articleItem.artid === item.articleItem.artid)) {
                            return item;
                        }
                    });
                }
            }

            if(newItem) {
                this.articlesAnalyticService.sendAddToBasketEventData(
                    {
                        ...newItem.articleItem,
                        vehicle: newItem.vehicle,
                        amountNumber: newItem.quantity,
                        source: ARTICLE_EVENT_SOURCE.EVENT_SOURCE_DVSE,
                        basketItemSourceId: newItem.basketItemSourceId,
                        basketItemSourceDesc: newItem.basketItemSourceDesc
                    },
                    basket,
                    {
                        type: GrossPriceType.UVPE.toString(),
                        brand: ''
                    }
                );
            }
            
            this.currentBasketItems = basket.items || [];
            this.currentTotalCartItems = basket && basket.numberOfItems || 0;
        });
    }

    private getCurrentTotalCartItemsOfDvseCatalog() {
        if (this.userService.userDetail && this.userService.userDetail.custNr) {
            this.subs.sink = this.shoppingBasketService.getTotalNumberCart(this.userService.userDetail.cachedUserId, this.userService.userDetail.custNr)
                .pipe(
                    finalize(() => {
                        this.getCurrentTotalCartItemsInBasket();
                    })
                )
                .subscribe(total => {
                    if (total !== this.currentTotalCartItems) {
                        this.shoppingBasketService.loadMiniBasket();
                    }
                });
        }
    }

    private updateDvseCatelogTop() {
        const topMessage = this.document.querySelector('.top-message');
        if (!!topMessage) {
            const message = topMessage.querySelector('.banner') || topMessage.querySelector('.alert');
            const dvseCatelog = this.document.querySelector('.dvse-section');
            let height = this.initHeight;

            if (!!message && !!dvseCatelog) {
                height = Number(message.offsetHeight) + height;
            }

            this.renderer.setStyle(dvseCatelog, 'top', `${height}px`);
        }
    }
}
