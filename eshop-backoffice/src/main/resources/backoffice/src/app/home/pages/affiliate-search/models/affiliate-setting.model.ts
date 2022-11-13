import { AffiliateInfoModel } from './affiliate-info.model';
import { PermissionConfigurationModel } from './permission-configuration.model';

export class AffiliateSettingModel extends AffiliateInfoModel {
    perms: Array<PermissionConfigurationModel>;
}
