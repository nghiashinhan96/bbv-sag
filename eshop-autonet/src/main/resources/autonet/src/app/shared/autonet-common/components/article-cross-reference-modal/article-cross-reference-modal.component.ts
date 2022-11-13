import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { BsModalRef } from "ngx-bootstrap/modal";
import { Observable } from "rxjs";
import { finalize, map } from "rxjs/operators";
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
    selector: 'autonet-article-cross-reference-modal',
    templateUrl: './article-cross-reference-modal.component.html'
})
export class ArticleCrossReferenceModalComponent implements OnInit, OnDestroy {
    @Input() set article(art: ArticleModel) {
        this.root = AppHelperUtil.convertAutonetData([art])[0];
    }
    @Input() vehicle: any;
    @Input() category: any;

    root: ArticleModel;
    data: any[];
    moreData: any;
    hasMoreData: boolean;

    articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;

    isSubBasket = false;

    private categoryLevel: MerkmaleCategory;

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
            this.spinner = SpinnerService.start('modal-container:last-child autonet-article-cross-reference-modal .modal-body');
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
        const offset = this.categoryLevel.offset + 1;
        this.searchArticle(offset)
            .pipe(finalize(() => {
                if (callback) {
                    callback();
                }
            }))
            .subscribe((response: any) => {
                this.hasMoreData = this.isHasMoreData(response);
                this.moreData = AppHelperUtil.convertAutonetData(get(response, 'articles.content', []))
            });
    }

    private searchArticle(offset = 0): Observable<any> {
        this.categoryLevel = new MerkmaleCategory(SEARCH_MODE.CROSS_REFERENCE);
        this.categoryLevel.offset = offset;
        this.categoryLevel.size = 10;
        this.categoryLevel.setCrossReference({ articleNumber: this.root.artnr, dlnrId: this.root.dlnrId });
        return this.articleNonContextService.getMerkmaleCategory(this.categoryLevel);
    }

    private extractData(response: any) {
        const articles = AppHelperUtil.convertAutonetData(get(response, 'articles.content', []));
        this.data = [...articles];
        this.moreData = [];
        this.hasMoreData = this.isHasMoreData(response);
        SpinnerService.stop(this.spinner);
    }


    private isHasMoreData(response) {
        return get(response, 'articles.numberOfElements') > 0 && !get(response, 'articles.last');
    }
}