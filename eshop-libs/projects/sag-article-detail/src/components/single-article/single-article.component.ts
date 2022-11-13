import { Component, OnInit, Input, TemplateRef, ChangeDetectorRef, Output, EventEmitter } from '@angular/core';
import { ArticleModel } from '../../models/article.model';
import { ARTICLE_MODE } from '../../enums/article-mode.enum';
import { ArticleDetailConfigService } from '../../services/article-detail-config.service';
import { BroadcastService } from 'sag-common';
import { ArticleBroadcastKey } from '../../enums/article-broadcast-key.enum';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';
import { ArticlesService } from '../../services/articles.service';
import { catchError } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import { ArticleUtil } from '../../utils/article.util';

@Component({
    selector: 'sag-article-detail-single',
    templateUrl: './single-article.component.html'
})
export class SagArticleDetailSingleComponent implements OnInit {
    @Input() set article(value) {
        this.art = value;
        if (!!this.art.availabilities) {
            this.availabilities.push({
                key: this.art.pimId,
                value: this.art.availabilities
            });
        }
        this.requestAvail([this.art]);
    }
    @Input() userSetting: LibUserSetting;

    @Input() specialInfoTemplateRef: TemplateRef<any>;
    @Input() memosTemplateRef: TemplateRef<any>;
    @Input() customAvailTemplateRef: TemplateRef<any>;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() priceTemplateRef: TemplateRef<any>;
    @Input() totalPriceTemplateRef: TemplateRef<any>;

    @Output() onArticleNumberClickEmitter = new EventEmitter();
    @Output() onShowAccessoriesEmitter = new EventEmitter();
    @Output() onShowPartsListEmitter = new EventEmitter();
    @Output() onShowCrossReferenceEmitter = new EventEmitter();

    articleMode = ARTICLE_MODE.EXTENDED_MODE;
    art: ArticleModel;
    availabilities = [];

    constructor(
        public config: ArticleDetailConfigService,
        private broadcaster: BroadcastService,
        private articlesService: ArticlesService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit() {
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

    public navigateTo(code: string) {
        this.broadcaster.broadcast(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE, code);
    }

    private requestAvail(arts: ArticleModel[]) {
        let index = 0;
        const requests = [];
        this.availabilities = [];
        const requestAvailItems = arts.filter(art => !art.availRequested && !art.availabilities);
        const erpRequest = {
            availabilityRequested: true,
            priceRequested: true
        };
        while (index < requestAvailItems.length) {
            const requestItemsBatch = requestAvailItems.slice(index, 1);
            requests.push(this.articlesService.getArticlesInfoWithBatch(requestItemsBatch, requestItemsBatch.length, erpRequest));
            index++;
        }
        if (requests.length) {
            forkJoin(requests)
                .pipe(catchError(() => of([])))
                .subscribe(respones => {
                    respones.forEach(res => {
                        const infos = this.articlesService.getDisplayedInfo(res.items);
                        const artInfo = infos.find(avail => avail.key === this.art.pimId);
                        this.availabilities = [{
                            key: artInfo.key,
                            value: artInfo.availabilities
                        }];
                        ArticleUtil.assignArticleErpInfo(this.art, artInfo, erpRequest, false);
                        this.cdr.detectChanges();
                    });
                });
        }
    }
}
