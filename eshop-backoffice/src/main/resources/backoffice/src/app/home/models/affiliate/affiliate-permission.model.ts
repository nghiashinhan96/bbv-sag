import { AffiliatePermissionItem } from './affiliate-permission-item.model';

export class AffiliatePermission {
    affiliateShortName: string;
    permissions: AffiliatePermissionItem[] = [];

    constructor(json?: AffiliatePermission) {
        if (!json) {
            return;
        }
        this.affiliateShortName = json.affiliateShortName;
        this.permissions = (json.permissions || []).map(item => new AffiliatePermissionItem(item));
    }
}
