import { Injectable } from '@angular/core';
import { ProjectId } from 'sag-common';
import { ArticleConfigModel } from '../models/article-config.model';

@Injectable()
export class ArticleDetailConfigService {
    baseUrl: string;
    spinner: any;
    affiliate: string;
    // libUserSetting: LibUserSetting;
    // isSimpleMode: boolean;
    branchName: string;
    projectId: ProjectId;
    articleConfig: ArticleConfigModel;
    appLangCode: string;
    defaultLangCode: string;

    constructor() { }
}
