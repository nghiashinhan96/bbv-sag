import { Injectable } from '@angular/core';
import { HaynesProService } from './haynespro.service';
import { HaynesProAccessUrl } from '../models/haynes-pro-access-url.model';
import { TranslateService } from '@ngx-translate/core';
import { HaynesProIntegrationService } from './haynes-pro-inregration.service';
import { HaynesProLicense } from '../enums/haynespro.enum';

@Injectable()
export class HaynesLinkHandleService {

    linkLoginHaynesPro = [];

    constructor(
        private haynesProService: HaynesProService,
        private translateService: TranslateService,
        private haynesProIntegration: HaynesProIntegrationService
    ) { }

    getLinkHaynesPro(userDetail, groupVehiclesCategories) {
        const groupVehicleIds = groupVehiclesCategories.filter(item => item.vehicleId !== '');
        for (let i = 0; i < groupVehicleIds.length; i++) {
            this.loginHaynesPro(userDetail, groupVehicleIds[i].vehicleId, i);
        }
    }

    loginHaynesPro(userDetail, vehicleId, index) {
        // find vehicleId belongs to selected icon +
        this.haynesProIntegration.getVehiclesByVehId(vehicleId).subscribe(
            (res: any) => {
                this.linkLoginHaynesPro[index] = {};
                const vatRate = userDetail.settings ? userDetail.settings.vatRate : null;
                const request = new HaynesProAccessUrl();
                const hourlyRate = userDetail.hourlyRate;
                request.subject = this.haynesProService.HAYNESPRO_SELECTED;
                request.licenseType = HaynesProLicense.ULTIMATE.toString();
                request.hourlyRate = isNaN(hourlyRate) ? '' : hourlyRate.toString();
                request.vatRate = isNaN(vatRate) ? '' : vatRate.toString();
                request.kType = res.ktype;
                request.motorId = res.id_motor;
                request.callbackBtnText = this.translateService.instant('HAYNES_PRO.EXPORT_TO_WEBSHOP');

                // call one api for search, user can not save to cache
                this.doSearchAndOpenHaynesPro(index, request, vehicleId, false);
                // call one api for add, user can save to cache
                this.doSearchAndOpenHaynesPro(index, request, vehicleId, true);
            }, error => {
                console.log('error getting vehicle info ' + error);
            }
        );
    }

    private doSearchAndOpenHaynesPro(index, request: HaynesProAccessUrl, vehicle, isSaveToCache) {
        // enable button save to cache, so user can retrieve data from BE side
        request.callbackBtnText = isSaveToCache ? this.translateService.instant('HAYNES_PRO.EXPORT_TO_WEBSHOP') : '';
        this.haynesProService.getAccessUrl(request).subscribe((resp: any) => {
            if (isSaveToCache) {
                this.linkLoginHaynesPro[index].url_add = resp.accessUrl;
            } else {
                this.linkLoginHaynesPro[index].url_search = resp.accessUrl;
            }
            this.linkLoginHaynesPro[index].vehicleId = vehicle;
        }, error => {
            console.log('error occured when get response from haynesPro: ', error);
        });
    }
}
