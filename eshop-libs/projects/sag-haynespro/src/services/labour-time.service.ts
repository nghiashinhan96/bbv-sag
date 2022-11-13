import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { HaynesProIntegrationService } from './haynes-pro-inregration.service';

@Injectable()
export class LabourTimeService {
    constructor(
        private http: HttpClient,
        private config: HaynesProIntegrationService
    ) { }

    getLabourTime(vehicleId) {
        const url = `${this.config.baseUrl}haynespro/labour-time`;
        const params = { vehicleId };
        return this.http.get(url, { params });
    }

    deleteLabourRecord(vehicleId, awNumber) {
        const url = `${this.config.baseUrl}haynespro/labour-time/remove`;
        const params = { vehicleId, awNumber };
        return this.http.put(url, null, { params });
    }

    loadLabourTimesForAllVehicle(groupVehiclesCategories) {
        const labourTimes = [];
        const groupVehicles = groupVehiclesCategories.filter(item => !!item.vehicleId);
        groupVehicles.map((vehicle) => {
            this.loadLabourTimeForVehicle(vehicle.vehicleId).then(labourTimesByVehicle => {
                if (labourTimesByVehicle && labourTimesByVehicle.vehicleId) {
                    labourTimes.push(labourTimesByVehicle);
                }
            });
        });
        return labourTimes;
    }

    async loadLabourTimeForVehicle(selectedVehicleId) {
        return await this.getLabourTime(selectedVehicleId)
            .pipe(map((res: any) => {
                if (res.length) {
                    res.vehicleId = selectedVehicleId;
                    return res;
                }
                return {};
            })).toPromise();
    }

    getAllVehicleLabourTimes(groupVehiclesCategories) {
        const labourTimesRequests = [];
        const groupVehicles = groupVehiclesCategories.filter(item => !!item.vehicleId);
        groupVehicles.forEach(vehicle => {
            labourTimesRequests.push(this.loadLabourTimeForVehicle(vehicle.vehicleId));
        });
        return Promise.all(labourTimesRequests);
    }

}
