import { Injectable } from '@angular/core';
import { ArticleDetailConfigService } from 'sag-article-detail';

@Injectable()
export class ArticleListConfigService extends ArticleDetailConfigService {
    constructor() {
        super();
    }
}
