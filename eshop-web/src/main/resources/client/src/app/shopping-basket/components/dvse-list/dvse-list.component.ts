import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { ArticleModel } from 'sag-article-detail';
import { SEARCH_MODE } from 'sag-article-list';

@Component({
    selector: 'connect-dvse-list',
    templateUrl: './dvse-list.component.html',
    styleUrls: ['./dvse-list.component.scss']
})
export class DvseListComponent implements OnInit {

    @Input() data = [];
    @Output() removeArticleEmitter: EventEmitter<any> = new EventEmitter();
    constructor(
        private router: Router
    ) { }

    ngOnInit() {
    }

    removeFromCart(cartKey) {
        this.removeArticleEmitter.emit([cartKey]);
    }

    goToArticleSearch(articleItem: ArticleModel) {
        this.router.navigate(['article', 'result'], {
            queryParams: {
                type: SEARCH_MODE.ARTICLE_NUMBER,
                articleNr: articleItem.artnr || articleItem.artnrDisplay
            }
        });
    }

}
