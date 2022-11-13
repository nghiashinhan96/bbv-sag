import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ShoppingBasketContextModel } from '../../models/shopping-basket-context.model';
import { ShoppingConditionHeaderModel } from '../../models/shopping-condition-header.model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { PaymentSetting } from 'src/app/core/models/payment-settings.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { DeliveryType } from 'src/app/core/models/delivery-type.model';
import {
    ORDER_TYPE,
    DELIVERY_TYPE,
    ADDRESS_TYPE,
    INVOICE_TYPE,
    PAYMENT_METHOD,
    AX_SEND_METHOD,
    AX_PAYMENT_TYPE
} from '../../../core/enums/shopping-basket.enum';
import { CustomerBranchModel } from 'src/app/core/models/customer-branch.model';
import { OrderDashboardListItemModel } from 'src/app/order-dashboard/models/order-dashboard-list-item.model';
import { environment } from 'src/environments/environment';
import { AffiliateEnum, AffiliateUtil } from 'sag-common';

const EXCLUDED_BRANCHES = ['2000', '2001', '2114', '9998', 'DL', 'FI07SC'];
@Component({
    selector: 'connect-shopping-condition',
    templateUrl: './shopping-condition.component.html',
    styleUrls: ['./shopping-condition.component.scss']
})
export class ShoppingConditionComponent implements OnInit {
    conditionHeader: ShoppingConditionHeaderModel = new ShoppingConditionHeaderModel();
    isCounterBasketMode: boolean;

    settingForm: FormGroup;
    @Input() userDetail: UserDetail;
    DELIVERY_TYPE = DELIVERY_TYPE;
    defaultBranch: CustomerBranchModel;

    @Input() set context(val: ShoppingBasketContextModel) {
        this.basketContext = val;
        this.conditionHeader = new ShoppingConditionHeaderModel(val);
        this.isCounterBasketMode = this.checkCounterBasketMode(val);
        if (!this.basketContext.pickupBranch || !this.basketContext.pickupBranch.branchId) {
            // set default brach
            this.basketContext.pickupBranch = {
                branchId: this.userDetail.defaultBranchId,
                branchName: this.userDetail.defaultBranchName
            };
        }
        this.pathValue(val);
    }

    @Input() set paymentSetting(val: PaymentSetting) {
        if (!!val) {
            this.settings = val;
            if (this.isCz) {
                this.settings.paymentMethods =
                    (this.settings.paymentMethods || []).filter(item => item.descCode !== PAYMENT_METHOD.EUR_PAYMENT);
            }
            this.filterAddress(this.settings);
            // customer sales ooption
            this.initialSaleOptions();
        }
    }

    @Input() set deliveryInfo(val) {
        this.deliverySummary = val || {};
        if (val) {
            this.correctKSLMode();
        }
    }

    @Output() updateShoppingBasketContext = new EventEmitter<{
        body: ShoppingBasketContextModel;
        reload: boolean;
        done: () => void;
    }>();

    @Input() set allBranches(val: CustomerBranchModel[]) {
        if (val) {
            this.branchesForPickup = val.sort((b1: CustomerBranchModel, b2: CustomerBranchModel) => b1.branchName < b2.branchName ? -1 : 1);
            const branchId = this.basketContext && this.basketContext.pickupBranch && this.basketContext.pickupBranch.branchId || '';
            const isHiddenBrand = branchId && !this.branchesForPickup.some(item => item.branchId === branchId);
            if (!isHiddenBrand) {
                this.settingForm.get('branchId').setValue(branchId, { emitEvent: false });
                this.conditionChanged.emit(this.settingForm.value);
            }
        }
    }

    @Input() finalOrder: OrderDashboardListItemModel;

    @Output() conditionChanged = new EventEmitter();

    branchesForPickup = [];
    settings: PaymentSetting;
    deliveryAddress: any[] = [];
    billingAddress: any[] = [];
    deliverySummary: any;

    basketContext: ShoppingBasketContextModel;
    isShownDeliveryForceMessage: boolean;

    cz = AffiliateEnum.CZ;
    isCz = AffiliateUtil.isCz(environment.affiliate);

    constructor(private fb: FormBuilder) {
        this.settingForm = this.fb.group({
            invoiceId: '',
            paymentId: '',
            deliveryId: '',
            collectiveDeliveryId: '',
            deliveryAddressId: '',
            billingAddressId: '',
            branchId: ''
        });
        this.settingForm.valueChanges.subscribe(value => {
            this.conditionChanged.emit(value);
        });
    }


    ngOnInit() {
        this.isShownDeliveryForceMessage = this.userDetail &&
            this.userDetail.isSalesOnBeHalf &&
            this.userDetail.defaultBranchId &&
            this.userDetail.customer &&
            this.userDetail.customer.axSendMethod === AX_SEND_METHOD.TOUR &&
            this.userDetail.customer.axPaymentType === AX_PAYMENT_TYPE.BAR;

        this.settingForm.get('invoiceId').valueChanges.subscribe(invoiceType => {
            const invoice = this.settings.invoiceTypes.find(item => item.invoiceType === invoiceType);
            const updatedContext = new ShoppingBasketContextModel(this.basketContext);
            updatedContext.invoiceType = { ...invoice };
            // update context;
            this.updateContext(updatedContext);
        });
        this.settingForm.get('paymentId').valueChanges.subscribe(paymentId => {
            const payment = (this.settings.paymentMethods || []).find(type => type.id === paymentId);
            if (payment) {
                const updatedContext = new ShoppingBasketContextModel(this.basketContext);
                updatedContext.paymentMethod = { ...payment };
                // update context;
                this.updateContext(updatedContext, false, () => {
                    setTimeout(() => {
                        this.initialSaleOptions();
                    });
                });
            }
        });
        this.settingForm.get('deliveryId').valueChanges.subscribe(deliveryId => {
            const delivery = (this.settings.deliveryTypes || []).find(type => type.id === deliveryId);
            if (delivery) {
                const updatedContext = new ShoppingBasketContextModel(this.basketContext);
                if (delivery.id === -1) {
                    updatedContext.orderType = ORDER_TYPE.COUNTER;
                } else {
                    updatedContext.orderType = ORDER_TYPE.ORDER;
                    updatedContext.deliveryType = { ...delivery };
                }

                if(delivery.descCode === DELIVERY_TYPE.TOUR && this.defaultBranch && this.defaultBranch.branchId) {
                    updatedContext.pickupBranch = this.defaultBranch;
                    this.resetToDefaultBranch(this.defaultBranch && this.defaultBranch.branchId);
                }

                this.updateContext(updatedContext, true, () => {
                    setTimeout(() => {
                        this.initialSaleOptions();
                    });
                });
            }
        });
        this.settingForm.get('collectiveDeliveryId').valueChanges.subscribe(collectiveDeliveryId => {
            const collection = (this.settings.collectiveTypes || []).find(type => type.id === collectiveDeliveryId);
            if (collection) {
                const updatedContext = new ShoppingBasketContextModel(this.basketContext);
                updatedContext.collectionDelivery = { ...collection };
                // update context;
                this.updateContext(updatedContext);
            }
        });

        this.settingForm.get('deliveryAddressId').valueChanges.subscribe(deliveryAddressId => {
            const collection = (this.deliveryAddress || []).find(type => type.id === deliveryAddressId);
            if (collection) {
                const updatedContext = new ShoppingBasketContextModel(this.basketContext);
                updatedContext.deliveryAddress = { ...collection };
                this.updateContext(updatedContext, true, () => {
                    setTimeout(() => {
                        this.initialSaleOptions();
                    });
                });
            }
        });

        this.settingForm.get('billingAddressId').valueChanges.subscribe(billingAddressId => {
            const collection = (this.billingAddress || []).find(type => type.id === billingAddressId);
            if (collection) {
                const updatedContext = new ShoppingBasketContextModel(this.basketContext);
                updatedContext.billingAddress = { ...collection };
                this.updateContext(updatedContext);
            }
        });

        this.settingForm.get('branchId').valueChanges.subscribe(branchId => {
            const collection = (this.branchesForPickup || []).find(type => type.branchId === branchId);
            if (collection) {
                const updatedContext = new ShoppingBasketContextModel(this.basketContext);
                updatedContext.pickupBranch = { ...collection };
                this.updateContext(updatedContext, true, () => {
                    setTimeout(() => {
                        this.initialSaleOptions();
                    });
                });
            }
        });

        this.defaultBranch = new CustomerBranchModel(this.userDetail.customer && this.userDetail.customer.branch);
    }

    resetToDefaultBranch(branchId) {
        const isHiddenBrand = branchId && !this.branchesForPickup.some(item => item.branchId === branchId);
        if (isHiddenBrand) {
            this.settingForm.get('branchId').setValue(null, { emitEvent: false });
        } else {
            this.settingForm.get('branchId').setValue(branchId, { emitEvent: false });
        }
    }

    searchBrand(term: string, item: any) {
        return (item.branchId || '').toLowerCase().indexOf(term.toLowerCase()) !== -1 ||
            (item.branchName || '').toLowerCase().indexOf(term.toLowerCase()) !== -1;
    }

    private updateContext(body: ShoppingBasketContextModel, reload = false, done?: () => void) {
        this.updateShoppingBasketContext.emit({
            body,
            reload,
            done
        });
    }

    private checkCounterBasketMode(context: ShoppingBasketContextModel) {
        return context &&
            context.deliveryType &&
            context.deliveryType.descCode === DELIVERY_TYPE.PICKUP &&
            context.orderType === ORDER_TYPE.COUNTER &&
            context.showKSLMode;
    }

    private filterAddress(settings: PaymentSetting) {
        this.deliveryAddress = settings.addresses
            .filter(x => x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DELIVERY]
                || x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT]);

        // remove default if more than one address type
        if (this.deliveryAddress.length > 1) {
            this.deliveryAddress = this.deliveryAddress.filter(item => {
                return item.addressType !== ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT];
            });
        }

        this.billingAddress = settings.billingAddresses
            .filter(x => x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.INVOICE]
                || x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT]);
    }

    private pathValue(context: ShoppingBasketContextModel) {
        const currentValue = {
            invoiceId: this.convertInvoiceType(context && context.invoiceType && context.invoiceType.invoiceType || ''),
            paymentId: context && context.paymentMethod && context.paymentMethod.id || '',
            deliveryId: this.convertDeliveryId(context),
            collectiveDeliveryId: context && context.collectionDelivery && context.collectionDelivery.id || '',
            deliveryAddressId: context && context.deliveryAddress && context.deliveryAddress.id || '',
            billingAddressId: context && context.billingAddress && context.billingAddress.id || ''
        };
        this.settingForm.patchValue(currentValue, { emitEvent: false });
    }

    private convertInvoiceType(invoiceType) {
        if (invoiceType === INVOICE_TYPE.SINGLE_INVOICE || invoiceType === INVOICE_TYPE.SINGLE_INVOICE_WITH_CREDIT_SEPARATION) {
            return INVOICE_TYPE.SINGLE_INVOICE;
        }
        return INVOICE_TYPE.WEEKLY_INVOICE;
    }

    private convertDeliveryId(context) {
        const deliveryTypeId = context && context.deliveryType && context.deliveryType.id || ''
        if (!this.userDetail || !this.userDetail.isSalesOnBeHalf || !context || !this.settings) {
            return deliveryTypeId;
        }
        if (context.showKSLMode) {
            const counter = this.getCounterBasketOpt(this.settings && this.settings.deliveryTypes || []);
            if (counter.allowChoose) {
                if (this.checkCounterBasketMode(context)) {
                    return counter.id;
                }
            }
        }
        return deliveryTypeId;
    }

    private handlePaymentMethodForSaleView() {
        if (this.isCz) {
            return;
        }
        this.settings.paymentMethods.forEach(type => {
            if (this.isCashOrCard(type.descCode)) {
                type.allowChoose = this.basketContext.showKSLMode ?
                    this.basketContext.orderType === ORDER_TYPE.COUNTER
                    : this.basketContext.deliveryType.descCode !== DELIVERY_TYPE.TOUR;
            }
        });
    }

    private handleDeliveryTypeForSaleView() {
        if (this.isCz) {
            return;
        }
        this.settings.deliveryTypes.forEach(type => {
            if (type.descCode === DELIVERY_TYPE.TOUR) {
                type.allowChoose = !this.isCashOrCard(this.basketContext.paymentMethod.descCode);
            }
            if (type.descCode === DELIVERY_TYPE.PICKUP && this.basketContext.showKSLMode) {
                type.allowChoose = !this.isCashOrCard(this.basketContext.paymentMethod.descCode);
            }
            if (type.descCode === ORDER_TYPE[ORDER_TYPE.COUNTER]) {
                type.allowChoose = this.basketContext.showKSLMode;
            }
        });
    }

    private correctKSLMode() {
        if (!this.isSaleView) {
            return;
        }
        if (this.basketContext.showKSLMode) {
            const counter = this.getCounterBasketOpt(this.settings && this.settings.deliveryTypes || []);
            counter.allowChoose = this.deliverySummary && this.deliverySummary.isAllSofort;
            if (counter.allowChoose) {
                if (this.checkCounterBasketMode(this.basketContext)) {
                    this.settingForm.get('deliveryId').setValue(counter.id, { emitEvent: false });
                }
            } else {
                if (this.basketContext.orderType === ORDER_TYPE[ORDER_TYPE.COUNTER]) {
                    // If basket is empty or all items dont have immediate deivery,
                    // switch delivery type to Abloher and set order type to order
                    this.basketContext.orderType = ORDER_TYPE[ORDER_TYPE.ORDER];
                    // 3417: If payment condition = (CASH or CARD), then set payment to DIRECT INVOICE)
                    if (this.isCashOrCard(this.basketContext.paymentMethod.descCode)) {
                        const invoice = (this.settings.paymentMethods || [])
                            .find(method => method.descCode === PAYMENT_METHOD.DIRECT_INVOICE);
                        this.basketContext.paymentMethod = { ...invoice };
                    }
                    this.updateContext(this.basketContext);
                }
            }
        }
    }

    private initialSaleOptions() {
        if (!this.isSaleView) {
            return;
        }
        this.addCounterBasketOpt(this.settings && this.settings.deliveryTypes || []);
        this.handlePaymentMethodForSaleView();
        this.handleDeliveryTypeForSaleView();
    }

    private isCashOrCard(paymentMethod: string) {
        return paymentMethod === PAYMENT_METHOD.CASH.toString()
            || paymentMethod === PAYMENT_METHOD.CARD.toString();
    }

    private getCounterBasketOpt(deliveryTypes) {
        return (deliveryTypes || []).find(type => type.descCode === ORDER_TYPE.COUNTER);
    }

    private addCounterBasketOpt(deliveryTypes) {
        let counter = this.getCounterBasketOpt(deliveryTypes);
        if (!counter && !this.isCz) {
            counter = new DeliveryType();
            counter.id = -1;
            counter.descCode = ORDER_TYPE[ORDER_TYPE.COUNTER];
            counter.description = '';
            counter.allowChoose = false;
            deliveryTypes.push(counter);
        }
        return counter;
    }

    private get isSaleView() {
        return this.userDetail && this.userDetail.isSalesOnBeHalf && this.basketContext && this.settings;
    }
}
