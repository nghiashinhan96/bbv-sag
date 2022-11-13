import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/internal/operators/map';
import { CustomerSetting, CustomerSettingRequest } from '../models/customer-setting.model';
import { HttpClient } from '@angular/common/http';
import { UpdateUserSetting } from 'src/app/core/models/user-setting.model';
import { UserService } from 'src/app/core/services/user.service';

@Injectable({
    providedIn: 'root'
})
export class SettingsService {
    private readonly GET_CUSTOMER_SETTING_URL = 'customer/settings/default/';
    private readonly UPDATE_CUSTOMER_SETTING_URL = 'customer/settings/default/edit';
    private readonly UPDATE_USER_SETTING_URL = 'user/settings/update';

    private readonly BASE_URL = environment.baseUrl;

    constructor(
        private http: HttpClient,
        private userService: UserService
    ) { }

    getCustomerSettings() {
        const url = `${this.BASE_URL}${this.GET_CUSTOMER_SETTING_URL}`;
        return this.http.get(url).pipe(map((customerSettings) => new CustomerSetting(customerSettings)));
    }

    getUserSettingByAdmin(UserId: number) {
        const url = `${this.BASE_URL}users/${UserId}/settings`;
        return this.http.get(url);
    }

    getPaymentSettingByAdmin(UserId: number) {
        const url = `${this.BASE_URL}users/${UserId}/payment`;
        return this.http.get(url);
    }

    updateCustomerSettings(newSetting: CustomerSettingRequest) {
        const url = `${this.BASE_URL}${this.UPDATE_CUSTOMER_SETTING_URL}`;
        return this.http.put(url, newSetting);
    }

    updateUserSettings(newSetting: UpdateUserSetting) {
        const url = `${this.BASE_URL}${this.UPDATE_USER_SETTING_URL}`;
        return this.http.post(url, newSetting).pipe(
            map(data => {
                this.userService.updateSettings(data);
                this.userService.updateNetPriceSetting(data);
            })
        );
    }

    updateUserSettingByAdmin(data: any) {
        const url = `${this.BASE_URL}users/settings/update`;
        return this.http.post(url, data);
    }

    handleErrorMessage(error: any) {
        if (!error || !error.error_code) {
            return 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY';
        }
        switch (error.error_code) {
            case 'DUPLICATED_USERNAME_IN_AFFILIATE':
                return 'REGISTER.MESSAGES.INVALID_USERNAME_IN_AFFILIATE';
            case 'NOT_FOUND_EXTERNAL_CUSTOMER':
                return 'SETTINGS.USER_MANAGEMENT.MESSAGE.NOT_FOUND_EXTERNAL_CUSTOMER';
            case 'WRONG_OLD_PASSWORD':
                return 'SETTINGS.PROFILE.PASSWORD_CHANGE.WRONG_OLD_PASSWORD';
            default:
                return 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY';
        }
    }
}
