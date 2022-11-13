import { Injectable } from '@angular/core';
import { SpinnerService } from '../core/utils/spinner';
import { environment } from 'src/environments/environment';

import * as cookie from 'js-cookie';

@Injectable({ providedIn: 'root' })
export class ThuleConnectConfigService {

    baseUrl: string;
    spinner: any;
    cookie: any;
    thuleStorageKey = 'THULE_DATA_KEY';

    constructor() {
        this.baseUrl = environment.baseUrl;
        this.spinner = SpinnerService;
        this.cookie = cookie;
    }
}
