import { OrderDashboardListItemModel } from './order-dashboard-list-item.model';

export class OrderDashboardRefModel {
    reference: string;
    branchRemark: string;
    positionReferences: string | string[];
    enabled = false;

    constructor(json?: OrderDashboardListItemModel) {
        if (!json) {
            return;
        }
        this.reference = json.reference;
        this.branchRemark = json.branchRemark;
        if (!!json.positionReferences && !Array.isArray(json.positionReferences)) {
            this.positionReferences = json.positionReferences.split(';');
        }

        if (!!json.reference || !!json.branchRemark || !!json.positionReferences) {
            this.enabled = true;
        }
    }
}
