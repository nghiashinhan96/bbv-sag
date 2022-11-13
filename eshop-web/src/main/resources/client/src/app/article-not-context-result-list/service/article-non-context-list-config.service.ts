import { Injectable } from '@angular/core';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { environment } from 'src/environments/environment';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { LibUserSetting } from 'sag-article-detail';

@Injectable({
  providedIn: 'root'
})
export class ArticleNonContextListConfigService {

  baseUrl: string;
  spinner: any;
  isCH: boolean;
  // isOnBehalfUser: boolean;
  libUserSetting: LibUserSetting;
  isSimpleMode: boolean;
  branchName: string;
  projectId: string;

  constructor(private appStorage: AppStorageService) {
    this.libUserSetting = this.appStorage.userPrice;
    this.projectId = 'CONNECT';
    this.baseUrl = environment.baseUrl;
    this.spinner = SpinnerService;
    // this.isCH = AffiliateUtil.isAffiliateCH(environment.affiliate);
  }
}
