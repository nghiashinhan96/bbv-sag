import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { BehaviorSubject } from 'rxjs';

import { AffiliatesSearch } from '../../home/models/affiliate/affiliate-search.model';
import { AffiliateSettingRequest } from '../../home/models/affiliate/affiliate-setting-request';
import { ApiUtil } from '../utils/api.util';

@Injectable()
export class AffiliateService {
    private affiliatesSubj = new BehaviorSubject<AffiliatesSearch>(
        new AffiliatesSearch()
    );

    constructor(
        private http: HttpClient) { }

    getShortInfos() {
        const url = ApiUtil.getUrl('admin/affiliates/short-infos');
        return this.http.get(url);
    }

    getInfos(affShortName?: string) {
        const path = affShortName ? `admin/affiliates/infos?affShortName=${affShortName}` : 'admin/affiliates/infos';
        const url = ApiUtil.getUrl(path);
        return this.http.get(url);
    }

    getSettings(affShortName: string) {
        const url = ApiUtil.getUrl(`admin/affiliates/settings?affShortName=${affShortName}`);
        return this.http.get(url);
    }

    updateSettings(model: AffiliateSettingRequest) {
        const url = ApiUtil.getUrl('admin/affiliates/settings/update');
        return this.http.post(url, model);
    }

    getAvailMasterData() {
        const url = ApiUtil.getUrl('admin/affiliates/settings/availability/master-data');
        return this.http.get(url);
    }
}
