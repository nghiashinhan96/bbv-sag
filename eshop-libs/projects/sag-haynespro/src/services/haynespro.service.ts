import { HaynesProLicenseSettings } from './../models/haynes-pro-license-settings.model';
import { Injectable, Injector } from '@angular/core';
import { HaynesProAccessUrl } from '../models/haynes-pro-access-url.model';
import { HaynesProOption } from '../components/haynespro-modal/haynes-pro-option.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HaynesProIntegrationService } from './haynes-pro-inregration.service';

@Injectable()
export class HaynesProService {

    // haynespro-selected arg
    public readonly HAYNESPRO_SELECTED = 'repairtimes';
    // ultimate license
    public readonly HAYNESPRO_LICENSE = 'ultimate';

    constructor(
        private http: HttpClient,
        private haynesProIntegrationService: HaynesProIntegrationService
    ) { }

    getAccessOptions(): Observable<HaynesProOption[]> {
        return this.http.get<HaynesProOption[]>(`${this.haynesProIntegrationService.baseUrl}haynespro/access/options`);
    }

    getAccessUrl(request: HaynesProAccessUrl) {
        return this.http.post(`${this.haynesProIntegrationService.baseUrl}haynespro/access/url`, request);
    }

    getResponse(vehicleId) {
        const url = `${this.haynesProIntegrationService.baseUrl}haynespro/response?vehicleId=${vehicleId}`;
        return this.http.get(url);
    }

    getHaynesProLicense() {
        return this.http.get(`${this.haynesProIntegrationService.baseUrl}haynespro/license`);
    }

    requestTrialLicense() {
        return this.http.get(`${this.haynesProIntegrationService.baseUrl}haynespro/license/request?type=trial`);
    }

    getCategoriesByGaids(gaid) {
        return this.haynesProIntegrationService.getCategoriesByGaids(gaid);
    }

    checkOnCategoryTree(categoryIds, isChecked, emitSearchEvent) {
        return this.haynesProIntegrationService.checkOnCategoryTree(categoryIds, isChecked, emitSearchEvent);
    }

    checkOilCate(cateIds, emitSearchEvent) {
        return this.haynesProIntegrationService.checkOilCate(cateIds, emitSearchEvent);
    }

    get spinner() {
        return this.haynesProIntegrationService.spinner;
    }

    get baseUrl() {
        return this.haynesProIntegrationService.baseUrl;
    }

}
