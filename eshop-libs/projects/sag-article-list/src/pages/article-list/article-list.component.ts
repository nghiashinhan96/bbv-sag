import { Component, OnInit, Input, Output, EventEmitter, TemplateRef, OnDestroy } from '@angular/core';
import { ArticleModel, LibUserSetting } from 'sag-article-detail';
import { ArticleGroupModel } from '../../models/article-group.model';
import { ARTICLE_LIST_TYPE } from '../../enums/article-list-type.enum';
import { MultiLevelSelectedFilter } from '../../models/multi-level-selected-filter.model';
import { ArticleSort } from '../../models/article-sort.model';
import { BehaviorSubject, Subject } from 'rxjs';
import { BrandFilterItem } from '../../models/brand-filter-item.model';
import { BarFilter } from "sag-article-detail";
import { FAVORITE_BROADCAST_EVENT } from 'sag-article-detail';
import { SubSink } from 'subsink';
import { PopoverDirective } from 'ngx-bootstrap/popover';

@Component({
    selector: 'sag-article-list',
    templateUrl: './article-list.component.html',
    styleUrls: ['./article-list.component.scss']
})
export class SagArticleListComponent implements OnInit, OnDestroy {
    @Input() groupByRelevance = false;
    @Input() data: any;
    @Input() moreData: any;
    @Input() hasMoreData: boolean;
    @Input() articleCount = 0;
    @Input() articleListType = ARTICLE_LIST_TYPE.IN_CONTEXT;
    @Input() filterDataOnBadge: MultiLevelSelectedFilter;
    @Input() notFoundTempRef: TemplateRef<any>;
    @Input() specialInfoTemplateRef: TemplateRef<any>;
    @Input() memosTemplateRef: TemplateRef<any>;
    @Input() customAvailTemplateRef: TemplateRef<any>;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() priceTemplateRef: TemplateRef<any>;
    @Input() totalPriceTemplateRef: TemplateRef<any>;

    @Output() deselectMerkmaleEmitter = new EventEmitter();
    @Output() goToPreviousRouteEmitter = new EventEmitter();
    @Output() showMoreArticlesEmitter = new EventEmitter();
    @Output() goToDefaultBasketEmitter = new EventEmitter();

    @Output() removeArticlesEmitter = new EventEmitter();

    @Input() articleMode: string;
    @Output() articleModeChange = new EventEmitter<string>();
    @Input() userSetting: LibUserSetting;
    @Output() currentNetPriceChange = new EventEmitter();

    @Input() vehicle: any;

    @Input() labourTimes: any[];
    @Input() hasHaynesProFeatures: boolean;
    @Input() linkLoginHaynesPro: any = [];
    @Output() loginHaynesPro = new EventEmitter<any>();
    @Output() openHaynesProModal = new EventEmitter<any>();

    @Input() currentStateVatConfirm = false;

    @Output() customPriceChange = new EventEmitter();

    @Input() brandsData: BrandFilterItem[] = [];
    @Output() brandsItemsChange = new EventEmitter<BrandFilterItem>();
    @Output() brandsStateChange = new EventEmitter<BrandFilterItem>();

    @Output() barFilterStateChange = new EventEmitter<BarFilter>();

    @Input() returnUrl: string;

    @Input() barFilterOption: BarFilter;

    @Input() genArts = [];
    @Input() gaId: number;

    @Output() rootHeaderClick = new EventEmitter();

    @Output() sendArticleResultData = new EventEmitter();
    @Output() sendArticlesGaData = new EventEmitter();

    @Output() barFilterItemsChange = new EventEmitter<BrandFilterItem>();
    @Output() onArticleNumberClickEmitter = new EventEmitter();
    @Output() onShowAccessoriesEmitter = new EventEmitter();
    @Output() onShowPartsListEmitter = new EventEmitter();
    @Output() onShowCrossReferenceEmitter = new EventEmitter();

    @Input() isFavoritedList = false;
    @Input() enabledFavorite = false;
    @Input() isSubBasket = false;
    @Input() hasPositionWithCvpValueIsNull = false;

    @Input() disableSort = false;
    @Input() rootModalName = '';

    @Output() onRemovePseudoGroup = new EventEmitter();

    TYPE = ARTICLE_LIST_TYPE;
    isSelectedAll = {
        status: false,
        isHeaderChecked: false
    };
    selectedArtilces = [];

    sortArticle = new BehaviorSubject<ArticleSort>(null);
    actionFavorite = FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_LEAF;

    private pops: PopoverDirective[] = [];
    private subs = new SubSink();

    constructor(
    ) { }

    ngOnInit() {
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
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

    deselectMerkmale(deselectedMerkmale) {
        this.deselectMerkmaleEmitter.emit(deselectedMerkmale);
    }

    goToPreviousRoute() {
        this.goToPreviousRouteEmitter.emit();
    }

    showMoreArticles(callback) {
        this.showMoreArticlesEmitter.emit(callback);
    }

    goToDefaultBasket() {
        this.goToDefaultBasketEmitter.emit();
    }

    trackByFn(index: number, group: ArticleGroupModel) {
        return group.key;
    }

    onCurrentNetPriceChange() {
        this.currentNetPriceChange.emit();
    }

    removeSelectedArticles() {
        if (this.selectedArtilces.length === 0) { return; }

        this.removeArticlesEmitter.emit(this.selectedArtilces);
    }

    onCustomPriceChange(price) {
        this.customPriceChange.emit({ price });
    }

    onBasketCustomPriceChange({ price, vehicleId }) {
        this.customPriceChange.emit({ price, vehicleId });
    }

    onWspGaidHeaderClick() {
        if (this.articleListType === ARTICLE_LIST_TYPE.WSP) {
            this.rootHeaderClick.emit();
        }
    }

    removePseudoGroup(group) {
        group.deleted = true;
        this.onRemovePseudoGroup.emit(group.key);
    }

    popoversChanged(pops: PopoverDirective[]) {
        (pops || []).forEach(pop => {
            if (!this.pops.some(p => p === pop)) {
                this.subs.sink = pop.onShown.subscribe(() => {
                    this.pops
                        .filter(p => p !== pop)
                        .forEach(p => {
                            if (p && p.isOpen) {
                                p.hide();
                            }
                        });
                });
                this.pops.push(pop);
            }
        })
    }
}
