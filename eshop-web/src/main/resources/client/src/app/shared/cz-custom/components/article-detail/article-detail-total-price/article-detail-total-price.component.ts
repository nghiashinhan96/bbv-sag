import { Component, Input, OnInit, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { SubSink } from 'subsink';
import { get } from 'lodash';
import { ArticleModel, LibUserSetting } from 'sag-article-detail';
import { UserService } from 'src/app/core/services/user.service';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-cz-article-detail-total-price',
    templateUrl: 'article-detail-total-price.component.html'
})
export class ArticleDetailTotalPriceComponent implements OnInit, OnChanges, OnDestroy {
    @Input() article: ArticleModel;
    @Input() currentStateVatConfirm: boolean;

    totalPrice: number;
    totalPriceWithVat: number;

    priceSettings: LibUserSetting;

    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
    isShowVat = false;

    private subs = new SubSink();

    constructor(
        public userService: UserService
    ) { }

    ngOnInit() {
        const inclVATSettings = this.userService.userPrice && this.userService.userPrice.vatTypeDisplayConvert;
        this.isShowVat = inclVATSettings && inclVATSettings.list;

        this.subs.sink = this.userService.userPrice$.subscribe(priceSettings => {
            if (!priceSettings) {
                return;
            }
            this.priceSettings = priceSettings;
            this.initData();
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.article && !changes.article.firstChange) {
            this.initData();
        }

        if (changes.currentStateVatConfirm && !changes.currentStateVatConfirm.firstChange) {
            this.initData();
        }
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    private initData() {
        if(this.isEhCz) {
            if (this.isShowVat) {
                this.totalPrice = this.article.getFcTotalPrice(this.userService.userPrice, this.userService.userPrice.currentStateNetPriceView);
                this.totalPriceWithVat = this.article.getFcTotalPrice(this.userService.userPrice, this.userService.userPrice.currentStateNetPriceView, true);
            } else {
                this.totalPrice = this.article.getFcTotalPrice(this.userService.userPrice, this.userService.userPrice.currentStateNetPriceView, this.currentStateVatConfirm);
            }
        } else {
            this.article.setTotalGrossPrice(this.currentStateVatConfirm);
            this.article.setTotalNetPrice(this.currentStateVatConfirm);
            this.totalPrice = this.article.getTotalPrice(this.userService.userPrice.currentStateNetPriceView);
        }
    }
}
