import { AffiliateUtil } from 'sag-common';
import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { environment } from 'src/environments/environment';

export class LoginLogoutEvent extends MetadataLogging {
    eventType = AnalyticEventType.LOGIN_LOGOUT_EVENT;
    loginLogout = false; // 1 for login, 0 for logout
    loginUserName: string;
    loginRole: string;
    countryCode = this.getCountryCode();
    loginTabSelected = 'Einloggen';
    loginSourceType: string;
    loginSourceProvider: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any) {
        super(metadata, user);
        if (data) {
            this.loginLogout = data.loginLogout; // 1 for login, 0 for logout
            this.loginUserName = user.username;
            this.loginRole = user.roleName;
            this.loginSourceType = data.loginSourceType;
            this.loginSourceProvider = data.loginSourceProvider;
        }
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            login_logout: this.loginLogout,
            login_user_name: this.loginUserName,
            login_role: this.loginRole,
            login_tab_selected: this.loginTabSelected,
            login_source_type: this.loginSourceType,
            login_source_provider: this.loginSourceProvider
        };
    }
}
