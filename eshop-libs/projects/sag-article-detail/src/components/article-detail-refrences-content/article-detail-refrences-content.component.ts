import { Component, OnInit, Input, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { ArticleModel } from '../../models/article.model';
import { chunk } from 'lodash';
import { AffiliateUtil } from 'sag-common';
import { ArticleDetailConfigService } from '../../services/article-detail-config.service';

const COLS = 4;

@Component({
    selector: 'sag-article-detail-refrences-content',
    templateUrl: './article-detail-refrences-content.component.html',
    styleUrls: ['./article-detail-refrences-content.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})

export class SagArticleDetailRefrencesContentComponent implements OnInit {
    @Input() article: ArticleModel;
    @Input() isCollapsible = false;
    @Input() isShownContent = true;
    @Output() navigateEmiter = new EventEmitter();

    @Input() enableRefNrChange = true;

    oeNumbers = [];
    iamNumbers = [];
    sagId;
    eanIds;
    pccIds;
    isSb: boolean;

    objectKeys = Object.keys;

    constructor(
        private config: ArticleDetailConfigService
    ) {
        this.isSb = AffiliateUtil.isSb(this.config.affiliate);
    }

    ngOnInit() {
        this.oeNumbers = this.mapKeyValue(this.article.oeNumbers);
        this.iamNumbers = this.mapKeyValue(this.article.iamNumbers);
        this.sagId = this.article.pimId;
        this.eanIds = this.groupEanIds(this.article.pnrnEANs, COLS) || [];
        this.pccIds = this.article.pnrnPccs || [];
    }

    navigateTo(event, code: string) {
        event.preventDefault();
        event.stopPropagation();
        
        if (this.enableRefNrChange) {
            this.navigateEmiter.emit(code);
        }
    }

    private groupEanIds(eanIds: string[], length: number) {
        return chunk(eanIds, length);
    }

    private mapKeyValue(obj: any) {
        if (!obj) {
            return [];
        }
        return Object.keys(obj).map(key => ({ key, values: obj[key] }));
    }
}
