import { Offer } from './offer.model';

export class OffersResponse {
    offers: Array<Offer>;
    first: boolean;
    last: boolean;
    number: number;
    numberOfElements: number;
    size: number;
    sort: any;
    totalElements: number;
    totalPages: number;
    isLoaded: boolean;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.offers = data.content;
        this.first = data.first;
        this.last = data.last;
        this.number = data.number;
        this.numberOfElements = data.numberOfElements;
        this.size = data.size;
        this.sort = data.sort;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalPages;
        this.isLoaded = false;
    }
}
