export class GtMotiveEstimate {
    url: string;
    estimateId: string;
    errorMessage: string;
    errorCode: string;
    vinLogId: any;

    constructor(data?: any) {
        if (data) {
            this.url = data.url;
            this.estimateId = data.estimateId;
            this.errorMessage = data.errorMessage;
            this.errorCode = data.errorCode;
            this.vinLogId = data.vinLogId;
        }
    }
}
