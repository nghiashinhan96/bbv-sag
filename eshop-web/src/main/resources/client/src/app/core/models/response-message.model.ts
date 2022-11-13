export class ResponseMessage {
    key: string;
    isError: boolean;

    constructor(data?: any) {
        if (data) {
            this.key = data.key;
            this.isError = data.isError;
        }
    }
}