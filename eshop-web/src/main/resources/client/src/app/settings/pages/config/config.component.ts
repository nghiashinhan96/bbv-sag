import { Component, OnDestroy, OnInit, AfterViewInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { PaymentSetting } from 'src/app/core/models/payment-settings.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { UserSetting } from 'src/app/core/models/user-setting.model';
import { UserService } from 'src/app/core/services/user.service';
import { CustomerSetting } from '../../models/customer-setting.model';
import { SettingsService } from '../../services/settings.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { OciService } from 'src/app/oci/services/oci.service';

@Component({
    selector: 'connect-config',
    templateUrl: './config.component.html',
    styleUrls: ['./config.component.scss']
})
export class ConfigComponent implements OnInit, OnDestroy, AfterViewInit {
    userDetail: UserDetail;
    userSetting: UserSetting;
    paymentSetting: PaymentSetting;
    customerSettings: CustomerSetting;
    isUserOpened = true;
    isAdminOpened = true;

    isOciFlow: boolean;

    private destroy = new Subject();

    constructor(
        private userService: UserService,
        private settingService: SettingsService,
        private appStorage: AppStorageService,
        private ociService: OciService) { }

    ngOnInit() {
        this.userService.userDetail$
            .pipe(takeUntil(this.destroy))
            .subscribe(userDetail => {
                this.userDetail = userDetail;
                this.getCustomerSetting();
            });
        this.userService.userSetting$
            .pipe(takeUntil(this.destroy))
            .subscribe(setting => this.userSetting = setting);

        this.userService.userPaymentSetting$
            .pipe(takeUntil(this.destroy))
            .subscribe(payment => this.paymentSetting = payment);
    }

    ngOnDestroy() {
        this.destroy.complete();
        this.destroy.unsubscribe();
    }

    ngAfterViewInit(): void {
        this.isOciFlow = this.ociService.getOciState();
    }

    getCustomerSetting() {
        // If user is admin then get customer setting
        if (this.userDetail && this.userDetail.userAdminRole) {
            this.settingService.getCustomerSettings()
                .pipe(takeUntil(this.destroy))
                .subscribe(customerSetttings => this.customerSettings = customerSetttings);
        }
    }

    updateUserSettings({ request, callback }) {
        this.settingService.updateUserSettings(request).subscribe(() => {
            this.appStorage.classicViewMode = request.classicCategoryView === 'true';
            callback({ type: 'SUCCESS', message: 'SETTINGS.MESSAGE_SUCCESSFUL' });
        }, error => callback({ type: 'ERROR', message: 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY' }));
    }

    updateAdminSettings({ request, callback }) {
        this.settingService.updateCustomerSettings(request).subscribe(() => {
            callback({ type: 'SUCCESS', message: 'SETTINGS.MESSAGE_SUCCESSFUL' });
        }, error => callback({ type: 'ERROR', message: 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY' }));
    }
}
