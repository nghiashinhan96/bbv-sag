import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";
import { Observable } from "rxjs";
import { get } from 'lodash';
import { ArticleModel } from "sag-article-detail";
import { ARTICLE_LIST_TYPE, MerkmaleCategory, SEARCH_MODE } from "sag-article-list";
import { UserService } from "src/app/core/services/user.service";
import { SubSink } from "subsink";
import { finalize, map } from "rxjs/operators";
import { SpinnerService } from "src/app/core/utils/spinner";
import { SagValidator } from "sag-common";
import { AppStorageService } from "src/app/core/services/custom-local-storage.service";
import { ArticleNonContextService } from "src/app/passenger-car/pages/articles-non-context/services/article-non-context.service";
import { AppHelperUtil } from "src/app/core/utils/helper.util";
import { CommonModalService } from "../../services/common-modal.service";

@Component({
    selector: 'autonet-article-replace-modal',
    templateUrl: './article-replace-modal.component.html'
})
export class ArticleReplaceModalComponent implements OnInit, OnDestroy {
    @Input() article: ArticleModel;
    
    articleListType = ARTICLE_LIST_TYPE.NOT_IN_CONTEXT;

    data: any;

    private subs = new SubSink();
    
    constructor(
        private modalService: BsModalService,
        public bsModalRef: BsModalRef,
        public storage: AppStorageService,
        public userService: UserService,
        private articleNonContextService: ArticleNonContextService,
        private commonModalService: CommonModalService
    ) {

    }

    ngOnInit() {
        const spinner = SpinnerService.start('modal-container:last-child autonet-article-replace-modal .modal-body');
        this.subs.sink = this.searchArticle()
            .pipe(
                map(res => this.extractData(res)),
                finalize(() => SpinnerService.stop(spinner))
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

    private searchArticle(): Observable<any> {
        const categoryLevel = new MerkmaleCategory(SEARCH_MODE.ONLY_ARTICLE_NUMBER_AND_SUPPLIER);
        categoryLevel.setKeyword(this.article.hasReplacement);
        categoryLevel.idDlnr = this.article.dlnrId;
        categoryLevel.offset = 0;
        return this.articleNonContextService.getMerkmaleCategory(categoryLevel);
    }

    private extractData(response: any) {
        const articles = AppHelperUtil.convertAutonetData(get(response, 'articles.content', []));
        const artnr = this.normalizeArtnr(this.article.hasReplacement || '');

        this.data = (articles || [])
            .filter((art: ArticleModel) => this.normalizeArtnr(art.artnr) === artnr)
            .filter((art: ArticleModel) => {
                const replaceForArtIds = (art.isReplacementFor || '').split(',');
                return replaceForArtIds.some(id => this.normalizeArtnr(id) === this.normalizeArtnr(this.article.artnr));
            });
    }
    
    private normalizeArtnr(artnr) {
        return SagValidator.removeWhiteSpace(SagValidator.removeNonAlphaCharacter(artnr || '')).toLowerCase();
    }
}