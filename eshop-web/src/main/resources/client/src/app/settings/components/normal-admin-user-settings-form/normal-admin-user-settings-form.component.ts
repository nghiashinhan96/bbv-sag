import { OnInit, Component, Input } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { SettingsService } from '../../services/settings.service';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { UserSetting } from 'src/app/core/models/user-setting.model';
import { PaymentSetting } from 'src/app/core/models/payment-settings.model';
import { CustomerSetting } from '../../models/customer-setting.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { finalize } from 'rxjs/operators';

@Component({
    selector: 'connect-normal-admin-user-settings-form',
    templateUrl: 'normal-admin-user-settings-form.component.html',
    styleUrls: ['normal-admin-user-settings-form.component.scss']
})
export class NormalAdminUserSettingsFormComponent implements OnInit {
    @Input() userId: number;
    @Input() selectedUsername: string;
    userDetail: UserDetail;
    userSetting: UserSetting;
    paymentSetting: PaymentSetting;
    customerSettings: CustomerSetting;
    isUserOpened = true;
    isAdminOpened = true;

    private readonly spinnerSelector = '.user-settings-form';

    constructor(
        private userService: UserService,
        private settingService: SettingsService
    ) {

    }

    ngOnInit() {
        this.userService.userDetail$
            .subscribe(userDetail => this.userDetail = userDetail);

        this.settingService.getUserSettingByAdmin(this.userId)
            .subscribe(setting => this.userSetting = new UserSetting(setting));

        this.settingService.getPaymentSettingByAdmin(this.userId)
            .subscribe(setting => this.paymentSetting = new PaymentSetting(setting));
    }

    updateUserSettings({ request, callback }) {
        SpinnerService.start(this.spinnerSelector);
        request.userId = this.userId;
        this.settingService.updateUserSettingByAdmin(request)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe(
                () => {
                    callback({ type: 'SUCCESS', message: 'SETTINGS.PROFILE.MESSAGE_SUCCESSFUL' });
                },
                () => {
                    callback({ type: 'ERROR', message: 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY' });
                }
            );
    }
}
