import { Observable } from 'rxjs';

export abstract class HaynesProIntegration {

    baseUrl;
    spinner;

    abstract getVehiclesByVehId(vehicleId): Observable<any>;
    abstract getCategoriesByGaids(gaid): any[];
    abstract checkOnCategoryTree(categoryIds, isChecked, emitSearchEvent);
    abstract checkOilCate(cateIds, emitSearchEvent);
}
