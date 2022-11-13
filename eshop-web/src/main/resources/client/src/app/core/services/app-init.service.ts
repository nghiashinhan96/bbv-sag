import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';
import { switchMap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { AffiliateService } from './affiliate.service';
import { AppStorageService } from './app-storage.service';
import { ThemeService } from './theme.service';
import { UserService } from './user.service';
import { OciUtil } from 'src/app/oci/utils/oci.util';
import { OciService } from 'src/app/oci/services/oci.service';
import { ParamUtil } from '../utils/params.utils';
import { SagNumericService } from 'sag-currency';
import { DmsUtil } from 'src/app/dms/utils/dms.util';
import { AppCommonService } from './app-common.service';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { OptimizelyService } from 'src/app/analytic-logging/services/optimizely.service';

@Injectable({
    providedIn: 'root'
})
export class AppInitService {
    private baseUrl = environment.baseUrl;

    constructor(
        private appStorage: AppStorageService,
        private themeService: ThemeService,
        private http: HttpClient,
        private affiliateService: AffiliateService,
        private ociService: OciService,
        private userService: UserService,
        private sagLibNumericService: SagNumericService,
        private appCommonService: AppCommonService,
        private gaService: GoogleAnalyticsService,
        private optimizelyService: OptimizelyService
    ) { }

    init() {
        const affiliate = environment.affiliate;
        const query = window.location.search.substring(1);
        const queryParam = ParamUtil.parseQueryFromUrl(query);
        if (OciUtil.isOciQuery(queryParam)) {
            this.ociService.preUrlHandle(queryParam);
        }
        return forkJoin([
            this.affiliateService.getAffiliateSettings(affiliate),
            this.getJsonEventList(),
            this.getAppVersion()
        ]).pipe(
            switchMap((unauthRes) => {
                const setting = unauthRes[0];
                this.appStorage.jsonEventList = unauthRes[1];
                this.themeService.setTheme(setting);
                this.appStorage.defaultSetting = setting;
                // tslint:disable-next-line: no-string-literal
                this.appStorage.currencyLocale = setting['setting_locale'];
                this.sagLibNumericService.updateSetting();
                const version: any = unauthRes[2];

                const appVer = this.appStorage.appVersion || this.appStorage.appOldVersion;
                if (appVer && appVer !== version.releaseVersion) {
                    this.appStorage.appVersion = version.releaseVersion;
                    this.appCommonService.invalidVersionMessage();
                    return of(null);
                }
                this.appStorage.appVersion = version.releaseVersion;
                if (DmsUtil.isDmsQuery(queryParam)) {
                    this.appStorage.removeAll();
                    return of(null);
                }

                setTimeout(() => {
                    this.gaService.clear();
                    this.gaService.initiateGoogleAnalytics(setting);
                    this.optimizelyService.initScript(setting);
                });

                return this.userService.initUser();
            })
        ).toPromise();

    }

    getAppVersion() {
        const timestamp = new Date().getTime();
        const url = `release?ver=${timestamp}`;
        return this.http.get(`${this.baseUrl}${url}`);
    }

    getJsonEventList() {
        return this.affiliateService.getJsonEventList();
    }
}
