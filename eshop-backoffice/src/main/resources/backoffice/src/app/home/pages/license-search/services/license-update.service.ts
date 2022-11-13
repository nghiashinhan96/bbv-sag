import { Injectable } from '@angular/core';
import { IAngularMyDpOptions, IMyDateModel } from 'angular-mydatepicker';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { CustomerService } from 'src/app/home/services/customer/customer.service';
import { Observable } from 'rxjs';
import { LICENSE_DATE_TYPE } from '../enums/license.enum';

@Injectable()
export class LicenseUpdateService {
    private datePickerCommonSetting;

    constructor(
        private customerService: CustomerService
    ) {
        this.datePickerCommonSetting = DatePickerUtil.commonSetting;
    }

    initDateValidityConstraints(input, type: LICENSE_DATE_TYPE): any {
        const datePickerSetting: IAngularMyDpOptions = {};
        Object.assign(datePickerSetting, this.datePickerCommonSetting);
        if (type === LICENSE_DATE_TYPE.BEGIN) {
            datePickerSetting.disableUntil = input.singleDate.date;
        } else {
            datePickerSetting.disableSince = input.singleDate.date;
        }
        return datePickerSetting;
    }

    changeDate(date, dateChanged, type) {
        const copy = JSON.parse(JSON.stringify(date));
        if (type === LICENSE_DATE_TYPE.BEGIN) {
            copy.disableUntil = dateChanged;
        } else {
            copy.disableSince = dateChanged;
        }
        return copy;
    }

    updateLicense(data): Observable<any> {
        const licenseId = data.id;
        return this.customerService.updateLicense(data, licenseId);
    }

    deleteLicense(licenseId): Observable<any> {
        return this.customerService.deleteLicense(licenseId);
    }

    buildDataDatePicker({ date }) {
        return {
            isRange: false,
            singleDate: {
                date: {
                    year: date.year,
                    month: date.month,
                    day: date.day
                }
            }
        } as IMyDateModel;
    }
}
