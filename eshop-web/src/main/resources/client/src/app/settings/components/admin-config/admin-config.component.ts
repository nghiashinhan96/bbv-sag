import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CustomerSetting, CustomerSettingRequest } from '../../models/customer-setting.model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { OfferSetting } from '../../models/offer-setting.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BsModalService } from 'ngx-bootstrap/modal';
import { DigiInvoiceRequestModalComponent } from '../digi-invoice-request-modal/digi-invoice-request-modal.component';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { AffiliateEnum } from 'sag-common';

@Component({
    selector: 'connect-admin-config',
    templateUrl: './admin-config.component.html',
    styleUrls: ['./admin-config.component.scss']
})
export class AdminConfigComponent implements OnInit {
    @Input() userDetail: UserDetail;

    @Input() set custSettings(newSettings: CustomerSetting) {
        if (newSettings) {
            this.customerSettings = newSettings;
            this.offerSettings = [
                {
                    group: 'SETTINGS.SETTING.OFFER.FORMAT_ALIGN',
                    title: 'SETTINGS.SETTING.OFFER.FORMAT_ALIGN.TITLE',
                    options: ['LEFT', 'RIGHT'],
                    orgSettings: this.customerSettings.orgPropertyOffer.formatAlign
                },
                {
                    group: 'SETTINGS.SETTING.OFFER.PRINT_VENDOR_ADDR',
                    title: 'SETTINGS.SETTING.OFFER.PRINT_VENDOR_ADDR.TITLE',
                    options: ['TRUE', 'FALSE'],
                    orgSettings: this.customerSettings.orgPropertyOffer.printVendorAddr
                }
            ];
            this.initAdminSettingForm(newSettings);
            if (this.userDetail.isSalesOnBeHalf) {
                this.adminSettingForm.disable();
            }
        }
    }

    @Output() updateAdminSettingEmitter = new EventEmitter<{ request: CustomerSettingRequest, callback: any }>();

    customerSettings: CustomerSetting;
    offerSettings: OfferSetting[];
    adminSettingForm: FormGroup;
    respondMessage: any;
    isUpdating: boolean;
    hidePriceOptions = true;
    shouldShowInvoiceRequestSection = false;
    sb = AffiliateEnum.SB;

    constructor(private fb: FormBuilder, private modalService: BsModalService, private appModal: AppModalService) { }

    ngOnInit() {
        this.shouldShowInvoiceRequestSection = this.checkConditionToShowInvoiceRequestSection();
    }

    onSubmit() {
        this.customerSettings.orgPropertyOffer.footerText = this.adminSettingForm.value.footer;
        const request = {
            allowDiscountChanged: this.customerSettings.allowDiscountChanged,
            allowNetPriceChanged: this.customerSettings.allowNetPriceChanged,
            allowNetPriceConfirmChanged: this.customerSettings.allowNetPriceConfirmChanged,
            allowViewBillingChanged: this.customerSettings.allowViewBillingChanged,
            netPriceConfirm: this.netPriceConfirm.value,
            netPriceView: this.netPriceView.value,
            notifier: this.customerSettings.notifier,
            viewBilling: this.adminSettingForm.value.viewBilling,
            priceDisplaySettings: this.customerSettings.priceDisplaySettings,
            orgPropertyOffer: this.customerSettings.orgPropertyOffer,
            showDiscount: this.customerSettings.showDiscount
        } as CustomerSettingRequest;

        this.isUpdating = true;
        this.respondMessage = null;
        SpinnerService.start('.admin-setting');
        this.updateAdminSettingEmitter.emit({
            request,
            callback: (message) => this.reset(message)
        });
    }

    onChangePriceDisplaySetting(id: number) {
        this.customerSettings.priceDisplaySettings = this.customerSettings.priceDisplaySettings
            .map(setting => ({ ...setting, enable: (setting.id === id) }));
    }

    onChangeAddressSetting(value, group) {
        switch (group) {
            case this.offerSettings[0].group:
                this.customerSettings.orgPropertyOffer.formatAlign = value;
                break;
            case this.offerSettings[1].group:
                this.customerSettings.orgPropertyOffer.printVendorAddr = value;
                break;
        }
    }

    get netPriceView() { return this.adminSettingForm.get('netPriceView'); }
    get netPriceConfirm() { return this.adminSettingForm.get('netPriceConfirm'); }

    private reset(message) {
        this.isUpdating = false;
        this.respondMessage = message;
        SpinnerService.stop('.admin-setting');
    }

    private initAdminSettingForm(customerSetting: CustomerSetting) {
        this.adminSettingForm = this.fb.group({
            viewBilling: [{
                value: (customerSetting && customerSetting.viewBilling) || false,
                disabled: customerSetting && !customerSetting.allowViewBillingChanged
            }],
            netPriceView: [{
                value: (customerSetting && customerSetting.netPriceView) || false,
                disabled: customerSetting && !customerSetting.allowNetPriceChanged
            }],
            netPriceConfirm: [
                {
                    value: (customerSetting && customerSetting.netPriceConfirm) || false,
                    disabled: customerSetting && !customerSetting.allowNetPriceConfirmChanged
                }
            ],
            footer: [customerSetting && customerSetting.orgPropertyOffer && customerSetting.orgPropertyOffer.footerText]
        });

        this.netPriceView.valueChanges.subscribe(value => {
            if (!value) {
                this.netPriceConfirm.disable();
                this.netPriceConfirm.setValue(false);
            } else {
                this.netPriceConfirm.enable();
            }
        });
    }

    checkConditionToShowInvoiceRequestSection() {
        let shouldShow = false;
        const invoiceRequestAllowed = this.userDetail.settings && this.userDetail.settings.invoiceRequestAllowed;
        if (this.userDetail.userAdminRole && !this.userDetail.isFinalUserRole && invoiceRequestAllowed) {
            shouldShow = true;
        }
        return shouldShow;
    }

    onRequest() {
        this.appModal.modals = this.modalService.show(DigiInvoiceRequestModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                userDetail: this.userDetail
            }
        });
    }
}
