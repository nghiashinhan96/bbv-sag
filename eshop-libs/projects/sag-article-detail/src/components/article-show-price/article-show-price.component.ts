import { Component, Input, OnInit, SimpleChanges, OnChanges, TemplateRef, ViewChild } from '@angular/core';
import { AffiliateUtil } from 'sag-common';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';

@Component({
    selector: 'sag-article-show-price',
    template: ''
})
export class SagArticleShowPriceComponent implements OnInit, OnChanges {
    @Input() affiliateCode: string;
    @Input() userSetting: LibUserSetting;
    @Input() popoverDelay: any;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() currentStateVatConfirm: boolean;

    isShownDiscount: boolean;
    isShowPromotionDiscount: boolean;
    includeVAT: boolean;

    isPDP = false;
    isShowVat: boolean;
    isFinalCustomer: boolean;

    netPrice: number;

    netPriceGreaterThanOrEqualNet1Price = false;
    netPriceLessThanNet1Price = false;

    @ViewChild('pop', { static: false }) pop: any;

    constructor (
    ) { }

    ngOnInit() {
        this.isPDP = AffiliateUtil.isAffiliateApplyPDP(this.affiliateCode);
        this.isFinalCustomer = AffiliateUtil.isFinalCustomerAffiliate(this.affiliateCode);

        this.initialShowVat();
        this.initData();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.userSetting && !changes.userSetting.firstChange) {
            this.initialShowVat();
        }

        if(changes.currentStateVatConfirm && !changes.currentStateVatConfirm.firstChange) {
            this.initData();
        }
    }

    initialItem() {
        return null;
    }

    initData() { }

    initialShowVat() {
        const inclVATSettings = this.userSetting && this.userSetting.vatTypeDisplayConvert;
        this.isShowVat = inclVATSettings && inclVATSettings.list;
    }

    getShownDiscount() {
        if (!this.userSetting) {
            return false;
        }

        const item = this.initialItem();

        if (!item) {
            return;
        }

        // user setting and show 'Rabatt' as allowed by ERP
        const conditionOne = this.userSetting && this.userSetting.showDiscount && !!item.getDiscount();

        if (this.isPDP) {
            // is Wholesaler FC
            if (this.isFinalCustomer) {
                return false;
            }

            // don't have a Brutto, or when "Brutto < Netto1", then the discount is not shown.
            let conditionTwo = false;

            // net1PriceFound and nettto2 < netto1
            if(item.getNet1PriceFound() && item.netPriceLessThanNet1Price()) {
                conditionTwo = item.getBrutto() && item.getNet1Price() && item.getBrutto() > item.getNet1Price();
            }

            // net1PriceFound not found || nettto2 >= netto1
            if(!item.getNet1PriceFound() || item.netPriceGreaterThanOrEqualNet1Price()) {
                conditionTwo = item.getBrutto() && item.getNetPrice() && item.getBrutto() > item.getNetPrice();
            }

            return conditionOne && conditionTwo;
        } else {
            return conditionOne;
        }
    }

    getShownPromotionDiscount() {
        if (!this.userSetting) {
            return false;
        }

        const item = this.initialItem();

        if (!item) {
            return;
        }

        const conditionOne = this.userSetting && this.userSetting.showDiscount && !!item.getPromotionDiscount();

        if (this.isPDP) {
            // is Wholesaler FC
            if (this.isFinalCustomer) {
                return false;
            }

            // "Netto2 < Netto1"
            const conditionTwo = item.netPriceLessThanNet1Price();

            return conditionOne && conditionTwo;
        } else {
            return conditionOne;
        }
    }
}
