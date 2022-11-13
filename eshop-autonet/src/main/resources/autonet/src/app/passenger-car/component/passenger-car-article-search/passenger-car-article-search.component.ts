import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { SEARCH_MODE } from 'sag-article-list';
import {
    LIB_VEHICLE_ARTICLE_NUMBER_SEARCH,
    LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH,
    ARTICLE_SEARCH_MODE,
    ArticleSearchService
} from 'sag-article-search';
import { SubSink } from 'subsink';

@Component({
    selector: 'autonet-passenger-car-article-search',
    templateUrl: './passenger-car-article-search.component.html',
    styleUrls: ['./passenger-car-article-search.component.scss']
})
export class PassengerCarArticleSearchComponent implements OnInit {
    subs = new SubSink();
    
    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private articleSearchService: ArticleSearchService
    ) { }

    ngOnInit() {

    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    searchArticle(data) {
        const searchType = data.searchType;
        let info: any = {
            searchTerm: data.search,
            rawSearchTerm: data.search,
        };
        switch (searchType) {
            case LIB_VEHICLE_ARTICLE_NUMBER_SEARCH:
                this.router.navigate(['article'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: data.search },
                    relativeTo: this.activatedRoute
                });
                break;
            case LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH:
                this.router.navigate(['article'], {
                    queryParams: { type: SEARCH_MODE.FREE_TEXT, articleId: data.search },
                    relativeTo: this.activatedRoute
                });
                info.searchMode = ARTICLE_SEARCH_MODE.ARTICLE_DESC;
                break;
        }
        this.subs.sink = this.articleSearchService.addHistory(info).subscribe();
    }
}
