import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { MessageSearchCriteriaModel } from '../models/message-search-criteria.model';
import { MessageSavingModel } from '../models/message-saving.model';
import { ApiUtil } from 'src/app/core/utils/api.util';


@Injectable()
export class MessageService {

    constructor(
        private http: HttpClient
    ) { }

    getMasterData() {
        const url = ApiUtil.getUrl('admin/message/master-data');
        return this.http.get(url);
    }

    create(model: MessageSavingModel) {
        const url = ApiUtil.getUrl('admin/message/create');
        return this.http.post(url, model);
    }

    getFilteringOptions() {
        const url = ApiUtil.getUrl('admin/message/search/master-data');
        return this.http.get(url);
    }

    search(criteria: MessageSearchCriteriaModel, page?) {
        let url = page ? `admin/message/search?page=${page.page}&size=${page.size}` : `admin/message/search`;
        url = ApiUtil.getUrl(url);
        return this.http.post(url, criteria);
    }

    delete(messageId) {
        const url = ApiUtil.getUrl(`admin/message/${messageId}/delete`);
        return this.http.delete(url);
    }

    findById(messageId) {
        const url = ApiUtil.getUrl(`admin/message/${messageId}`);
        return this.http.get(url);
    }

    update(messageId, model: MessageSavingModel) {
        const url = ApiUtil.getUrl(`admin/message/${messageId}/update`);
        return this.http.put(url, model);
    }
}
