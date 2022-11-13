import { Component, OnInit } from '@angular/core';
import {
    LIB_VEHICLE_ARTICLE_NUMBER_SEARCH,
    LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH,
    ArticleSearchService,
    ARTICLE_SEARCH_MODE
} from 'sag-article-search';
import {
    Router,
    ActivatedRoute
} from '@angular/router';
import { get } from 'lodash';
import { SEARCH_MODE } from 'sag-article-list';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { FeedbackArticleSearch } from 'src/app/feedback/models/feedback-article-search.model';
import { ArticlesAnalyticService } from 'src/app/analytic-logging/services/articles-analytic.service';

@Component({
    selector: 'connect-article-search',
    templateUrl: './article-search.component.html',
    styleUrls: ['./article-search.component.scss']
})
export class ArticleSearchComponent implements OnInit {

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private articleAnalyticService: ArticlesAnalyticService,
        private fb: FeedbackRecordingService,
        private articleSearchService: ArticleSearchService
    ) { }

    ngOnInit() {

    }

    searchArticle(data) {
        const searchType = data.searchType;
        const contextKey = get(data, 'contextKey', '');
        this.articleAnalyticService.sendArticleSearchEventData(data);

        let info: any = {
            searchTerm: data.search,
            rawSearchTerm: data.search,
        };
        switch (searchType) {
            case LIB_VEHICLE_ARTICLE_NUMBER_SEARCH:
                this.router.navigate(['../', 'article', 'result'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: data.search, keywords: data.keywords, contextKey },
                    relativeTo: this.activatedRoute
                });
                info.searchMode = ARTICLE_SEARCH_MODE.ARTICLE_NUMBER;
                this.fb.recordArticleSearch(new FeedbackArticleSearch({ articleNr: data.search }));
                break;
            case LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH:
                this.router.navigate(['../', 'article', 'result'], {
                    queryParams: { type: SEARCH_MODE.FREE_TEXT, articleId: data.search, keywords: data.keywords, contextKey },
                    relativeTo: this.activatedRoute
                });
                info.searchMode = ARTICLE_SEARCH_MODE.ARTICLE_DESC;
                this.fb.recordArticleSearch(new FeedbackArticleSearch({ articleName: data.search }));
                break;
        }
        this.articleSearchService.addHistory(info).subscribe();
    }
}
