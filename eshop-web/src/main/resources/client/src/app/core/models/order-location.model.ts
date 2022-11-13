import { CourierServiceModel } from './courier-service.model';
import { DeliveryType } from './delivery-type.model';
import { OrderLocationBranchModel } from './order-location-branch.model';

export class OrderLocationModel {
    branch: OrderLocationBranchModel;
    courierServices: CourierServiceModel[];
    deliveryTypes: DeliveryType[];
    paymentMethods: any[];

    expanded: boolean = true;
    formCtrl: any;
    pickupBranchs: OrderLocationBranchModel[] = [];
    isPickupMode: boolean;
    isCourierMode: boolean;

    constructor (data = null) {
        if (data) {
            this.branch = data.branch && new OrderLocationBranchModel(data.branch) || null;
            this.courierServices = data.courierServices || [].map((courier: any) => new CourierServiceModel(courier));
            this.deliveryTypes = (data.deliveryTypes || []).map((deliveryType: any) => new DeliveryType(deliveryType));
            this.paymentMethods = data.paymentMethods || [];
            this.expanded = data.expanded === false;
            this.pickupBranchs = data.pickupBranchs || [];
            this.isPickupMode = data.isPickupMode === false;
            this.isCourierMode = data.isCourierMode === false;
        }
    }
}