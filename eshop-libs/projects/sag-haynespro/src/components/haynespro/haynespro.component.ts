import { AffiliateEnum } from 'sag-common';
import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, Optional } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { finalize } from 'rxjs/operators';
import { uniq, partition } from 'lodash';
import { HaynesProService } from '../../services/haynespro.service';
import { HaynesProAccessUrl } from '../../models/haynes-pro-access-url.model';
import { HaynesProLicense } from '../../enums/haynespro.enum';
import { SagHaynessproModalComponent } from '../haynespro-modal/haynespro-modal.component';
import { SagHaynesproErrorModalComponent } from '../haynespro-error-modal/haynespro-error-modal.component';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { HaynesProLicenseSettings } from '../../models/haynes-pro-license-settings.model';


@Component({
    selector: 'sag-haynespro',
    templateUrl: './haynespro.component.html',
    styleUrls: ['./haynespro.component.scss']
})
export class SagHaynesProComponent implements OnInit, OnChanges, OnDestroy {

    @Input() isRedirectToHaynesPro: boolean;
    @Input() user: any;
    @Input() vehicle: any;

    @Output() haynesProSearch = new EventEmitter();

    public enableHaynesproLink = false;
    public haynesProRedirectUrl: any;
    public haynesProSelect = ' ';
    public isRequestedLicense = false;
    private modalRef: BsModalRef;

    public isDisabledLink: boolean;

    // HaynesPro Parts variables
    private haynesProLicense: HaynesProLicenseSettings;
    private haynesProOptionsRes: any;
    // #1089: 8%
    private readonly VAT = '8';

    loadHaynesProSpinner = '.sag-haynesspro-modal .modal-content';
    loadRequestLicenseSpinner = 'sag-haynespro-error-component';

    constructor(
        private translateService: TranslateService,
        private modalService: BsModalService,
        private haynesProService: HaynesProService
    ) { }

    ngOnChanges(changes: SimpleChanges) {
        this.checkLicense();

        if (changes.user && changes.user.currentValue.customer) {
            this.isRequestedLicense = false;
        }
    }

    ngOnInit() {
        this.haynesProService.getAccessOptions().subscribe(res => this.haynesProOptionsRes = res);
    }

    ngOnDestroy(): void {
    }

    handleHaynesProUrl() {
        this.isDisabledLink = true;
        if (!this.enableHaynesproLink) {
            this.openErrorModal();
            return;
        }
        if (this.isRedirectToHaynesPro) {
            this.loginHaynesPro();
            return;
        }

        if (!this.haynesProOptionsRes) {
            return;
        }

        this.modalService.show(SagHaynessproModalComponent, {
            ignoreBackdropClick: true,
            class: 'sag-haynesspro-modal',
            initialState: {
                vehicleInfo: this.vehicle ? this.vehicle.vehicleInfo : '',
                haynesProOptions: this.buildHaynesProOptions(this.haynesProOptionsRes),
                haynesProSelect: this.haynesProSelect,
                haynesProRedirectUrl: this.haynesProRedirectUrl,
                getHaynesProParts: () => this.getHaynesProParts(),
                loginHaynesPro: (value, cb) => this.loginHaynesPro(value, cb),
                reGenerateLoginUrl: (callback) => this.reGenerateLoginUrl(callback)
            }
        });
    }

    /**
     * single sign on Haynes Pro
     */
    loginHaynesPro(selectedHaynesPro?, callback?) {
        const spinner = this.haynesProService.spinner.start(this.loadHaynesProSpinner, { withoutText: true });

        if (selectedHaynesPro) {
            this.haynesProSelect = selectedHaynesPro;
        }
        this.isDisabledLink = false;
        const request = new HaynesProAccessUrl();
        request.licenseType = this.haynesProLicense && this.haynesProLicense.licenseType;
        request.optionalParameters = this.haynesProLicense && this.haynesProLicense.optionalParameters;
        request.hourlyRate = this.user.hourlyRate ? this.user.hourlyRate : '';
        request.vatRate = this.user.settings.vatRate ? this.user.settings.vatRate : this.VAT;
        request.callbackBtnText = this.translateService.instant('HAYNES_PRO.EXPORT_TO_WEBSHOP');
        request.subject = this.haynesProSelect;

        if (this.vehicle) {
            request.kType = this.vehicle.ktype;
            request.motorId = this.vehicle.id_motor;
            request.vehicleInfo = this.vehicle.vehicleInfo;
        }

        this.haynesProService.getAccessUrl(request)
            .pipe(finalize(() => this.haynesProService.spinner.stop(spinner)))
            .subscribe((res: any) => {
                this.haynesProRedirectUrl = res.accessUrl;
                if (callback) {
                    callback(this.haynesProRedirectUrl);
                }
                if (this.isRedirectToHaynesPro) {
                    this.goToLink(this.haynesProRedirectUrl);
                }
            },
                () => this.openErrorModal());
    }

    getHaynesProParts() {
        this.haynesProService.getResponse(this.vehicle.id)
            .subscribe(
                (response: any) => {
                    if (!response.vehicle_id) {
                        return;
                    }
                    this.handleHaynesProResponse(response.gen_arts);
                },
                () => {
                    this.haynesProSearch.emit(false);
                }
            );
    }

    reGenerateLoginUrl(callback) {
        setTimeout(() => this.loginHaynesPro(null, callback), 1000);
    }

    requestTrialLicense() {
        this.haynesProService.spinner.start(this.loadRequestLicenseSpinner, { withoutText: true, containerMinHeight: 100 });

        this.haynesProService.requestTrialLicense()
            .pipe(finalize(() => this.haynesProService.spinner.stop(this.loadRequestLicenseSpinner)))
            .subscribe(
                () => {
                    this.isRequestedLicense = true;
                    this.openErrorModal();
                }
            );
    }

    private handleHaynesProResponse(gaids) {
        gaids = uniq(gaids);

        const haynesProCates: any[] = this.haynesProService.getCategoriesByGaids(gaids);
        const cates = partition(haynesProCates, (cate: any) => cate.oilCate);
        const oilCateIds = cates[0].map(cate => cate.id);
        const cateIds = cates[1].map(cate => cate.id);
        this.haynesProSearch.emit(true);
        if (cateIds.length) {
            this.haynesProService.checkOnCategoryTree(cateIds, true, oilCateIds.length === 0);
        }
        if (oilCateIds.length) {
            // hanlde oates
            this.haynesProService.checkOilCate(oilCateIds, true);
        }
    }

    private checkLicense() {
        this.isDisabledLink = true;
        this.haynesProService.getHaynesProLicense()
            .pipe(finalize(() => this.isDisabledLink = false))
            .subscribe(
                (license: HaynesProLicenseSettings) => {
                    this.haynesProLicense = license;
                    this.enableHaynesproLink = this.checkLicenseType();
                },
                () => {
                    this.haynesProLicense = null;
                    this.enableHaynesproLink = false;
                });
    }

    private buildHaynesProOptions(optionRes: any) {
        if (!optionRes) {
            return;
        }

        let result = [];
        result.push({ value: ' ', label: this.translateService.instant('HAYNES_PRO.SELECT_OPTION.OVERVIEW') });

        result = result.concat(optionRes.map((option: any) => ({
            value: option.code,
            label: this.translateService.instant(`HAYNES_PRO.SELECT_OPTION.${option.name}`)
        })));

        return result;
    }

    private checkLicenseType(): boolean {
        const licenseType = this.haynesProLicense && this.haynesProLicense.licenseType;
        return licenseType === HaynesProLicense.PROFESSIONAL.toString() ||
            licenseType === HaynesProLicense.ULTIMATE.toString() ||
            licenseType === HaynesProLicense.CAR_TECH_ELEC.toString();
    }

    private goToLink(link: string) {
        window.open(link, '_blank');
    }

    private openErrorModal() {
        let errorMessage = 'HAYNES_PRO.ERROR.NO_LICENSE';
        let showRequestTrialLicense = false;
        if ([
            AffiliateEnum.DEREND_CH.toString(),
            AffiliateEnum.TECHNO_CH.toString(),
            AffiliateEnum.MATIK_AT.toString(),
            AffiliateEnum.DEREND_AT.toString()
        ].indexOf(this.user.customer.affiliateShortName) >= 0) {
            errorMessage = 'HAYNES_PRO.ERROR.REQUEST_TRIAL_LICENSE_MESG';
            showRequestTrialLicense = true;
        }
        if (this.modalRef) {
            this.modalRef.hide();
        }

        this.modalRef = this.modalService.show(SagHaynesproErrorModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                errorMessage,
                isRequestedLicense: this.isRequestedLicense,
                showRequestTrialLicense,
                requestTrialLicense: () => this.requestTrialLicense()
            }
        });
        this.isDisabledLink = false;
    }
}
