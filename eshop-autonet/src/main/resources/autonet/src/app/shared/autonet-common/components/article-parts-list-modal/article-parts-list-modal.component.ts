import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { BsModalRef } from "ngx-bootstrap/modal";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { get } from 'lodash';
import { ArticleModel } from "sag-article-detail";
import { ARTICLE_LIST_TYPE, SEARCH_MODE, MerkmaleCategory } from "sag-article-list";
import { UserService } from "src/app/core/services/user.service";
import { SpinnerService } from "src/app/core/utils/spinner";
import { SubSink } from "subsink";
import { AppStorageService } from "src/app/core/services/custom-local-storage.service";
import { ArticleNonContextService } from "src/app/passenger-car/pages/articles-non-context/services/article-non-context.service";
import { CommonModalService } from "../../services/common-modal.service";
import { AppHelperUtil } from "src/app/core/utils/helper.util";

@Component({
    selector: 'autonet-article-parts-list-modal',
    templateUrl: './article-parts-list-modal.component.html'
})
export class PartsListModalComponent implements OnInit, OnDestroy {
    @Input() set article(art: ArticleModel) {
        this.partsListItems = art.partsListItems;
        this.root = AppHelperUtil.convertAutonetData([art])[0];
    }
    @Input() vehicle: any;
    @Input() category: any;

    root: ArticleModel;
    partsListItems: any[];

    articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;

    parts: any[];
    moreParts: any[] = [];
    isSubBasket = false;

    private readonly articlePageSize = 5;

    private spinner;
    private subs = new SubSink();

    constructor(
        public bsModalRef: BsModalRef,
        public appStorage: AppStorageService,
        public userService: UserService,
        private articleNonContextService: ArticleNonContextService,
        private commonModalService: CommonModalService
    ) {

    }

    ngOnInit() {
        setTimeout(() => {
            this.spinner = SpinnerService.start('modal-container:last-child autonet-article-parts-list-modal .modal-body');
        }, 50);
        this.subs.sink = this.searchArticle()
            .pipe(
                map(res => this.extractData(res))
            ).subscribe();
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

    showMoreArticles(callback?) {
        const articles = this.moreParts.slice(0, this.articlePageSize);
        this.parts = [...this.parts, ...articles];
        this.moreParts.splice(0, this.articlePageSize);
        if (callback) {
            callback();
        }
    }

    private searchArticle(): Observable<any> {
        const categoryLevel = new MerkmaleCategory(SEARCH_MODE.PART_LIST);
        categoryLevel.offset = 0;
        categoryLevel.size = 0;
        categoryLevel.setArticlePartList({ partListItems: this.partsListItems, size: 0, offset: 0 });
        return this.articleNonContextService.getMerkmaleCategory(categoryLevel);
    }

    private extractData(response: any) {
        const articles = AppHelperUtil.convertAutonetData(get(response, 'articles.content', []));
        const visibleArticles = articles.splice(0, this.articlePageSize);
        this.parts = [...visibleArticles];
        this.moreParts = articles;
        SpinnerService.stop(this.spinner);
    }
}