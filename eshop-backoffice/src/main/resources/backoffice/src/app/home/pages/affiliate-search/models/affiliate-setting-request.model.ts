import { BaseModel } from '../../../../shared/models/base.model';

export class AffiliateSettingRequest extends BaseModel {
  shortName: string;
  vat: number;
}
