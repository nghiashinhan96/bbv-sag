export class ProfileModel {
    password: string;
    rePassword: string;
    vinCall: any;
    license: any;
    userName: string;
    surName: string;
    firstName: string;
    salutationId: any;
    salutations: any;
    email: string;
    phoneNumber: string;
    languages: any;
    languageId: any;
    types: any;
    typeId: any;
    id: any;
    hourlyRate: number;
    showNetPriceEnabled: boolean;
    netPriceView: boolean;
    netPriceConfirm: boolean;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.password = data.password;
        this.rePassword = data.rePassword;
        this.vinCall = data.vinCall;
        this.license = data.license;
        this.userName = data.userName;
        this.surName = data.surName;
        this.firstName = data.firstName;
        this.salutationId = data.salutationId;
        this.salutations = data.salutations;
        this.email = data.email;
        this.phoneNumber = data.phoneNumber;
        this.languages = data.languages;
        this.languageId = data.languageId;
        this.types = data.types;
        this.typeId = data.typeId;
        this.id = data.id;
        this.hourlyRate = data.hourlyRate;
        this.showNetPriceEnabled = data.showNetPriceEnabled;
        this.netPriceView = data.netPriceView;
        this.netPriceConfirm = data.netPriceConfirm;
    }
}