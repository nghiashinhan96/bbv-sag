import { Injectable } from '@angular/core';
import { ArticleDetailConfigService } from 'sag-article-detail';
import { ArticleListConfigService } from './article-list-config.service';

@Injectable()
export class ArticleDetailConfigServiceImpl extends ArticleDetailConfigService {
    constructor(private articleListConfigService: ArticleListConfigService) {
        super();
        this.baseUrl = articleListConfigService.baseUrl;
        this.spinner = articleListConfigService.spinner;
        this.projectId = articleListConfigService.projectId;
        this.affiliate = articleListConfigService.affiliate;
        this.articleConfig = articleListConfigService.articleConfig;
    }

    get branchName() {
        return this.articleListConfigService.branchName;
    }
}
