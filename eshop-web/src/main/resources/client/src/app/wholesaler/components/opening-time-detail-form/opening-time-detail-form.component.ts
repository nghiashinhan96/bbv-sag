import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Constant, DAY_IN_WEEK, REGEX } from 'src/app/core/conts/app.constant';
import { Branch, CountryBranchModel, BranchModel, WssBranchOpeningTime } from '../../models/branch-opening-time.model';
import { BranchService } from '../../services/branch.service';
import { Validator } from 'src/app/core/utils/validator';
import {
    OPENING_HOUR, CLOSING_HOUR,
    LUNCH_OPENING_HOUR, LUNCH_CLOSING_HOUR,
    TIMEOUT, NUMBER_ONLY, ALPHANUMBERIC,
    TEXT,
    TIME_REGEX,
    LUNCH_TIME_REGIX,
    PASTED_ORG_FIELD,
    ORG_ID_REGEX,
    PASTED_BRANCH_NUMBER_FIELD,
    BRANCH_NUMBER_REGEX,
    PASTED_EMAIL_FIELD
} from '../../services/constant';
import { openingTimePickerValidator } from '../../services/opening-time-picker-validator';
import { DateUtil } from 'src/app/core/utils/date.util';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { finalize } from 'rxjs/operators';
import { get } from 'lodash';

@Component({
    selector: 'connect-opening-time-detail-form',
    templateUrl: './opening-time-detail-form.component.html',
    styleUrls: ['./opening-time-detail-form.component.scss']
})
export class OpeningTimeDetailFormComponent implements OnInit {

    @Input() isCreatedNewBranch: boolean;
    @Input() branchId: string;
    @Input() branchOpeningTime: string;
    @Input() branchClosingTime: string;

    onClose: () => void;

    branch: BranchModel;
    branchDetailForm: FormGroup;
    hourList = DateUtil.buildDataTimePicker();
    countries: CountryBranchModel[];

    showingTimePickerCode = '';

    isCreatedSuccessful: boolean;
    isCreatedFail: boolean;
    noticeMessage: string;
    requiredErrorMessage = 'BRANCHES.ERRORS.REQUIRED_ERROR_MESSAGE';
    invalidTimeErrorMessage = 'BRANCHES.ERRORS.INVALID_TIME_VALUE';

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
        private branchService: BranchService,
        private router: Router,
        private fb: FormBuilder,
        public bsModalRef: BsModalRef
    ) { }

    ngOnInit() {
        this.buildForm();
        if (this.branchId) {
            this.branchNumber.setValue(this.branchId);
        }
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
        if (this.isCreatedNewBranch) {
            SpinnerService.start('connect-opening-time-detail-form');
            this.branchService
                .createBranch(this.prepareBranchData())
                .pipe(
                    finalize(() => {
                        SpinnerService.stop('connect-opening-time-detail-form');
                    })
                )
                .subscribe((branches: BranchModel) => {
                    this.isCreatedSuccessful = true;
                    this.noticeMessage = 'BRANCHES.BRANCH_DETAIL_FORM.CREATE_SUCCESS_MESSAGE';
                    this.branchDetailForm.disable();
                    setTimeout(() => {
                        this.goBack();
                    }, TIMEOUT);
                }, ({ error }) => {
                    this.handleError(error);
                });
        } else if (!this.isCreatedNewBranch) {
            SpinnerService.start('connect-opening-time-detail-form');
            this.branchService.editBranch(this.prepareBranchData())
                .pipe(
                    finalize(() => {
                        SpinnerService.stop('connect-opening-time-detail-form');
                    })
                )
                .subscribe(res => {
                    this.isCreatedSuccessful = true;
                    this.noticeMessage = 'BRANCHES.BRANCH_DETAIL_FORM.UPDATE_SUCCESS_MESSAGE';
                    this.branchDetailForm.disable();
                    setTimeout(() => {
                        this.goBack();
                    }, TIMEOUT);
                }, ({ error }) => {
                    this.handleError(error);
                });
        }
    }

    goBack() {
        this.onClose();
        this.bsModalRef.hide();
    }

    validateUserInput(e: KeyboardEvent, maxLength: number, type: string) {
        this.noticeMessage = Constant.EMPTY_STRING;
        switch (type) {
            case NUMBER_ONLY:
                return Validator.validateNumberInput(e, maxLength);
            case ALPHANUMBERIC:
                return Validator.validateAlphanumeric(e, maxLength);
            case TEXT:
                return Validator.validateAlphanumericWithSpace(e, maxLength);
            default:
                return Validator.validateAlphanumericWithSpace(e, maxLength);
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

    get branchNumber() { return this.branchDetailForm && this.branchDetailForm.get('branchNumber'); }
    get branchCode() { return this.branchDetailForm && this.branchDetailForm.get('code'); }
    dayForm(day) { return this.branchDetailForm && this.branchDetailForm.get(day); }
    openingTime(day) { return this.branchDetailForm && this.branchDetailForm.get(day).get('openingTime'); }
    closingTime(day) { return this.branchDetailForm && this.branchDetailForm.get(day).get('closingTime'); }
    lunchOpeningTime(day) { return this.branchDetailForm && this.branchDetailForm.get(day).get('lunchOpeningTime'); }
    lunchClosingTime(day) { return this.branchDetailForm && this.branchDetailForm.get(day).get('lunchClosingTime'); }

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

    isRequireFilledValue(day, currentInput: string, targetInput: string) {
        if (this.branchDetailForm && this.branchDetailForm.get(day)) {
            if (!!this.branchDetailForm.get(day).get(targetInput).value) {
                return !this.branchDetailForm.get(day).get(currentInput).value;
            }
        }
        return false;
    }

    private buildForm(): void {

        const timeForms = {};
        this.dayInWeek.forEach(day => timeForms[day] = this.fb.group({
            openingTime: [Constant.EMPTY_STRING, [
                Validator.invalidValueValidator(TIME_REGEX, true)]
            ],
            lunchOpeningTime: [Constant.EMPTY_STRING, [Validator.invalidValueValidator(LUNCH_TIME_REGIX)]],
            lunchClosingTime: [Constant.EMPTY_STRING, [Validator.invalidValueValidator(LUNCH_TIME_REGIX)]],
            closingTime: [Constant.EMPTY_STRING, [
                Validator.invalidValueValidator(TIME_REGEX, true)]
            ],
        }, { validators: openingTimePickerValidator }));

        this.branchDetailForm = this.fb.group({
            branchNumber: [Constant.EMPTY_STRING, [Validators.required]],
            code: [Constant.EMPTY_STRING, [Validators.required]],
            ...timeForms
        });

    }

    private setUpDefaultValueForForm(): void {
        if (!this.isCreatedNewBranch) {
            this.branchService.getBranch(this.branchNumber.value).subscribe(branchDetail => {
                if (branchDetail) {
                    this.branch = branchDetail;
                    this.setValuesForBranchDetailForm(branchDetail);
                }
            }, ({ error }) => {
                this.handleError(error);
            });
        }
    }

    private prepareBranchData(): Branch {
        const branchData = new Branch();
        branchData.branchNr = this.branchNumber.value;
        branchData.branchCode = this.branchCode.value;

        this.dayInWeek.forEach(day => {
            const openingTime = this.formatTimeToSubmit(this.openingTime(day).value);
            const closingTime = this.formatTimeToSubmit(this.closingTime(day).value);
            const lunchStartTime = this.formatTimeToSubmit(this.lunchOpeningTime(day).value);
            const lunchEndTime = this.formatTimeToSubmit(this.lunchClosingTime(day).value);

            let id;
            let wssBranchId;
            if (!this.isCreatedNewBranch && this.branch) {
                const dayData = this.branch.wssBranchOpeningTimes.filter(wssDay => wssDay.weekDay === day)[0];
                if (dayData) {
                    id = dayData.id;
                    wssBranchId = dayData.wssBranchId;
                }
            }


            if (openingTime && closingTime) {
                branchData.wssBranchOpeningTimes.push(new WssBranchOpeningTime({
                    weekDay: day,
                    openingTime,
                    closingTime,
                    lunchStartTime,
                    lunchEndTime,
                    id,
                    wssBranchId
                }))
            }
        })

        return branchData;
    }

    private setValuesForBranchDetailForm(branchDetail: BranchModel): void {
        this.branchCode.setValue(branchDetail.branchCode);
        (branchDetail.wssBranchOpeningTimes || []).forEach(dayTime => {
            this.openingTime(dayTime.weekDay).setValue(this.formatTimePicker(dayTime.openingTime));
            this.lunchOpeningTime(dayTime.weekDay).setValue(this.formatTimePicker(dayTime.lunchStartTime));
            this.lunchClosingTime(dayTime.weekDay).setValue(this.formatTimePicker(dayTime.lunchEndTime));
            this.closingTime(dayTime.weekDay).setValue(this.formatTimePicker(dayTime.closingTime));
        })

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
        return time ? time.substr(0, 5) : Constant.EMPTY_STRING;
    }

    private formatTimeToSubmit(time: string) {
        return time ? `${time}:00` : Constant.EMPTY_STRING;
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
                this.isPastedEmailInvalid = !formatedValue.match(REGEX.EMAIL_REGEX);
                break;
        }
    }
}
