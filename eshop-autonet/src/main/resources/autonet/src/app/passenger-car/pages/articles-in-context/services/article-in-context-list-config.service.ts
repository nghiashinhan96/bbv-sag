import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { SagInContextConfigService } from 'sag-in-context';
import { ProjectId } from 'sag-common';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { DEFAULT_LANG_CODE } from 'src/app/core/conts/app.constant';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';

@Injectable({
    providedIn: 'root'
})
export class ArticleInContextListConfigService extends SagInContextConfigService {
    constructor(
        private appStorage: AppStorageService,
        private shoppingBasketService: ShoppingBasketService
    ) {
        super();
        this.baseUrl = environment.baseUrl;
        this.spinner = SpinnerService;
        this.projectId = ProjectId.AUTONET;
        this.affiliate = environment.affiliate;
        this.defaultLangCode = DEFAULT_LANG_CODE;
    }
}
