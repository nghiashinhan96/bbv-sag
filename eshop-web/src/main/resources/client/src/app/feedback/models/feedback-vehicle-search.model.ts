export class FeedbackVehicleSearch {
    type: string;
    searchText: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.type = data.type;
        this.searchText = data.searchText;
    }

    get content(): string {
        return `${this.type}: ${this.searchText}`;
    }
}
