import { Component, OnInit, OnDestroy } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { first } from 'rxjs/internal/operators/first';
import { AutonetCommonService } from 'src/app/shared/autonet-common/autonet-common.service';
import { SEARCH_MAIN_PAGE } from '../../car-search-page.contanst';
import { UrlUtil } from 'src/app/core/utils/url.util';

@Component({
    selector: 'autonet-passenger-car',
    templateUrl: './passenger-car.component.html',
    styleUrls: ['./passenger-car.component.scss']
})
export class PassengerCarComponent implements OnInit, OnDestroy {

    url = `${UrlUtil.autonetServer(this.appStore.country)}portal/promoContainer.aspx`;
    topRightAdsUrl = `${UrlUtil.autonetServer(this.appStore.country)}portal/Promo3.aspx`;
    params;
    topRightAdsParams;
    constructor(
        private appStore: AppStorageService,
        private appContext: AppContextService,
        private autonetCommonService: AutonetCommonService
    ) { }

    ngOnInit() {
        const autonet = this.appStore.autonet as AutonetAuthModel;
        this.params = {
            uid: autonet.uid,
            14: autonet.lid,
            selid: 9
        };
        this.topRightAdsParams = {
            uid: autonet.uid,
            14: autonet.lid
        };
        this.appContext.updateVehicleContext().pipe(first()).toPromise();

        setTimeout(() => {
            this.autonetCommonService.emitPageName(SEARCH_MAIN_PAGE);
        });
    }

    ngOnDestroy(): void {
        this.autonetCommonService.emitPageName('');
    }
}
