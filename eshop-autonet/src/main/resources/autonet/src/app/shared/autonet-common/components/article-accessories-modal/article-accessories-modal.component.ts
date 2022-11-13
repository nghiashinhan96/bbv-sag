import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { BsModalRef } from "ngx-bootstrap/modal";
import { PopoverDirective } from "ngx-bootstrap/popover";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { get } from 'lodash';
import { ArticleBroadcastKey, ArticleModel } from "sag-article-detail";
import { ArticleListStorageService, ARTICLE_LIST_TYPE, SEARCH_MODE, MerkmaleCategory } from "sag-article-list";
import { BroadcastService, SagMessageData } from "sag-common";
import { ShoppingBasketService } from "src/app/core/services/shopping-basket.service";
import { UserService } from "src/app/core/services/user.service";
import { SpinnerService } from "src/app/core/utils/spinner";
import { SubSink } from "subsink";
import { TranslateService } from "@ngx-translate/core";
import { AppStorageService } from "src/app/core/services/custom-local-storage.service";
import { ArticleNonContextService } from "src/app/passenger-car/pages/articles-non-context/services/article-non-context.service";
import { CommonModalService } from "../../services/common-modal.service";
import { AppHelperUtil } from "src/app/core/utils/helper.util";

@Component({
    selector: 'autonet-article-accessories-modal',
    templateUrl: './article-accessories-modal.component.html'
})
export class AccessoryListModalComponent implements OnInit, OnDestroy {
    @Input() set article(art: ArticleModel) {
        this.accessoryLists = art.accessoryLists;
        this.root = AppHelperUtil.convertAutonetData([art])[0];
    }
    @Input() vehicle: any;
    @Input() category: any;

    root: ArticleModel;
    accessoryLists: any[];

    articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;

    groups: any[] = [];
    moreGroups: any[] = [];
    isSubBasket = false;

    notifier: SagMessageData;

    private readonly groupPageSize = 3;
    private readonly articlePageSize = 5;
    private readonly groupsHint = 9;

    private spinner;
    private subs = new SubSink();

    constructor(
        private router: Router,
        public bsModalRef: BsModalRef,
        public appStorage: AppStorageService,
        public userService: UserService,
        private articleListBroadcastService: BroadcastService,
        private articleListStorageService: ArticleListStorageService,
        private commonModalService: CommonModalService,
        private translateService: TranslateService,
        private articleNonContextService: ArticleNonContextService
    ) {

    }

    ngOnInit() {
        setTimeout(() => {
            this.spinner = SpinnerService.start('modal-container:last-child autonet-article-accessories-modal .modal-body');
        }, 50);
        this.subs.sink = this.searchArticle()
            .pipe(
                map(res => this.extractData(res))
            ).subscribe();

            this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
                .subscribe((vehicleId: string) => {
                    if (vehicleId) {
                        this.bsModalRef.hide();
                        this.articleListStorageService.accessoryArticle = new ArticleModel({...this.root, criteria: []});
                        this.router.navigate(['vehicle', vehicleId]);
                    }
                });
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    onArticleNumberClick(article: ArticleModel) {
        this.commonModalService.showReplaceModal(article);
    }

    onShowAccessories(data: any) {
        this.commonModalService.showAccessoriesModal(data);
    }

    onShowPartsList(data: any) {
        this.commonModalService.showPartsListModal(data);
    }

    onShowCrossReference(data: any) {
        this.commonModalService.showCrossReferenceModal(data);
    }

    showMoreGroups(callback) {
        const groups = this.moreGroups.splice(0, this.groupPageSize);
        this.groups.push(...groups);
        callback();
    }

    showMoreArticles(seqNo, callback?) {
        const group = this.groups.find(g => g.seqNo === seqNo);
        if (group) {
            const articles = group.moreValues.slice(0, this.articlePageSize);
            group.values = [...group.values, ...articles];
            group.moreValues.splice(0, this.articlePageSize);
            if (callback) {
                callback();
            }
        }
    }

    private searchArticle(): Observable<any> {
        const categoryLevel = new MerkmaleCategory(SEARCH_MODE.ACCESSORY_LIST);
        categoryLevel.offset = 0;
        categoryLevel.size = 0;
        categoryLevel.setAccessory({ accessoryList: this.accessoryLists });
        return this.articleNonContextService.getMerkmaleCategory(categoryLevel);
    }

    private extractData(response: any) {
        const articles = AppHelperUtil.convertAutonetData(get(response, 'articles.content', []));
        let start = 1;
        const groups = this.groupArticles(articles).map(g => {
            let accessoryListsText = g.values[0].accessoryListsText;
            if (!accessoryListsText) {
                accessoryListsText = this.translateService.instant('COMMON_LABEL.ACCESSORIES_LIST') + ` ${start}`;
                start++;
            }
            return {
                seqNo: g.values[0].seqNo,
                accesoryListsText: accessoryListsText,
                accessoryLinkText: g.values[0].accessoryLinkText,
                values: g.values.slice(0, this.articlePageSize),
                moreValues: g.values.slice(this.articlePageSize)
            };
        });
        if (groups.length > this.groupsHint) {
            this.notifier = {
                type: 'INFO',
                message: this.translateService.instant('COMMON_LABEL.ACCESSORIES_GROUPS_HINT')
            };
        }
        const visibleGroups = groups.splice(0, this.groupPageSize);
        this.groups.push(...visibleGroups);
        this.moreGroups = groups;
        SpinnerService.stop(this.spinner);
    }

    private groupArticles(articles: ArticleModel[]) {
        const obj = {};
        let groups = [];
        if (!articles || articles.length === 0) {
            return [];
        }
        let order = 0;
        articles.forEach(art => {
            if (obj[art.seqNo]) {
                obj[art.seqNo].values.push(art);
            } else {
                obj[art.seqNo] = { order, values: [art] };
                order++;
            }
        });
        Object.keys(obj).forEach(seqNo => {
            groups.push(obj[seqNo]);
        });
        groups.sort((a, b) => a.seqNo - b.seqNo);
        return groups;
    }
}