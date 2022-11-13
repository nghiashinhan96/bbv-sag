import { AVAILABILITY_INFO } from 'sag-article-detail';
import { AffiliateUtil } from 'sag-common';
import { CZ_DELIVERY_TYPE } from 'src/app/shared/cz-custom/enums/delivery-type.enum';
import { WTSB_DELIVERY_TYPE } from 'src/app/shared/sb-custom/enums/delivery-type.enum';
import { environment } from 'src/environments/environment';

export class OrderCondition {
    isTourMode = false;
    isPickupMode = false;
    isCourier = false;
    tourName = '';
    branch = '';
    tourTime = '';
    pickUpTime = '';

    setStateForSendMethodCode(methodCode: string) {
        if (AffiliateUtil.isCz(environment.affiliate)) {
            this.setStateForSendMethodCodeCz(methodCode);
            return;
        }
        if (AffiliateUtil.isSb(environment.affiliate)) {
            this.setStateForSendMethodCodeSb(methodCode);
            return;
        }
        switch (methodCode) {
            case AVAILABILITY_INFO.PICKUP:
            case CZ_DELIVERY_TYPE.PICKUP:
                this.isTourMode = false;
                this.isPickupMode = true;
                break;
            case AVAILABILITY_INFO.TOUR:
            case CZ_DELIVERY_TYPE.TOUR:
                this.isTourMode = true;
                this.isPickupMode = false;
                break;
        }
    }

    setStateForSendMethodCodeCz(methodCode: string) {
        switch (methodCode) {
            case CZ_DELIVERY_TYPE.PICKUP:
                this.isTourMode = false;
                this.isPickupMode = true;
                break;
            case CZ_DELIVERY_TYPE.TOUR:
                this.isTourMode = true;
                this.isPickupMode = false;
                break;
        }
    }

    setStateForSendMethodCodeSb(methodCode: string) {
        switch (methodCode) {
            case WTSB_DELIVERY_TYPE.PICKUP:
                this.isTourMode = false;
                this.isPickupMode = true;
                this.isCourier = false;
                break;
            case WTSB_DELIVERY_TYPE.TOUR:
                this.isTourMode = true;
                this.isPickupMode = false;
                this.isCourier = false;
                break;
            case WTSB_DELIVERY_TYPE.COURIER:
                this.isTourMode = false;
                this.isPickupMode = false;
                this.isCourier = true;
                break;
        }
    }
}
