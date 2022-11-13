import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppStorageService } from './app-storage.service';

@Injectable({
    providedIn: 'root'
})
export class ConnectCommonConfigService {
    baseUrl: string;
    adsServer: string;
    affiliate: string;
    projectId: string;

    constructor(private appStorage: AppStorageService) {
        this.baseUrl = environment.baseUrl;
        this.adsServer = environment.adsServer;
        this.affiliate = environment.affiliate;
        this.projectId = 'CONNECT';
    }

    get appToken() {
        return this.appStorage.appToken;
    }
}
