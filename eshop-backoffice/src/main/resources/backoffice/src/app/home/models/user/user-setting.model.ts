export class UserSetting {
    public userId: number;
    public userName: string;
    public salutationId: number;
    public companyName: string;
    public orgCode: string;

    public firstName: string;
    public lastName: string;
    public email: string;
    public telephone: string;
    public fax: string;
    public languageId: number;
    public typeId: number;
    public hourlyRate: number;
    public emailNotificationOrder: boolean;
    public netPriceConfirm: boolean;
    public netPriceView: boolean;
    public showDiscount: boolean;

    public viewBilling = false;
    public deliveryId: number;
    public collectiveDelivery: number;
    public paymentId: number;
    public externalCustomerName: string;
    public externalUserName: string;
    public isUserActive: boolean;
    public affiliate: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        const userSettingsDto = data.userSettingsDto;
        this.userName = data.userName;
        this.salutationId = data.salutationId.toString();
        this.companyName = data.companyName;
        this.orgCode = data.orgCode;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.email = data.email;
        this.telephone = data.telephone;
        this.fax = data.fax;
        this.languageId = data.languageId.toString();
        this.typeId = data.typeId.toString();
        this.hourlyRate = data.hourlyRate;
        this.emailNotificationOrder = userSettingsDto.emailNotificationOrder;
        this.netPriceConfirm = userSettingsDto.netPriceConfirm;
        this.netPriceView = userSettingsDto.netPriceView;
        this.showDiscount = userSettingsDto.showDiscount;
        this.paymentId = userSettingsDto.paymentId;
        this.deliveryId = userSettingsDto.deliveryId;
        this.collectiveDelivery = userSettingsDto.collectiveDelivery;
        this.externalCustomerName = data.externalCustomerName;
        this.externalUserName = data.externalUserName;
        this.isUserActive = data.isUserActive;
        this.affiliate = data.affiliate;
        return this;
    }
}

