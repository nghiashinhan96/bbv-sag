export class UserProfileSalutation {
    id: number;
    code: string;
    description: string;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.code = data.code;
            this.description = data.description;
        }
    }
}
