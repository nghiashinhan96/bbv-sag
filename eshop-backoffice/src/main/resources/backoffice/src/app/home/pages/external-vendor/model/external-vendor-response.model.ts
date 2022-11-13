import { ExternalVendorDetailRequest } from './external-vendor-item.model';

export class ExternalVendorResponse {
    content = new Array<ExternalVendorDetailRequest>();
    first: boolean;
    last: boolean;
    number: number;
    numberOfElements: number;
    size: number;
    totalElements: number;
    totalPages: number;
    sort: any;
    pageable: any;

    constructor(data?: ExternalVendorResponse) {
        if (!data) {
            return;
        }

        this.content = data.content;
        this.first = data.first;
        this.last = data.last;
        this.number = data.number;
        this.numberOfElements = data.numberOfElements;
        this.size = data.size;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalPages;
        this.sort = data.sort;
        this.pageable = data.pageable;
    }
}
