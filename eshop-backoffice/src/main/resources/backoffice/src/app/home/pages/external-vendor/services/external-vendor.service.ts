import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ExternalVendorDetailRequest } from '../model/external-vendor-item.model';
import { ExternalVendorSearch } from '../model/external-vendor-search.model';
import { HttpClient } from '@angular/common/http';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { UploadService } from 'src/app/core/services/upload.service';

@Injectable()
export class ExternalVendorService {

    constructor(
        private http: HttpClient,
        private uploadService: UploadService) { }

    find(id: string): Observable<ExternalVendorDetailRequest> {
        let url = `admin/external-vendor/${id}/find`;
        url = ApiUtil.getUrl(url);
        return this.http.get(url).pipe(
            map(data => new ExternalVendorDetailRequest(data as any))
        );
    }

    search(request: ExternalVendorSearch, page?): Observable<any> {
        let url = page ? `admin/external-vendor/search?page=${page.page}&size=${page.size}` : `admin/external-vendor/search`;
        url = ApiUtil.getUrl(url);
        return this.http.post(url, request);
    }

    create(request: ExternalVendorDetailRequest) {
        let url = 'admin/external-vendor/create';
        url = ApiUtil.getUrl(url);
        return this.http.post(url, request);
    }

    update(request: ExternalVendorDetailRequest) {
        let url = 'admin/external-vendor/update';
        url = ApiUtil.getUrl(url);
        return this.http.post(url, request);
    }

    delete(id: string) {
        let url = `admin/external-vendor/delete/${id}`;
        url = ApiUtil.getUrl(url);
        return this.http.delete(url);
    }

    getInitData(): Observable<any> {
        let url = 'admin/external-vendor/init-data';
        url = ApiUtil.getUrl(url);
        return this.http.get(url);
    }

    upload(file: File) {
        const url = 'admin/external-vendor/import';
        const formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.uploadService.executeUpload(url, formData);
    }
}
