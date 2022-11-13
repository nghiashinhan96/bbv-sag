import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Constant } from 'src/app/core/conts/app.constant';
import { SearchHistory } from '../models/search-history.model';

@Injectable({
    providedIn: 'root'
})
export class ArticleListSearchStorageService {
    constructor(
        private storage: LocalStorageService
    ) { }

    getHistory(userId: number) {
        const key = `${Constant.KEY_ARTICLE_LIST_SEARCHED}_${userId}`;
        return this.storage.retrieve(key) || [];
    }

    setHistory(userId: number, value: SearchHistory[]) {
        const key = `${Constant.KEY_ARTICLE_LIST_SEARCHED}_${userId}`;
        this.storage.store(key, value);
    }

    getSubBasketHistory(userId: number) {
        const key = `${Constant.KEY_ARTICLE_LIST_SEARCHED}_${userId}_${Constant.SUB_BASKET_TEXT}`;
        return this.storage.retrieve(key) || [];
    }

    setSubBasketHistory(userId: number, value) {
        const key = `${Constant.KEY_ARTICLE_LIST_SEARCHED}_${userId}_${Constant.SUB_BASKET_TEXT}`;
        this.storage.store(key, value);
    }

    clearAllHistory(userId) {
        this.setHistory(userId, []);
        this.setSubBasketHistory(userId, []);
    }
}
