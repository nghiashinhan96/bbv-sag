import { OfferPerson } from './offer-person.model';

export class OfferPersonsResponse {
    public persons: OfferPerson[];
    public pagi: number;
    public size: number;
    public total: number;

    constructor(res: any) {
        this.persons = res.content.map(item => new OfferPerson(item));
        this.pagi = res.number;
        this.size = res.size;
        this.total = res.totalElements;
    }
}
