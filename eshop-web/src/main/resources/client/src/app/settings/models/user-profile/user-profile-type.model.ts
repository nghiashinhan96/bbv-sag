export class UserProfileType {
    id: number;
    description: string;
    name: string;

    constructor(data: any) {
        if (data) {
            this.id = data.id;
            this.description = data.description;
            this.name = data.name;
        }
    }
}
