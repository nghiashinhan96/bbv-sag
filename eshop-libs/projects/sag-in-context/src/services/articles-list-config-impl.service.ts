import { Injectable } from '@angular/core';
import { ArticleListConfigService } from 'sag-article-list';
import { SagInContextConfigService } from './articles-in-context-config.service';

@Injectable()
export class SagArticleListConfigServiceImpl extends ArticleListConfigService {
    constructor(private articleInContextConfigService: SagInContextConfigService) {
        super();
        this.baseUrl = articleInContextConfigService.baseUrl;
        this.spinner = articleInContextConfigService.spinner;
        this.projectId = articleInContextConfigService.projectId;
        this.affiliate = articleInContextConfigService.affiliate;
        this.articleConfig = articleInContextConfigService.articleConfig;
    }

    get branchName() {
        return this.articleInContextConfigService.branchName;
    }
}
