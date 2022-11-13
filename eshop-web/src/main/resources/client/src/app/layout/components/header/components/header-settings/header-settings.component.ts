import { Component, OnInit, HostListener, ViewChild } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { map, catchError, first } from 'rxjs/operators';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { Observable, of, Subscription } from 'rxjs';
import { BsModalService } from 'ngx-bootstrap/modal';
import { PriceFileComponent } from '../../dialogs/price-file/price-file.component';
import { environment } from 'src/environments/environment';
import { CustomerUtil } from 'src/app/core/utils/customer.util';
import { IncentivePoints } from '../../models/incentive-points.model';
import { HeaderSettingsService } from '../../services/header-settings.service';
import { HappyPointDialogComponent } from '../../dialogs/happy-point-dialog/happy-point-dialog.component';
import { AffiliateEnum, AffiliateUtil } from 'sag-common';
import { EnvUtil } from 'src/app/core/utils/env.util';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { FeedbackModalComponent } from 'src/app/feedback/components/feedback-modal/feedback-modal.component';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { SpinnerService } from 'src/app/core/utils/spinner';


const LANG_CODE_FR = 'fr';
const HAPPY_BONUS = 'HAPPY_BONUS';
const BIG_POINTS = 'BIG_POINTS';
const MILES = 'MILES';

@Component({
    selector: 'connect-header-settings',
    templateUrl: './header-settings.component.html',
    styleUrls: ['./header-settings.component.scss']
})
export class HeaderSettingsComponent implements OnInit {

    @ViewChild('pop', { static: false }) popover: any;

    loggedUser$: Observable<UserDetail>;
    isWBB = AffiliateUtil.isWbb(environment.affiliate);
    externalLinks = [];
    incentiveLinkInfo: IncentivePoints;

    HAPPY_BONUS = 'HAPPY_BONUS';
    BIG_POINTS = 'BIG_POINTS';
    MILES = 'MILES';
    AT_HAPPY_POINTS = 'AT_HAPPY_POINTS';
    CH_HAPPY_POINTS = 'CH_HAPPY_POINTS';
    isSaleLogged = false;
    cz = AffiliateEnum.CZ;
    sb = AffiliateEnum.SB;
    ehcz = AffiliateEnum.EH_CZ;
    ehaxcz = AffiliateEnum.EH_AX_CZ;

    private enableIncentive = environment.incentive.enable || false;

    private sub: Subscription;

    constructor(
        public userService: UserService,
        private modalService: BsModalService,
        private headerSettingsService: HeaderSettingsService,
        private analyticService: AnalyticLoggingService,
        private appModals: AppModalService
    ) { }

    @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent) {
        // tslint:disable-next-line: deprecation
        const key = event.keyCode || event.which;
        if (key === 27) {
            this.popover.hide();
        }
    }

    ngOnInit() {
        this.loggedUser$ = this.userService.userDetail$.pipe(
            map((user: UserDetail) => {
                if (user && user.id && user.custNr) {
                    const isMatikChCustomer = CustomerUtil.isMatikChCustomer(user.affiliateShortName);
                    const isTechnoChCustomer = CustomerUtil.isTechnoChCustomer(user.affiliateShortName);
                    const isDerendChCustomer = CustomerUtil.isDerendChCustomer(user.affiliateShortName);
                    const isDerendAtCustomer = CustomerUtil.isDerendAtCustomer(user.affiliateShortName);
                    const isMatickAtCustomer = CustomerUtil.isMatikAtCustomer(user.affiliateShortName);
                    const affliateIsUsedIncentive = isMatikChCustomer || isTechnoChCustomer || isDerendChCustomer || isDerendAtCustomer || isMatickAtCustomer;
                    this.externalLinks = this.getDefaultDigitalCatalogLink(affliateIsUsedIncentive, user);

                    this.incentiveLinkInfo = null;
                    if (affliateIsUsedIncentive && this.enableIncentive && !user.salesUser) {
                        this.initIncentiveLink();
                    } else {
                        this.headerSettingsService.resetIncentiveLink();
                    }

                    this.isSaleLogged = user.salesUser || user.isSalesOnBeHalf;
                }
                return user;
            })
        );
    }

    get enableFeedback() {
        return !AffiliateUtil.isEhCh(environment.affiliate) && !AffiliateUtil.isEhCz(environment.affiliate);
    }

    showSettings() {

    }

    showPriceList() {
        this.modalService.show(PriceFileComponent, {
            class: 'info-listed-modal',
            ignoreBackdropClick: true
        });
    }

    showFeeback() {
        this.appModals.modals = this.modalService.show(FeedbackModalComponent, {
            ignoreBackdropClick: true
        });
    }

    redeemHappyPoints() {
        if (!!this.incentiveLinkInfo) {
            const spinner = SpinnerService.start('.header-settings .happy-bonus-wrapper', { containerMinHeight: 0 });
            this.initIncentiveLink(() => {
                SpinnerService.stop(spinner);
                if (this.incentiveLinkInfo.acceptHappyPointTerm) {
                    this.redirectLink(this.incentiveLinkInfo.url);
                } else {
                    const ref = this.modalService.show(HappyPointDialogComponent, {
                        ignoreBackdropClick: true
                    });
                    ref.content.close = () => {
                        this.headerSettingsService.saveAcceptTerm()
                            .pipe(catchError(err => {
                                throw (err);
                            })).subscribe((res) => {
                                if (this.incentiveLinkInfo) {
                                    this.incentiveLinkInfo.acceptHappyPointTerm = true;
                                    this.redirectLink(this.incentiveLinkInfo.url);
                                }
                            });
                    };
                }
            })
        }
    }

    logout() {
        this.sendEventData().then();
        setTimeout(() => {
            this.userService.logout();
        }, 300)
    }

    private redirectLink(url) {
        const link = document.createElement('a');
        link.setAttribute('target', '_blank');
        link.setAttribute('href', url);
        link.click();
        const spinner = SpinnerService.start('.header-settings .happy-bonus-wrapper', { containerMinHeight: 0 });
        this.initIncentiveLink(() => {
            SpinnerService.stop(spinner);
        });
    }

    private initIncentiveLink(callback?) {
        if (this.sub) {
            this.sub.unsubscribe();
        }
        this.sub = this.headerSettingsService.getIncentiveLink()
            .pipe(catchError(err => {
                return of(null);
            }))
            .subscribe(response => {
                this.incentiveLinkInfo = new IncentivePoints(response);
                if (callback) {
                    callback();
                }
            });
    }

    private getDefaultDigitalCatalogLink(affliateIsUsedIncentive: boolean, user: UserDetail) {

        const isAffiliateAT = AffiliateUtil.isAffiliateAT(environment.affiliate);
        const isDerendAtCustomer = CustomerUtil.isDerendAtCustomer(user.affiliateShortName);
        if (!affliateIsUsedIncentive && !isAffiliateAT) {
            return [];
        }
        let extLinks = [];
        if (isAffiliateAT) {
            extLinks = environment.incentive.external_links || [];

            if (isDerendAtCustomer) {
                extLinks = extLinks.filter(item => item.label !== 'MENU.EXTERNAL_LINKS.HAPPY_POINTS_SHOP');
            }
        }

        const isMatikChCustomer = CustomerUtil.isMatikChCustomer(user.affiliateShortName);
        if (isMatikChCustomer) {
            const langCode = user.language;
            extLinks = new Array(this.getDigitalCatalogLinkForMatikCh(langCode));
        } else {
            const isSaleLogged = user.salesUser || user.isSalesOnBeHalf;
            if (isSaleLogged) {
                return [];
            }
        }
        return extLinks;
    }

    private getDigitalCatalogLinkForMatikCh(langCode: string) {
        if (environment.incentive.digital_catalog_links) {
            if (langCode === LANG_CODE_FR) {
                return environment.incentive.digital_catalog_links.fr;
            }
            return environment.incentive.digital_catalog_links.de;
        }
        return [];
    }

    private async sendEventData() {
        try {
            const request = this.analyticService.createLoginLogoutEventData(
                {
                    loginLogout: false,
                    loginSourceType: '',
                    loginSourceProvider: '',
                    loginRole: this.userService.userDetail.roleName,
                    loginUserName: this.userService.userDetail.username
                }
            );
            await this.analyticService
                .postEventFulltextSearch(request, AnalyticEventType.LOGIN_LOGOUT_EVENT)
                .pipe(first())
                .toPromise();
        } catch (error) { }
    }
}
