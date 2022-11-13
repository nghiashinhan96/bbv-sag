import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';

import { Subject } from 'rxjs/internal/Subject';
import { finalize, first } from 'rxjs/operators';
import { Adal8Service } from 'adal-angular8';

import { AuthModel } from 'sag-auth';
import { AffiliateUtil } from 'sag-common';

import { UserService } from 'src/app/core/services/user.service';
import { environment } from 'src/environments/environment';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { DmsUtil } from 'src/app/dms/utils/dms.util';
import { DmsInfo } from 'src/app/dms/models/dms-info.model';
import { LoginModeEnum } from '../../enums/login-mode.enum';
import { Constant, LEGAL_TERM_NOT_ACCEPTED } from 'src/app/core/conts/app.constant';
import { OciInfo } from 'src/app/oci/models/oci-info.model';
import { OciService } from 'src/app/oci/services/oci.service';
import { LoginSourceType } from 'src/app/analytic-logging/enums/login-source-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { ActiveDmsProcessor } from 'src/app/dms/context/active-dms-processor';
import { SagCustomPricingStorageService } from 'sag-custom-pricing';
import { SUPPORTED_LANG_CODES, SUPPORTED_LANG_CODES_CZ } from 'src/app/core/conts/app-lang-code.constant';
import { CountryUtil } from 'src/app/core/utils/country.util';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { GASignUpModel } from 'src/app/analytic-logging/models/ga.model';
import { GA_SIGN_UP_STEP } from 'src/app/analytic-logging/enums/ga.model.enums';

@Component({
    selector: 'connect-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    userName: string;
    password: string;
    errorMessage;
    inputUsername: string;
    inputEmail: string;
    inputCode: string;
    browserLanguage: string;
    showValue = false;
    redirectUrl: string;
    destroyed$ = new Subject<boolean>();
    codeFormShown = false;
    resetPasswordFormShown = false;
    passForgotFormShown = true;
    selectedTabName: string;
    isShownAddtionalPage = false;

    loginData: AuthModel;

    affiliate = environment.affiliate;
    isRegistrationShowed = !AffiliateUtil.isEhCh(environment.affiliate);
    ssoWhitelist = environment.sso_whitelist;

    supportedLangCodes: any;

    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);

    constructor(
        private userService: UserService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private dmsService: ActiveDmsProcessor,
        private appStorage: AppStorageService,
        private ociService: OciService,
        private analyticService: AnalyticLoggingService,
        private gaService: GoogleAnalyticsService,
        private adal8Service: Adal8Service,
        private customPriceStorageService: SagCustomPricingStorageService
    ) { }

    ngOnInit() {
        this.checkForLoginParams();
        this.supportedLangCodes = CountryUtil.getSupportedLangCodes(environment.affiliate);
    }

    async checkForLoginParams() {
        const dmsInfo = this.dmsService.getDmsInfo();
        let isCheckForRedirect = true;
        if (dmsInfo) {
            isCheckForRedirect = !(await this.checkDmsAutoLogin(dmsInfo));
        }

        const ociInfo = this.ociService.getOciInfo();
        if (ociInfo) {
            isCheckForRedirect = !this.checkOciAutoLogin(ociInfo);
        }

        const axProfile = this.adal8Service.userInfo || null;
        if (!!axProfile && axProfile.authenticated) {
            this.handleSsoLogin(axProfile);
        }

        if (isCheckForRedirect) {
            this.redirectIfUserAuthed();
        }
    }

    redirectIfUserAuthed() {
        const params: Params = this.activatedRoute.snapshot.queryParams;
        const redirectUrl = params.redirect || Constant.HOME_PAGE;
        if (this.appStorage.appToken) {
            this.router.navigateByUrl(redirectUrl);
        }
    }

    login(body) {
        const data = new AuthModel(body);
        const spinner = SpinnerService.startApp();
        this.appStorage.removeAll();
        this.userService.authorize(data, true).subscribe(res => {
            this.sendEvent();
            const { returnUrl } = this.activatedRoute.snapshot.queryParams || { returnUrl: '' };
            if (returnUrl) {
                this.router.navigateByUrl(returnUrl).then(() => {
                    SpinnerService.stop(spinner);
                });
            } else {
                this.router.navigateByUrl('/').then(() => {
                    SpinnerService.stop(spinner);
                });
            }
        }, ({ error }) => {
            SpinnerService.stop(spinner);
            this.handleLoginErr(error || {});
        });
    }

    forgorPassword() {
        this.router.navigateByUrl('/forgotpassword');
    }

    handleLoginErr({ error, message }) {
        let displayedMessage = 'LOGIN.ERROR_MESSAGE.INVALID_CREDENTIALS';
        if (message === 'timeout') {
            displayedMessage = 'LOGIN.ERROR_MESSAGE.SYSTEM_ERROR';
        }
        if (error === 'access_denied') {
            displayedMessage = 'LOGIN.ERROR_MESSAGE.USER_IS_NOT_ACTIVED';
        }
        if (error === 'blocked_customer') {
            displayedMessage = 'LOGIN.ERROR_MESSAGE.BLOCKED_CUSTOMER';
        }
        if (error === 'wholesaler_permission_access_denied') {
            displayedMessage = 'LOGIN.ERROR_MESSAGE.WHOLESALER_PERMISSION_ACCESS_DENIED';
        }
        if (error === LEGAL_TERM_NOT_ACCEPTED) {
            displayedMessage = 'LEGAL_TERM.ERROR_MESSAGE.NOT_ACCEPTED';
        }
        this.errorMessage = { message: displayedMessage, type: 'ERROR' };
    }

    selectedTab(tabName: string) {
        if (this.selectedTabName !== tabName) {
            this.selectedTabName = tabName;
            this.isShownAddtionalPage = false;
        }
    }

    ssoLogin() {
        this.adal8Service.login();
    }

    handleRegister(key) {
        const data = {
            method: 'email'
        } as GASignUpModel;
        if (key === 'potential-customer-register') {
            data.step = GA_SIGN_UP_STEP.LEAD;
        } else {
            data.step = GA_SIGN_UP_STEP.COMPLETE;
        }
        this.gaService.signUp(data);
    }

    private handleSsoLogin(adalProfile: any) {
        const user = {
            salutationId: 1,
            userName: adalProfile.userName,
            surName: adalProfile.profile.given_name,
            firstName: adalProfile.profile.family_name,
            email: adalProfile.profile.unique_name, // should be an email
            phoneNumber: '123456789', // default, can be modified by Admin
            languageId: this.isAxCz ? 3 : 1, // default for temp, can be modified by Admin
            hourlyRate: 11, // default for temp, can be modified by Admin
            uuid: adalProfile.profile.oid,
            groupUuids: adalProfile.profile.groups
        };
        SpinnerService.start();
        this.userService.createAxUser(user)
            .pipe(
                finalize(() => {
                    SpinnerService.stop();
                })
            )
            .subscribe(
                (data) => {
                    const userProfile = new AuthModel({
                        userName: adalProfile.userName,
                        password: '',
                        language: null,
                        loginMode: LoginModeEnum.SSO,
                        affiliate: environment.affiliate
                    });

                    this.loginData = userProfile;
                    this.appStorage.removeAll();

                    SpinnerService.start();
                    this.userService.authorize(userProfile)
                        .pipe(
                            finalize(() => {
                                SpinnerService.stop();
                            })
                        )
                        .subscribe(res => {
                            this.sendEvent();
                            this.router.navigate([Constant.HOME_PAGE]);

                        }, (err) => {
                            this.handleLoginErr(err);
                        });
                }
            );
    }

    // check if we need to process Dms login
    private async checkDmsAutoLogin(dmsInfo: DmsInfo): Promise<boolean> {

        if (DmsUtil.isValidCloudDms(dmsInfo.token, dmsInfo.hookUrl)) {
            this.appStorage.removeAll();
            dmsInfo.userPass = null; // password should not be saved
            this.dmsService.updateDmsInfo(dmsInfo);

            let user: any;
            this.appStorage.appToken = dmsInfo.token;
            this.customPriceStorageService.isCustomPriceShown = true;
            user = await this.userService.initUser().toPromise();
            if (user) {
                this.sendEvent();
            }

            const { hasDmsPermission } = this.userService.userDetail;
            this.dmsService.processSearch(dmsInfo, hasDmsPermission);
            return Promise.resolve(true);
        } else {
            this.userName = dmsInfo.username;
            this.password = dmsInfo.userPass;
            if (this.userName && this.password) {
                this.appStorage.removeAll();
                const spinner = SpinnerService.startApp();
                dmsInfo.userPass = null; // password should not be saved
                this.dmsService.updateDmsInfo(dmsInfo);
                const data = new AuthModel({
                    userName: this.userName,
                    password: this.password,
                    loginMode: LoginModeEnum.DMS,
                    affiliate: environment.affiliate
                });
                this.loginData = data;
                this.userService.authorize(data)
                    .pipe(finalize(() => SpinnerService.stop(spinner)))
                    .subscribe(
                        res => {
                            this.sendEvent();
                            this.dmsService.processSearch(dmsInfo, true);
                        },
                        ({error}) => {
                            this.handleLoginErr(error || {});
                        });
                return Promise.resolve(true);
            }
        }
        return Promise.resolve(false);
    }
    // Check if we need to process Oci login
    private checkOciAutoLogin(ociInfo: OciInfo): boolean {

        this.userName = ociInfo.userName;
        this.password = ociInfo.password;
        if (this.userName && this.password) {
            this.appStorage.removeAll();
            const spinner = SpinnerService.startApp();
            const data = new AuthModel({
                userName: this.userName,
                password: this.password,
                language: ociInfo.language,
                loginMode: LoginModeEnum.OCI,
                affiliate: environment.affiliate
            });

            ociInfo.password = null;
            this.ociService.setOciInfo(ociInfo);

            this.loginData = data;
            this.userService.authorize(data)
                .pipe(finalize(() => SpinnerService.stop(spinner)))
                .subscribe(
                    res => {
                        this.sendEvent();
                        this.router.navigate([Constant.HOME_PAGE]);
                    },
                    ({error}) => {
                        this.handleLoginErr(error || {});
                    });
            return true;
        }
        return false;
    }

    private sendEvent() {
        setTimeout(() => {
            const dmsInfo = this.dmsService.getDmsInfo();
            const ociInfo = this.ociService.getOciInfo();
            let loginSourceType = LoginSourceType.SHOP;
            let loginSourceProvider = '';

            if (dmsInfo) {
                loginSourceType = DmsUtil.isValidCloudDms(dmsInfo.token, dmsInfo.hookUrl)
                    ? LoginSourceType.REST : LoginSourceType.MET;
                loginSourceProvider = dmsInfo.origin;
            } else if (ociInfo && ociInfo.isOciFlow) {
                loginSourceType = LoginSourceType.OCI;
            }

            const request = this.analyticService.createLoginLogoutEventData(
                {
                    loginLogout: true,
                    loginSourceType,
                    loginSourceProvider
                }
            );
            this.analyticService
                .postEventFulltextSearch(request, AnalyticEventType.LOGIN_LOGOUT_EVENT)
                .pipe(first())
                .toPromise();

            const user = this.userService.userDetail;
            this.gaService.login(user);
        });
    }
}
