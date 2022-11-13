import { Component, OnInit } from '@angular/core';
import { UserDetail } from '../core/models/user-detail.model';
import { UserService } from '../core/services/user.service';
import { Router } from '@angular/router';
import { AppStorageService } from '../core/services/app-storage.service';
import { ShoppingBasketAnalyticService } from '../analytic-logging/services/shopping-basket-analytic.service';
import { AnalyticLoggingService } from '../analytic-logging/services/analytic-logging.service';
import { BasketItemSourceDesc } from '../analytic-logging/enums/basket-item-source-desc.enum';
import { BasketItemSource } from '../analytic-logging/models/basket-item-source.model';

@Component({
    selector: 'connect-thule',
    templateUrl: './thule.component.html'
})
export class ThuleComponent implements OnInit {

    user: UserDetail;
    private targetUrl = '/shopping-basket';
    fallbackUrl = '';
    basketItemSource: BasketItemSource;

    constructor(
        private userService: UserService,
        private router: Router,
        private storage: AppStorageService,
        private shoppingBasketAnalyticService: ShoppingBasketAnalyticService,
        private analyticService: AnalyticLoggingService
    ) { }

    ngOnInit() {
        this.user = this.userService.userDetail;
        this.basketItemSource = this.analyticService.createBasketItemSource(BasketItemSourceDesc.THULE_CATALOG);
    }

    onFallback(r) {
        this.router.navigateByUrl(this.fallbackUrl);
    }

    onSuccess(r) {
        const notFoundPartNumbers = r && r.notFoundPartNumbers || null;
        // send event
        this.sendJsonEvent(r && r.items || []);
        this.storage.thuleMessage = notFoundPartNumbers;
        this.router.navigateByUrl(this.targetUrl);
    }

    private sendJsonEvent(basketItems) {
        if (basketItems && basketItems.length > 0) {
            this.shoppingBasketAnalyticService.sendThuleEventData(basketItems);
        }
    }
}
