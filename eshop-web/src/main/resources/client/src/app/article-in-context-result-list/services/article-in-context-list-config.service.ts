import { Injectable } from '@angular/core';
import { get } from 'lodash';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { environment } from 'src/environments/environment';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { SagInContextConfigService } from 'sag-in-context';
import { ProjectId } from 'sag-common';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { DEFAULT_LANG_CODE } from 'src/app/core/conts/app-lang-code.constant';

@Injectable({
    providedIn: 'root'
})
export class ArticleInContextListConfigService extends SagInContextConfigService {
    constructor(
        private appStorage: AppStorageService,
        private context: AppContextService,
        private shoppingBasketService: ShoppingBasketService
    ) {
        super();
        this.baseUrl = environment.baseUrl;
        this.spinner = SpinnerService;
        this.projectId = ProjectId.CONNECT;
        this.affiliate = environment.affiliate;
        this.defaultLangCode = DEFAULT_LANG_CODE;
    }

    get branchName() {
        return get(this.context, 'shoppingBasketContext.pickupBranch.branchName');
    }

    get shopType() {
        return this.shoppingBasketService.basketType;
    }

    get libUserSetting(){
        return this.appStorage.userPrice;
    }

    get appLangCode() {
        return this.appStorage.appLangCode;
    }
}
