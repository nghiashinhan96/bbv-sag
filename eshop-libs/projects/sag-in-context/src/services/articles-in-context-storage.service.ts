import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { CLASSIC_CATE_VIEW_KEY, APP_ARTICLE_MODE, APP_SELECTED_VEHICLE } from '../articles.const';

@Injectable()
export class SagInContextStorageService {
    constructor(
        private storage: LocalStorageService,
    ) { }

    set articleMode(mode: string) {
        this.storage.store(APP_ARTICLE_MODE, mode);
    }

    get articleMode() {
        return this.storage.retrieve(APP_ARTICLE_MODE);
    }

    set selectedVehicle(vehicle: any) {
        this.storage.store(APP_SELECTED_VEHICLE, vehicle);
    }

    get selectedVehicle() {
        return this.storage.retrieve(APP_SELECTED_VEHICLE) || null;
    }

    set classicViewMode(value: boolean) {
        this.storage.store(CLASSIC_CATE_VIEW_KEY, value);
    }

    get classicViewMode() {
        return this.storage.retrieve(CLASSIC_CATE_VIEW_KEY);
    }


}
