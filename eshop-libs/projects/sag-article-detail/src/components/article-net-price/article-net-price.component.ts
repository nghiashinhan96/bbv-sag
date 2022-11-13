import { AfterViewInit, Component, Input, OnChanges, OnDestroy, QueryList, SimpleChanges, ViewChildren } from '@angular/core';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { SubSink } from 'subsink';
import { SagArticleShowPriceComponent } from '../article-show-price/article-show-price.component';
import { ArticleModel } from './../../models/article.model';
@Component({
    selector: 'sag-article-net-price',
    templateUrl: './article-net-price.component.html',
    styleUrls: ['./article-net-price.component.scss']
})

export class SagArticleNetPriceComponent extends SagArticleShowPriceComponent implements OnChanges, AfterViewInit, OnDestroy {
    @Input() article: ArticleModel;
    // Use for lazy loading the price from ERP
    @Input() set price(val: any) {
        if (this.article && val) {
            this.article.price = val;
            this.initData();
        }
    }

    @ViewChildren(PopoverDirective) popovers: QueryList<PopoverDirective>;

    netPrice: number;
    pops = [];
    fcCanViewNetPrice: boolean = false;
    private subs = new SubSink();

    ngAfterViewInit() {
        if (this.popovers) {
            this.subs.sink = this.popovers.changes.subscribe(pops => {
                if (pops && pops.length > 0) {
                    this.pops = this.pops.concat(pops.toArray());
                } else {
                    const openingPop = this.pops.find(p => p.isOpen);
                    if (openingPop) {
                        openingPop.hide();
                        this.pops = [];
                    }
                }
            })
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        super.ngOnChanges(changes);

        if (changes.article && !changes.article.firstChange) {
            this.initData();
        }
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    initialItem() {
        return this.article;
    }

    initData() {
        if (!this.isPDP) {
            if (this.isFinalCustomer) {
                if (!this.isShowVat) {
                    this.article.setFCNetPrice(this.userSetting, this.currentStateVatConfirm);
                }
                this.fcCanViewNetPrice = this.userSetting.canViewNetPrice;
            } else {
                this.article.setNetPrice(this.currentStateVatConfirm);
            }
        } else {
            this.compareNetPriceAndNetPriceExlPromotion();
            this.isShownDiscount = this.getShownDiscount();
            this.isShowPromotionDiscount = this.getShownPromotionDiscount();

            if (this.isShowVat) {
                this.netPrice = this.getNetPrice();
            } else {
                this.netPrice = this.getNetPrice(this.currentStateVatConfirm);
            }
        }
    }

    getNetPrice(includeVat = false) {
        this.article.setNetPrice(includeVat);
        return this.article.netPrice;
    }

    compareNetPriceAndNetPriceExlPromotion() {
        this.netPriceGreaterThanOrEqualNet1Price = this.article.netPriceGreaterThanOrEqualNet1Price();
        this.netPriceLessThanNet1Price = this.article.netPriceLessThanNet1Price();
    }
}
