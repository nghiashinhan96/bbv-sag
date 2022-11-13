export class OlyslagerModel {
    id: string;
    name: string;
    constructor(json?: OlyslagerModel | any) {
        if (!json) {
            return;
        }
        this.id = json.id;
        this.name = json.name;
    }
}
