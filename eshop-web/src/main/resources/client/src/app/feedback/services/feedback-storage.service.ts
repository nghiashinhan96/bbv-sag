import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Constant } from 'src/app/core/conts/app.constant';

@Injectable({
    providedIn: 'root'
})
export class FeedbackStorageService {
    constructor(
        private storage: LocalStorageService
    ) { }

    getFeedbackTechnicalData(userId) {
        const key = `${Constant.FEEDBACK_TECHNICAL_DATA_KEY}_${userId}`;
        return this.storage.retrieve(key);
    }

    setFeedbackTechnicalData(userId, value) {
        const key = `${Constant.FEEDBACK_TECHNICAL_DATA_KEY}_${userId}`;
        this.storage.store(key, value);
    }
}
