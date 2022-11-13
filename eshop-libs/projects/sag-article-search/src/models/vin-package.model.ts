export class VinPackage {
    id: number;
    packId: number;
    packName: string;
    productParameters: any;
    packArticleId: string;
    packArticleNo: any;
    packUmarId: any;
    quantity: number;
    lastUpdate: any;
    lastUpdatedBy: any;
    typeOfLicense: any;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.packId = data.packId;
            this.packName = data.packName;
            this.productParameters = data.productParameters;
            this.packArticleId = data.packArticleId;
            this.packArticleNo = data.packArticleNo;
            this.packUmarId = data.packUmarId;
            this.quantity = data.quantity;
            this.lastUpdate = data.lastUpdate;
            this.lastUpdatedBy = data.lastUpdatedBy;
            this.typeOfLicense = data.typeOfLicense;
        }
    }
}