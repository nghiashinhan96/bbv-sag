export class CollectiveType {
    id: number;
    descCode: string;
    collectiveType: string;
    description: string;
    allowChoose: boolean;
    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.descCode = data.descCode;
            this.collectiveType = data.collectiveType;
            this.description = data.description;
            this.allowChoose = data.allowChoose;
        }
    }
}
