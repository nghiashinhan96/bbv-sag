import { Component, Input, TemplateRef, Output, OnDestroy, EventEmitter, ViewChild, OnInit } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { xor, groupBy, sortBy } from 'lodash';

import { AffiliateEnum } from 'sag-common';
import { LibUserSetting, ArticleModel } from 'sag-article-detail';
import { ArticleSortUtil } from '../../utils/article-sort.util';
import { ArticleGroupModel } from '../../models/article-group.model';
import { BrandFilterItem } from '../../models/brand-filter-item.model';
import { BrandFilterUtil } from '../../utils/brand-filter.util';
import { ArticleSort } from '../../models/article-sort.model';
import { ARTICLE_LIST_TYPE } from '../../enums/article-list-type.enum';
import { BarFilter, BarFilterOption } from 'sag-article-detail';

@Component({
    selector: 'sag-article-list-gaid-group',
    templateUrl: './article-gaid-group.component.html'
})
export class SagArticleListGaidGroupComponent implements OnInit, OnDestroy {

    @Input() set barFilterOptionData(val: BarFilter[]) {
        if (val) {
            this.setBarFilterOption(val)
        }
    }

    @Input() set group(val: ArticleGroupModel) {
        if (val) {
            this.pseudo = val.pseudo;
            this.initialData(val);
            if (val.isForceChanged && this.gaidGroups && this.gaidGroups.length > 0) {
                this.gaidGroups.forEach(g => {
                    g.gaIdKeyChanged = this.getNewTimeStamp(g.gaId);
                });
            }
        }
    }
    @Input() vehicle: any;
    @Input() articleMode: boolean;
    @Input() userSetting: LibUserSetting;
    @Input() specialInfoTemplateRef: TemplateRef<any>;
    @Input() memosTemplateRef: TemplateRef<any>;
    @Input() customAvailTemplateRef: TemplateRef<any>;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() priceTemplateRef: TemplateRef<any>;
    @Input() totalPriceTemplateRef: TemplateRef<any>;
    @Input() set brandsData(val: BrandFilterItem[]) {
        this.groupBrandsData = groupBy(val, 'key');
        this.brands = sortBy(BrandFilterUtil.getUniqCombinedBrands(this.groupBrandsData[this.groupName]), 'name');
        this.totalSelectedBrands = this.brands.filter(b => b.checked).length;
    }
    @Input() sortArticle: BehaviorSubject<ArticleSort>;
    @Input() articleListType = ARTICLE_LIST_TYPE.IN_CONTEXT;

    @Output() brandsItemsChange = new EventEmitter<BrandFilterItem>();
    @Output() brandsStateChange = new EventEmitter<BrandFilterItem>();

    @Output() barFilterItemsChange = new EventEmitter<BarFilter[]>();
    @Output() barFilterStateChange = new EventEmitter<BarFilter[]>();

    @Output() showMoreArticleEmitter = new EventEmitter();
    @Input() set hasMoreData(isMore: boolean) {
        this.hasMoreArticles = isMore;
    }

    @Input() moreArticles: ArticleModel[];
    @Input() rootModalName: string = '';

    @Output() sendArticleResultData = new EventEmitter();
    @Output() sendArticlesGaData = new EventEmitter();
    @Output() onArticleNumberClickEmitter = new EventEmitter();
    @Output() onShowAccessoriesEmitter = new EventEmitter();
    @Output() onShowPartsListEmitter = new EventEmitter();
    @Output() onShowCrossReferenceEmitter = new EventEmitter();

    @Output() popoversChanged = new EventEmitter();

    @ViewChild('pop', { static: false }) pop: any;

    pseudo = false;
    gaidGroups: ArticleGroupModel[];
    groupName: string;
    hasLiquidation = false;
    isShownliquidation: boolean;
    liquidationSub = new Subject();
    totalGroupGaIDShowNoAvailMsg = 0;
    groupBrandsData: any = {};
    brands = [];
    totalSelectedBrands = 0;
    cz = AffiliateEnum.CZ;
    isBrandFilterEnable = false;
    sb = AffiliateEnum.SB;

    barFilterOptions: BarFilter[] = [];
    isFilterByCaId = false;
    showFilterBar = false;
    hasMoreArticles = false;

    constructor() {
    }

    ngOnInit(): void {
        if (this.userSetting) {
            this.isBrandFilterEnable = this.userSetting.isSalesOnBeHalf ? this.userSetting.salesBrandFilterEnabled : this.userSetting.customerBrandFilterEnabled;
        }
    }

    ngOnDestroy(): void {
        this.liquidationSub.complete();
    }

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

    checkHasHeadeLiquidation(hasLiquidation) {
        this.hasLiquidation = this.hasLiquidation || hasLiquidation;
    }

    showAllLiquidation() {
        this.isShownliquidation = !this.isShownliquidation;
        this.liquidationSub.next(this.isShownliquidation);
    }

    initialData(group: ArticleGroupModel) {
        const gaidGroups = ArticleSortUtil.groupBy(group.values, article => [article.gaID]);
        gaidGroups.sort((g1, g2) => {
            return g1[0].gaID - g2[0].gaID;
        });
        this.gaidGroups = gaidGroups.map(g => {
            const gaidGroup = {
                key: group.key,
                values: g,
                root: group.root,
                cate: group.cate,
                cateId: group.cateId,
                cateBrands: group.cateBrands,
                gaId: g[0].gaID,
                pseudo: this.pseudo
            } as ArticleGroupModel;
            this.checkChanged(gaidGroup);
            return gaidGroup;
        });
        this.groupName = this.gaidGroups[0] && this.gaidGroups[0].key || group.key;
    }

    trackByGaIdFn(index: number, group: ArticleGroupModel) {
        return group.gaIdKeyChanged;
    }

    onBrandsItemsChange(data: BrandFilterItem) {
        this.brandsItemsChange.emit(data);
    }

    onBrandsStateChange(data: BrandFilterItem) {
        this.brandsStateChange.emit(data);
    }

    deselectBrand(brand) {
        brand.checked = false;
        const data = new BrandFilterItem({
            key: this.groupName,
            brands: this.brands
        });
        this.brandsStateChange.emit(data);
    }

    deselectAllBrands() {
        this.brands.forEach(brand => {
            brand.checked = false;
        });
        const data = new BrandFilterItem({
            key: this.groupName,
            brands: this.brands
        });
        this.brandsStateChange.emit(data);
    }

    closeBrandFilterPopover() {
        if (this.pop) {
            this.pop.hide();
        }
    }

    showMoreArticles(callback) {
        this.showMoreArticleEmitter.emit(callback);
    }

    private checkChanged(newGroup: ArticleGroupModel) {
        if (!this.gaidGroups) {
            newGroup.gaIdKeyChanged = this.getNewTimeStamp(newGroup.gaId);
            return true;
        }
        const oldGroup = this.gaidGroups.find(g => g.gaId === newGroup.gaId);
        if (!oldGroup) {
            newGroup.gaIdKeyChanged = this.getNewTimeStamp(newGroup.gaId);
            return true;
        }
        const olds = oldGroup.values.map(art => art.pimId);
        const news = newGroup.values.map(art => art.pimId);
        const difference = xor(olds, news);
        if (difference.length > 0) {
            newGroup.gaIdKeyChanged = this.getNewTimeStamp(newGroup.gaId);
        } else {
            newGroup.gaIdKeyChanged = oldGroup.gaIdKeyChanged;
        }
    }

    private getNewTimeStamp(gaId: string) {
        return `${gaId}-${new Date().getTime()}`;
    }

    setBarFilterOption(data) {
        if (!data) {
            return;
        }

        if (this.articleListType == ARTICLE_LIST_TYPE.IN_CONTEXT) {
            this.barFilterOptions = (data || []).filter(filter => filter.key === this.groupName);
            if (this.barFilterOptions.length == 0) {
                return;
            }
            this.isFilterByCaId = true;
            return;
        }
        this.barFilterOptions = data;
        this.isFilterByCaId = true;
    }

    onBarFilterFromPopupChange(data: BarFilter[]) {
        let isEmit = false;
        if (!data) {
            return;
        }
        data.map((barFilter, index) => {
            (barFilter.options || []).map(cOp => {
                if (!this.barFilterOptions[index] || !this.barFilterOptions[index].options) {
                    return;
                }
                const option = this.barFilterOptions[index].options.find(op => op.cvp == cOp.cvp);
                if (option.checked !== cOp.checked) {
                    isEmit = true;
                }
            })
        })

        if (isEmit) {
            this.barFilterOptions = data;
            this.barFilterStateChange.emit(this.barFilterOptions);
        }
    }

    selectBarOption(event: any, filter: any) {
        filter.checked = event;
        this.barFilterStateChange.emit(this.barFilterOptions);
    }
}
