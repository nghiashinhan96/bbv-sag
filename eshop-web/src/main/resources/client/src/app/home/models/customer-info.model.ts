import { HYPHEN, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';

export class CustomerInfoModel {
    hasCustomer = false;
    customerNr = HYPHEN;
    customerName = HYPHEN;
    companyName = HYPHEN;
    customerDescription = HYPHEN;
    customerEmail = HYPHEN;
    customerPhone = HYPHEN;
    customerFax = NOT_AVAILABLE;
    customerAffiliate = HYPHEN;
    customerPaymentType = HYPHEN;
    customerPaymentTypeDesc = HYPHEN;
    sendNotiMail = false;
    sendNotiMailDesc = NOT_AVAILABLE;

    creditLimitAvailable: any = HYPHEN;
    alreadyUsedCredit: any = HYPHEN;

    workPhoneSeller = NOT_AVAILABLE;
    personalPhoneSeller = NOT_AVAILABLE;
    customerCategory = NOT_AVAILABLE;
    personalSellerEmail = NOT_AVAILABLE;
    salesGroup = HYPHEN;
    termOfPayment = HYPHEN;
    cashDiscount = HYPHEN;
    netPriceConfirm = HYPHEN;
    comment = HYPHEN;
    // Default address
    defaultCustomerFullAddress = HYPHEN;
    branchId = HYPHEN;
    branchName = HYPHEN;
    // Delivery
    deliveryCustomerFullAddress = HYPHEN;
    deliveryCustomerDescription = HYPHEN;
    deliveryCustomerEmail = NOT_AVAILABLE;
    deliveryCustomerPhone = NOT_AVAILABLE;
    deliveryCustomerFax = NOT_AVAILABLE;
    deliveryCustomerSendMethod = HYPHEN;
    deliveryCustomerSendMethodDesc = HYPHEN;
    shippingCustomerSendMethod = HYPHEN;
    shippingCustomerSendMethodDesc = HYPHEN;
    // Invoice address
    invoiceCustomerFullAddress = HYPHEN;
    invoiceCustomerType = HYPHEN;
    axInvoiceType = HYPHEN;
    invoiceCustomerTypeDesc = HYPHEN;
    invoiceCustomerDunningLevel = NOT_AVAILABLE;
    invoiceCustomerPaymentType = HYPHEN;
    invoiceCustomerPaymentTypeDesc = HYPHEN;
    // Settings
    viewBilling = HYPHEN;
    displayNetPrice = HYPHEN;
    affiliateShortName = HYPHEN;
    affiliateName = HYPHEN;
    salesRepPersonalNumber = HYPHEN;

    phoneContacts: any[];
    emailContacts: any[];
    faxContacts: any[];
    orderLocations: any[];
}
