const vinPermission = {
    name: 'VIN',
    functions: {
        search: 'VIN_API_SEARCH',
        packages: 'VIN_API_PACKAGES'
    }
};

const haynesproPermission = {
    name: 'HAYNESPRO',
    functions: {
        haynespro: 'HAYNESPRO'
    }
};

export const permissions = {
    vin: vinPermission,
    haynespro: haynesproPermission
};
