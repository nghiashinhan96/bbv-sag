import { Injectable } from '@angular/core';
import { ProjectId } from 'sag-common';
@Injectable()
export class ArticleSearchConfigService {
    baseUrl: string;
    spinner: any;
    affiliate: string;
    projectId: ProjectId;
    constructor() { }
}
