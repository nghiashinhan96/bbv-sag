import { Injectable } from '@angular/core';
import { ProfileTabModel } from '../models/profile-tab.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Injectable()
export class ProfileBusinessService {
    private readonly INVOICE_HISTORY = 'INVOICE_HISTORY';
    private readonly USED_INVOICE_HISTORY = 'USED_INVOICE_HISTORY';

    constructor() { }

    getSettingTabList(userDetail: UserDetail): ProfileTabModel[] {
        const isCz = AffiliateUtil.isCz(environment.affiliate);
        const isSb = AffiliateUtil.isSb(environment.affiliate);
        return [
            {
                routerLink: 'profile',
                routerText: 'SETTINGS.PROFILE_TAB',
                isShown: true
            },
            {
                routerLink: 'order-history',
                routerText: 'SETTINGS.ORDER_HISTORY_TAB',
                isShown: !!userDetail && !userDetail.isFinalUserRole
            },
            {
                routerLink: 'final-user-orders',
                routerText: 'SETTINGS.FINAL_USER_ORDER_TAB',
                isShown: !!userDetail && userDetail.isFinalUserRole && !isCz
            },
            {
                routerLink: 'invoices',
                routerText: 'COMMON_LABEL.INVOICE_ARCHIVE',
                isShown: this.checkPermission(userDetail, this.INVOICE_HISTORY, this.USED_INVOICE_HISTORY) && !isCz && !isSb
            },
            {
                routerLink: 'analytical-card',
                routerText: 'SETTINGS.ANALYTICAL_CARD.TITLE',
                isShown: this.checkPermission(userDetail, this.INVOICE_HISTORY, this.USED_INVOICE_HISTORY) && isSb
            },
            {
                routerLink: 'administrator',
                routerText: 'SETTINGS.USER_ADMINISTRATION_TAB',
                isShown: !!userDetail && userDetail.userAdminRole && !userDetail.isSalesOnBeHalf
            },
            {
                routerLink: 'final-user-admin',
                routerText: 'SETTINGS.USER_ADMINISTRATION_TAB',
                isShown: !!userDetail && userDetail.finalCustomer && userDetail.finalUserAdminRole
            },
            {
                routerLink: 'configuration',
                routerText: 'SETTINGS.CONFIG',
                isShown: !!userDetail && (!userDetail.salesUser || userDetail.isSalesOnBeHalf)
            }
        ];
    }

    private checkPermission(userDetail: UserDetail, permissionName: string, functionName: string) {
        if (!userDetail) {
            return false;
        }

        const permission = userDetail.permissions.find((pers) => pers.permission === permissionName);
        if (permission) {
            const funcName = permission.functions.find(func => func.functionName === functionName);
            return !!funcName;
        }
        return false;
    }
}
