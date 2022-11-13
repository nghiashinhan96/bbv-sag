import { Injectable, Injector } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/internal/operators/map';
import { ThemeService } from './theme.service';
import { BehaviorSubject, of } from 'rxjs';
import { tap } from 'rxjs/internal/operators/tap';
import { AppStorageService } from './custom-local-storage.service';
import { UserDetail } from '../models/user-detail.model';
import { LocalStorageService } from 'ngx-webstorage';
import { AutonetAuthModel } from '../models/autonet-auth.model';
import { switchMap } from 'rxjs/operators';
import { LIB_USER_SETTING } from 'sag-article-list';
import { LibUserSetting } from 'sag-article-detail';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private baseUrl = environment.baseUrl;

    private userSettingS$ = new BehaviorSubject<UserDetail>(null);

    userSettingObserver = this.userSettingS$.asObservable();

    constructor(
        private http: HttpClient,
        private themeService: ThemeService,
        private appStorage: AppStorageService,
        private localStorage: LocalStorageService
    ) { }

    initUserSetting() {
        const appToken = this.appStorage.appToken;
        if (!!appToken) {
            return this.getUserSetting().pipe(switchMap(userDetail => {
                return this.updateLoggedSession().pipe(map(res => {
                    return userDetail;
                }));
            }));
        } else {
            return of(null);
        }
    }

    getUserSetting() {
        const url = `${this.baseUrl}user/detail`;
        return this.http.get(url).pipe(map(body => {
            const userDetail = new UserDetail(body);
            const autonet = this.appStorage.autonet;
            userDetail.userCode = autonet.user;
            userDetail.customerName = autonet.customerFirstName;
            const libUserSetting = this.bindUserPriceFromUserDetail(userDetail);
            this.localStorage.store(LIB_USER_SETTING, libUserSetting);
            this.themeService.updateTheme(userDetail);
            this.userSettingS$.next(userDetail);
            return userDetail;
        }));
    }

    updatePriceSetting(libUserSetting) {
        const url = `${this.baseUrl}user/view-net-price/toggle`;
        this.http.post(url, null, { observe: 'body' }).subscribe(res => {
            // updated price
        });
    }

    updateLoggedSession() {
        const autonet = this.appStorage.autonet;
        const request = new AutonetAuthModel(autonet).requestDto;
        const url = `${this.baseUrl}autonet/save-session`;
        return this.http.post(url, request, { observe: 'body' });
    }

    logout(redirectUrl = '') {
        this.appStorage.removeAll();
        const loginUrl = redirectUrl || environment.autonetServer;
        window.location.href = loginUrl;
    }

    private bindUserPriceFromUserDetail(userDetail: UserDetail) {
        const userPrice = new LibUserSetting();
        const userSettings = userDetail.settings;

        userPrice.netPriceView = userSettings.netPriceView;
        userPrice.currentStateNetPriceView = userSettings.currentStateNetPriceView;

        userPrice.vatConfirm = userDetail.vatConfirm;
        userPrice.showDiscount = userSettings.showDiscount;
        userPrice.showTyresDiscount = userSettings.showTyresDiscount;
        userPrice.currentStateVatConfirm = false; // PO: inital state of VAT is exclude
        userPrice.showTyresGrossPriceHeader = userSettings.showTyresGrossPriceHeader;
        userPrice.hasAvailabilityPermission = userDetail.hasAvailabilityPermission;
        userPrice.customerBrandFilterEnabled = userSettings.customerBrandFilterEnabled;
        userPrice.salesBrandFilterEnabled = userSettings.salesBrandFilterEnabled;
        userPrice.mouseOverFlyoutDelay = userDetail.settings.mouseOverFlyoutDelay;
        userPrice.externalPartSettings = userSettings.externalPartSettings;

        return userPrice;
    }
}
