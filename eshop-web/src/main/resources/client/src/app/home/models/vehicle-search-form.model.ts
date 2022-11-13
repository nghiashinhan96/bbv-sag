import { Constant } from 'src/app/core/conts/app.constant';

export class VehicleSearchForm {
    public freeText: string;
    public isValidVehiclesDesc = true;
    public isValidYear = true;
    public searchType: string;
    public vehiclesDesc: string;
    public vin: string;
    public year: string;

    onFreeTextChanged() {
        this.vehiclesDesc = '';
        this.year = '';
        this.vin = '';
        this.isValidYear = true;
        this.isValidVehiclesDesc = true;
        this.searchType = Constant.VEHICLE_SEARCH_FREETEXT;
    }

    onFreeTextPasted(value) {
        this.onFreeTextChanged();
        this.freeText = value;
    }

    onVehiclesDescChanged() {
        this.freeText = '';
        this.vin = '';
        this.searchType = Constant.VEHICLE_SEARCH_DESC_YEAR;
    }

    onYearChanged() {
        this.freeText = '';
        this.vin = '';
        this.searchType = Constant.VEHICLE_SEARCH_DESC_YEAR;
    }

    onVinChanged() {
        this.freeText = '';
        this.vehiclesDesc = '';
        this.year = '';
        this.isValidYear = true;
        this.isValidVehiclesDesc = true;
        this.searchType = Constant.VEHICLE_SEARCH_VIN;
    }
}
