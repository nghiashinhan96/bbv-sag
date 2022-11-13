import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { ArticleModel } from 'sag-article-detail';
import { SAG_COMMON_OIL_IDS } from 'sag-common';
import {
    LIB_IS_SIMPLE_MODE,
    LIB_USER_SETTING,
    LIB_SELECTED_CATEGORIES,
    LIB_HIDE_NO_AVAIL_ARTICLE,
    ACCESSORY_ARTICLE
} from '../consts/article-list.const';

@Injectable()
export class ArticleListStorageService {

    constructor(private storage: LocalStorageService) { }

    set isSimpleMode(status: boolean) {
        this.storage.store(LIB_IS_SIMPLE_MODE, status);
    }

    get isSimpleMode() {
        return this.storage.retrieve(LIB_IS_SIMPLE_MODE);
    }

    isSimpleModeChange() {
        return this.storage.observe(LIB_IS_SIMPLE_MODE);
    }

    set libUserSetting(setting) {
        this.storage.store(LIB_USER_SETTING, setting);
    }

    get libUserSetting() {
        return this.storage.retrieve(LIB_USER_SETTING);
    }

    isLibUserSettingChange() {
        return this.storage.observe(LIB_USER_SETTING);
    }

    set selectedCateIds(cates: string[]) {
        this.storage.store(LIB_SELECTED_CATEGORIES, cates);
    }

    get selectedCateIds() {
        return this.storage.retrieve(LIB_SELECTED_CATEGORIES);
    }

    set selectedOils(value: any) {
        const selectedIds: any = this.selectedOils;
        if (Array.isArray(value)) {
            (value || []).forEach(newVal => {
                const notFound = !selectedIds.find(val => val.key === newVal.key);
                if (notFound) {
                    selectedIds.push(newVal);
                }
            });
        } else {
            const notFound = !selectedIds.find(val => val.key === value.key);
            if (notFound) {
                selectedIds.push(value);
            }
        }
        this.storage.store(SAG_COMMON_OIL_IDS, [...selectedIds]);
    }

    get selectedOils() {
        return this.storage.retrieve(SAG_COMMON_OIL_IDS) || [];
    }

    set hideNonAvailArticle(checked: boolean) {
        this.storage.store(LIB_HIDE_NO_AVAIL_ARTICLE, checked);
    }

    get hideNonAvailArticle() {
        return this.storage.retrieve(LIB_HIDE_NO_AVAIL_ARTICLE);
    }

    isHideNonAvailArticleChange() {
        return this.storage.observe(LIB_HIDE_NO_AVAIL_ARTICLE);
    }

    removeSelectedOil(key: string) {
        const selectedIds: any = this.selectedOils;
        const remaining = selectedIds.filter(val => val.key !== key);
        this.storage.store(SAG_COMMON_OIL_IDS, [...remaining]);
    }

    removeAllSelectedOil() {
        this.storage.clear(SAG_COMMON_OIL_IDS);
    }

    set accessoryArticle(value: ArticleModel) {
        this.storage.store(ACCESSORY_ARTICLE, value);
    }

    get accessoryArticle() {
        return this.storage.retrieve(ACCESSORY_ARTICLE);
    }
}
