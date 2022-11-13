import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';
import { UrlUtil } from 'src/app/core/utils/url.util';

@Component({
    selector: 'autonet-price-offer',
    templateUrl: './price-offer.component.html',
    styleUrls: ['./price-offer.component.scss']
})
export class PriceOfferComponent implements OnInit {
    url = `${UrlUtil.autonetServer(this.appStore.country)}portal/OferteViz.aspx`;
    params = {
        uid: '',
        14: null,
        400: ''
    };
    constructor(
        private appStore: AppStorageService
    ) { }

    ngOnInit() {
        const autonet = this.appStore.autonet as AutonetAuthModel;
        this.params.uid = autonet.uid;
        this.params['14'] = autonet.lid;

        const pageUrl = AppHelperUtil.objectToUrl(this.url, this.params);
        this.params['400'] = pageUrl;
    }
}
