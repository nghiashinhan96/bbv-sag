import { OrderAvailabilityModel } from './order-availability.model';
import { OrderLocationBranchModel } from 'src/app/core/models/order-location-branch.model';

export class OrderResponseModel {
    orderNr: string;
    frontEndBasketNr: string;
    axOrderURL: string;
    workIds: any[];
    orderHistoryId: number;
    _links: any;
    cartKeys: string[];
    errorMsg: string;
    warningMsgCode: string;
    orderType: string;
    subTotalWithNet: number;
    vatTotalWithNet: number;
    orderExecutionType: string;
    location: OrderLocationBranchModel;
    orderAvailabilities: OrderAvailabilityModel[]= [];

    constructor(data = null) {
        if(data) {
            if(data.orderAvailabilities) {
                data.orderAvailabilities = (data.orderAvailabilities || []).map(item => new OrderAvailabilityModel(item));
            }

            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });
        }
    }
}