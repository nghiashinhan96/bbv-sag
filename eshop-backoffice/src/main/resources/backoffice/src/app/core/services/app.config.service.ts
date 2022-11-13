import { Injectable, Injector } from '@angular/core';
import { ReleaseService } from './release.service';
import { map } from 'rxjs/operators';
import { AppStorageService } from './custom-local-storage.service';
import { AppCommonService } from './app-common.service';
import { environment } from 'src/environments/environment';
import { SpinnerService } from '../utils/spinner';

@Injectable({
    providedIn: 'root'
})
export class AppConfigService {
    constructor() {

    }
    get spinner() {
        return SpinnerService;
    }
    get baseUrl() {
        return environment.baseUrl;
    }
    get affiliate() {
        return '';
    }
}
