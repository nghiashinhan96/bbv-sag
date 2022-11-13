import { Component, OnInit, Input, ChangeDetectionStrategy } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BroadcastService } from 'sag-common';
import { ArticleBroadcastKey } from '../../enums/article-broadcast-key.enum';
import { ArticleModel } from '../../models/article.model';

@Component({
    selector: 'sag-article-detail-refrences',
    templateUrl: './article-detail-refrences.component.html',
    styleUrls: ['./article-detail-refrences.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SagArticleDetailRefrencesComponent implements OnInit {

    @Input() article: ArticleModel;
    @Input() enableRefNrChange = true;
    @Input() oeNumbers = [];
    @Input() iamNumbers = [];

    constructor(
        public bsModalRef: BsModalRef,
        private broadcaster: BroadcastService
    ) { }

    ngOnInit() { }

    navigateTo(code: string) {
        this.bsModalRef.hide();
        this.broadcaster.broadcast(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE, code);
    }
}
