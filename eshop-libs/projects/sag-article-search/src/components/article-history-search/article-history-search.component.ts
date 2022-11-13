import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { of } from 'rxjs/internal/observable/of';
import { ArticleSearchService } from '../../services/article-search.service';
import { finalize, map } from 'rxjs/operators';
import { ArticleSearchConfigService } from '../../services/article-search-config.service';
import { ProjectId, SAG_COMMON_DATETIME_FORMAT } from 'sag-common';
import { SEARCH_MODE } from 'sag-article-list';
import { SearchTermUtil } from '../../utils/search-term.util';
const AUT_TOTAL_DISPLAY = 5;
@Component({
    selector: 'sag-search-article-history',
    templateUrl: './article-history-search.component.html'
})
export class SagSearchArticleHistoryComponent implements OnInit, OnChanges {
    @Input() custNr: string;
    @Output() selectedArticleHistoryEmitter = new EventEmitter<string>();
    articleHistorySearch$: Observable<any[]>;
    dateTimeFormat = SAG_COMMON_DATETIME_FORMAT;
    totalDisplay: number;

    constructor(private searchService: ArticleSearchService, private config: ArticleSearchConfigService) { }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.custNr && !changes.custNr.firstChange) {
            this.articleHistorySearch$ = of(null);
            setTimeout(() => {
                this.articleHistorySearch$ = this.loadHistory();
            });
        }
    }

    ngOnInit() {
        this.articleHistorySearch$ = this.loadHistory();
        if (this.config.projectId === ProjectId.AUTONET) {
            this.totalDisplay = AUT_TOTAL_DISPLAY;
        }
    }

    viewArticle(article: string) {
        if (article) {
            this.selectedArticleHistoryEmitter.emit(article);
        }
    }

    private loadHistory() {
        const spinner = this.config.spinner.start('sag-article-history-search .list-group');
        return this.searchService.getLatestArticleSearchHistory()
            .pipe(
                map(res => (res && res.content || [])),
                finalize(() => this.config.spinner.stop(spinner))
            );
    }
}
