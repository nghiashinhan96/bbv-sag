import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { AutonetCommonService } from 'src/app/shared/autonet-common/autonet-common.service';
import { SEARCH_MAIN_PAGE } from '../../car-search-page.contanst';
import { UrlUtil } from 'src/app/core/utils/url.util';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'autonet-car-container',
    templateUrl: './car-container.component.html',
    styleUrls: ['./car-container.component.scss']
})
export class CarContainerComponent implements OnInit {

    topRightAdsUrl = `${UrlUtil.autonetServer(this.appStore.country)}portal/Promo3.aspx`;
    url = `${UrlUtil.autonetServer(this.appStore.country)}portal/promoContainer.aspx`;
    params;
    topRightAdsParams;

    isSearchMainPage = false;
    pageName;
    constructor(
        private appStore: AppStorageService,
        public autonetCommonService: AutonetCommonService,
        private router: Router,
        private activatedRoute: ActivatedRoute
    ) { }

    ngOnInit() {
        this.autonetCommonService.currentPageObs.subscribe((pageName) => {
            this.pageName = pageName;
            this.isSearchMainPage = pageName === SEARCH_MAIN_PAGE;
        });
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
    }

    viewSelectedVehicleHistory(vehicle: any) {
        const vehid = vehicle && vehicle.vehicle_id;
        if (!vehid) {
          return;
        }
        this.router.navigate(['vehicle', vehid, 'quick-click'], {
          relativeTo: this.activatedRoute
        });
    }
}
