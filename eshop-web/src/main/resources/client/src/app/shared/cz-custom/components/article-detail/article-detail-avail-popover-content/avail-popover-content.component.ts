import { Component, Input, OnInit } from '@angular/core';
import { SubSink} from 'subsink';
import { ArticleModel, CZ_AVAIL_STATE } from 'sag-article-detail';
import { LibUserSetting } from 'sag-article-detail';
import { UserService } from 'src/app/core/services/user.service';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { SHOPPING_BASKET_ENUM } from 'src/app/core/enums/shopping-basket.enum';

@Component({
    selector: 'connect-cz-avail-popover-content',
    templateUrl: 'avail-popover-content.component.html'
})
export class AvailPopoverContentComponent implements OnInit {
    @Input() article: ArticleModel;
    @Input() currency: string;

    availState = CZ_AVAIL_STATE;
    isShowNetPrice: boolean;

    priceSettings: LibUserSetting;
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
    isSubBasket: boolean;

    private subs = new SubSink();

    constructor(
        public userService: UserService,
        private shoppingBasketService: ShoppingBasketService
    ) { }

    ngOnInit() {
        this.isSubBasket = this.shoppingBasketService.basketType === SHOPPING_BASKET_ENUM.FINAL;
        this.subs.sink = this.userService.userPrice$.subscribe(priceSettings => {
            if (!priceSettings) {
                return;
            }
            this.priceSettings = priceSettings;
            this.setVisibleOfNetPrice();
        });
    }

    setVisibleOfNetPrice() {
        this.isShowNetPrice = this.priceSettings.currentStateNetPriceView;
        if (this.isEhCz) {
            this.isShowNetPrice = this.priceSettings.netPriceView && this.priceSettings.fcUserCanViewNetPrice;
        }
    }
}
