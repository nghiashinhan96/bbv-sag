import { Injectable, Injector } from '@angular/core';
import { AffiliateService } from './affiliate.service';
import { map } from 'rxjs/internal/operators/map';
import { ThemeService } from './theme.service';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { forkJoin, of } from 'rxjs';
import { UserService } from './user.service';
import { AppStorageService } from './custom-local-storage.service';
import { finalize, switchMap, catchError } from 'rxjs/operators';
import { Router, ActivatedRoute } from '@angular/router';
import { AppContextService } from './app-context.service';
import { DEFAULT_LOCALE } from '../conts/app.constant';

@Injectable({
    providedIn: 'root'
})
export class AppInitService {
    private baseUrl = environment.baseUrl;

    constructor(
        private affilite: AffiliateService,
        private appStorage: AppStorageService,
        private themeService: ThemeService,
        private http: HttpClient,
        private userService: UserService,
        private injector: Injector,
        private appContextService: AppContextService
    ) { }

    init() {
        const router = this.injector.get(Router);
        const activatedRoute = this.injector.get(ActivatedRoute);
        const queryParam = this.parseQueryString();
        if (Object.keys(queryParam).length > 0) {
            this.appStorage.autonet = queryParam;
            router.navigate([],
                {
                    relativeTo: activatedRoute,
                    queryParams: {},
                    queryParamsHandling: 'merge',
                    replaceUrl: true
                });
        }
        return forkJoin([
            this.userService.initUserSetting(),
            this.getAppVersion()
        ]).pipe(
            switchMap(res => {
                const userSetting = res[0];
                const version: any = res[1];
                const appVer = this.appStorage.appVersion;
                if (appVer && appVer !== version.releaseVersion) {
                    // TODO show new release message and reload app
                    window.location.reload();
                }
                // this.appStorage.defaultSetting = setting;
                this.appStorage.appVersion = version.releaseVersion;
                const affiliate = userSetting.affiliateShortName;
                return forkJoin([
                    this.affilite.getAffiliateSettings(affiliate),
                    this.appContextService.initAppContext(),
                ]).pipe(
                    map((res2: any) => {
                        const affiliateSettings = res2[0];
                        // this.themeService.setTheme(affiliateSettings);
                        this.appStorage.currencyLocale = affiliateSettings.setting_locale;
                        return;
                    }),
                    catchError(err => {
                        this.appStorage.currencyLocale = DEFAULT_LOCALE;
                        return of(null);
                    })
                );
            })
        ).toPromise().catch(ex => {
            console.log(ex);
            // do nothing
        });
    }

    getAppVersion() {
        const timestamp = new Date().getTime();
        const url = `release?ver=${timestamp}`;
        return this.http.get(`${this.baseUrl}${url}`);
    }

    private parseQueryString() {
        const query = window.location.search.substring(1);
        const result = {};
        if (!!query && query.indexOf('Token') !== -1 && query.indexOf('UID') !== -1) {
            const varibales = query.split('&');
            varibales.map(varibale => {
                const pair = varibale.split('=');
                Object.assign(result, { [pair[0]]: decodeURIComponent(pair[1]) });
            });
        }
        return result;
    }
}
