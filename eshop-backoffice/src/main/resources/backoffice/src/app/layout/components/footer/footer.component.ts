import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ReleaseService } from 'src/app/core/services/release.service';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { BUILD_ENVIRONMENT } from 'src/app/core/conts/app.constant';
import { AppCommonService } from 'src/app/core/services/app-common.service';

@Component({
    selector: 'backoffice-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

    buildEnv: string;
    version: string;

    constructor(
        private appStorage: AppStorageService,
        private releaseService: ReleaseService,
        private appCommonService: AppCommonService
    ) { }

    ngOnInit() {
        this.getEnvironment();
        this.getAppReleaseVersion();
    }

    private getEnvironment() {
        const env = `${environment.env}` || 'dev';
        this.buildEnv = BUILD_ENVIRONMENT[env];
    }

    private getAppReleaseVersion() {
        if (!this.appStorage.appToken) {
            return;
        }
        this.releaseService.getRelease().subscribe(
            res => {
                this.version = res.releaseVersion;
                const appVer = this.appStorage.appVersion;
                if (appVer && appVer !== res.releaseVersion) {
                    this.appStorage.appVersion = res.releaseVersion;
                    this.appCommonService.invalidVersionMessage();
                    return null;
                }
                this.appStorage.appVersion = res.releaseVersion;
            }
        );
    }
}
