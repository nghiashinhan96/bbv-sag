export class ExternalVendorConstants {
    public static readonly COUNTRY = 'COUNTRY';
    public static readonly ID = 'ID';
    public static readonly NAME = 'NAME';
    public static readonly PRIORITY = 'PRIORITY';
    public static readonly DELIVERY_PROFILE = 'DELIVERY_PROFILE';
    public static readonly EXTERNAL_VENDOR_NAME_MAX_LENGTH = 50;
    public static readonly SORT_FIELD_MAP = {
        country: 'orderByCountryDesc',
        vendorId: 'orderByVendorIdDesc',
        vendorName: 'orderByVendorNameDesc',
        vendorPriority: 'orderByVendorPriorityDesc',
        deliveryProfileName: 'orderByDeleviryProfileNameDesc'
    }
}
