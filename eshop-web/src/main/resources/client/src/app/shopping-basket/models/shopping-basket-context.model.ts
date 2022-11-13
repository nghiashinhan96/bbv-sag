
import { AllocationType } from 'src/app/core/models/allocation-type.model';
import { InvoiceType } from 'src/app/core/models/invoice-type.model';
import { PaymentMethod } from 'src/app/core/models/payment-method.model';
import { DeliveryType } from 'src/app/core/models/delivery-type.model';
import { CollectiveType } from 'src/app/core/models/collective-type.model';
import { CustomerBranchModel } from 'src/app/core/models/customer-branch.model';
import { UserAddress } from 'src/app/core/models/user-address.model';
import { CourierServiceModel } from 'src/app/core/models/courier-service.model';
import { OrderLocationBranchModel } from 'src/app/core/models/order-location-branch.model';
import { TourTime } from 'src/app/core/models/tour-time.model';

export class ShoppingBasketContextModel {
    allocation: AllocationType;
    invoiceType: InvoiceType;
    paymentMethod: PaymentMethod;
    deliveryType: DeliveryType;
    collectionDelivery: CollectiveType;
    pickupBranch: CustomerBranchModel;

    billingAddress: UserAddress;
    deliveryAddress: UserAddress;
    orderType: string;
    showKSLMode: boolean;
    location: OrderLocationBranchModel;
    pickupLocation: OrderLocationBranchModel;
    courierService: CourierServiceModel;
    referenceTextByLocation: string;
    messageToBranch: string;
    eshopBasketContextByLocation: ShoppingBasketContextModel[] = [];

    expanded: boolean = true;
    tourTimes: TourTime[] = [];

    constructor(json?: ShoppingBasketContextModel) {
        if (!json) {
            return;
        }
        this.allocation = new AllocationType(json.allocation);
        this.invoiceType = new InvoiceType(json.invoiceType);
        this.paymentMethod = new PaymentMethod(json.paymentMethod);
        this.deliveryType = new DeliveryType(json.deliveryType);
        this.collectionDelivery = new CollectiveType(json.collectionDelivery);
        this.pickupBranch = json.pickupBranch && new CustomerBranchModel(json.pickupBranch) || null;
        this.billingAddress = new UserAddress(json.billingAddress);
        this.deliveryAddress = new UserAddress(json.deliveryAddress);
        this.orderType = json.orderType;
        this.showKSLMode = json.showKSLMode;
        this.location = json.location && new OrderLocationBranchModel(json.location) || null;
        this.pickupLocation = json.pickupLocation && new OrderLocationBranchModel(json.pickupLocation) || null;
        this.courierService = json.courierService && new CourierServiceModel(json.courierService) || null;
        this.referenceTextByLocation = json.referenceTextByLocation;
        this.messageToBranch = json.messageToBranch;
        this.eshopBasketContextByLocation = (json.eshopBasketContextByLocation || []).map(context => new ShoppingBasketContextModel(context));
        this.tourTimes = (json.tourTimes || []).map(tourTime => new TourTime(tourTime));
    }
}
