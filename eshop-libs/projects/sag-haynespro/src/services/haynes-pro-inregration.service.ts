import { Injectable } from '@angular/core';
import { HaynesProIntegration } from './haynes-pro-integration';
import { Observable } from 'rxjs';

@Injectable()
export class HaynesProIntegrationService implements HaynesProIntegration {

    baseUrl: any;
    spinner: any;

    getCategoriesByGaids(gaid: any): any[] {
        throw new Error('Method not implemented.');
    }
    checkOnCategoryTree(categoryIds: any, isChecked: any, emitSearchEvent: any) {
        throw new Error('Method not implemented.');
    }
    checkOilCate(cateIds: any, emitSearchEvent: any) {
        throw new Error('Method not implemented.');
    }
    getVehiclesByVehId(vehicleId: any): Observable<any> {
        throw new Error('Method not implemented.');
    }

}
