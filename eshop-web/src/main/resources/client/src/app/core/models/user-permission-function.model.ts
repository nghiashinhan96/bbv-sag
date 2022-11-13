export class UserPermissionFunction {
    id: number;
    functionName: string;
    description: string;
    relativeUrl: string;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.functionName = data.functionName;
            this.description = data.description;
            this.relativeUrl = data.relativeUrl;
        }
    }
}
