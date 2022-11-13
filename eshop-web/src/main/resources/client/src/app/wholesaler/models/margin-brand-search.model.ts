export class BrandSearchModel {
    dlnrid: string;
    suppname: string;

    constructor (data = null) {
        if (data) {
            this.dlnrid = data.dlnrid;
            this.suppname = data.suppname;
        }
    }
}