import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';

import { IAngularMyDpOptions } from 'angular-mydatepicker';
import { Observable } from 'rxjs';

import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { BOValidator } from 'src/app/core/utils/validator';
import { CustomerService } from 'src/app/home/services/customer/customer.service';

@Injectable()
export class LicenceService {
    private datePickerCommonSetting;

    constructor(
        private formBuilder: FormBuilder,
        private customerService: CustomerService
    ) {
        this.datePickerCommonSetting = DatePickerUtil.commonSetting;
    }

    public buildLicenceForm(
        selectedLicenceType,
        fromDate,
        toDate,
        quantity
    ): FormGroup {
        return this.formBuilder.group({
            licenceType: new FormControl({ value: selectedLicenceType.value, label: selectedLicenceType.label }),
            fromDate: new FormControl(fromDate),
            toDate: new FormControl(toDate),
            quantity: new FormControl(quantity, [BOValidator.validateQuantity]),
        });
    }

    public initFromDateValidityConstraints(toDate): any {
        const datePickerFromSetting: IAngularMyDpOptions = {};
        Object.assign(datePickerFromSetting, this.datePickerCommonSetting);
        datePickerFromSetting.disableSince = toDate.singleDate.date;
        // datePickerFromSetting.editableDateField = true;
        return datePickerFromSetting;
    }

    public initToDateValidityConstraints(fromDate): any {
        const datePickerToSetting: IAngularMyDpOptions = {};
        Object.assign(datePickerToSetting, this.datePickerCommonSetting);
        datePickerToSetting.disableUntil = fromDate.singleDate.date;
        // datePickerToSetting.disableDateRanges = true;
        return datePickerToSetting;
    }

    public changeMaxDate(date, maxDate) {
        const copy = JSON.parse(JSON.stringify(date));
        copy.disableUntil = maxDate;
        return copy;
    }

    public changeMinDate(date, minDate) {
        const copy = JSON.parse(JSON.stringify(date));
        copy.disableSince = minDate;
        return copy;
    }

    public findQuantityLicenceByValue(licenceTypes, value) {
        const results = licenceTypes.filter((item) => item.value === value);
        if (results.length > 0) {
            return results[0].quantity;
        }
        return null;
    }

    public createCustomerLicense(data): Observable<any> {
        return this.customerService.createCustomerLicense(data);
    }

    public updateLicense(data, id): Observable<any> {
        return this.customerService.updateLicense(data, id);
    }
}
