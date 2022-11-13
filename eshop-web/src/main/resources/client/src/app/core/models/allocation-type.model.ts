export class AllocationType {
    id: number;
    descCode: string;
    allocationType: string;
    description: string;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.descCode = data.descCode;
            this.allocationType = data.allocationType;
            this.description = data.description;
        }
    }
}
