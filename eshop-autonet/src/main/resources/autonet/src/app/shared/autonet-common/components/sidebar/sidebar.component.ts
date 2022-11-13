import { Component, Input, OnInit } from '@angular/core';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { AutonetCommonService } from 'src/app/shared/autonet-common/autonet-common.service';
import { UrlUtil } from 'src/app/core/utils/url.util';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'autonet-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
    @Input() enableFreetextSearch = true;

    topRightAdsUrl = `${UrlUtil.autonetServer(this.appStore.country)}portal/Promo3.aspx`;
    topRightAdsParams;

    constructor(
        private appStore: AppStorageService,
        public autonetCommonService: AutonetCommonService,
        private router: Router,
        private activatedRoute: ActivatedRoute
    ) { }

    ngOnInit() {
        const autonet = this.appStore.autonet as AutonetAuthModel;
        this.topRightAdsParams = {
            uid: autonet.uid,
            14: autonet.lid
        };
    }

    viewSelectedVehicleHistory(vehicle: any) {
        const vehid = vehicle && vehicle.vehicleId;
        if (!vehid) {
          return;
        }
        this.router.navigate(['cars', 'vehicle', vehid, 'quick-click']);
    }
}
