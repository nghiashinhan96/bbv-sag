import { BaseModel } from 'src/app/shared/models/base.model';
import { SagAvailDisplaySettingModel, ExternalPartSettings } from 'sag-common';

export class AffiliateRequestModel extends BaseModel {
    public affiliate = ' ';

    constructor(data?: any) {
        super(data);
    }
}

export class AffiliateModel {
    public description: string;
    public name: string;
    public orgCode: string;
    public shortName: string;
    public vat: number;
    public customerAbsEnabled: boolean;
    public salesAbsEnabled: boolean;
    public ksoEnabled: boolean;
    public c4sBrandPriorityAvailFilter: string;
    public customerBrandPriorityAvailFilter: string;
    public vatTypeDisplay: string;
    public availSetting: SagAvailDisplaySettingModel;
    public customerBrandFilterEnabled: boolean;
    public salesBrandFilterEnabled: boolean;
    public disabledBrandPriorityAvailability: boolean;
    public externalPartSettings: ExternalPartSettings;
    public availDisplaySettings: SagAvailDisplaySettingModel[];
    public invoiceRequestAllowed: boolean;
    public invoiceRequestEmail: string;


    constructor(data?) {
        if (!data) {
            return;
        }
        this.shortName = data.shortName;
        this.name = data.name;
        this.description = data.description;
        this.orgCode = data.orgCode;
        this.vat = data.vat;
        this.customerAbsEnabled = data.customerAbsEnabled;
        this.salesAbsEnabled = data.salesAbsEnabled;
        this.ksoEnabled = data.ksoEnabled;
        this.c4sBrandPriorityAvailFilter = data.c4sBrandPriorityAvailFilter;
        this.customerBrandPriorityAvailFilter = data.customerBrandPriorityAvailFilter;
        this.customerBrandFilterEnabled = data.customerBrandFilterEnabled;
        this.salesBrandFilterEnabled = data.salesBrandFilterEnabled;
        this.disabledBrandPriorityAvailability = data.disabledBrandPriorityAvailability;
        this.vatTypeDisplay = data.vatTypeDisplay;

        if (data.availSetting) {
            this.availSetting = new SagAvailDisplaySettingModel(data.availSetting);
        }

        if (data.externalPartSettings) {
            this.externalPartSettings = data.externalPartSettings;
        }

        if(data.availDisplaySettings) {
            this.availDisplaySettings = (data.availDisplaySettings || []).map(item => new SagAvailDisplaySettingModel(item));
        }
        this.invoiceRequestAllowed = data.invoiceRequestAllowed;
        this.invoiceRequestEmail = data.invoiceRequestEmail;
    }
}
