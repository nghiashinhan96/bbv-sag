import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { SEARCH_MODE } from 'sag-article-list';
import {
    ArticleSearchService,
    ARTICLE_SEARCH_MODE,
    LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH
} from 'sag-article-search';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { SubSink } from 'subsink';

@Component({
    selector: 'autonet-article-free-text-search',
    templateUrl: './article-free-text-search.component.html',
    styleUrls: ['./article-free-text-search.component.scss']
})
export class ArticleFreeTextSearchComponent implements OnInit, OnDestroy {
    searchForm: FormGroup;
    articleDescSearchType = LIB_VEHICLE_ARTICLE_DESCRIPTION_SEARCH;
    keywords: string;
    subs = new SubSink();

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private storageService: AppStorageService,
        private articleSearchService: ArticleSearchService
    ) { }

    ngOnInit() {
        this.keywords = this.activatedRoute.snapshot.queryParams.articleId || this.storageService.lastKeyword;
        this.storageService.lastKeyword = this.keywords;
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    searchArticle(data) {
        let info: any = {
            searchTerm: data.search,
            rawSearchTerm: data.search,
            searchMode: ARTICLE_SEARCH_MODE.ARTICLE_DESC
        };
        this.subs.sink = this.articleSearchService.addHistory(info).subscribe();
        this.router.navigate(['cars', 'article'], {
            queryParams: { type: SEARCH_MODE.FREE_TEXT, articleId: data.search }
        });
    }

}
