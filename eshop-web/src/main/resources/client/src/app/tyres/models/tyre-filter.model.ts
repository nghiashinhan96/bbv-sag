import { Constant } from 'src/app/core/conts/app.constant';

export class TyreFilter {
    width: string = Constant.SPACE;
    height: string = Constant.SPACE;
    radius: string = Constant.SPACE;
    matchCode: string = Constant.SPACE;
    speedIndex: string = Constant.SPACE;
    supplier: string = Constant.SPACE;
    runflat = false;
    spike = false;
    tyreSegment: string = Constant.SPACE;
    loadIndex: string = Constant.SPACE;
    totalElements = 0;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.width = data.width;
        this.height = data.height;
        this.radius = data.radius;
        this.matchCode = data.matchCode;
        this.speedIndex = data.speedIndex;
        this.supplier = data.supplier;
        this.runflat = data.runflat;
        this.spike = data.spike;
        this.tyreSegment = data.tyreSegment;
        this.loadIndex = data.loadIndex;
    }
}