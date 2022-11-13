export class CustomerBranchModel {
    branchId: string;
    branchName: string;
    constructor(json?) {
        if (!json) {
            return;
        }
        this.branchId = json.branchId;
        this.branchName = json.branchName;
    }
}
