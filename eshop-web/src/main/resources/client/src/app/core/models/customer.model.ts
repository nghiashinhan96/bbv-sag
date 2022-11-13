import { CustomerContactModel } from './customer-contact.model';
import { CustomerBranchModel } from './customer-branch.model';

export class CustomerModel {
    id: number;
    nr: number;
    companyId: number;
    shortName: string;
    companyName: string;
    lastName: string;
    name: string;
    active: boolean;
    vatNr: string;
    comment: string;
    languageId: number;
    language: string;
    organisationId: number;
    organisationShort: string;
    addressSalutation: string;
    statusShort: string;
    letterSalutation: string;
    freeDeliveryEndDate: any;
    currencyId: number;
    currency: string;
    oepUvpPrint: boolean;
    sendMethod: number;
    showDiscount: boolean;
    showNetPrice: boolean;
    cashOrCreditTypeCode: string;
    invoiceTypeCode: string;
    sendMethodCode: string;
    defaultBranchId: string;
    branch: CustomerBranchModel;
    affiliateShortName: string;
    affiliateName: string;
    category: string;
    termOfPayment: string;
    cashDiscount: string;
    salesRepPersonalNumber: string;
    salesGroup: string;
    phoneContacts: CustomerContactModel[];
    emailContacts: CustomerContactModel[];
    faxContacts: CustomerContactModel[];
    axInvoiceType: string;
    axSendMethod: string;
    // #2705: axPaymentType always get from ax service, not save to connect's DB
    axPaymentType: string;
    // _links?: any;
    token?: string;
    // for KSL
    isShopCustomer?: boolean;
    isOpenInfoBox?: boolean;
}
