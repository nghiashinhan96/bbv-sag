export class PriceFile {
    fileName: string;
    fileUrl: string;
    modifiedDate: Date = null;
    constructor(json?) {
        if (!json) {
            return;
        }
        this.fileName = json.fileName;
        this.fileUrl = json.fileUrl;
        this.modifiedDate = new Date(json.modifiedDate);
    }
}
