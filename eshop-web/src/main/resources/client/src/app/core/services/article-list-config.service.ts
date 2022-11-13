import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { LibUserSetting } from 'sag-article-detail';

@Injectable({
    providedIn: 'root'
})
export class ConnectArticleListConfigService {
    baseUrl: string;
    spinner: any;
    isCH: boolean;
    // isOnBehalfUser: boolean;
    libUserSetting: LibUserSetting;
    branchName: string;
    projectId: string;

    constructor() {
        this.baseUrl = environment.baseUrl;
        this.spinner = SpinnerService;
        this.isCH = true;
    }
}
