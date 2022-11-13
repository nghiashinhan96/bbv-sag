export interface ItemVehicleCode {
    idVehCode: string;
    vehCodeType: string;
    vehCodeAttr: string;
    vehCodeValue: string;
}

export interface ItemVehicleGtLink {
    gtStart: string;
    gtEnd: string;
    gtDrv: string;
    gtEng: string;
    gtModAlt: string;
    gtMod: string;
    gtUmc: string;
    gtmodName: string;
}

export class OrderItemVehicle {
    id: string;
    vehid: string;
    vehicleDriveType: string;
    sort: number;
    ktype: number;
    idMake: number;
    idModel: number;
    idMotor: number;
    vehicleName: string;
    vehicleBrand: string;
    vehicleModel: string;
    vehicleBuiltYearFrom: string;
    vehicleBuiltYearTill: string;
    vehicleBodyType: string;
    vehicleFuelType: string;
    vehicleEngineCode: string;
    vehiclePowerHp: string;
    vehiclePowerKw: string;
    vehicleEngine: string;
    vehicleCapacityCcTech: number;
    vehicleZylinder: number;
    codes: ItemVehicleCode[];
    gtLinks: ItemVehicleGtLink[];
    isElectric: number;
    vehicleInfo: string;
    vinSearch: boolean;
    vehTypeDesc: string;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.vehid = data.vehid;
            this.vehicleDriveType = data.vehicle_drive_type;
            this.sort = data.sort;
            this.ktype = data.ktype;
            this.idMake = data.id_make;
            this.idModel = data.id_model;
            this.idMotor = data.id_motor;
            this.vehicleName = data.vehicle_name;
            this.vehicleBrand = data.vehicle_brand;
            this.vehicleModel = data.vehicle_model;
            this.vehicleBuiltYearFrom = data.vehicle_built_year_from;
            this.vehicleBuiltYearTill = data.vehicle_built_year_till;
            this.vehicleBodyType = data.vehicle_body_type;
            this.vehicleFuelType = data.vehicle_fuel_type;
            this.vehicleEngineCode = data.vehicle_engine_code;
            this.vehiclePowerHp = data.vehicle_power_hp;
            this.vehiclePowerKw = data.vehicle_power_kw;
            this.vehicleEngine = data.vehicle_engine;
            this.vehicleCapacityCcTech = data.vehicle_capacity_cc_tech;
            this.vehicleZylinder = data.vehicle_zylinder;
            this.codes = (data.codes || []).map((code: any) => ({
                idVehCode: code.id_veh_code,
                vehCodeType: code.veh_code_type,
                vehCodeAttr: code.veh_code_attr,
                vehCodeValue: code.veh_code_value
            } as ItemVehicleCode));
            this.gtLinks = (data.gt_links || []).map((link: any) => ({
                gtStart: link.gt_start,
                gtEnd: link.gt_end,
                gtDrv: link.gt_drv,
                gtEng: link.gt_eng,
                gtModAlt: link.gt_mod_alt,
                gtMod: link.gt_mod,
                gtUmc: link.gt_umc,
                gtmodName: link.gtmod_name
            } as ItemVehicleGtLink));
            this.isElectric = data.is_electric;
            this.vehicleInfo = data.vehicleInfo;
            this.vinSearch = data.vinSearch;
            this.vehTypeDesc = data.vehTypeDesc;
        }
    }
}
