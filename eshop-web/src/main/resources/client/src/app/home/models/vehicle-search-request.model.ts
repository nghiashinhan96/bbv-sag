export class VehicleSearchRequest {
    vehicleData: string;
    vehicleDesc: string;

    buildVehicleDescriptionRequest(request) {
        const requestData = {
            term: {
                vehicleDesc: request.vehicleName.trim()
            },
            filtering: {}
        };
        if (request.vehicleYear) {
            requestData.filtering = {
                built_year_month_from: request.vehicleYear
            };
        }
        return requestData;
    }

    buildVehicleCodeRequest(request) {
        return {
            term: {
                vehicleData: request.vehicleCode.trim()
            }
        };
    }
}
