export class DesignationModel {
    language: string;
    text: string;

    constructor (data = null) {
        if (data) {
            this.language = data.language;
            this.text = data.text;
        }
    }
}