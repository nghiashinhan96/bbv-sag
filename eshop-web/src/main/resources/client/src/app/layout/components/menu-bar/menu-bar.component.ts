import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { Observable, Subject } from 'rxjs';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { map } from 'rxjs/internal/operators/map';
import { CustomerUtil } from 'src/app/core/utils/customer.util';
import { environment } from 'src/environments/environment';
import { BsModalService } from 'ngx-bootstrap/modal';
import { PromotionModalComponent } from './dialogs/promotion-modal/promotion-modal.component';
import { ShopInfoModalComponent } from './dialogs/shop-info-modal/shop-info-modal.component';
import { AffiliateEnum, AffiliateUtil } from 'sag-common';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter, takeUntil } from 'rxjs/operators';
import { FormUtil } from 'src/app/core/utils/form.util';
import { AffiliateService } from 'src/app/core/services/affiliate.service';
import { TranslateService } from '@ngx-translate/core';
import { Constant } from 'src/app/core/conts/app.constant';
import { MOTORBIKE_PERMISSION_CODE } from 'src/app/shared/connect-common/enums/motorbike-permission-code.enum';

@Component({
    selector: 'connect-menu-bar',
    templateUrl: './menu-bar.component.html',
    styleUrls: ['./menu-bar.component.scss']
})
export class MenuBarComponent implements OnInit, OnDestroy, AfterViewInit {

    user$: Observable<UserDetail>;
    isDerendChCustomer: boolean;
    isTechnoChCustomer: boolean;
    isRbeCustomer: boolean;
    isWbbCustomer: boolean;
    isAtCustomer: boolean;
    isDerendAtCustomer: boolean;
    isMatikAtCustomer: boolean;
    isMatikChCustomer: boolean;
    isMatikCustomer: boolean;
    thuleExternalUrl: string;
    mediaDirectory = this.updateAffiliateDirectory(environment.affiliate);
    outletUrl: string;
    cz = AffiliateEnum.CZ;
    ehcz = AffiliateEnum.EH_CZ;
    sb = AffiliateEnum.SB;
    ehch = AffiliateEnum.EH_CH;
    ehat = AffiliateEnum.EH_AT;
    axcz = AffiliateEnum.AXCZ;
    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isCz9 = AffiliateUtil.isAffiliateCZ9(environment.affiliate);
    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);
    isSb = AffiliateUtil.isSb(environment.affiliate);
    isCzBased = this.isCz || this.isAxCz;
    ehaxcz = AffiliateEnum.EH_AX_CZ;
    enableOutletBox = false;

    openSgtFrom = '';
    FROM_PROMOTION = 'promotion';
    FROM_SGT = 'sgt';
    SGT_URL = '/saginfo';
    hasWSPPermission = false;
    hasDvsePermission = false;
    isEh = false;
    isAT = AffiliateUtil.isAffiliateAT(environment.affiliate);
    isAffiliateApplyMotorbikeShop = AffiliateUtil.isAffiliateApplyMotorbikeShop(environment.affiliate);
    isCH = AffiliateUtil.isAffiliateCH(environment.affiliate);
    MOTORBIKE_PERMISSION_CODE = MOTORBIKE_PERMISSION_CODE;

    @ViewChild('dataForm', { static: false }) dataForm: ElementRef;
    @ViewChild('navbar', { static: false }) navbar: ElementRef;

    private destroy$ = new Subject<boolean>();

    constructor(
        private userService: UserService,
        private modalService: BsModalService,
        private router: Router,
        private translateService: TranslateService,
        private affiliateService: AffiliateService
    ) {
        this.setDefaultAffiliate();
    }

    ngOnInit() {
        this.isEh = AffiliateUtil.isEhCh(environment.affiliate) || AffiliateUtil.isEhAt(environment.affiliate) || AffiliateUtil.isEhCz(environment.affiliate);

        this.userService.hasPermission(Constant.PERMISSIONS.UNIPARTS)
            .pipe(
                takeUntil(this.destroy$)
            )
            .subscribe(per => this.hasWSPPermission = per);
        this.userService.hasPermission(Constant.PERMISSIONS.DVSE)
            .pipe(
                takeUntil(this.destroy$)
            )
            .subscribe(per => this.hasDvsePermission = per);

        this.user$ = this.userService.userDetail$.pipe(
            map(userDetail => {
                this.setAffiliateInfo(userDetail);
                // Get external url of Thule to set to Information link popup.
                if (userDetail && userDetail.settings && userDetail.settings.externalUrls && userDetail.settings.externalUrls.thule) {
                    this.thuleExternalUrl = userDetail.settings.externalUrls.thule;
                }

                return userDetail;
            })
        );

        if (this.router.url == this.SGT_URL && !this.openSgtFrom) {
            this.openSgtFrom = this.FROM_SGT;
        }

        this.router.events
            .pipe(
                takeUntil(this.destroy$),
                filter(event => event instanceof NavigationEnd))
            .subscribe((event: NavigationEnd) => {
                if (event.urlAfterRedirects !== this.SGT_URL) {
                    this.openSgtFrom = '';
                }
            });

        this.enableOutletBox = this.isEnableOutletBox();
        this.buildOutletFormData();
    }

    ngAfterViewInit(): void {
        this.setBorderRightToLastVisibleMenuItem();
    }

    ngOnDestroy() {
        this.destroy$.next(true);
        this.destroy$.complete();
    }

    showPromotion() {
        this.modalService.show(PromotionModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                isDerendChCustomer: this.isDerendChCustomer,
                isTechnoChCustomer: this.isTechnoChCustomer,
                isRbeCustomer: this.isRbeCustomer,
                isWbbCustomer: this.isWbbCustomer,
                isAtCustomer: this.isAtCustomer,
                isDerendAtCustomer: this.isDerendAtCustomer,
                isMatikAtCustomer: this.isMatikAtCustomer,
                isMatikChCustomer: this.isMatikChCustomer,
                isCz: this.isCz,
                mediaDirectory: this.mediaDirectory
            }
        });
    }

    showInfo() {
        this.modalService.show(ShopInfoModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                isDerendChCustomer: this.isDerendChCustomer,
                isTechnoChCustomer: this.isTechnoChCustomer,
                isRbeCustomer: this.isRbeCustomer,
                isWbbCustomer: this.isWbbCustomer,
                isAtCustomer: this.isAtCustomer,
                isDerendAtCustomer: this.isDerendAtCustomer,
                isMatikAtCustomer: this.isMatikAtCustomer,
                isMatikChCustomer: this.isMatikChCustomer,
                mediaDirectory: this.mediaDirectory,
                thuleExternalUrl: this.thuleExternalUrl
            }
        });
    }

    openSGTInfo(fromRef) {
        this.openSgtFrom = fromRef;
        this.router.navigate([this.SGT_URL]);
    }

    openOutletBox() {
        const btn = this.dataForm.nativeElement.querySelector('input[type=submit]') as any;
        if (btn && this.enableOutletBox) {
            btn.click();
        }
    }

    openRentTools(event) {
        window.open(`https://www.mobility-market.eu/de/derendinger/`, '_blank');
        event.preventDefault();
        event.stopPropagation();
    }

    private buildOutletFormData() {
        if (!this.isCz) {
            this.affiliateService.getOutletBoxInfo().subscribe(data => {
                if (!data) {
                    return;
                }

                const langCode = this.translateService.currentLang.toUpperCase();
                const f = FormUtil.buildFormData(data, `https://www.outletbox.ch/sagauth/authenticate/?language=${langCode}`);
                this.dataForm.nativeElement.innerHTML = '';
                this.dataForm.nativeElement.appendChild(f);
            })
        }
    }

    private updateAffiliateInfo(affiliateShortName) {
        this.isDerendChCustomer = CustomerUtil.isDerendChCustomer(affiliateShortName);
        this.isDerendAtCustomer = CustomerUtil.isDerendAtCustomer(affiliateShortName);
        this.isMatikAtCustomer = CustomerUtil.isMatikAtCustomer(affiliateShortName);
        this.isTechnoChCustomer = CustomerUtil.isTechnoChCustomer(affiliateShortName);
        this.isRbeCustomer = CustomerUtil.isRbeCustomer(affiliateShortName);
        this.isWbbCustomer = CustomerUtil.isWbbCustomer(affiliateShortName);
        this.isAtCustomer = CustomerUtil.isAtCustomer(affiliateShortName);
        this.mediaDirectory = this.updateAffiliateDirectory(affiliateShortName);
        this.isMatikChCustomer = CustomerUtil.isMatikChCustomer(affiliateShortName);
        this.isMatikCustomer = this.isMatikChCustomer || this.isMatikAtCustomer;
    }
    private setDefaultAffiliate() {
        this.isDerendChCustomer = AffiliateUtil.isDerendCh(environment.affiliate);
        this.isDerendAtCustomer = AffiliateUtil.isDerendAt(environment.affiliate);
        this.isMatikAtCustomer = AffiliateUtil.isMatikAt(environment.affiliate);
        this.isTechnoChCustomer = AffiliateUtil.isTechnoCh(environment.affiliate);
        this.isRbeCustomer = AffiliateUtil.isRbe(environment.affiliate);
        this.isWbbCustomer = AffiliateUtil.isWbb(environment.affiliate);
        this.isAtCustomer = AffiliateUtil.isBaseAT(environment.affiliate);
        this.mediaDirectory = this.updateAffiliateDirectory(environment.affiliate);
    }

    private updateAffiliateDirectory(affiliateShortName) {
        if (this.isCz) {
            return `https://s3.exellio.de/connect_media/stahlgruber-cz/pdfs`;
        }
        return `https://s3.exellio.de/connect_media/${affiliateShortName}/pdfs`;
    }

    private setAffiliateInfo(userDetail: UserDetail) {
        if (userDetail && userDetail.customer && userDetail.customer.affiliateShortName) {
            this.updateAffiliateInfo(userDetail.customer.affiliateShortName);
        } else {
            this.setDefaultAffiliate();
        }
    }

    private isEnableOutletBox() {
        const aff = environment.affiliate;
        return AffiliateUtil.isDerendCh(aff) || AffiliateUtil.isMatikCh(aff) || AffiliateUtil.isTechnoCh(aff);
    }

    private setBorderRightToLastVisibleMenuItem() {
        // Used for set border-right to last visible menu item in case of having some hidden menus afterward
        const liElements = this.navbar && this.navbar.nativeElement.getElementsByTagName('li');
        let lastElement;
        if (liElements) {
            for (let i = 0; i < liElements.length; i++) {
                const liElement = liElements[i];
                if (liElement.getAttribute("hidden") === null) {
                    lastElement = liElement;
                }
            }
        }
        if (lastElement) {
            const aElement = lastElement.querySelector('a');
            if (aElement) {
                aElement.classList.add('nav-link-border-right');
            }
        }
    }
}
