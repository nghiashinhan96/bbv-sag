export class OciInfo {
    userName: string;
    password: string;
    language: string;
    isOciFlow: boolean;
    exportData: any;
    target: string;
    action: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.userName = data.userName;
        this.password = data.password;
        this.language = data.language;
        this.isOciFlow = data.isOciFlow;
        this.exportData = data.exportData;
        this.target = data.target;
        this.action = data.action;
    }

}
