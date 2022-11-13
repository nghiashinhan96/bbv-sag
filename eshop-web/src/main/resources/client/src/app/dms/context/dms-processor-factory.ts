import { DmsProcessor } from './dms-processor';
import { Injectable, Injector } from '@angular/core';
import { CloudDmsProcessor } from './cloud-dms-processor';
import { MetBrowserProcessor } from './met-browser-processor';
import { DmsInfo } from '../models/dms-info.model';
import { DmsUtil } from '../utils/dms.util';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { DmsBaseService } from '../services/dms-base.service';
import { Params, Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class DmsProcessorFactory {
    constructor(
        private cloudDmsProcessor: CloudDmsProcessor,
        private metBrowserProcessor: MetBrowserProcessor,
        private appStorage: AppStorageService,
        private dmsStorageService: DmsBaseService,
        private injector: Injector
    ) { }

    get(): DmsProcessor {
        const dmsInfo = this.dmsStorageService.getDmsInfo();
        const isValidCloudDms = dmsInfo && DmsUtil.isValidCloudDms(dmsInfo.token, dmsInfo.hookUrl);
        if (isValidCloudDms) {
            return this.cloudDmsProcessor;
        }
        return this.metBrowserProcessor;
    }

    preUrlHandle(params: Params) {
        if (DmsUtil.isDmsQuery(params)) {
            this.appStorage.removeAll();
            const dmsInfo = DmsInfo.extractDmsInfo(params);
            this.dmsStorageService.updateDmsInfo(dmsInfo);
            const router = this.injector.get(Router);
            router.navigate([],
                {
                    queryParams: {},
                    queryParamsHandling: 'merge',
                    replaceUrl: true
                });
        }
    }
}
