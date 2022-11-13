import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { IMyDateModel } from 'angular-mydatepicker';
import { TranslateService } from '@ngx-translate/core';
import { BOValidator } from 'src/app/core/utils/validator';
import { LicenseUpdateService } from '../../services/license-update.service';
import { LICENSE_DATE_TYPE } from '../../enums/license.enum';
import { MESSAGE_TIMEOUT } from 'src/app/core/conts/app.constant';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { Subscription } from 'rxjs';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { LicenseEditRequestModel } from '../../models/license-edit-request.model';
import * as moment from 'moment';

@Component({
    selector: "backoffice-license-edit-modal",
    templateUrl: './license-edit-modal.component.html',
})
export class LicenseEditModalComponent implements OnInit, OnDestroy {
    @Input() license;
    @Input() licensePackage;
    @Output() saveEventEmitter = new EventEmitter();
    @Output() closeModalEventEmitter = new EventEmitter();

    licenseForm: FormGroup;
    customerNr: string;
    beginDate: IMyDateModel;
    endDate: IMyDateModel;
    quantity: number;
    datePickerFromSetting: any;
    datePickerToSetting: any;
    notifier: NotificationModel;
    locale: string = '';
    licenseEditRequestModel: LicenseEditRequestModel;

    private subs = new Subscription();

    constructor(
        private formBuilder: FormBuilder,
        private licenseUpdateService: LicenseUpdateService,
        private translateService: TranslateService
    ) {
        this.locale = this.translateService.currentLang;
    }

    ngOnInit(): void {
        if (!this.license) {
            return;
        }

        this.initValue();
        this.buildLicenseForm();
        this.initDateValidityConstraints();
    }

    ngOnDestroy(): void {
        if (this.subs) {
            this.subs.unsubscribe();
        }
    }

    onBeginDateChanged(event: IMyDateModel) {
        this.datePickerToSetting = this.licenseUpdateService.changeDate(
            this.datePickerToSetting,
            event.singleDate.date,
            LICENSE_DATE_TYPE.BEGIN
        );
    }

    onEndDateChanged(event: IMyDateModel) {
        this.datePickerFromSetting = this.licenseUpdateService.changeDate(
            this.datePickerFromSetting,
            event.singleDate.date,
            LICENSE_DATE_TYPE.END
        );
    }

    closeModal() {
        this.closeModalEventEmitter.emit();
    }

    saveLicense() {
        if ( !this.license || !this.licenseForm || (this.licenseForm && !this.licenseForm.valid)) {
            return;
        }
        const rawValues = {
            id: this.license.id,
            packName: this.licenseForm.getRawValue().packName.value,
            beginDate: DatePickerUtil.getDateFromToDatePicker(this.licenseForm.getRawValue().beginDate.singleDate.date),
            endDate: DatePickerUtil.getDateFromToDatePicker(this.licenseForm.getRawValue().endDate.singleDate.date),
            quantity: this.licenseForm.getRawValue().quantity,
        };
        this.licenseEditRequestModel = new LicenseEditRequestModel(rawValues);

        this.subs.add(
            this.licenseUpdateService.updateLicense(this.licenseEditRequestModel)
            .subscribe(res => {
                this.saveEventEmitter.emit();
            }, ({ error }) => {
                    this.handleError(error);
                }
            )
        )
    }

    updateQuantity(data) {
        const selectedPack = this.licensePackage.find(item => item.value === data.value);
        const quantity = selectedPack.quantity || 0;
        this.licenseForm.controls.quantity.setValue(quantity);
    }

    private initValue() {
        this.datePickerFromSetting = {};
        this.datePickerToSetting = {};
        this.customerNr = this.license.customerNr;
        this.beginDate = this.licenseUpdateService.buildDataDatePicker(DatePickerUtil.buildDataDatePicker(new Date(moment(this.license.beginDate, 'DDMMYYYY').format('L'))));
        this.endDate = this.licenseUpdateService.buildDataDatePicker(DatePickerUtil.buildDataDatePicker(new Date(moment(this.license.endDate, 'DDMMYYYY').format('L'))));
        this.quantity = this.license.quantity;
    }

    private initDateValidityConstraints(): void {
        this.datePickerFromSetting = this.licenseUpdateService.initDateValidityConstraints(this.endDate, LICENSE_DATE_TYPE.END);
        this.datePickerToSetting = this.licenseUpdateService.initDateValidityConstraints(this.beginDate, LICENSE_DATE_TYPE.BEGIN);
    }

    private buildLicenseForm(): void {
        this.licenseForm = this.formBuilder.group({
            packName: new FormControl({ value: this.license.packName || '', label: this.license.packName || '' }),
            beginDate: new FormControl(this.beginDate || ''),
            endDate: new FormControl(this.endDate || ''),
            quantity: new FormControl(this.license.quantity || 0, [BOValidator.validateQuantity]),
        });
    }

    private handleError(error) {
        if (!error) {
            return;
        }

        const errorMess = ApiUtil.handleErrorReponse(error);
        this.notifier = { messages: [errorMess], status: false };

        // Remove notifier after 4000ms
        setTimeout(() => {
            this.notifier = undefined;
        }, MESSAGE_TIMEOUT);
    }
}
