import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { ProjectId, AffiliateUtil, AffiliateEnum } from 'sag-common';
import { LibUserSetting, ARTICLE_MODE } from 'sag-article-detail';

import { ARTICLE_LIST_TYPE } from '../../enums/article-list-type.enum';
import { ArticleListConfigService } from '../../services/article-list-config.service';
import { ArticleSort } from '../../models/article-sort.model';
import { BehaviorSubject } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryTreeService } from '../../services/category-tree.service';
import { ArticleListStorageService } from '../../services/article-list-storage.service';

@Component({
    selector: 'sag-article-list-header',
    templateUrl: './article-list-header.component.html',
    styleUrls: ['./article-list-header.component.scss']
})
export class SagArticleListHeaderComponent implements OnInit, OnChanges {

    @Input() articleListType: string;
    @Input() sortArticle: BehaviorSubject<ArticleSort>;
    @Input() disableSort = false;

    @Input() isAffiliateCH = true;
    @Input() isAllChecked: any;
    @Output() isAllCheckedChange = new EventEmitter();

    @Output() removeEmitter = new EventEmitter();

    @Input() articleMode: string;
    @Output() articleModeChange = new EventEmitter<string>();

    @Input() userSetting: LibUserSetting;
    @Output() currentNetPriceChange = new EventEmitter();

    @Output() customPriceChange = new EventEmitter();

    @Input() returnUrl: string;

    removable = false;
    selectable = false;

    simpleMode = ARTICLE_MODE.SIMPLE_MODE;
    detailMode = ARTICLE_MODE.EXTENDED_MODE;

    isTyreMode: boolean;
    grossPriceTranslationKey: string;
    LIST_TYPE = ARTICLE_LIST_TYPE;

    projectId = ProjectId.CONNECT;
    PROJECT_ID = ProjectId;

    sort = new ArticleSort();
    isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate);
    enableCzSort: boolean;
    isEhCh = false;
    sb = AffiliateEnum.SB;
    isSb = false;
    isShoppingCartMode = false;
    isAxCz = AffiliateUtil.isAxCz(this.config.affiliate);
    isCzBased = this.isCz || this.isAxCz;

    isShowNetPrice = false;
    canViewNetPrice = false;

    showFilterNoAvail = false;
    hideNonAvailArticle = false;
    hasAvailabilityPermission = false;

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private config: ArticleListConfigService,
        private categoryTreeService: CategoryTreeService,
        private libStorage: ArticleListStorageService
    ) {
        this.isSb = AffiliateUtil.isSb(this.config.affiliate);
        this.hideNonAvailArticle = this.libStorage.hideNonAvailArticle;
        this.showFilterNoAvail = AffiliateUtil.isAffiliateHasFilterNoAvail(this.config.affiliate, this.config.projectId);
    }

    get isSimpleMode() {
        return this.articleMode === ARTICLE_MODE.SIMPLE_MODE;
    }

    ngOnInit() {
        this.isTyreMode = this.articleListType === ARTICLE_LIST_TYPE.TYPRE;
        this.enableCzSort = this.isCzBased && !this.disableSort && this.articleListType !== ARTICLE_LIST_TYPE.SHOPPING_BASKET;
        this.isShoppingCartMode = this.articleListType === ARTICLE_LIST_TYPE.SHOPPING_BASKET;

        this.grossPriceTranslationKey = this.getGrossPriceTranslationKey();
        this.projectId = this.config.projectId || ProjectId.CONNECT;
        if (this.isShoppingCartMode) {
            this.selectable = true;
            this.removable = true;
        }

        if (this.userSetting) {
            this.isShowNetPrice = this.userSetting.currentStateNetPriceView;

            this.canViewNetPrice = this.userSetting.canViewNetPrice;
            this.hasAvailabilityPermission = this.userSetting.hasAvailabilityPermission;
        }
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.articleListType && !changes.articleListType.firstChange) {
            this.isTyreMode = this.articleListType === ARTICLE_LIST_TYPE.TYPRE;
            this.sort = new ArticleSort();
        }

        if (changes.userSetting && !changes.userSetting.firstChange) {
            this.isShowNetPrice = this.userSetting.currentStateNetPriceView;
        }
    }

    applyFilterHideArticleNoAvail(checked) {
        this.hideNonAvailArticle = checked;
        this.libStorage.hideNonAvailArticle = this.hideNonAvailArticle;
    }

    sortBySupplier() {
        this.sort.sortBySupplier();
        this.sortArticle.next(this.sort);
    }

    sortByGrossPrice() {
        this.sort.sortByGrossPrice();
        this.sortArticle.next(this.sort);
    }

    sortByNetPrice() {
        this.sort.sortByNetPrice();
        this.sortArticle.next(this.sort);
    }

    onOffNetPrice() {
        if (this.userSetting && this.userSetting.netPriceView) {
            this.currentNetPriceChange.emit();
        }
    }

    switchArticleDisplayedMode(mode?: string) {
        this.articleMode = mode;
        this.articleModeChange.emit(mode);
    }

    removeSelected() {
        this.removeEmitter.emit(true);
    }

    checkBoxChecked(event) {
        this.isAllCheckedChange.emit({
            status: event,
            isHeaderChecked: true
        });
    }

    onCustomPriceChange(price) {
        this.customPriceChange.emit(price);
    }

    goToReturnUrl() {
        this.categoryTreeService.resetCategoryTree();
        this.router.navigate([this.returnUrl], { relativeTo: this.activatedRoute });
    }

    private getGrossPriceTranslationKey(): string {
        if (this.isAffiliateCH) {
            if (this.isTyreMode) {
                return 'ARTICLE.UVPE_VALUE';
            }
            return 'ARTICLE.GROSS_VALUE';
        } else {
            return 'ARTICLE.UVPE_VALUE';
        }
    }
}
