
import { WHITE_SPACE } from '../../../../core/conts/app.constant';
import { BaseModel } from 'src/app/shared/models/base.model';

export class AffiliateRequestModel extends BaseModel {
    public affiliate: string = WHITE_SPACE;

    constructor(data?: any) {
        super(data);
    }
}

export class AffiliateInfoModel {
    public description: string;
    public name: string;
    public orgCode: string;
    public shortName: string;
    public vat: number;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.description = data.description;
        this.name = data.name;
        this.orgCode = data.orgCode;
        this.shortName = data.shortName;
        this.vat = data.vat;
    }
}

