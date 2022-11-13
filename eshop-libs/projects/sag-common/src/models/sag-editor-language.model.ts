export class SagEditorLanguageModel {
    langIso: string;
    content: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.langIso = data.langIso;
        this.content = data.content;
    }
}