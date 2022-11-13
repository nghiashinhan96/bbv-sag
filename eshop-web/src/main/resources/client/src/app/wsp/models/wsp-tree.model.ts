export class WspTreeModel {
    treeId: string;
    treeImage: string;
    treeName: string;
    treeSort: string;
    treeExternalServiceAttribute: string;
    treeExternalServiceType: string;

    treeIdNum: number;

    active = false;

    constructor(data = null) {
        if(data) {
            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });

            this.treeIdNum = Number(this.treeId);
            this.treeExternalServiceType = data.treeExternalService;
        }
    }
}

export interface IWspLink {
    type: string;
    attr: string;
    link: string;
    event?: any;
}