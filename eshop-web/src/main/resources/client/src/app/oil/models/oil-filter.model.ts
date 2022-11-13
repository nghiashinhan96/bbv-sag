import { Constant } from 'src/app/core/conts/app.constant';

export class OilFilter {
    vehicle: string = Constant.SPACE;
    aggregate: string = Constant.SPACE;
    viscosity: string = Constant.SPACE;
    bottleSize: string = Constant.SPACE;
    approved: string = Constant.SPACE;
    specification: string = Constant.SPACE;
    brand: string = Constant.SPACE;
    totalElements = 0;
    isRangeBottleSizeCb = false;
}
