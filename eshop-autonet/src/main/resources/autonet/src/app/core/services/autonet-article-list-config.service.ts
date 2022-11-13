import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { LibUserSetting } from 'sag-article-detail';

@Injectable({
    providedIn: 'root'
})
export class AutonetArticleListConfigService {
    baseUrl: string;
    spinner: any;
    isCH: boolean;
    // isOnBehalfUser: boolean;
    libUserSetting: LibUserSetting;
    isSimpleMode: boolean;
    branchName: string;
    projectId: string;

    constructor(private appStorage: AppStorageService) {
        this.libUserSetting = this.appStorage.libUserSetting;
        this.isSimpleMode = this.appStorage.isSimpleMode;
        this.projectId = 'AUTONET';
        this.baseUrl = environment.baseUrl;
        this.spinner = SpinnerService;
        this.isCH = true;
    }

    get appLangCode() {
        return this.appStorage.appLangCode;
    }
}
