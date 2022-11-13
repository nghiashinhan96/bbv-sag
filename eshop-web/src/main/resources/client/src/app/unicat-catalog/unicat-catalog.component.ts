import { DOCUMENT } from '@angular/common';
import { AfterViewInit, ElementRef, Renderer2, ViewChild, Inject } from '@angular/core';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { SubSink } from 'subsink';
import { Constant } from '../core/conts/app.constant';

import { ShoppingBasketModel } from '../core/models/shopping-basket.model';

import { UserService } from '../core/services/user.service';
import { CatalogIframeUtil } from '../core/utils/catalog-iframe.util';
import { SpinnerService } from '../core/utils/spinner';
import { UnicatService } from '../shared/cz-custom/services/unicat.service';
import { ShoppingBasketService } from './../core/services/shopping-basket.service';

@Component({
    selector: 'connect-unicat-catalog',
    templateUrl: './unicat-catalog.component.html',
    styleUrls: ['./unicat-catalog.component.scss']
})
export class UnicatCatalogComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('unicatIframe', { static: true }) unicatIframe: ElementRef;
    @ViewChild('unicatSection', { static: true }) unicatSection: ElementRef;

    isMaxSize = false;
    footerIsShowed = false;
    mainMenuIsShowed = true;

    currentTotalCartItems: number;
    subs = new SubSink();
    pollSubject: Observable<any>;
    intervalUpdateTop: any;
    initHeight = 170;

    constructor(
        private userService: UserService,
        private renderer: Renderer2,
        private shoppingBasketService: ShoppingBasketService,
        private unicatService: UnicatService,
        @Inject(DOCUMENT) private document: any,
    ) { }

    ngOnInit() {
        this.updateUnicatLayout();
        this.getCurrentTotalCartItemsInBasket();

        SpinnerService.start();
        this.unicatService.getUnicatCatalogUri()
            .pipe(finalize(() => SpinnerService.stop()))
            .subscribe(data => {
                this.loadUnicatUrl(data);
            });
    }

    ngAfterViewInit() {
        const timeout = Constant.POLL_INTERVAL;

        this.pollSubject = new Observable(observer => {
            let count = 0;
            const interval = setInterval(() => {
                observer.next(count++);
            }, timeout);

            return () => {
                clearInterval(interval);
            };
        });

        this.subs.sink = this.pollSubject.subscribe(value => {
            this.getCurrentTotalCartItemsOfUnicatCatalog();
        });

        this.intervalUpdateTop = setInterval(() => {
            CatalogIframeUtil.updateCatalogIframeTop(this.renderer, this.document, '.unicat-section', this.initHeight);
        }, 1000);
    }

    ngOnDestroy() {
        this.destroyCustomLayout();
        this.subs.unsubscribe();
        document.body.classList.remove('unicat-openned');
        clearInterval(this.intervalUpdateTop);
    }

    toggleIframeSize() {
        const unicatSection = this.unicatSection.nativeElement;

        if (unicatSection.classList.contains('container-lg')) {
            this.removeDimension(unicatSection);
            this.isMaxSize = false;
        } else {
            this.setDimension(unicatSection);
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

    private loadUnicatUrl(data) {
        if (data && data.unicatCatalogUri) {
            this.renderer.setAttribute(this.unicatIframe.nativeElement, 'src', data.unicatCatalogUri);
        }
    }

    private updateUnicatLayout() {
        document.body.classList.add('unicat-openned');
        document.body.classList.toggle('menu-hide', !this.mainMenuIsShowed);
        document.body.classList.toggle('footer-hide', !this.footerIsShowed);
        this.renderer.removeClass(this.unicatSection.nativeElement, 'container-lg');
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
        document.body.classList.remove('menu-hide', 'footer-hide', 'unicat-openned');
    }

    private getCurrentTotalCartItemsInBasket() {
        this.subs.sink = this.shoppingBasketService.miniBasket$.subscribe((basket: ShoppingBasketModel) => {
            this.currentTotalCartItems = basket && basket.numberOfItems || 0;
        });
    }

    private getCurrentTotalCartItemsOfUnicatCatalog() {
        if (this.userService.userDetail && this.userService.userDetail.custNr) {
            this.subs.sink =
                this.shoppingBasketService.getTotalNumberCart(this.userService.userDetail.cachedUserId, this.userService.userDetail.custNr)
                    .subscribe(total => {
                        if (total !== this.currentTotalCartItems) {
                            this.shoppingBasketService.loadMiniBasket();
                        }
                    });
        }
    }
}
