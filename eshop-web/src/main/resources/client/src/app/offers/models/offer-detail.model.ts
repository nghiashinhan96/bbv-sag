import { CustomPrice } from 'sag-custom-pricing';
import { OfferPerson } from './offer-person.model';
import { OfferPosition } from './offer-position.model';

export class OfferDetail {
    articlePrice: number;
    currentUserId: number;
    customerNr: string;
    discount: number;
    id: number;
    numberOfItems: number;
    offerDate = 0;
    deliveryDate = null;
    offerNr = '';
    offerPerson = new OfferPerson();
    offerPositions: OfferPosition[] = [];
    organisationId: number;
    remark: string;
    status: string;
    vat: number;
    workingPrice: number;
    totalGrossPrice = 0;
    totalArticle: number;
    totalExcludeVat: number;
    totalIncludeVat: number;
    totalOwnArticle: number;
    totalRemark: number;
    totalVat: number;
    totalWork: number;
    formattedOfferDate: string;
    formattedDeliveryDate: string;
    displayedPrice: CustomPrice;

    constructor(data?) {
        if (!data) {
            return;
        }

        this.articlePrice = data.articlePrice;
        this.currentUserId = data.currentUserId;
        this.customerNr = data.customerNr;
        this.discount = data.discount;
        this.id = data.id;
        this.numberOfItems = data.numberOfItems;
        this.offerDate = data.offerDate;
        this.deliveryDate = data.deliveryDate;
        this.offerNr = data.offerNr;
        this.offerPerson = data.offerPerson;
        this.offerPositions = (data.offerPositions || []).map(pos => new OfferPosition(pos));
        this.organisationId = data.organisationId;
        this.remark = data.remark;
        this.status = data.status;
        this.vat = data.vat;
        this.workingPrice = data.workingPrice;
        this.totalGrossPrice = data.totalGrossPrice;
        this.totalArticle = data.totalArticle;
        this.totalExcludeVat = data.totalExcludeVat;
        this.totalIncludeVat = data.totalIncludeVat;
        this.totalOwnArticle = data.totalOwnArticle;
        this.totalRemark = data.totalRemark;
        this.totalVat = data.totalVat;
        this.totalWork = data.totalWork;
        this.formattedOfferDate = data.formattedOfferDate;
        this.formattedDeliveryDate = data.formattedDeliveryDate;
        this.displayedPrice = data.displayedPrice;
    }

    get updateRequestModel() {
        const displayedPrice = this.displayedPrice;
        if (displayedPrice) {
            displayedPrice.price = null;
        }
        return {
            offerId: this.id,
            offerPersonId: this.offerPerson && this.offerPerson.id || null,
            offerNumber: this.offerNr,
            offerDate: this.offerDate && (typeof (this.offerDate) === 'number') && new Date(this.offerDate).toISOString() || this.offerDate,
            deliveryDate: this.deliveryDate
                && (typeof (this.deliveryDate) === 'number') && new Date(this.deliveryDate).toISOString() || null,
            remark: this.remark,
            offerPositionRequests: (this.offerPositions || []).map(pos => Object.assign(pos, { offerPositionId: pos.id })),
            status: this.status,
            vat: this.vat,
            totalGrossPrice: this.totalIncludeVat,
            displayedPrice
        };
    }
}
