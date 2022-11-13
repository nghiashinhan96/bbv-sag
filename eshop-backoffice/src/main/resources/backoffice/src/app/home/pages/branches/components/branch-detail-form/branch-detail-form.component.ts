import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormControl, Validators, AbstractControl, FormBuilder } from '@angular/forms';

import BranchesUtils from '../../branches.utils';
import { BranchService } from '../../service/branch.service';
import { Branch, BranchModel, CountryBranchModel } from '../../model/branch.model';
import { BOValidator } from 'src/app/core/utils/validator';
import { invalidValueValidator, branchTimePickerValidator } from './branches.time-picker-validation';
import { EMPTY_STRING } from 'src/app/core/conts/app.constant';
import {
    CREATE_NEW_BRANCH,
    BRANCH_ROUTE, EDIT_SELECTED_BRANCH,
    OPENING_HOUR, CLOSING_HOUR,
    LUNCH_OPENING_HOUR, LUNCH_CLOSING_HOUR,
    TIMEOUT, NUMBER_ONLY, ALPHANUMBERIC,
    TEXT,
    TIME_REGEX,
    LUNCH_TIME_REGIX,
    EMAIL_REGEX,
    PASTED_ORG_FIELD,
    ORG_ID_REGEX,
    PASTED_BRANCH_NUMBER_FIELD,
    BRANCH_NUMBER_REGEX,
    PASTED_EMAIL_FIELD,
    DAY_IN_WEEK
} from '../../branches.constant';
import { BranchOpeningTime } from '../../model/branch-response.model';
import { openingTimePickerValidator } from '../../service/opening-time-picker-validator';
import { get } from 'lodash';

@Component({
    selector: 'backoffice-branch-detail-form',
    templateUrl: './branch-detail-form.component.html',
    styleUrls: ['./branch-detail-form.component.scss']
})
export class BranchDetailFormComponent implements OnInit {
    @ViewChild('branchCodeInput', { static: true }) branchCodeInput: ElementRef;
    @ViewChild('openingHourInput', { static: true }) openingHourInput: ElementRef;
    @ViewChild('closingHourInput', { static: true }) closingHourInput: ElementRef;

    branch: BranchModel;
    branchDetailForm: FormGroup;
    hourList = BranchesUtils.buildDataTimePicker();
    countries: CountryBranchModel[];

    showingTimePickerCode = '';

    isCreatedSuccessful: boolean;
    isCreatedFail: boolean;
    noticeMessage: string;
    requiredErrorMessage = 'BRANCHES.ERRORS.REQUIRED_ERROR_MESSAGE';
    // invalidTimeErrorMessage = 'BRANCHES.ERRORS.INVALID_TIME_VALUE';
    isCreatedNewBranch: string;

    focusingTimeInput = '';
    isPastedBranchNumberInvalid: boolean;

    isPastedOrgIdInvalid: boolean;
    isPastedEmailInvalid: boolean;
    isPastedUrlInvalid: boolean;
    dayInWeek = DAY_IN_WEEK;

    relatedTimeField = {
        [OPENING_HOUR]: 'openingTime',
        [CLOSING_HOUR]: 'closingTime',
        [LUNCH_OPENING_HOUR]: 'lunchOpeningTime',
        [LUNCH_CLOSING_HOUR]: 'lunchClosingTime'
    }

    constructor(
        private route: ActivatedRoute,
        private branchService: BranchService,
        private router: Router,
        private fb: FormBuilder
    ) { }

    ngOnInit() {
        this.buildForm();
        this.route.params.subscribe(params => {
            this.branchNumber.setValue(params.id);
        });
        this.route.queryParams.subscribe(params => {
            this.isCreatedNewBranch = params.createNew;
        });
        this.setUpDefaultValueForForm();
    }

    onToggleTimePicker(type: string, day: string) {
        const code = `${type}.${day}`;
        this.showingTimePickerCode = this.showingTimePickerCode === code ? '' : code;
    }

    onSetHour(value: any, type: string, day: string) {
        this.onToggleTimePicker(type, day);
        this.onDetectInputFocus(type, day);
        this.branchDetailForm.get(day).patchValue({ [this.relatedTimeField[type]]: value.formatText });
    }

    onSubmit() {
        this.resetMessageStyle();
        if (this.branchDetailForm.invalid) {
            return;
        }
        if (this.isCreatedNewBranch === CREATE_NEW_BRANCH) {
            this.branchService
                .createBranch(this.prepareBranchData())
                .subscribe((branches: BranchModel) => {
                    this.isCreatedSuccessful = true;
                    this.noticeMessage = 'BRANCHES.BRANCH_DETAIL_FORM.CREATE_SUCCESS_MESSAGE';
                    this.branchDetailForm.disable();
                    setTimeout(() => {
                        this.goBack();
                    }, TIMEOUT);
                }, error => {
                    this.handleError(error);
                });
        } else if (this.isCreatedNewBranch === EDIT_SELECTED_BRANCH) {
            this.branchService.editBranch(this.prepareBranchData()).subscribe(res => {
                this.isCreatedSuccessful = true;
                this.noticeMessage = 'BRANCHES.BRANCH_DETAIL_FORM.UPDATE_SUCCESS_MESSAGE';
                this.branchDetailForm.disable();
                setTimeout(() => {
                    this.goBack();
                }, TIMEOUT);
            }, error => {
                this.handleError(error);
            });
        }
    }

    goBack() {
        this.router.navigate([BRANCH_ROUTE]);
    }

    validateUserInput(e: KeyboardEvent, maxLength: number, type: string) {
        this.noticeMessage = EMPTY_STRING;
        switch (type) {
            case NUMBER_ONLY:
                return BOValidator.validateNumber(e, maxLength);
            case ALPHANUMBERIC:
                return BOValidator.validateAlphanumeric(e, maxLength);
            case TEXT:
                return BOValidator.validateAlphanumericWithSpace(e, maxLength);
            default:
                return BOValidator.validateAlphanumericWithSpace(e, maxLength);
        }
    }

    onDetectInputFocus(type: string, day: string) {
        this.focusingTimeInput = `${type}.${day}`;
    }

    onPaste(e: ClipboardEvent, maxLength: number, control: AbstractControl, controlName?: string) {
        e.preventDefault();
        const clipboardData = e.clipboardData || (window as any).clipboardData;
        const pastedText = clipboardData.getData('text');

        const targetText = control.value.length === maxLength ? pastedText.slice(0, maxLength) :
            control.value + pastedText.slice(0, maxLength - control.value.length);

        control.setValue(targetText);

        this.validatePastedData(targetText, controlName);
    }

    onInputChange(value: string, controlName: string) {
        this.validatePastedData(value, controlName);
    }

    isRequiredError(control: AbstractControl): boolean {
        return (control.dirty || control.touched) && control.invalid ? control.errors.required : false;
    }

    get branchNumber() { return this.branchDetailForm.get('branchNumber'); }
    get branchCode() { return this.branchDetailForm.get('code'); }

    dayForm(day) { return this.branchDetailForm && this.branchDetailForm.get(day); }
    openingTime(day) { return this.branchDetailForm && this.branchDetailForm.get(day).get('openingTime'); }
    closingTime(day) { return this.branchDetailForm && this.branchDetailForm.get(day).get('closingTime'); }
    lunchOpeningTime(day) { return this.branchDetailForm && this.branchDetailForm.get(day).get('lunchOpeningTime'); }
    lunchClosingTime(day) { return this.branchDetailForm && this.branchDetailForm.get(day).get('lunchClosingTime'); }

    get addressDesc() { return this.branchDetailForm.get('addressDesc'); }
    get addressStreet() { return this.branchDetailForm.get('addressStreet'); }
    get addressCity() { return this.branchDetailForm.get('addressCity'); }
    get addressCountry() { return this.branchDetailForm.get('addressCountry'); }
    get countryId() { return this.branchDetailForm.get('countryId'); }
    get zipCode() { return this.branchDetailForm.get('zipCode'); }

    get orgId() { return this.branchDetailForm.get('orgId'); }
    get regionId() { return this.branchDetailForm.get('regionId'); }
    get primaryPhone() { return this.branchDetailForm.get('primaryPhone'); }
    get primaryEmail() { return this.branchDetailForm.get('primaryEmail'); }
    get primaryFax() { return this.branchDetailForm.get('primaryFax'); }
    get validForKSL() { return this.branchDetailForm.get('validForKSL'); }
    get primaryUrl() { return this.branchDetailForm.get('primaryUrl'); }
    get hideFromCustomers() { return this.branchDetailForm.get('hideFromCustomers'); }
    get hideFromSales() { return this.branchDetailForm.get('hideFromSales'); }

    isLunchOpeningTimeHasError(day) {
        let dayFormErrors = this.dayForm(day).errors || {};
        const { lunchTimeStartRequired, invalidLunchStartTime } = dayFormErrors;

        let lunchOpenErrors = this.lunchOpeningTime(day).errors || {};
        const { invalidValue } = lunchOpenErrors;

        return lunchTimeStartRequired ||
            invalidLunchStartTime ||
            invalidValue;
    }

    isLunchClosingTimeHasError(day) {
        let dayFormErrors = this.dayForm(day).errors || {};
        const { lunchTimeEndRequired, invalidLunchEndTime } = dayFormErrors;

        let lunchCloseErrors = this.lunchClosingTime(day).errors || {};
        const { invalidValue } = lunchCloseErrors;

        return lunchTimeEndRequired ||
            invalidValue && this.isInputFocusing('LUNCH_CLOSING_HOUR', day) ||
            invalidLunchEndTime;
    }

    isOpeningTimeHasError(day) {
        let dayFormErrors = this.dayForm(day).errors || {};
        const { invalidTime, invalidOpeningTime, openingTimeRequired } = dayFormErrors;

        let openingFormErrors = this.openingTime(day).errors || {};
        const { invalidValue } = openingFormErrors;

        return openingTimeRequired ||
            (invalidTime && this.isInputFocusing('OPENING_HOUR', day)) ||
            invalidValue ||
            (invalidOpeningTime);
    }

    isClosingTimeHasError(day) {
        let dayFormErrors = this.dayForm(day).errors || {};
        const { invalidTime, invalidClosingTime, closingTimeRequired } = dayFormErrors;

        let closingFormErrors = this.closingTime(day).errors || {};
        const { invalidValue } = closingFormErrors;

        return closingTimeRequired ||
            (invalidTime && this.isInputFocusing('CLOSING_HOUR', day)) ||
            invalidValue || invalidClosingTime;
    }

    isInputFocusing(type, day) {
        return this.focusingTimeInput === `${type}.${day}`;
    }

    private buildForm(): void {
        const timeForms = {};
        this.dayInWeek.map(day => timeForms[day] = this.fb.group({
            openingTime: ['', [BOValidator.invalidValueValidator(TIME_REGEX, true)]],
            lunchOpeningTime: ['', [BOValidator.invalidValueValidator(LUNCH_TIME_REGIX)]],
            lunchClosingTime: ['', [BOValidator.invalidValueValidator(LUNCH_TIME_REGIX)]],
            closingTime: ['', [BOValidator.invalidValueValidator(TIME_REGEX, true)]],
        }, { validators: openingTimePickerValidator }));

        this.branchDetailForm = this.fb.group({
            branchNumber: [EMPTY_STRING, [Validators.required]],
            code: EMPTY_STRING,
            ...timeForms,
            addressDesc: EMPTY_STRING,
            addressStreet: EMPTY_STRING,
            addressCity: EMPTY_STRING,
            addressCountry: EMPTY_STRING,
            countryId: EMPTY_STRING,
            zipCode: EMPTY_STRING,
            orgId: EMPTY_STRING,
            regionId: EMPTY_STRING,
            primaryPhone: EMPTY_STRING,
            primaryFax: EMPTY_STRING,
            primaryEmail: [EMPTY_STRING, [invalidValueValidator(EMAIL_REGEX)]],
            validForKSL: false,
            primaryUrl: EMPTY_STRING,
            hideFromCustomers : false,
            hideFromSales: false
        });

    }

    private setUpDefaultValueForForm(): void {
        if (this.isCreatedNewBranch === CREATE_NEW_BRANCH) {
            this.getCountryListForBranch();
        } else {
            this.branchService.getBranch(this.branchNumber.value).subscribe(branchDetail => {
                if (branchDetail) {
                    this.branch = branchDetail;
                    this.setValuesForBranchDetailForm(branchDetail);
                    this.getCountryListForBranch();
                }
            }, error => {
                this.handleError(error);
            });
        }
    }

    private prepareBranchData(): Branch {
        const branchData = new Branch();
        branchData.branchNr = this.branchNumber.value;
        branchData.branchCode = this.branchCode.value;

        this.dayInWeek.map(day => {
            const openingTime = this.formatTimeToSubmit(this.openingTime(day).value);
            const closingTime = this.formatTimeToSubmit(this.closingTime(day).value);
            const lunchStartTime = this.formatTimeToSubmit(this.lunchOpeningTime(day).value);
            const lunchEndTime = this.formatTimeToSubmit(this.lunchClosingTime(day).value);

            let id;
            let branchId;
            if (this.isCreatedNewBranch === EDIT_SELECTED_BRANCH && this.branch) {
                const dayData = this.branch.branchOpeningTimes.filter(wssDay => wssDay.weekDay === day)[0];
                if (dayData) {
                    id = dayData.id;
                    branchId = dayData.branchId;
                }
            }


            if (openingTime && closingTime) {
                branchData.branchOpeningTimes.push(new BranchOpeningTime({
                    weekDay: day,
                    openingTime,
                    closingTime,
                    lunchStartTime,
                    lunchEndTime,
                    id,
                    branchId
                }))
            }
        })

        branchData.addressDesc = this.addressDesc.value;
        branchData.addressStreet = this.addressStreet.value;
        branchData.addressCity = this.addressCity.value;
        branchData.addressCountry = this.addressCountry.value;
        branchData.countryId = this.countryId.value.value;
        branchData.zip = this.zipCode.value;

        branchData.orgId = this.orgId.value;
        branchData.regionId = this.regionId.value;
        branchData.primaryPhone = this.primaryPhone.value;
        branchData.primaryEmail = this.primaryEmail.value;
        branchData.primaryFax = this.primaryFax.value;
        branchData.validForKSL = this.validForKSL.value;
        branchData.primaryUrl = this.primaryUrl.value;
        branchData.hideFromCustomers = this.hideFromCustomers.value;
        branchData.hideFromSales = this.hideFromSales.value;

        return branchData;
    }

    private setValuesForBranchDetailForm(branchDetail: BranchModel): void {
        this.branchCode.setValue(branchDetail.branchCode);
        this.addressDesc.setValue(branchDetail.addressDesc);
        this.addressStreet.setValue(branchDetail.addressStreet);
        this.addressCity.setValue(branchDetail.addressCity);
        this.addressCountry.setValue(branchDetail.addressCountry);

        this.zipCode.setValue(branchDetail.zip);
        this.orgId.setValue(branchDetail.orgId);
        this.regionId.setValue(branchDetail.regionId);
        this.primaryPhone.setValue(branchDetail.primaryPhone);
        this.primaryFax.setValue(branchDetail.primaryFax);
        this.primaryEmail.setValue(branchDetail.primaryEmail);
        this.validForKSL.setValue(branchDetail.validForKSL);
        this.primaryUrl.setValue(branchDetail.primaryUrl);
        this.hideFromCustomers.setValue(branchDetail.hideFromCustomers);
        this.hideFromSales.setValue(branchDetail.hideFromSales);

        (branchDetail.branchOpeningTimes || []).forEach(dayTime => {
            this.openingTime(dayTime.weekDay).setValue(this.formatTimePicker(dayTime.openingTime));
            this.lunchOpeningTime(dayTime.weekDay).setValue(this.formatTimePicker(dayTime.lunchStartTime));
            this.lunchClosingTime(dayTime.weekDay).setValue(this.formatTimePicker(dayTime.lunchEndTime));
            this.closingTime(dayTime.weekDay).setValue(this.formatTimePicker(dayTime.closingTime));
        });
    }

    private handleError(error: any): void {
        this.isCreatedFail = true;
        if (!error) {
            this.noticeMessage = 'BRANCHES.BRANCH_DETAIL_FORM.CREATE_COMMON_ERROR_MESSAGE';
            return;
        }

        const errorCode = get(error, 'error.error_code') || get(error, 'error_code');

        switch (errorCode) {
            case 'DUPLICATED_BRANCH_NUMBER':
            case 'DUPLICATED_BRANCH_CODE':
                this.noticeMessage = 'BRANCHES.BRANCH_DETAIL_FORM.CREATE_DUPLICATION_ERROR_MESSAGE';
                break;
            case 'MISSING_OPENING_TIME':
                this.noticeMessage = 'BRANCHES.BRANCH_DETAIL_FORM.MISSING_OPENING_TIME';
                break;
            default:
                this.noticeMessage = 'BRANCHES.BRANCH_DETAIL_FORM.CREATE_COMMON_ERROR_MESSAGE';
                break;
        }
    }

    private formatTimePicker(time: string): string {
        return time ? time.substr(0, 5) : EMPTY_STRING;
    }

    private formatTimeToSubmit(time: string) {
        return time ? `${time}:00` : EMPTY_STRING;
    }

    private resetMessageStyle() {
        this.isCreatedFail = false;
        this.isCreatedSuccessful = false;
    }

    private validatePastedData(formatedValue: string, controlName: string) {
        this.resetMessageStyle();
        switch (controlName) {
            case PASTED_ORG_FIELD:
                this.isPastedOrgIdInvalid = !formatedValue.match(ORG_ID_REGEX);
                break;
            case PASTED_BRANCH_NUMBER_FIELD:
                this.isPastedBranchNumberInvalid = !formatedValue.match(BRANCH_NUMBER_REGEX);
                break;
            case PASTED_EMAIL_FIELD:
                this.isPastedEmailInvalid = !formatedValue.match(EMAIL_REGEX);
                break;
        }
    }

    private getCountryListForBranch() {
        this.branchService.getCountryListForBranch().subscribe(res => {
            const countries = [];
            this.countries = countries.concat(
                res.map(item => ({ value: item.id.toString(), label: item.shortCode }))
            );
            this.setCountrySelectValue();

        }, error => {
            this.handleError(error);
        });
    }

    private setCountrySelectValue() {
        let countryValue = this.branch && this.branch.countryId && this.countries.find(country => {
            return country.value === this.branch.countryId.toString();
        });

        countryValue = countryValue ? countryValue : {
            value: EMPTY_STRING,
            label: EMPTY_STRING,
        } as CountryBranchModel;

        this.countryId.setValue(countryValue);
    }
}
