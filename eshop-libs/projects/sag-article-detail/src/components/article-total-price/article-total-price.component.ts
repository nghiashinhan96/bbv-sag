import { Component, Input, OnInit, TemplateRef, OnChanges, SimpleChanges } from '@angular/core';
import { cloneDeep } from 'lodash';
import { ArticleModel } from './../../models/article.model';
import { ProjectId } from 'sag-common';
import { SagArticleShowPriceComponent } from '../article-show-price/article-show-price.component';
import { CustomPrice } from 'sag-custom-pricing';

@Component({
    selector: 'sag-article-total-price',
    templateUrl: './article-total-price.component.html',
    styleUrls: ['./article-total-price.component.scss']
})
export class SagArticleTotalPriceComponent extends SagArticleShowPriceComponent implements OnInit, OnChanges {
    @Input() article: ArticleModel;
    @Input() totalPriceTemplateRef: TemplateRef<any>;
    @Input() projectId: string = ProjectId.CONNECT;
    @Input() cartKey: boolean;
    @Input() displayedPrice: CustomPrice;
    // Use for lazy loading the price from ERP
    @Input() set price(val: any) {
        if (this.article && val) {
            this.article.price = val;
            this.initData();
        }
    }

    PROJECT_ID = ProjectId;

    totalPrice: number;
    totalPriceVat: number;

    ngOnChanges(changes: SimpleChanges): void {
        super.ngOnChanges(changes);

        if (changes.article && !changes.article.firstChange) {
            this.initData();
        }

        if (changes.userSetting && !changes.userSetting.firstChange) {
            this.initData();
        }

        if (changes.cartKey && !changes.cartKey.firstChange) {
            this.initData();
        }

        if(changes.displayedPrice && !changes.displayedPrice.firstChange) {
            this.article.displayedPrice = changes.displayedPrice.currentValue;
            this.initData();
        }
    }

    initialItem() {
        return cloneDeep(this.article);
    }

    initData() {
        if (!this.isPDP) {
            this.initDataWithOneLine();
        } else {
            this.compareNetPriceAndNet1Price();

            if (this.isShowVat) {
                this.totalPrice = this.article.getTotalPriceInPDP(this.userSetting.currentStateNetPriceView, !!this.cartKey);
                this.totalPriceVat = this.article.getTotalPriceWithVat(this.userSetting.currentStateNetPriceView);
            } else {
                this.initDataWithOneLine();
            }
        }
    }

    private compareNetPriceAndNet1Price() {
        this.netPriceGreaterThanOrEqualNet1Price = this.article.netPriceGreaterThanOrEqualNet1Price();
        this.netPriceLessThanNet1Price = this.article.netPriceLessThanNet1Price();
    }

    private initDataWithOneLine() {
        if (this.isFinalCustomer) {
            if(this.isShowVat) {
                this.totalPrice = this.article.getFcTotalPrice(this.userSetting, this.userSetting.currentStateNetPriceView);
                this.totalPriceVat = this.article.getFcTotalPrice(this.userSetting, this.userSetting.currentStateNetPriceView, true);
            } else {
                this.totalPrice = this.article.getFcTotalPrice(this.userSetting, this.userSetting.currentStateNetPriceView, this.currentStateVatConfirm);
            }
        } else {
            this.article.setTotalGrossPrice(this.currentStateVatConfirm, !!this.cartKey);
            this.article.setTotalNetPrice(this.currentStateVatConfirm);
            this.totalPrice = this.article.getTotalPrice(this.userSetting.currentStateNetPriceView);
        }
    }
}