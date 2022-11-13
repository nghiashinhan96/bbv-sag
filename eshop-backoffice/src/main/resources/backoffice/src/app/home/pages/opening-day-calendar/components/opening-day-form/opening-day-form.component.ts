import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn, ValidationErrors, AbstractControl } from '@angular/forms';

import { NgOption } from '@ng-select/ng-select';
import { IMyDateModel } from 'angular-mydatepicker';

import { OpeningDayRequestModel, OpeningDayRequest, OpeningDayException } from '../../model/opening-day.model';
import { OPENING_DAY_ROUTE, DELIVERY_ID } from '../../constant';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { EMPTY_STRING } from 'src/app/core/conts/app.constant';
import { BOValidator } from 'src/app/core/utils/validator';
import { TranslateService } from '@ngx-translate/core';


const exceptionValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const branchCode = control.get('branchCode').value;
    const addressDelvryIds = control.get('addressDelvryIds').value;
    if (!branchCode && !addressDelvryIds) {
        return { exceptionInvalid: true };
    }
    return null;
};

@Component({
    selector: 'backoffice-opening-day-form',
    templateUrl: './opening-day-form.component.html',
    styleUrls: ['./opening-day-form.component.scss']
})
export class OpeningDayFormComponent implements OnInit {
    @Input() openingDay: OpeningDayRequestModel;
    @Input() countries: Array<NgOption>;
    @Input() affiliates: Array<NgOption>;
    @Input() branches: Array<any>;
    @Input() workingCodes: Array<any>;
    @Input() onlyNumberHint: string;

    @Output() save = new EventEmitter();
    @Output() update = new EventEmitter();
    @Output() goBackEmmiter = new EventEmitter();

    openingDayForm: FormGroup;
    datePickerCommonSetting = DatePickerUtil.commonSetting;
    isExceptionEnabled = false;
    selectedCountry: string;
    selectedWorkingCode: string;
    exceptDelvryAddressId: string[];
    selectedAffiliateID: string;
    selectedWorkingId: string;
    selectedBranchNumbers: string[];
    selectedExceptionWorkingId: string;
    addressIds = [];
    locale = '';

    private selectedAffiliate: string;
    private selectedExceptionWorkingCode: string;
    private addressInitialId = 0;
    private branchCodes = [];

    constructor(private fb: FormBuilder, private translateService: TranslateService) {
        this.locale = this.translateService.currentLang;
    }

    ngOnInit() {
        this.buildForm();
        this.setFormData();
        this.toggleException();
    }

    toggleException($event?) {
        if ($event) {
            this.isExceptionEnabled = !this.isExceptionEnabled;
        }
        if (this.isExceptionEnabled) {
            this.updateValidatorForWorkingCode();
            this.exceptionGroup.enable();
            this.exceptionGroup.setValidators([exceptionValidator]);
            this.exceptionGroup.updateValueAndValidity();
            this.exceptionGroup.get('exceptDelvryId').setValidators([this.invalidValueValidator(DELIVERY_ID)]);
            this.exceptionGroup.get('exceptDelvryId').updateValueAndValidity();
            this.updateValidatorForExcpWorkingCode();
        } else {
            this.exceptionGroup.disable();
            this.workingCode.setValidators([Validators.required]);
            this.workingCode.updateValueAndValidity();
        }
    }

    onSelectCountry() {
        const selectValue = this.openingDayForm.get('country').value;
        this.selectedCountry = selectValue;
    }

    onSelectedAffiliate($event: any) {
        const selectValue = this.exceptionGroup.get('affiliate').value;
        this.selectedAffiliate = this.affiliates.find(a => a.value === selectValue).label;
    }

    onSelectedBranch($event: any) {
        this.selectedBranchNumbers = this.selectedBranchNumbers ? [...this.selectedBranchNumbers, $event.branch] : [$event.branch];
        this.branchCodes = this.branches.filter(branch => this.selectedBranchNumbers.includes(branch.branch));
        this.exceptionGroup.patchValue({
            branchCode: this.branchCodes.map(branch => branch.code).toString()
        });
    }

    onDeselectBranch(event: any) {
        this.selectedBranchNumbers = this.selectedBranchNumbers.filter(br => br !== event.value.branch);
        this.branchCodes = this.branchCodes.filter((branch) => {
            return branch.value !== event.value.value;
        });
        this.exceptionGroup.patchValue({
            branchCode: this.branchCodes.map(branch => branch.code).toString()
        });
    }

    onSelectedWorkingCode() {
        const selectedValue = this.openingDayForm.get('workingCode').value;
        const selectedWC = this.workingCodes.find(c => c.value === selectedValue);

        this.selectedWorkingCode = selectedWC.originalCode;
        this.selectedWorkingId = selectedWC.value;
        if (this.isExceptionEnabled) {
            this.updateValidatorForExcpWorkingCode();
        }
    }

    onSelectedExceptionWorkingCode(event: any) {
        const selectedValue = this.exceptionGroup.get('exceptionWorkingCode').value;
        const selectedWC = this.workingCodes.find(c => c.value === selectedValue);

        this.selectedExceptionWorkingCode = selectedWC.originalCode;
        this.selectedExceptionWorkingId = selectedWC.value;
        this.updateValidatorForWorkingCode();
    }

    onSubmit() {
        if (this.openingDayForm.valid) {
            this.openingDayForm.disable();
            const openingDayRequest = this.prepareOpeningDataItem();
            if (this.isExceptionEnabled) {
                openingDayRequest.exceptions = this.prepareExceptionData();
            }
            if (openingDayRequest.id) {
                this.update.next({ request: openingDayRequest, form: this.openingDayForm });
            } else {
                this.save.next({ request: openingDayRequest, form: this.openingDayForm });
            }
        }
    }

    goBack() {
        this.goBackEmmiter.emit(OPENING_DAY_ROUTE);
    }

    onValidate(event: KeyboardEvent) {
        return BOValidator.validateNumber(event, 19);
    }

    addSelectedAddress() {
        if (this.exceptDelvryAddressId) {
            this.addressIds.push({ value: this.addressInitialId++, label: this.exceptDelvryAddressId });
            this.addressDelvryIds.setValue(this.addressIds.toString());
            this.exceptionGroup.get('exceptDelvryId').setValue(EMPTY_STRING);
        }
    }

    removeSelectedAddress({ value, label }) {
        this.addressIds = this.addressIds.filter((address) => {
            return address.value !== value;
        });
        this.addressDelvryIds.setValue(this.addressIds.toString());
    }

    trackByFn(index, item) {
        return index;
    }

    get fromDate() { return this.openingDayForm.get('fromDate'); }
    get workingCode() { return this.openingDayForm.get('workingCode'); }
    get exceptionGroup() { return this.openingDayForm.get('exceptionGroup'); }

    set exceptionCheckbox(newValue) { this.openingDayForm.get('exceptionCheckbox').setValue(newValue); }
    get exceptionCheckbox() { return this.openingDayForm.get('exceptionCheckbox').value; }

    get branchCode() { return this.exceptionGroup.get('branchCode'); }
    get exceptionDelvryId() { return this.exceptionGroup.get('exceptDelvryId'); }
    get addressDelvryIds() { return this.exceptionGroup.get('addressDelvryIds'); }

    private buildForm() {
        const countryValue = this.openingDay ? this.selectedCountry = this.openingDay.countryId.toString() : EMPTY_STRING;
        const workingCodeValue = this.openingDay ? this.setWorkingIdFromCode(this.openingDay.workingDayCode, false) : EMPTY_STRING;
        this.isExceptionEnabled = this.openingDay && this.openingDay.exceptions ? true : false;
        this.openingDayForm = this.fb.group({
            fromDate: [this.buildDateData(DatePickerUtil.initDate().date, false), Validators.required],
            country: [countryValue, Validators.required],
            workingCode: [workingCodeValue, Validators.required],
            exceptionCheckbox: [this.isExceptionEnabled],
            exceptionGroup: this.fb.group({
                affiliate: [EMPTY_STRING],
                branchNumber: [EMPTY_STRING],
                branchCode: [EMPTY_STRING],
                exceptionWorkingCode: [EMPTY_STRING],
                exceptDelvryId: [EMPTY_STRING],
                addressDelvryIds: [EMPTY_STRING]
            })
        });

    }

    private setFormData() {
        if (this.openingDay) {
            const date = new Date(this.openingDay.date);
            const dateValue = this.buildDateData(date);
            this.openingDayForm.patchValue({
                fromDate: dateValue
            });

            if (this.openingDay.exceptions) {
                this.extractException();
            }
        }
        if (this.isExceptionEnabled) {
            this.updateValidatorForWorkingCode();
        }
    }

    private buildDateData(date, isJsDate = true) {
        if (isJsDate) {
            return {
                isRange: false,
                singleDate: {
                    date:
                    {
                        year: date.getUTCFullYear(),
                        month: date.getUTCMonth() + 1,
                        day: date.getUTCDate(),
                    }
                }
            } as IMyDateModel;
        }
        return {
            isRange: false,
            singleDate: {
                date:
                {
                    year: date.year,
                    month: date.month,
                    day: date.day,
                }
            }
        } as IMyDateModel;
    }

    private prepareOpeningDataItem(): OpeningDayRequestModel {
        const formatDate = DatePickerUtil.getDateFromToDatePicker(this.fromDate.value.singleDate.date, true);
        const openingDayRequest = new OpeningDayRequest(formatDate);
        openingDayRequest.id = this.openingDay ? this.openingDay.id : null;
        openingDayRequest.countryId = +this.selectedCountry;
        openingDayRequest.workingDayCode = this.selectedWorkingCode;
        return openingDayRequest;
    }

    private prepareExceptionData(): OpeningDayException {
        const openingDayException = new OpeningDayException();
        openingDayException.affiliate = this.selectedAffiliateID ? this.selectedAffiliate : null;
        openingDayException.branches = this.selectedBranchNumbers ? this.selectedBranchNumbers : [];
        openingDayException.workingDayCode = this.selectedExceptionWorkingCode ? this.selectedExceptionWorkingCode : null;
        openingDayException.deliveryAdrressIDs = this.addressIds ? this.addressIds.map(id => id.label) : [];
        return openingDayException;
    }

    private extractException() {
        this.affiliates.forEach(affiliate => {
            if (affiliate.label === this.openingDay.exceptions.affiliate) {
                this.selectedAffiliateID = affiliate.value.toString();
                this.selectedAffiliate = affiliate.label;
            }
        });
        this.branchCodes = this.openingDay.exceptions.branches ?
            this.branches.filter(branch => this.openingDay.exceptions.branches.includes(branch.branch)) : [];
        this.selectedBranchNumbers = this.openingDay.exceptions.branches;
        this.branchCode.setValue(this.branchCodes.map(branch => branch.code).toString());
        this.setWorkingIdFromCode(this.openingDay.exceptions.workingDayCode, true);
        if (this.openingDay.exceptions.deliveryAdrressIDs) {
            this.openingDay.exceptions.deliveryAdrressIDs.map(id => this.addressIds.push({ value: this.addressInitialId++, label: id }));
            this.addressDelvryIds.setValue(this.openingDay.exceptions.deliveryAdrressIDs.toString());
        } else {
            this.addressDelvryIds.setValue(EMPTY_STRING);
        }

        this.exceptionGroup.get('exceptionWorkingCode').setValue(this.selectedExceptionWorkingId);
        this.exceptionGroup.get('branchNumber').setValue(this.selectedBranchNumbers);
        this.exceptionGroup.get('affiliate').setValue(this.selectedAffiliateID);
    }

    private updateValidatorForWorkingCode() {
        this.workingCode.setValidators([Validators.required, this.workingCodeValidator(this.selectedExceptionWorkingId)]);
        this.workingCode.updateValueAndValidity();
    }

    private updateValidatorForExcpWorkingCode() {
        this.exceptionGroup.get('exceptionWorkingCode').setValidators([Validators.required,
        this.exceptionWorkingCodeValidator(this.selectedWorkingId)]);
        this.exceptionGroup.get('exceptionWorkingCode').updateValueAndValidity();
    }

    private setWorkingIdFromCode(workingCode: string, exception: boolean) {
        this.workingCodes.forEach(code => {
            if (code.originalCode === workingCode) {
                if (exception) {
                    this.selectedExceptionWorkingId = code.value;
                    this.selectedExceptionWorkingCode = code.originalCode;
                } else {
                    this.selectedWorkingCode = code.originalCode;
                    this.selectedWorkingId = code.value;
                }
            }
        });
        return this.selectedWorkingId;
    }

    private invalidValueValidator(nameRe: RegExp): ValidatorFn {
        return (control: AbstractControl): { [key: string]: any } | null => {
            const valid = nameRe.test(control.value ? control.value : EMPTY_STRING);
            return valid ? null : { invalidValue: { value: control.value } };
        };
    }

    private workingCodeValidator(exceptionWorkingCode: string): ValidatorFn {
        if (!exceptionValidator) {
            return null;
        }
        return (control: AbstractControl): { [ket: string]: any } | null => {
            const invalid = control.value === exceptionWorkingCode;
            return invalid ? { invalidValue: { value: exceptionWorkingCode } } : null;
        };
    }

    private exceptionWorkingCodeValidator(workingCode: string): ValidatorFn {
        return (control: AbstractControl): { [ket: string]: any } | null => {
            const invalid = control.value === workingCode;
            return invalid ? { invalidValue: { value: workingCode } } : null;
        };
    }
}
