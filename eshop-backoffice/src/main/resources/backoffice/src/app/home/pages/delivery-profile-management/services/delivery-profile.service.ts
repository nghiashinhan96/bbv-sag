import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';

import { DeliveryProfileModel } from '../models/delivery-profile.model';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { UploadService } from 'src/app/core/services/upload.service';

@Injectable()
export class DeliveryProfileService {

    constructor(
        private http: HttpClient,
        private uploadService: UploadService) { }

    importDeliveryProfile(body: FormData) {
        const url = 'admin/delivery-profile/import';
        return this.uploadService.executeUpload(url, body);
    }

    getDeliveryProfile(id: number): Observable<DeliveryProfileModel> {
        let url = `admin/delivery-profile/${id}/find`;
        url = ApiUtil.getUrl(url);
        return this.http.get(url).pipe(
            map(val => new DeliveryProfileModel(val as any))
        );
    }

    search(body) {
        let url = 'admin/delivery-profile/search';
        url = ApiUtil.getUrl(url);
        return this.http.post(url, body);
    }

    createOrupdateDeliveryProfile(body) {
        if (body.id) {
            let url = `admin/delivery-profile/${body.id}/update`;
            url = ApiUtil.getUrl(url);
            return this.http.put(url, body);
        } else {
            let url = 'admin/delivery-profile/create';
            url = ApiUtil.getUrl(url);
            return this.http.post(url, body);
        }
    }

    deleteDeliveryProfile(deliveryProfileId: number) {
        let url = `admin/delivery-profile/${deliveryProfileId}/remove`;
        url = ApiUtil.getUrl(url);
        return this.http.delete(url);
    }

    initDropdownData() {
        let url = 'admin/delivery-profile/init-data';
        url = ApiUtil.getUrl(url);
        return this.http.get(url);
    }

    searchDeliveryProfile(deliveryProfileId) {
        let url = `admin/delivery-profile/${deliveryProfileId}/delivery-profile-name`;
        url = ApiUtil.getUrl(url);
        return this.http.get(url).pipe(
            map(res => res && (res as any).deliveryProfileName)
        );
    }
}
