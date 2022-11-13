import { OfferPosition } from './offer-position.model';
import { OfferDetail } from './offer-detail.model';

export class OfferUpdateRequest {
    offerId: number;
    offerPersonId: number;
    offerAddressId: number;
    offerNumber: string;
    offerDate: string;
    deliveryDate: string;
    remark: string;
    status: string;
    totalGrossPrice: number;
    vat: number;
    offerPositionRequests: OfferPosition[];

    static convertToRequestUpdateOffer(offer: OfferDetail) {
        const offerUpdateRequest = new OfferUpdateRequest();
        offerUpdateRequest.offerId = offer.id;
        offerUpdateRequest.offerPersonId = offer.offerPerson ? offer.offerPerson.id : null;
        offerUpdateRequest.offerNumber = offer.offerNr;
        offerUpdateRequest.offerDate = (typeof (offer.offerDate) === 'number') ? new Date(offer.offerDate).toISOString() : offer.offerDate;
        offerUpdateRequest.deliveryDate = (offer.deliveryDate != null && (typeof (offer.deliveryDate) === 'number')) ?
            new Date(offer.deliveryDate).toISOString() : null;
        offerUpdateRequest.remark = offer.remark;
        offerUpdateRequest.offerPositionRequests = Object.assign([], offer.offerPositions);
        offerUpdateRequest.status = offer.status;
        offerUpdateRequest.vat = offer.vat;
        for (let i = 0; i < offerUpdateRequest.offerPositionRequests.length; i++) {
            offerUpdateRequest.offerPositionRequests[i]['offerPositionId'] = offer.offerPositions[i].id;
        }
        offerUpdateRequest.offerPositionRequests = offer.offerPositions;
        offerUpdateRequest.totalGrossPrice = offer.totalIncludeVat;
        return offerUpdateRequest;
    }
}
