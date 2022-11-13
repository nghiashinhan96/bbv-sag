export class UserProfileLanguage {
    id: number;
    langcode: string;
    langiso: string;
    description: string;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.langcode = data.langcode;
            this.langiso = data.langiso;
            this.description = data.description;
        }
    }
}