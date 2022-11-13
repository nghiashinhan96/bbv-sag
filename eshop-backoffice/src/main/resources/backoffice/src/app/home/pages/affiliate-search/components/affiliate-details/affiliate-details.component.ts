import { OnInit, Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { FormGroup } from '@angular/forms';

import { AffiliateSettingModel } from '../../models/affiliate-setting.model';
import { AffiliateService } from '../../../../../core/services/affiliate.service';
import { AffiliateSettingRequest } from '../../models/affiliate-setting-request.model';
import { NotificationModel } from '../../../../../shared/models/notification.model';
import { AffiliateModel } from '../../../../models/affiliate/affiliate.model';
import { RegistrationComponent } from '../registration/registration.component';
import { BrandPriorityAvailabilityUtil, SagEditorLanguageModel, SAG_AVAIL_DISPLAY_OPTIONS, SAG_AVAIL_DISPLAY_STATES, VatTypeDisplayUtil } from 'sag-common';
import { finalize } from 'rxjs/operators';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { BsModalService } from 'ngx-bootstrap/modal';

@Component({
    selector: 'backoffice-affiliate-detail',
    templateUrl: './affiliate-detail.component.html',
    styleUrls: ['./affiliate-details.component.scss']
})
export class AffiliateDetailsComponent implements OnInit {

    public affiliate: AffiliateSettingModel;
    public notifier: NotificationModel;
    public languages: SagEditorLanguageModel[] = [];

    constructor (
        private route: ActivatedRoute,
        private affiliateService: AffiliateService,
        private locationService: Location,
        private modalService: BsModalService
    ) { }

    ngOnInit(): void {
        this.route.queryParams.subscribe(({ affShortName }) => {
            SpinnerService.start();
            const getSettings = this.affiliateService.getSettings(affShortName);
            const getAvailMasterData = this.affiliateService.getAvailMasterData();

            return forkJoin([getSettings, getAvailMasterData])
                .pipe(
                    finalize(() => SpinnerService.stop())
                )
                .subscribe((res: any) => {
                    this.affiliate = res[0];
                    this.languages = this.getLanguages(res[1]);
                });
        });
    }

    showRegistrationFrom() {
        this.modalService.show(RegistrationComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                affiliateShortName: this.affiliate.shortName
            }
        });
    }

    abortAffiliateSetting() {
        this.locationService.back();
    }

    saveAffiliateSetting(settingForm) {
        if (!settingForm) return;

        SpinnerService.start();
        const settings = this.repairModel(settingForm);
        const requestBody = new AffiliateSettingRequest(settings);
        this.affiliateService
            .updateSettings(requestBody)
            .pipe(
                finalize(() => {
                    SpinnerService.stop();

                    window.scrollTo({
                        top: 0,
                        left: 0,
                        behavior: 'smooth'
                    });
                })
            )
            .subscribe(
                () => {
                    this.notifier = { messages: ['COMMON.MESSAGE.UPDATE_SUCCESSFULLY'], status: true };
                },
                () => {
                    this.notifier = { messages: ['COMMON.MESSAGE.UPDATE_FAILED'], status: false };
                }
            );
    }

    private getLanguages(data) {
        const languages = [];

        const supportedLanguages = data && data.supportedLanguages || [];
        if (supportedLanguages.length > 0) {
            supportedLanguages.forEach(element => {
                languages.push(new SagEditorLanguageModel(<SagEditorLanguageModel>{
                    langIso: element.langiso,
                    content: ''
                }));
            });
        }

        return languages;
    }

    private repairModel(form: FormGroup): AffiliateModel {
        const formValue = form.getRawValue();
        const vatDisplay = VatTypeDisplayUtil.mapDataToRequest({
            list: formValue.vtList,
            detail: formValue.vtDetail,
        });

        const cs4 = BrandPriorityAvailabilityUtil.mapDataToRequest({
            p1: formValue.cs4P1,
            p2: formValue.cs4P2,
            p3: formValue.cs4P3
        });

        const c4s = BrandPriorityAvailabilityUtil.mapDataToRequest({
            p1: formValue.c4sP1,
            p2: formValue.c4sP2,
            p3: formValue.c4sP3
        });

        const externalPartSettings = this.externalPartSettings(formValue.externalPartSettings);
        const availDisplaySettings = this.availDisplaySettings(formValue.availDisplaySettings);

        return new AffiliateModel({
            shortName: formValue.shortName,
            name: formValue.name,
            description: formValue.description,
            orgCode: formValue.orgCode,
            vat: formValue.vat,
            customerAbsEnabled: formValue.customerAbsEnabled,
            salesAbsEnabled: formValue.salesAbsEnabled,
            ksoEnabled: formValue.ksoEnabled,
            c4sBrandPriorityAvailFilter: c4s,
            customerBrandPriorityAvailFilter: cs4,
            vatTypeDisplay: vatDisplay,
            customerBrandFilterEnabled: formValue.customerBrandFilterEnabled,
            salesBrandFilterEnabled: formValue.salesBrandFilterEnabled,
            disabledBrandPriorityAvailability: formValue.disabledBrandPriorityAvailability,
            availDisplaySettings: availDisplaySettings,
            invoiceRequestAllowed: formValue.invoiceRequestAllowed,
            invoiceRequestEmail: formValue.invoiceRequestEmail,
            externalPartSettings
        });
    }

    private externalPartSettings(externalPartValue) {
        return {
            useExternalParts: externalPartValue.useExternalParts,
            showInReferenceGroup: externalPartValue.showInReferenceGroup,
            headerNames: this.getEditorAvailText(externalPartValue.headerNames),
            orderTexts: this.getEditorAvailText(externalPartValue.orderTexts)
        }
    }

    private availDisplaySettings(availSettingsValue) {
        return (availSettingsValue || []).map(item => ({
            availState: item.availState,
            title: item.title,
            color: item.color,
            displayOption: this.getDisplayOption(item),
            listAvailText: this.getEditorAvailText(item.listAvail),
            detailAvailText: this.getEditorAvailText(item.detailAvail),
            confirmColor: item.confirmColor
        }));
    }

    private getEditorAvailText(availEditor) {
        const availText: SagEditorLanguageModel[] = [];

        if (availEditor) {
            Object.keys(availEditor).forEach(key => {
                const keys = key.split('_');
                if (keys.length > 0) {
                    availText.push(new SagEditorLanguageModel(<SagEditorLanguageModel>{
                        langIso: keys[0],
                        content: availEditor[key]
                    }));
                }
            });
        }

        return availText.length > 0 ? availText : null;
    }

    getDisplayOption(data) {
        let displayOption = null;
        if (data.availState === SAG_AVAIL_DISPLAY_STATES.NOT_AVAILABLE) {
            displayOption = data.displayOption;
        } else {
            displayOption = data.displayOption === true ? SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT : SAG_AVAIL_DISPLAY_OPTIONS.NONE;
        }
        if (data.availState === SAG_AVAIL_DISPLAY_STATES.NOT_ORDERABLE) {
            displayOption = SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT;
        }
        return displayOption;
    }
}
