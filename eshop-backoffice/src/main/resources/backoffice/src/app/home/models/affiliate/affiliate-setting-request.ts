import { BaseModel } from 'src/app/shared/models/base.model';

export class AffiliateSettingRequest extends BaseModel {
    shortName: string;
    vat: number;
}
