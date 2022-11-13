import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { SalesSavingItemModel } from '../../model/sales-saving-item.model';
import { SalesSearchItemModel } from '../../model/sales-search-item.model';
import { ApiUtil } from 'src/app/core/utils/api.util';


@Injectable()
export class AadAccountService {

    constructor(
        private http: HttpClient
    ) { }

    create(model: SalesSavingItemModel) {
        const url = ApiUtil.getUrl('admin/aad-account/create');
        return this.http.post(url, model);
    }

    edit(model: SalesSavingItemModel, id: number) {
        const url = ApiUtil.getUrl(`admin/aad-account/${id}/update`);
        return this.http.put(url, model);
    }

    search(model: SalesSearchItemModel, page: any) {
        let url = page ? `admin/aad-account/search?page=${page.page}&size=${page.size}` : 'admin/aad-account/search';
        url = ApiUtil.getUrl(url);
        return this.http.post(url, model);
    }

    findById(id: number) {
        const url = ApiUtil.getUrl(`admin/aad-account/${id} `)
        return this.http.get(url);
    }
}
