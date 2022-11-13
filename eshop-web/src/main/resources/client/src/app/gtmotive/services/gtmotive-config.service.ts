import { Injectable } from '@angular/core';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class GtmotiveConfigService {
    libUserSetting: any;
    baseUrl: string;
    spinner: any;
    gtmotive: any;

    constructor(
        private appStorage: AppStorageService
    ) {
        this.libUserSetting = this.appStorage.userPrice;
        this.baseUrl = environment.baseUrl;
        this.spinner = SpinnerService;
        this.gtmotive = environment.gtmotive;
    }
}
