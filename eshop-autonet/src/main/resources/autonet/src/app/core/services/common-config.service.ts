import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppStorageService } from './custom-local-storage.service';

@Injectable({
    providedIn: 'root'
})
export class AutonetCommonConfigService {
    baseUrl: string;
    adsServer: string;
    affiliate: string;
    projectId: string;

    constructor(private appStorage: AppStorageService) {
        this.baseUrl = environment.baseUrl;
        this.affiliate = environment.affiliate;
        this.projectId = 'AUTONET';
    }

    get appToken() {
        return this.appStorage.appToken;
    }
}
