import { Injectable, Injector } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { TranslateService } from '@ngx-translate/core';
import { catchError, timeoutWith, finalize } from 'rxjs/operators';
import { Observable } from 'rxjs/internal/Observable';
import { BehaviorSubject, of, forkJoin, throwError, from } from 'rxjs';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { map, tap } from 'rxjs/internal/operators';
import { intersection, isEmpty, get } from 'lodash';

import { AffiliateUtil, VatTypeDisplayUtil, SagAvailDisplaySettingModel, SAG_AVAIL_DISPLAY_STATES, SAG_AVAIL_DISPLAY_OPTIONS, SagConfirmationBoxComponent } from 'sag-common';
import { AuthModel, SagAuthService } from 'sag-auth';
import { LibUserSetting, ARTICLE_MODE, AvailabilityUtil } from 'sag-article-detail';
import { SagCustomPricingStorageService } from 'sag-custom-pricing';

import { ThemeService } from './theme.service';
import { AppContextService } from './app-context.service';
import { CreditLimitService } from './credit-limit.service';
import { ShoppingBasketService } from './shopping-basket.service';
import { AppStorageService } from './app-storage.service';
import { ShoppingBasketHistoryService } from './shopping-basket-history.service';
import { CustomerModel } from '../models/customer.model';
import { UserDetail } from '../models/user-detail.model';
import { UserSetting } from '../models/user-setting.model';
import { PaymentSetting } from '../models/payment-settings.model';
import { ESHOP_ROLES } from '../enums/eshop-role.enum';
import { environment } from 'src/environments/environment';
import { Constant, LEGAL_TERM_NOT_ACCEPTED } from '../conts/app.constant';
import { apiCallTimeout } from '../interceptors/auth.interceptor';
import { SpinnerService } from '../utils/spinner';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { Adal8Service } from 'adal-angular8';
import { AppModalService } from './app-modal.service';
import { GET_CUSTOMER_SETTING_URL, UPDATE_SHOW_NET_PRICE } from 'src/app/wholesaler/services/constant';
import { CustomerSetting } from 'src/app/settings/models/customer-setting.model';
import { LegalTermService } from './legal-term.service';
import { LegalTermModalService } from './legal-term-modal.service';
import { CountryUtil } from '../utils/country.util';
import { StringUtil } from 'src/app/dms/utils/string.util';
import { BsModalService } from 'ngx-bootstrap/modal';
import { LegalDocument } from '../models/legal-document.model';
import { OptimizelyService } from 'src/app/analytic-logging/services/optimizely.service';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private tokenUrl = environment.tokenUrl;
    private baseUrl = environment.baseUrl;

    private userDetailSub$ = new BehaviorSubject<UserDetail>(null);
    private userSettingSub$ = new BehaviorSubject<UserSetting>(null);
    private userPriceSub$ = new BehaviorSubject<any>(new LibUserSetting());
    private employeeInfoSub$ = new BehaviorSubject<any>(null);
    private userPaymentSettingSub$ = new BehaviorSubject<PaymentSetting>(null);

    employeeInfo: any;
    userDetail: UserDetail;
    userSetting: UserSetting;
    userPrice: LibUserSetting = new LibUserSetting();
    userPaymentSetting: PaymentSetting;
    articleMode: string;

    isWbb = AffiliateUtil.isWbb(environment.affiliate);
    isEhCh = AffiliateUtil.isEhCh(environment.affiliate);
    isCz = AffiliateUtil.isCz(environment.affiliate);
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);

    private isSb = AffiliateUtil.isSb(environment.affiliate);
    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);
    isCzBased = this.isCz || this.isEhCz || this.isAxCz;
    isAffiliateApplyToGetCustomerCurrency = AffiliateUtil.isAffiliateApplyToGetCustomerCurrency(environment.affiliate);
    isAffiliateApplyArticleExtendedMode = AffiliateUtil.isAffiliateApplyArticleExtendedMode(environment.affiliate);

    constructor(
        private http: HttpClient,
        private themeService: ThemeService,
        private appStorage: AppStorageService,
        private translateService: TranslateService,
        private appContextService: AppContextService,
        private shoppingBasketService: ShoppingBasketService,
        private creditLimitService: CreditLimitService,
        private shoppingBasketHistoryService: ShoppingBasketHistoryService,
        private injector: Injector,
        private gaService: GoogleAnalyticsService,
        private optimizelyService: OptimizelyService,
        private authService: SagAuthService,
        private adal8Service: Adal8Service,
        private fbRecordingService: FeedbackRecordingService,
        private customPriceStorageService: SagCustomPricingStorageService,
        private appModal: AppModalService,
        private legalTermService: LegalTermService,
        private legalTermModalService: LegalTermModalService
    ) { }

    get employeeInfo$() {
        return this.employeeInfoSub$.asObservable();
    }

    get userDetail$() {
        return this.userDetailSub$.asObservable();
    }

    get userSetting$() {
        return this.userSettingSub$.asObservable();
    }

    get userPrice$() {
        return this.userPriceSub$.asObservable();
    }

    get userPaymentSetting$() {
        return this.userPaymentSettingSub$.asObservable();
    }

    initUser(isLogin = false) {
        const appToken = this.appStorage.appToken;
        this.appStorage.appToken = '';
        if (!!appToken && !this.authService.isTokenExpired(appToken)) {
            this.resetSaleData();
            return this.getUserDetail(appToken).pipe(
                switchMap((userDetail: UserDetail) => {
                    if (userDetail.legalAccepted || !isLogin) {
                        this.appStorage.appToken = appToken;
                        return of(userDetail);
                    }
                    if (userDetail.userAdminRole) {
                        return this.legalTermService.getDocuments(appToken).pipe(
                            switchMap((documents) => {
                                return this.legalTermModalService.showLegalTermInfo(documents, appToken);
                            }),
                            switchMap(result => {
                                if (result) {
                                    this.appStorage.appToken = appToken;
                                    return of(userDetail);
                                }
                                throw new Error(LEGAL_TERM_NOT_ACCEPTED);
                            })
                        )
                    } else {
                        return this.legalTermService.hasExpiredTerms(appToken).pipe(
                            switchMap((result: any) => {
                                if (result && !result.hasExpiredTerms) {
                                    return this.legalTermService.getUnacceptedTerm(appToken);
                                }
                                
                                throw new Error(LEGAL_TERM_NOT_ACCEPTED);
                            }),
                            switchMap((terms) => {
                                return from(this.showLegalTermMessageBox(terms)).pipe(
                                    switchMap(() => {
                                        this.appStorage.appToken = appToken;
                                        return of(userDetail);
                                    })
                                )
                            })
                        )
                    }
                }),
                tap((userDetail: UserDetail) => {
                    this.userDetailSub$.next(this.userDetail);
                    // Save sale username for compare with adal user info in logout function
                    if (userDetail.salesUser) {
                        this.appStorage.saleUserName = userDetail.username;
                    }
                    this.bindUserPriceFromUserDetail(userDetail);
                    this.setCurrentLanguage(userDetail.language);
                    this.themeService.updateTheme(userDetail);
                    this.themeService.updatePrimaryBackground(userDetail);
                    if (this.isAffiliateApplyToGetCustomerCurrency) {
                        if (userDetail.customer) {
                            const currencyFromCustomer = userDetail.customer.currency;
                            this.appStorage.customerCurrency = currencyFromCustomer;
                        }
                    } else {
                        const customerCurrency = this.appStorage.customerCurrency || '';
                        if (customerCurrency !== '') {
                            this.appStorage.clearCustomerCurrency();
                        }
                    }

                    this.shoppingBasketService.initBasket();
                    this.creditLimitService.initCreditCard({ isUserOnBehalf: userDetail.isSalesOnBeHalf });
                    let articleMode;
                    if (userDetail.salesUser) {
                        this.shoppingBasketService.clearBasket();
                    } else {
                        if (userDetail.isSalesOnBeHalf || this.isSb) {
                            this.creditLimitService.resetCreditCardInfo(userDetail);
                        }
                        this.shoppingBasketService.loadCartQuantity();
                        if (!this.appStorage.isExistedCustomer(userDetail.custNr)) {
                            this.appContextService.refreshNextWorkingDateCache().subscribe(() => { });
                        }
                    }
                    this.initArticleMode(userDetail);

                    this.shoppingBasketHistoryService.loadBasketHistoryQuantity();
                    // update price setting
                    // get employee info
                    this.getEmployeeInfo(userDetail);
                    setTimeout(() => {
                        this.gaService.setUserData(this.userDetail);
                        this.optimizelyService.setUserData(this.userDetail);
                        if (!isLogin) {
                            this.gaService.login(this.userDetail);
                        }
                    });
                }),
                switchMap((userDetail: UserDetail) => {
                    const obs: Observable<any>[] = [this.getSetting()];
                    if(!userDetail.isFinalUserRole && !AffiliateUtil.isSb(environment.affiliate)) {
                        obs.push(this.getPaymentSetting());
                    }

                    if (userDetail.isSalesOnBeHalf && this.appStorage.isExistedCustomer(userDetail.custNr)
                        || (this.isGetAppContext() && AffiliateUtil.isSb(environment.affiliate))) {
                        obs.push(this.appContextService.getAppContext()
                            .pipe(tap((res: any) => this.updateNetPriceSetting(res && res.userPriceContext || {}))));
                    } else {
                        obs.push(this.appContextService.initAppContext()
                            .pipe(tap((res: any) => this.updateNetPriceSetting(res && res.userPriceContext || {}))));
                    }

                    if (userDetail && userDetail.userAdminRole && this.isWbb) {
                        obs.push(this.getWssUserSetting()
                            .pipe(tap((customerSetttings: CustomerSetting) => {
                                if (customerSetttings) {
                                    this.toggleWssShowNetPrice(customerSetttings.wssShowNetPrice);
                                };
                            })
                            )
                        )
                    };

                    return forkJoin(obs).pipe(map(res => {
                        return true;
                    }));
                }),
                catchError(err => {
                    if (err.message === LEGAL_TERM_NOT_ACCEPTED) {
                        return throwError({ error: { error: err.message } });
                    }
                    return of(null);
                })
            );
        } else {
            return of(null);
        }
    }

    private isGetAppContext() {
        const activatedRoute = this.injector.get(ActivatedRoute);
        const returnUrl = activatedRoute.snapshot.queryParams['returnUrl'];

        if (returnUrl) {
            return ['/shopping-basket/order'].indexOf(returnUrl) > -1;
        }

        return StringUtil.isIncludesOf(window.location.pathname, ['/shopping-basket/order']);
    }

    authorize(data: AuthModel, isLogin = false): Observable<any> {
        data.locatedAffiliate = environment.affiliate;
        return this.authService.signIn(data).pipe(switchMap((body: any) => {
            if (!!body) {
                this.appStorage.appToken = body.access_token;
                this.customPriceStorageService.isCustomPriceShown = true;
                return this.initUser(isLogin);
            }
            return of(throwError(401));
        }));
    }

    authorizeCustomer(data: CustomerModel, adminUser?: string) {
        if (data.token) {
            this.appStorage.appToken = data.token;
            return this.initUser();
        } else {
            const currentUser = this.userDetailSub$.getValue() as UserDetail;
            if (currentUser.salesUser) {
                // create onBehalfToken
                this.appStorage.saleToken = this.appStorage.appToken;
            }
            const affiliate = data.affiliateShortName || environment.affiliate;
            const body = new AuthModel({
                affiliate,
                userName: adminUser,
                password: '',
                onBehalf: this.appStorage.saleToken
            });
            return this.authorize(body);
        }
    }

    throwTimeoutError() {
        return throwError(new Error(Constant.API_TIMEOUT_EXCEEDED_ERROR));
    }

    sendCode(affiliate: any, username: any, langCode: any, redirectUrl: string) {
        const url = `${this.baseUrl}user/send-code`;
        const body = { username, affiliateId: affiliate, langCode, redirectUrl };
        return this.http.post(url, body, { responseType: 'text' }).pipe(
            timeoutWith(apiCallTimeout, this.throwTimeoutError()),
            catchError(error => this.handleError(error)),
            finalize(() => SpinnerService.stop())
        );
    }

    checkCode(code: any, hash: string) {
        const url = `${this.baseUrl}user/forgot-password/code`;
        const body = { token: code, hashUsernameCode: hash };
        return this.http.post(url, body, { responseType: 'text' });
    }

    resetPassword(affiliate: string, langCode: string, newPassword: string, code: string, hash: string) {
        const url = `${this.baseUrl}user/forgot-password/reset-password`;
        const body = {
            affiliate,
            langCode,
            password: newPassword,
            token: code,
            hashUsernameCode: hash,
            isFinalUser: AffiliateUtil.isFinalCustomerAffiliate(affiliate)
        };
        return this.http.post(url, body, { responseType: 'text' });
    }

    backToSale() {
        this.appStorage.appToken = this.appStorage.saleToken;
        this.appStorage.saleToken = null;
        return this.initUser();
    }

    getUserDetail(token) {
        const url = `${this.baseUrl}user/detail`;
        const options = token ? { headers: { token } } : undefined;
        return this.http.get(url, options).pipe(map(body => {
            this.userDetail = new UserDetail(body);
            this.userDetail.isSalesOnBeHalf = !!this.appStorage.saleToken;
            const custNr = this.userDetail.custNr && this.userDetail.custNr.toString();
            this.userDetail.isShopCustomer = this.appStorage.isShipCustomer(custNr);
            return this.userDetail;
        }));
    }



    getSetting() {
        const url = `${this.baseUrl}user/settings`;
        const body = { salesOnbehalfToken: '' };
        return this.http.post(url, body).pipe(map(res => {
            this.updateSettings(res);
        }));
    }

    updateSettings(res) {
        const settings: any = res;
        this.appStorage.classicViewMode = settings.classicCategoryView;
        const userSetting = new UserSetting(settings);
        const userId = this.userDetail && this.userDetail.id;
        const currentStateSingleSelectMode = this.appStorage.getCurrentStateSingleSelectMode(userId);
        if (currentStateSingleSelectMode !== undefined && currentStateSingleSelectMode !== null) {
            userSetting.currentStateSingleSelectMode = currentStateSingleSelectMode;
        } else {
            userSetting.currentStateSingleSelectMode = userSetting.singleSelectMode;
        }
        this.appStorage.setCurrentStateSingleSelectMode(userId, userSetting.currentStateSingleSelectMode);
        this.userSetting = userSetting;
        this.userSettingSub$.next(userSetting);
    }

    getPaymentSetting() {
        const url = `${this.baseUrl}user/payment`;
        const body = { salesOnbehalfToken: this.appStorage.saleToken };
        return this.http.post(url, body).pipe(map(paymentSetting => {
            this.userPaymentSetting = new PaymentSetting(paymentSetting);
            this.userPaymentSettingSub$.next(this.userPaymentSetting);
            return this.userPaymentSetting;
        }));
    }

    updateNetPriceSetting(setting) {
        if (!setting) {
            return;
        }
        if (this.userDetail.isSalesOnBeHalf) {
            // user is on behalf by sales.
            // sale could not change this setting
            this.userPrice = Object.assign({}, (this.userPrice || {}), (setting || {}));
            this.userPriceSub$.next(this.userPrice);
        } else {
            // If normal customer update price config, only allow to change
            // variable below to effect the Article list
            if (!isEmpty(setting)) {
                this.userPrice.netPriceView = setting.netPriceView;
                this.userPrice.currentStateNetPriceView = setting.netPriceView;
                this.userPrice.showNetPriceEnabled = setting.netPriceView;
                setting.currentStateNetPriceView = this.userPrice.currentStateNetPriceView;
                this.updateCanViewNetPrice(setting);
                this.userPriceSub$.next(this.userPrice);
            }
        }
    }

    toggleNetPriceView() {
        if (!this.userPrice.netPriceView) {
            return;
        }

        if (this.userDetail.isSalesOnBeHalf) {
            this.userPrice.currentStateNetPriceView = !this.userPrice.currentStateNetPriceView;
            this.userPrice = Object.assign({}, this.userPrice);
            this.userPriceSub$.next(this.userPrice);
            return;
        }

        const url = `${this.baseUrl}user/view-net-price/toggle`;
        this.http.post(url, null, { observe: 'body' }).subscribe(res => {
            this.userPrice.currentStateNetPriceView = !this.userPrice.currentStateNetPriceView;
            this.userPrice = Object.assign({}, (this.userPrice || new LibUserSetting()));
            this.userPrice.fcUserCanViewNetPrice = this.userPrice.currentStateNetPriceView && this.userPrice.wholeSalerHasNetPrice && this.userPrice.finalCustomerHasNetPrice;
            this.userPriceSub$.next(this.userPrice);
        });
    }

    toggleSingleSelectMode() {
        const userId = this.userDetail && this.userDetail.id;
        const currentStateSingleSelectMode = this.appStorage.getCurrentStateSingleSelectMode(userId);
        this.appStorage.setCurrentStateSingleSelectMode(userId, !currentStateSingleSelectMode);
        this.userSetting.currentStateSingleSelectMode = !currentStateSingleSelectMode;
        this.userSettingSub$.next(this.userSetting);
    }

    updatePriceSetting(setting) {
        if (!setting) {
            return;
        }

        this.userPrice = Object.assign({}, (this.userPrice || {}), (setting || {}));
        this.userPriceSub$.next(this.userPrice);
    }

    createAxUser(body) {
        const headers = {
            'Content-Type': 'application/json'
        };
        return this.http.post(`${this.baseUrl}external/users/v2/create`, body, { headers });
    }

    private bindUserPriceFromUserDetail(userDetail: UserDetail) {
        this.userPrice = new LibUserSetting();
        const userSettings = userDetail.settings;
        if (this.appStorage.saleToken) {
            this.userPrice.netPriceView = true;
            this.userPrice.currentStateNetPriceView = true;
        } else {
            this.userPrice.netPriceView = userSettings.netPriceView;
            this.userPrice.currentStateNetPriceView = userSettings.currentStateNetPriceView;
        }
        this.userPrice.priceDisplayChanged = userSettings.priceDisplayChanged;
        this.userPrice.vatConfirm = userDetail.vatConfirm;
        this.userPrice.showDiscount = userSettings.showDiscount;
        this.userPrice.showTyresDiscount = userSettings.showTyresDiscount;
        this.userPrice.currentStateVatConfirm = false; // PO: inital state of VAT is exclude
        this.userPrice.showTyresGrossPriceHeader = userSettings.showTyresGrossPriceHeader;
        this.userPrice.hasAvailabilityPermission = userDetail.hasAvailabilityPermission;
        this.userPrice.showGross = userSettings.showGross;
        this.userPrice.brandPriorityAvailFilter = userSettings.brandPriorityAvailFilter;
        // avail settings

        this.userPrice.availDisplaySettings = (userSettings.availDisplaySettings || []).map(item => new SagAvailDisplaySettingModel(item));
        this.adaptOldAvailSetting();
        this.userPrice.wholeSalerHasNetPrice = userDetail.wholeSalerHasNetPrice;
        this.userPrice.finalCustomerHasNetPrice = userDetail.finalCustomerHasNetPrice;
        this.userPrice.showNetPriceEnabled = userDetail.showNetPriceEnabled;
        this.userPrice.externalUrls = userSettings.externalUrls;
        this.userPrice.mouseOverFlyoutDelay = userSettings.mouseOverFlyoutDelay;

        this.userPrice.vatTypeDisplay = userSettings.vatTypeDisplay;
        this.userPrice.vatTypeDisplayConvert = VatTypeDisplayUtil.convertResponseData(this.userPrice.vatTypeDisplay);

        this.updateCanViewNetPrice(userSettings);

        this.userPrice.customerBrandFilterEnabled = userSettings.customerBrandFilterEnabled;
        this.userPrice.salesBrandFilterEnabled = userSettings.salesBrandFilterEnabled;
        this.userPrice.disabledBrandPriorityAvailability = userSettings.disabledBrandPriorityAvailability;
        this.userPrice.isSalesOnBeHalf = userDetail.isSalesOnBeHalf;
        this.userPrice.externalPartSettings = userSettings.externalPartSettings;
        this.userPriceSub$.next(this.userPrice);
        this.appStorage.userPrice = this.userPrice;
        this.appStorage.currentStateVatConfirm = this.userPrice.vatTypeDisplayConvert && this.userPrice.vatTypeDisplayConvert.list;
        this.gaService.availDisplaySettings = this.userPrice.availDisplaySettings;
    }

    adaptOldAvailSetting() {
        const availSetting = AvailabilityUtil.getAvailSettingByAvailState(this.userPrice.availDisplaySettings, SAG_AVAIL_DISPLAY_STATES.NOT_AVAILABLE);
        if(availSetting) {
            this.userPrice.availIcon = availSetting.availIcon || availSetting.displayOption === SAG_AVAIL_DISPLAY_OPTIONS.DOT;
            this.userPrice.detailAvailText = availSetting.detailAvailText;
            this.userPrice.dropShipmentAvailability = availSetting.displayOption === SAG_AVAIL_DISPLAY_OPTIONS.DROP_SHIPMENT;
            this.userPrice.listAvailText = availSetting.listAvailText;
        }
    }

    updateCanViewNetPrice(userSettings) {
        this.userPrice.canViewNetPrice = userSettings.netPriceView;
        if (AffiliateUtil.isFinalCustomerAffiliate(environment.affiliate)) {
            this.userPrice.canViewNetPrice = userSettings.netPriceView && this.userDetail.wholeSalerHasNetPrice && this.userDetail.finalCustomerHasNetPrice;
            this.userPrice.fcUserCanViewNetPrice = userSettings.currentStateNetPriceView && this.userDetail.wholeSalerHasNetPrice && this.userDetail.finalCustomerHasNetPrice;
        }
        if (this.isCzBased) {
            if (this.userDetail && this.userDetail.isSalesOnBeHalf) {
                this.userPrice.canViewNetPrice = true;
            }
        }
    }

    setCurrentLanguage(appLangCode: string) {
        const defaultLangCode = CountryUtil.getDefaultLangCode(environment.affiliate);
        const supportLangCode = CountryUtil.getSupportedLangCodes(environment.affiliate);
        let currentLangCode = defaultLangCode;
        if (appLangCode) {
            if (supportLangCode.indexOf(appLangCode) !== -1) {
                currentLangCode = appLangCode;
            }
        } else {
            const browserLangCode = this.translateService.getBrowserLang();
            if (supportLangCode.indexOf(browserLangCode) !== -1) {
                currentLangCode = browserLangCode;
            }
        }
        this.appStorage.appLangCode = currentLangCode;
        this.translateService.use(currentLangCode);
    }

    getEmployeeInfo(user: UserDetail) {
        const affiliate = environment.affiliate;
        let observe: Observable<any>;
        if (user.roleName === ESHOP_ROLES.SALES_ASSISTANT.toString()) {
            const url = `${this.baseUrl}user/employee?affiliate=${affiliate}&emailAddress=${user.email}`;
            observe = this.http.get(url).pipe(
                catchError(err => {
                    // console.error('error when getting employee info: ', err);
                    return of({});
                }),
                map(res => {
                    this.appStorage.saleInfo = res;
                    return res;
                })
            );
        } else {
            observe = of(this.appStorage.saleInfo);
        }
        observe.subscribe((data: any) => {
            this.employeeInfo = data.employee;
            this.employeeInfoSub$.next(this.employeeInfo);
        });
    }

    checkRegistrationInfo(values) {
        const url = `${this.baseUrl}customers/info`;
        return this.http.post(url, values);
    }

    register(values) {
        const url = `${this.baseUrl}customers/register`;
        return this.http.post(url, values);
    }

    handleError(error: any) {
        let info;
        if (typeof error.json === 'function') {
            info = error.json();
            // Checking invalid version
            if (info.error_code === Constant.INVALID_VERSION_ERROR_CODE) {
                // this.hasInvalidReleaseVersion.next(true);
                return throwError(error);
            } else {
                // this.hasInvalidReleaseVersion.next(false);
            }
            // Checking invalid token
            if (info.error === Constant.INVALID_TOKEN_ERROR_CODE) {
                // this.hasInvalidToken.next(true);
                return throwError(error);
            } else {
                // this.hasInvalidToken.next(false);
            }
        } else if (error.message === Constant.API_TIMEOUT_EXCEEDED_ERROR) {
            info = error;
        } else {
            info = Constant.API_GENERAL_ERROR_MESSAGE;
        }
        return throwError(info);
    }

    hasPermissions(permissionName: string[]): Observable<boolean> {
        return this.userDetail$.pipe(
            map(user => {
                if (!!user && !!user.permissions) {
                    const permissions: any[] = user.permissions;
                    const isAllowed = permissions
                        .filter(p => !!p.functions && p.functions.length > 0)
                        .map(p => p.functions)
                        .find(f => {
                            const functionNames = f.map(fi => fi.functionName);
                            return intersection(permissionName, functionNames).length > 0;
                        });
                    return isAllowed;
                }
                return false;
            })
        );
    }

    logout(done?) {
        const spinner = SpinnerService.startApp();
        const isSalesOnBeHalf = this.userDetail && this.userDetail.isSalesOnBeHalf;
        let currentUsername = this.userDetail && this.userDetail.username;

        if (isSalesOnBeHalf) {
            currentUsername = this.appStorage.saleUserName;
        }

        let updateVehCtx = of(null);
        if (window.location.pathname.indexOf('/vehicle/') !== -1) {
            updateVehCtx = this.appContextService.updateVehicleContext().pipe(catchError(() => of(null)));
        }

        this.themeService.updatePrimaryBackground();

        updateVehCtx
            .subscribe(() => {
                this.authService.signOut().pipe(catchError(err => {
                    return of(null);
                })).subscribe(data => {
                    this.appStorage.removeAll();
                    this.resetUserData();
                    this.themeService.updateNormalView();
                    this.fbRecordingService.resetModel();
                    this.appModal.closeAll();
                    if (this.isAXUserLogin(currentUsername)) {
                        this.adal8Service.logOut();
                    }
                    if (done) {
                        done();
                    } else {
                        const router = this.injector.get(Router);
                        router.navigate([Constant.LOGIN_PAGE]).then(() => {
                            SpinnerService.stop(spinner);
                        });
                    }
                });
            });
    }

    private resetSaleData() {
        this.userSetting = null;
        this.userSettingSub$.next(this.userSetting);
        this.userPrice = new LibUserSetting();
        this.userPriceSub$.next(this.userPrice);
        this.userPaymentSetting = null;
        this.userPaymentSettingSub$.next(this.userPaymentSetting);
    }

    private resetUserData() {
        this.userDetail = null;
        this.userDetailSub$.next(this.userDetail);
        this.userSetting = null;
        this.userSettingSub$.next(this.userSetting);
        this.userPrice = new LibUserSetting();
        this.userPriceSub$.next(this.userPrice);
        this.userPaymentSetting = null;
        this.userPaymentSettingSub$.next(this.userPaymentSetting);
        this.employeeInfo = null;
        this.employeeInfoSub$.next(this.employeeInfo);
        this.shoppingBasketService.initBasket();
    }

    private isAXUserLogin(currentUsername): boolean {
        const axProfile = this.adal8Service.userInfo || null;

        if (!!axProfile && currentUsername) {
            const axUsername = this.adal8Service.userInfo.userName;
            return (axUsername === currentUsername);
        }

        return false;
    }

    updateShowFCNetPrice(isShow) {
        const url = `${this.baseUrl}${UPDATE_SHOW_NET_PRICE}?wssShowNetPrice=${isShow}`;
        return this.http.post(url, {}).pipe(tap(res => {
            this.toggleWssShowNetPrice(res);
        }))
    }

    getWssUserSetting() {
        const url = `${this.baseUrl}${GET_CUSTOMER_SETTING_URL}`;
        return this.http.get(url).pipe(map((customerSettings) => new CustomerSetting(customerSettings)));
    }

    toggleWssShowNetPrice(isEnable) {
        this.userDetail.wholeSalerHasNetPrice = isEnable;
        this.userDetailSub$.next(this.userDetail);
    }

    private initArticleMode(userDetail: UserDetail) {
        const defaultMode = get(environment, 'defaultArticleMode') || ARTICLE_MODE.SIMPLE_MODE;
        const salesMode = get(environment, 'salesArticleMode') || ARTICLE_MODE.SIMPLE_MODE;
        const customerMode = get(environment, 'customerArticleMode') || ARTICLE_MODE.EXTENDED_MODE;

        let articleMode = defaultMode;
        if (userDetail.salesUser || userDetail.isSalesOnBeHalf) {
            articleMode = salesMode;
        } else {
            articleMode = customerMode;
        }

        if (this.isAffiliateApplyArticleExtendedMode) {
            articleMode = ARTICLE_MODE.EXTENDED_MODE;
        }
        if (!this.appStorage.articleMode) {
            this.appStorage.articleMode = articleMode;
        }
    }

    hasPermission(perName: string) {
        return this.userDetail$.pipe(
            map(user => {
                if (!!user && !!user.permissions) {
                    const permissions: any[] = user.permissions;
                    return !!permissions.map(p => p.permission).find(code => code === perName);
                }
                return false;
            })
        );
    }

    private showLegalTermMessageBox(terms: LegalDocument[]) {
        return new Promise(resolve => {
            if (!terms || terms.length === 0) {
                resolve(true);
            }
            this.appModal.modals = this.injector.get(BsModalService).show(SagConfirmationBoxComponent, {
                ignoreBackdropClick: true,
                initialState: {
                    title: '',
                    message: 'LEGAL_TERM.MESSAGE.ACCEPT_DOCUMENT',
                    messageParams: { date: terms[0].dateValidTo },
                    okButton: 'COMMON_LABEL.BUTTON.CONTINUE',
                    showCancelButton: false,
                    close: () => {
                        resolve(true);
                    }
                }
            });
        })
    }
}
