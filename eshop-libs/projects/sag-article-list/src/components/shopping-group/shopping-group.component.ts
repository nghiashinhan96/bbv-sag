import { OnInit, Component, Input, Output, EventEmitter, OnChanges, SimpleChanges, TemplateRef } from '@angular/core';
import { BroadcastService } from 'sag-common';
import { ArticleModel, LibUserSetting, ARTICLE_PARENT } from 'sag-article-detail';
import { ArticleSortUtil } from '../../utils/article-sort.util';
import { ARTICLE_LIST_TYPE } from './../../enums/article-list-type.enum';

@Component({
    selector: 'sag-article-list-shopping-group',
    templateUrl: './shopping-group.component.html',
    styleUrls: ['shopping-group.component.scss']
})
export class SagArticleListShoppingGroupComponent implements OnInit, OnChanges {

    @Input() set group(value) {
        this.data = value;
        this.data.forEach(g => {
            if (this.selected && this.selected.length > 0) {
                g.value.forEach(element => {
                    if (this.selected.indexOf(element.cartKey) !== -1) {
                        element.articleItem.markAsDeleted = true;
                        this.checkAddtional(element, true);
                    }
                });
            }
            g.value = ArticleSortUtil.groupBy(g.value, item => [item.curGenArtDescription]);
            g.value.sort((item1, item2) => {
                if (item1.addedTime < item2.addedTime) { return 1; }
                if (item1.addedTime > item2.addedTime) { return -1; }
                return 0;
            });
        });
    }
    @Input() selectAll: any;
    @Output() selectAllChange = new EventEmitter<any>();

    @Output() removeArticleEmitter = new EventEmitter();

    @Input() selected = [];
    @Output() selectedChange = new EventEmitter();

    @Input() articleMode: boolean;
    @Input() userSetting: LibUserSetting;

    @Input() labourTimes: any[];
    @Input() hasHaynesProFeatures: boolean;
    @Input() linkLoginHaynesPro: any = [];
    @Output() loginHaynesPro = new EventEmitter<any>();
    @Output() openHaynesProModal = new EventEmitter<any>();

    @Input() currentStateVatConfirm = false;

    @Output() customPriceChange = new EventEmitter();

    @Input() specialInfoTemplateRef: TemplateRef<any>;
    @Input() memosTemplateRef: TemplateRef<any>;
    @Input() customAvailTemplateRef: TemplateRef<any>;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() priceTemplateRef: TemplateRef<any>;
    @Input() totalPriceTemplateRef: TemplateRef<any>;
    @Input() rootModalName: string = '';

    @Input() isSubBasket = false;
    @Output() onArticleNumberClickEmitter = new EventEmitter();
    @Output() onShowAccessoriesEmitter = new EventEmitter();
    @Output() onShowPartsListEmitter = new EventEmitter();
    @Output() onShowCrossReferenceEmitter = new EventEmitter();

    @Output() popoversChanged = new EventEmitter();

    checkedObj = {};
    data: any[];
    PARENT = ARTICLE_PARENT;
    SHOPPING_BASKET = ARTICLE_LIST_TYPE.SHOPPING_BASKET;

    constructor(private broadcaster: BroadcastService) {
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.selectAll && !changes.selectAll.firstChange) {
            const data = changes.selectAll.currentValue;
            if (data.isHeaderChecked) {
                this.selected = this.updateAllSelected(data.status);
                // for check change dection
                setTimeout(() => {
                    this.selectedChange.emit(this.selected);
                });
            }
        }
    }

    ngOnInit(): void { }

    onArticleNumberClick(article: ArticleModel) {
        if (!article) {
            return;
        }
        this.onArticleNumberClickEmitter.emit(article);
    }

    onShowAccessories(article: ArticleModel) {
        this.onShowAccessoriesEmitter.emit(article);
    }

    onShowPartsList(article: ArticleModel) {
        this.onShowPartsListEmitter.emit(article);
    }

    onShowCrossReference(article: ArticleModel) {
        this.onShowCrossReferenceEmitter.emit(article);
    }

    removeArticle(article: ArticleModel, cartItem) {
        this.removeArticleEmitter.emit([cartItem.cartKey]);
    }

    markAsDeleted(article: ArticleModel, cartItem) {
        this.checkAddtional(cartItem, article.markAsDeleted);
        this.selectAllChange.emit({ status: this.isAllChecked() === undefined, isHeaderChecked: false });
        this.selected = this.getAllSelected();
        this.selectedChange.emit(this.selected);
    }

    private isAllChecked() {
        return this.data.find(g => {
            return g.value.find(element => {
                return element.find(item => !item.articleItem.markAsDeleted);
            });
        });
    }

    private checkAddtional(cartItem, status: boolean) {
        if (cartItem && cartItem.attachedCartItems) {
            (cartItem.attachedCartItems || []).forEach(item => {
                item.articleItem.markAsDeleted = status;
            });
        }
    }

    updateAllSelected(status) {
        const keys = [];
        this.data.forEach(g => {
            g.value.forEach(element => {
                element.forEach(cartItem => {
                    cartItem.articleItem.markAsDeleted = status;
                    this.checkAddtional(cartItem, status);
                    if (status) {
                        keys.push(cartItem.cartKey);
                    }
                });
            });
        });
        return keys;
    }

    private getAllSelected() {
        const keys = [];
        this.data.forEach(g => {
            g.value.forEach(element => {
                element.forEach(item => {
                    if (item.articleItem.markAsDeleted) {
                        keys.push(item.cartKey);
                    }
                });
            });
        });
        return keys;
    }

    trackByGroupKey(index: number, group) {
        return group.key;
    }

    trackByCartKey(index: number, cartItem) {
        return cartItem.cartKey;
    }

    trackByGenArtDesc(index: number, cartItems) {
        return cartItems[0].curGenArtDescription;
    }

    onLoginHaynesPro(vehicleId, index) {
        if (this.loginHaynesPro) {
            this.loginHaynesPro.emit({ vehicleId, index });
        }
    }

    openModalToRetrieveDataAndRegenerateLink(vehicleId, index) {
        this.openHaynesProModal.emit({ vehicleId, index });
    }

    updateSelectedLabourTime(selectedLabourTime) {
        for (let i = 0; i < this.labourTimes.length; i++) {
            if (this.labourTimes[i].vehicleId === selectedLabourTime.vehicleId) {
                this.labourTimes[i] = selectedLabourTime;
            }
        }
    }

    onCustomPriceChange(price, vehicleId) {
        this.customPriceChange.emit({ price, vehicleId });
    }
}
