export class BrandModel {
    brandId: number;
    default: boolean;
    id: number;
    margin1: number;
    margin2: number;
    margin3: number;
    margin4: number;
    margin5: number;
    margin6: number;
    margin7: number;
    name: string;
    orgId: number;
    isDefault: boolean;

    constructor(data = null) {
        if(data) {
            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });
        }
    }
}