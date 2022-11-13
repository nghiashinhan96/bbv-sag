import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { cloneDeep } from 'lodash';
import { MAKE_MODEL_TYPE_CARS_HISTORY, MAKE_MODEL_TYPE_MOTO_HISTORY } from '../constant';

@Injectable()
export class ArticleSearchStorageService {

    constructor(private storage: LocalStorageService) { }

    get makeModelTypeCarsHistory() {
        const data = this.storage.retrieve(MAKE_MODEL_TYPE_CARS_HISTORY);
        return data ? cloneDeep(data) : null;
    }

    set makeModelTypeCarsHistory(value) {
        this.storage.store(MAKE_MODEL_TYPE_CARS_HISTORY, value);
    }

    get makeModelTypeMotoHistory() {
        const data =  this.storage.retrieve(MAKE_MODEL_TYPE_MOTO_HISTORY);
        return data ? cloneDeep(data) : null;
    }

    set makeModelTypeMotoHistory(value) {
        this.storage.store(MAKE_MODEL_TYPE_MOTO_HISTORY, value);
    }
}
