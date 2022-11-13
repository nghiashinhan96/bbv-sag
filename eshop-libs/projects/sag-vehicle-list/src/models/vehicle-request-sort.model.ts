export class VehicleRequestSortModel {
    vehicle_info?: string;
    built_year_month_from?: string;
    body_type?: string;
    fuel_type?: string;
    zylinder?: string;
    engine?: string;
    capacity_cc_tech?: string;
    motor_code?: string;
    power?: string;
    drive_type?: string;
    vehicle_advance_name?: string;
    
    constructor(
        json?: VehicleRequestSortModel
    ) {
        if (json) {
            this.vehicle_info = json.vehicle_info;
            this.built_year_month_from = json.built_year_month_from;
            this.body_type = json.body_type;
            this.fuel_type = json.fuel_type;
            this.zylinder = json.zylinder;
            this.engine = json.engine;
            this.capacity_cc_tech = json.capacity_cc_tech;
            this.motor_code = json.motor_code;
            this.power = json.power;
            this.drive_type = json.drive_type;
            this.vehicle_advance_name = json.vehicle_advance_name;
        }
    }
}
