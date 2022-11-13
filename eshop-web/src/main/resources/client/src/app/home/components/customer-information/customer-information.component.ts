import { Component, OnInit, Input } from '@angular/core';

import { UserDetail } from 'src/app/core/models/user-detail.model';
import { CustomerModel } from 'src/app/core/models/customer.model';
import { CreditLimitService, AxCreditLimit } from 'src/app/core/services/credit-limit.service';
import { UserAddress } from 'src/app/core/models/user-address.model';
import { AX_PAYMENT_TYPE, PAYMENT_METHOD } from 'src/app/core/enums/shopping-basket.enum';
import { NOT_AVAILABLE, HYPHEN } from 'src/app/core/conts/app.constant';
import { CustomerInfoModel } from '../../models/customer-info.model';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';


const i18nYes = 'COMMON_LABEL.YES';
const i18nNo = 'COMMON_LABEL.NO';

@Component({
    selector: 'connect-customer-information',
    templateUrl: './customer-information.component.html',
    styleUrls: ['./customer-information.component.scss']
})
export class CustomerInformationComponent implements OnInit {
    @Input() set userDetail(val: UserDetail) {
        if (val) {
            this.customerInfo = this.build(val);
        }
    }
    customerInfo;
    isShownMore = false;
    phoneContact;
    emailContact;
    isCoppied = false;
    orderLocation: any;
    isSb = AffiliateUtil.isSb(environment.affiliate);
    isCoppiedEmail = false;

    constructor(
        private creditLimitService: CreditLimitService
    ) { }

    ngOnInit() { }

    openEmailApp(event) {
        const email = event && event.value || '';
        if(!email && email !== NOT_AVAILABLE) {
            return;
        }

        window.location.href = `mailto:${email}`;
    }

    coppied($event, isPhone = true) {
        const listener = (e: ClipboardEvent) => {
            // tslint:disable-next-line: no-string-literal
            const clipboard = e.clipboardData || window['clipboardData'];
            if (isPhone) {
                clipboard.setData('text', this.phoneContact);
            } else {
                clipboard.setData('text', this.emailContact);
            }
            e.preventDefault();
        };
        if ((isPhone && !this.phoneContact) || (!isPhone && !this.emailContact)) {
            return;
        }
        document.addEventListener('copy', listener, false);
        document.execCommand('copy');
        document.removeEventListener('copy', listener, false);
        this.isCoppied = isPhone;
        this.isCoppiedEmail = !isPhone;
        setTimeout(() => {
            this.isCoppied = false;
            this.isCoppiedEmail = false;
        }, 1000);
    }

    build(userDetail: UserDetail) {

        const customerData = new CustomerInfoModel();
        const customer = userDetail.customer;
        const defaultAddress = userDetail.defaultAddress;
        const deliveryAddress = userDetail.deliveryAddress;
        const invoiceAddress = userDetail.invoiceAddress;
        customerData.customerDescription = this.getShortCustomerDesc(customer, defaultAddress);
        if (customer) {
            customerData.hasCustomer = true;
            customerData.customerNr = customer.nr.toString();
            customerData.customerName = customer.name;
            customerData.companyName = customer.companyName;
            customerData.customerEmail = HYPHEN;
            customerData.customerPhone = HYPHEN;
            customerData.customerPaymentType = this.getCustomerPaymentType(userDetail);
            customerData.customerPaymentTypeDesc = 'CONDITION.PAYMENT_METHOD.' + customerData.customerPaymentType;
            customerData.deliveryCustomerSendMethod = customer.sendMethodCode;
            customerData.deliveryCustomerSendMethodDesc = 'CONDITION.COLLECTIVE_DELIVERY.' + customer.sendMethodCode;
            customerData.shippingCustomerSendMethod = customer.sendMethodCode;
            customerData.shippingCustomerSendMethodDesc = 'CONDITION.DELIVERY_TYPE.' + customer.sendMethodCode;
            customerData.axInvoiceType = customer.axInvoiceType;
            customerData.invoiceCustomerType = 'CONDITION.INVOICE_TYPE.' + customer.invoiceTypeCode;
            customerData.invoiceCustomerTypeDesc = 'CONDITION.INVOICE_TYPE.' + customer.invoiceTypeCode;
            customerData.invoiceCustomerPaymentType = customerData.customerPaymentType;
            customerData.affiliateShortName = customer.affiliateShortName;
            customerData.affiliateName = customer.affiliateName;
            customerData.customerCategory = customer.category;
            customerData.salesRepPersonalNumber = customer.salesRepPersonalNumber;
            customerData.salesGroup = customer.salesGroup;
            customerData.termOfPayment = customer.termOfPayment;
            customerData.cashDiscount = customer.cashDiscount;
            customerData.comment = customer.comment;

            const creditLimitInfo = this.creditLimitService.axCreditInfo as AxCreditLimit;
            customerData.creditLimitAvailable = creditLimitInfo.availableCredit;
            customerData.alreadyUsedCredit = creditLimitInfo.alreadyUsedCredit;
            const settings = userDetail.settings;
            if (settings) {
                customerData.sendNotiMail = settings.emailOrderConfirmation;
                customerData.sendNotiMailDesc = settings.emailOrderConfirmation ? i18nYes : i18nNo;
                customerData.displayNetPrice = settings.netPriceView ? i18nYes : i18nNo;
                customerData.viewBilling = settings.viewBilling ? i18nYes : i18nNo;
                customerData.netPriceConfirm = settings.netPriceConfirm ? i18nYes : i18nNo;
            }

            if (customer.branch) {
                customerData.branchId = customer.branch.branchId;
                customerData.branchName = customer.branch.branchName;
            }

            if (customer.phoneContacts) {
                customerData.phoneContacts = this.sortByPrimary(customer.phoneContacts || []);
                if (customerData.phoneContacts.length > 0) {
                    this.phoneContact = customerData.phoneContacts[0].value;
                }
            }

            if (customer.emailContacts) {
                customerData.emailContacts = this.sortByPrimary(customer.emailContacts || []);
                if (customerData.emailContacts.length > 0) {
                    this.emailContact = customerData.emailContacts[0].value;
                }
            }

            customerData.faxContacts = this.sortByPrimary(customer.faxContacts || []);

            const primaryFaxContact = customerData.faxContacts.find(contact => contact.type === 'EMAIL');

            if (primaryFaxContact) {
                customerData.deliveryCustomerFax = primaryFaxContact.value;
            } else {
                customerData.deliveryCustomerFax = 'AX_CONNECTION.OTHER';
            }

            if (userDetail.orderLocations) {
                this.orderLocation = userDetail.orderLocations[0];
                customerData.orderLocations = userDetail.orderLocations;
            }
        }

        customerData.customerAffiliate = customer.companyName || '';

        if (defaultAddress) {
            customerData.defaultCustomerFullAddress = defaultAddress.getFullAddress();
        }

        if (deliveryAddress) {
            customerData.deliveryCustomerFullAddress = deliveryAddress.getFullAddress();
            customerData.deliveryCustomerDescription = this.getShortCustomerDesc(customer, deliveryAddress);
        } else {
            customerData.deliveryCustomerFullAddress = customerData.defaultCustomerFullAddress;
        }

        if (invoiceAddress) {
            customerData.invoiceCustomerFullAddress = invoiceAddress.getFullAddress();
        }

        return customerData;
    }

    private getShortCustomerDesc(customer: CustomerModel, custAddress: UserAddress) {
        if (!customer) {
            return '';
        }

        let shortDesc = '';
        if (customer.nr) {
            shortDesc += `${customer.nr} - `;
        }
        if (customer.companyName) {
            shortDesc += `${customer.companyName}, `;
        }
        if (custAddress) {
            shortDesc += custAddress.city;
        }
        return shortDesc;
    }

    private getCustomerPaymentType(userDetail: UserDetail) {
        if (userDetail.customer.axPaymentType === AX_PAYMENT_TYPE.SOFORT) {
            return PAYMENT_METHOD.DIRECT_INVOICE;
        }
        return userDetail.customer.cashOrCreditTypeCode;
    }

    private sortByPrimary(data: any[]) {
        if (data && data.length > 0) {
            return data.sort((a, b) => b.primary - a.primary);
        }
        return data;
    }
}
