export class ExternalVendorDetailRequest {
    availabilityTypeId: string;
    brandId: string;
    country: string;
    deliveryProfileName: string;
    id: number;
    sagArticleGroup: number;
    vendorId: number;
    vendorName: string;
    vendorPriority: number;
    deliveryProfileId: string;

    constructor(data?: ExternalVendorDetailRequest) {
        if (!data) {
            return;
        }
        this.deliveryProfileId = data.deliveryProfileId.toString();
        this.availabilityTypeId = data.availabilityTypeId;
        this.brandId = data.brandId ? data.brandId.toString() : '';
        this.country = data.country.toLowerCase();
        this.deliveryProfileName = data.deliveryProfileName;
        this.id = +data.id;
        this.sagArticleGroup = data.sagArticleGroup;
        this.vendorId = data.vendorId;
        this.vendorName = data.vendorName ? data.vendorName.trim() : '';
        this.vendorPriority = +data.vendorPriority;
    }
}
