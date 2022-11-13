import { Injectable, Injector } from '@angular/core';
import { ReleaseService } from './release.service';
import { map } from 'rxjs/operators';
import { AppStorageService } from './custom-local-storage.service';
import { AppCommonService } from './app-common.service';

@Injectable({
    providedIn: 'root'
})
export class AppInitService {

    constructor(
        private release: ReleaseService,
        private appStorage: AppStorageService,
        private injector: Injector
    ) { }

    init() {
        return this.release.getRelease().pipe(
            map(version => {
                const appVer = this.appStorage.appVersion;
                if (appVer && appVer !== version.releaseVersion) {
                    this.appStorage.appVersion = version.releaseVersion;
                    const appCommonService = this.injector.get(AppCommonService)
                    appCommonService.invalidVersionMessage();
                    return null;
                }
                this.appStorage.appVersion = version.releaseVersion;
                return version;
            })
        ).toPromise();
    }
}
