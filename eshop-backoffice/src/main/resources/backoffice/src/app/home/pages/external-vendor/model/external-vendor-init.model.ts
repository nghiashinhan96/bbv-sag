import { ExternalVendorCountry } from './external-vendor-country.model';
import { ExternalVendorProfile } from './external-vendor-profile.model';
import { ExternalVendorBrand } from './external-vendor-brand.model';


export class ExternalVendorInit {
    availabilityType: string[] = [];
    countries: ExternalVendorCountry[] = [];
    deliveryProfile: ExternalVendorProfile[] = [];
    brands: ExternalVendorBrand[] = [];

    constructor(data?: ExternalVendorInit) {
        if (!data) {
            return;
        }
        this.brands = data.brands;
        this.availabilityType = data.availabilityType;
        this.countries = data.countries;
        this.deliveryProfile = data.deliveryProfile;
    }
}
