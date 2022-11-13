import { Injectable } from '@angular/core';
import { ArticleListConfigService } from 'sag-article-list';
import { LibUserSetting } from 'sag-article-detail';

@Injectable()
export class SagInContextConfigService extends ArticleListConfigService {
    libUserSetting: LibUserSetting;
    affiliate: string;

    constructor() {
        super();
    }
}
