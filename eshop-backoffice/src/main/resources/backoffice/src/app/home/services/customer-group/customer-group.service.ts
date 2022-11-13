import { Injectable } from '@angular/core';

import uuid from 'uuid/v4';
import { Observable } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';

import { CustomerGroupRequestModel } from '../..//models/customer-group/customer-group-request.model';
import { CustomerGroupSearchModel } from '../../models/customer-group/customer-group-search.model';
import { CustomerNrPageModel } from '../../models/customer-group/customer-nr-page.model';
import { AffiliatePermission } from '../../models/affiliate/affiliate-permission.model';
import { CustomerGroupDetailModel } from '../../models/customer-group/customer-group-detail.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { TranslateService } from '@ngx-translate/core';
import { X_SAG_REQUEST_ID_HEADER_NAME } from 'src/app/core/conts/app.constant';
import { UploadService } from 'src/app/core/services/upload.service';

@Injectable()
export class CustomerGroupService {
    private customerNrPageSize = 500;

    constructor(
        private http: HttpClient,
        private translateService: TranslateService,
        private storage: AppStorageService,
        private uploadService: UploadService
    ) { }

    search(
        searchRequest: CustomerGroupRequestModel
    ): Observable<CustomerGroupSearchModel> {
        const url = ApiUtil.getUrl('admin/collections/search');
        return this.http.post(url, searchRequest).pipe(
            map((res) => {
                return new CustomerGroupSearchModel(res);
            })
        );
    }

    getCustomersByCollectionShortName(
        collectionShortName: string,
        page = 0
    ): Observable<CustomerNrPageModel> {
        const url = ApiUtil.getUrl(`admin/collections/${collectionShortName}/customers?page=${page}&size=${this.customerNrPageSize}`);
        return this.http.get(url).pipe(
            map((res) => {
                return new CustomerNrPageModel(res);
            })
        );
    }

    getTemplateByAffiliateShortName(
        affiliateShortName
    ): Observable<AffiliatePermission> {
        const url = ApiUtil.getUrl(`admin/collections/template/${affiliateShortName}`);
        return this.http.get(url) as Observable<any>;
    }

    uploadLogo(uploadType, file): Observable<any> {
        const url = `admin/upload/media/${uploadType}`;
        const formData = new FormData();
        formData.append('file', file, file.name);
        return this.uploadService.executeUpload(url, formData);
    }

    removeLogo(uploadType, fileName) {
        const headers = {
            Authorization: `Bearer ${this.storage.appToken}`,
            'Accept-Language': this.translateService.currentLang,
            [X_SAG_REQUEST_ID_HEADER_NAME]: uuid()
        };
        const options = { headers };

        const url = ApiUtil.getUrl(`admin/upload/remove/${uploadType}/${fileName}`);
        return this.http.delete(url, options);
    }

    createNewGroup(body) {
        const url = ApiUtil.getUrl('admin/collections/create');
        return this.http.post(url, body);
    }

    updateGroup(body) {
        const url = ApiUtil.getUrl('admin/collections/update');
        return this.http.post(url, body);
    }

    getCustomerGroup(collectionShortName): Observable<CustomerGroupDetailModel> {
        const url = ApiUtil.getUrl(`admin/collections/${collectionShortName}`);
        return this.http.get(url) as Observable<any>;
    }
}
