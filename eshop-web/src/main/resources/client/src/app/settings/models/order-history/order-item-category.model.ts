export class OrderItemCategory {
    gaId: string;
    gaDesc: string;
    rootDesc: string;

    constructor(data?: any) {
        if (data) {
            this.gaId = data.gaId;
            this.gaDesc = data.gaDesc;
            this.rootDesc = data.rootDesc;
        }
    }
}
