export class CustomerNrPageModel {
    content: string[] = [];
    totalElements: number;
    size: number;
    number: number;
    totalPages: number;

    constructor(data?) {
        if (!data) {
            return;
        }

        if (data.content) {
            data.content.forEach(element => {
                this.content.push(element);
            });
        }

        this.totalElements = data.totalElements;
        this.size = data.size;
        this.number = data.number;
        this.totalPages = data.totalPages;

    }
}
