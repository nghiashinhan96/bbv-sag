import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';

import { BsModalRef } from 'ngx-bootstrap/modal';
import { EMPTY_STR, IMyDateModel } from 'angular-mydatepicker';
import { forkJoin, Observable, of, timer } from 'rxjs';
import { SagMessageData, SagValidator } from 'sag-common';
import { Router } from '@angular/router';
import { finalize, take } from 'rxjs/operators';

import { AffiliateModel } from '../../models/affiliate.model';
import { BranchModel } from '../../models/branch.model';
import { CountryModel } from '../../models/country.model';
import { OpeningDayModel, OpeningDayRequestModel, OpeningDayRequest, OpeningDayException } from '../../models/opening-day.model';
import { WorkingDayCodeModel } from '../../models/working-day-code.model';
import { OpeningDayCalendarService } from '../../services/opening-day-calendar.service';
import { DateUtil } from 'src/app/core/utils/date.util';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Component({
    selector: 'connect-opening-day-form-modal',
    templateUrl: './opening-day-form-modal.component.html',
    styleUrls: ['./opening-day-form-modal.component.scss']
})
export class OpeningDayFormModalComponent implements OnInit {

    @Input() openingId: string;

    OPENING_DAY_ROUTE = 'wholesaler/opening-day';
    TIME_DURATION = 2000;
    DELIVERY_ID: RegExp = /^[0-9]{1,19}$|^$/;

    openingDay: OpeningDayModel;
    countries: CountryModel[];
    branches: BranchModel[];
    workingCodes: WorkingDayCodeModel[];

    noticeMessage: SagMessageData;
    isCreated: boolean;
    onlyNumberHint: string;

    openingDayForm: FormGroup;
    datePickerCommonSetting = DateUtil.getCommonSetting();
    isExceptionEnabled = false;
    selectedCountry: string;
    selectedWorkingCode: string;
    exceptDelvryAddressId: string[];
    selectedWorkingId: string;
    selectedBranchNumbers: string[];
    selectedExceptionWorkingId: string;
    addressIds = [];
    locale = '';

    onClose: () => void;

    private selectedExceptionWorkingCode: string;
    private addressInitialId = 0;
    private branchCodes = [];

    constructor(
        public bsModalRef: BsModalRef,
        private fb: FormBuilder,
        private openingDayService: OpeningDayCalendarService,
        private router: Router,
    ) { }

    getOpeningDayData(): Observable<any> {
        const id = this.openingId;
        let openingDay = of(null);
        if (id) {
            openingDay = this.openingDayService.getOpeningDay(id);
        }
        const countries = this.openingDayService.getAllCountries();
        const workingCodes = this.openingDayService.getWorkingDayCode();
        const branches = this.openingDayService.getBranches();
        return forkJoin([openingDay, countries, branches, workingCodes]);
    }

    ngOnInit() {
        this.onlyNumberHint = 'OPENING_DAY.OPENING_DAY_FORM.ONLY_NUMBER';
        this.getOpeningDayData()
            .pipe(
                finalize(() => {
                    this.buildForm();
                    this.setFormData();
                    this.toggleException();
                })
            )
            .subscribe((data: { openingDay: OpeningDayModel }) => {
                if (!data) {
                    return;
                }
                this.openingDay = data[0] ? data[0] : null;
                this.countries = data[1];
                this.branches = data[2];
                this.workingCodes = data[3];
            }, (error)=>{
                console.log(error)
            });
    }

    save({ request, form }) {
        SpinnerService.start('connect-opening-day-form-modal');
        this.openingDayService.createOpeningDay(request)
        .pipe(
            finalize(()=>{
                SpinnerService.stop('connect-opening-day-form-modal');
            })
        )
        .subscribe(res => {
            this.isCreated = true;
            this.noticeMessage = { type: 'SUCCESS', message: 'SETTINGS.MESSAGE_SUCCESSFUL' };
            this.initiateTimer(this.TIME_DURATION);
        }, ({error}) => {
            this.handleError(error);
            form.enable();
            form.get('exceptionGroup').disable();
        });
    }

    update({ request, form }) {
        SpinnerService.start('connect-opening-day-form-modal');
        this.openingDayService.updateOpeningDay(request)
        .pipe(
            finalize(()=>{
                SpinnerService.stop('connect-opening-day-form-modal');
            })
        )
        .subscribe(res => {
            this.isCreated = true;
            this.noticeMessage = { type: 'SUCCESS', message: 'SETTINGS.MESSAGE_SUCCESSFUL' };
            this.initiateTimer(this.TIME_DURATION);
        }, ({error}) => {
            this.handleError(error);
            form.enable();
        });
    }

    navigate(path: string) {
        this.router.navigate([path]);
    }

    toggleException($event?) {
        if ($event) {
            this.isExceptionEnabled = !this.isExceptionEnabled;
        }
        if (this.isExceptionEnabled) {
            this.updateValidatorForWorkingCode();
            this.exceptionGroup.enable();
            this.exceptionGroup.setValidators([this.exceptionValidator]);
            this.exceptionGroup.updateValueAndValidity();
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

    onSelectedBranch($event: any) {
        this.selectedBranchNumbers = this.selectedBranchNumbers ? [...this.selectedBranchNumbers, $event.branch] : [$event.branch];
        this.branchCodes = this.branches.filter(branch => this.selectedBranchNumbers.includes((branch as any).branch));
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

    onClearBranchs() {
        this.selectedBranchNumbers = [];
        this.branchCodes = [];
        this.exceptionGroup.patchValue({
            branchCode: []
        });
    }

    onSelectedWorkingCode() {
        const selectedValue = this.openingDayForm.get('workingCode').value;
        const selectedWC = this.workingCodes.find(c => c.value === selectedValue);

        if (!selectedWC) {
            return;
        }

        this.selectedWorkingCode = selectedWC.originalCode;
        this.selectedWorkingId = selectedWC.value;
        if (this.isExceptionEnabled) {
            this.updateValidatorForExcpWorkingCode();
        }
    }

    onSelectedExceptionWorkingCode(event: any) {
        const selectedValue = this.exceptionGroup.get('exceptionWorkingCode').value;
        const selectedWC = this.workingCodes.find(c => c.value === selectedValue);

        if (!selectedWC) {
            return;
        }

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
                this.update({ request: openingDayRequest, form: this.openingDayForm });
            } else {
                this.save({ request: openingDayRequest, form: this.openingDayForm });
            }
        }
    }

    goBack() {
        this.navigate(this.OPENING_DAY_ROUTE);
    }

    onValidate(event: KeyboardEvent) {
        return SagValidator.ValidateNumber(event);
    }

    addSelectedAddress() {
        if (this.exceptDelvryAddressId) {
            this.addressIds.push({ value: this.addressInitialId++, label: this.exceptDelvryAddressId });
        }
    }

    removeSelectedAddress({ value, label }) {
        this.addressIds = this.addressIds.filter((address) => {
            return address.value !== value;
        });
    }

    trackByFn(index, item) {
        return index;
    }

    exceptionValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
        const branchCode = control.get('branchCode').value;
        if (!branchCode) {
            return { exceptionInvalid: true };
        }
        return null;
    };

    get fromDate() { return this.openingDayForm && this.openingDayForm.get('fromDate'); }
    get workingCode() { return this.openingDayForm && this.openingDayForm.get('workingCode'); }
    get exceptionGroup() { return this.openingDayForm && this.openingDayForm.get('exceptionGroup'); }

    set exceptionCheckbox(newValue) {
        if (this.openingDayForm) {
            this.openingDayForm.get('exceptionCheckbox').setValue(newValue);
        }
    }
    get exceptionCheckbox() { return this.openingDayForm && this.openingDayForm.get('exceptionCheckbox').value; }

    get branchCode() { return this.exceptionGroup && this.exceptionGroup.get('branchCode'); }

    private buildForm() {
        const countryValue = this.openingDay ? this.selectedCountry = this.openingDay.countryId.toString() : EMPTY_STR;
        const workingCodeValue = this.openingDay ? this.setWorkingIdFromCode(this.openingDay.workingDayCode, false) : EMPTY_STR;
        this.isExceptionEnabled = this.openingDay && this.openingDay.exceptions ? true : false;
        this.openingDayForm = this.fb.group({
            fromDate: [DateUtil.buildDataDatePicker(new Date()).singleDate.date, Validators.required],
            country: [countryValue, Validators.required],
            workingCode: [workingCodeValue, Validators.required],
            exceptionCheckbox: [this.isExceptionEnabled],
            exceptionGroup: this.fb.group({
                branchNumber: [EMPTY_STR],
                branchCode: [EMPTY_STR],
                exceptionWorkingCode: [EMPTY_STR],
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
                        year: date.getFullYear(),
                        month: date.getMonth() + 1,
                        day: date.getDate(),
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
        const formatDate = DateUtil.buildFullDateData(this.fromDate.value.singleDate.date);
        const openingDayRequest = new OpeningDayRequest(formatDate);
        openingDayRequest.id = this.openingDay ? this.openingDay.id : null;
        openingDayRequest.countryId = +this.selectedCountry;
        openingDayRequest.workingDayCode = this.selectedWorkingCode;
        return openingDayRequest;
    }

    private prepareExceptionData(): OpeningDayException {
        const openingDayException = new OpeningDayException();
        openingDayException.branches = this.selectedBranchNumbers ? this.selectedBranchNumbers : [];
        openingDayException.workingDayCode = this.selectedExceptionWorkingCode ? this.selectedExceptionWorkingCode : null;
        return openingDayException;
    }

    private extractException() {
        this.branchCodes = this.openingDay.exceptions.branches ?
            this.branches.filter(branch => this.openingDay.exceptions.branches.includes(branch.branch)) : [];
        this.selectedBranchNumbers = this.openingDay.exceptions.branches;
        this.branchCode.setValue(this.branchCodes.map(branch => branch.code).toString());
        this.setWorkingIdFromCode(this.openingDay.exceptions.workingDayCode, true);

        this.exceptionGroup.get('exceptionWorkingCode').setValue(this.selectedExceptionWorkingId);
        this.exceptionGroup.get('branchNumber').setValue(this.selectedBranchNumbers);
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
            const valid = nameRe.test(control.value ? control.value : EMPTY_STR);
            return valid ? null : { invalidValue: { value: control.value } };
        };
    }

    private workingCodeValidator(exceptionWorkingCode: string): ValidatorFn {
        if (!this.exceptionValidator) {
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

    private initiateTimer(duration: number) {
        return timer(duration).pipe(
            take(1)
        ).subscribe(() => {
            if (this.onClose) {
                this.bsModalRef.hide();
                this.onClose();
            }
        });
    }

    private handleError(error: any): void {
        this.isCreated = false;
        if (!error) {
            this.noticeMessage = { type: 'ERROR', message: 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY' };
            return;
        }
        switch (error.error_code) {
            case 'DUPLICATED_OPENING_DAYS':
                this.noticeMessage = { type: 'ERROR', message: 'OPENING_DAY.DUPLICATED_OPENING_DAYS' };
                break;
            default:
                this.noticeMessage = { type: 'ERROR', message: 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY' };
                break;
        }
    }
}
