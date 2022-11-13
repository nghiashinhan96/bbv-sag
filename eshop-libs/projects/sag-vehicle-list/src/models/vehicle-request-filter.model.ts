export class VehicleRequestFilterModel {
    full_name?: string;
    built_year_month_from?: string;
    vehicle_advance_name?: string;
    body_type = '';
    fuel_type = '';
    zylinder = '';
    engine = '';
    capacity_cc_tech?: string;
    motor_code = '';
    power = '';
    drive_type = '';

    constructor(
        json?: VehicleRequestFilterModel
    ) {
        if (json) {
            this.full_name = json.full_name || undefined;
            this.built_year_month_from = json.built_year_month_from || undefined;
            this.vehicle_advance_name = json.vehicle_advance_name || undefined;
            this.capacity_cc_tech = json.capacity_cc_tech || undefined;
            this.body_type = json.body_type || '';
            this.fuel_type = json.fuel_type || '';
            this.zylinder = json.zylinder || '';
            this.engine = json.engine || '';
            this.motor_code = json.motor_code || '';
            this.power = json.power || '';
            this.drive_type = json.drive_type || '';
        }
    }
}
