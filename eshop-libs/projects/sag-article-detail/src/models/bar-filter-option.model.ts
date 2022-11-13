export class BarFilter {
    name: string;
    key: string;
    gaID: string;
    filterOpen: boolean;
    filterDefault: string;
    filterCaid: string;
    filterBar: boolean;
    filterSort: boolean;
    filterUom: string;
    options: BarFilterOption[];
    toggle: boolean;
    constructor(data) {
        if (!data) {
            return;
        }
        this.name = data.name;
        this.key = data.key;
        this.gaID = data.gaID;
        this.filterDefault = data.filterDefault;
        this.filterOpen = this.filterDefault && true || typeof (data.filterOpen) == 'boolean' && data.filterOpen || data.filterOpen === '1';
        this.filterCaid = data.filterCaid;
        this.filterUom = data.filterUom;
        this.filterBar = typeof (data.filterBar) == 'boolean' && data.filterBar || data.filterBar === '1';
        this.filterSort = typeof (data.filterSort) == 'boolean' && data.filterSort || data.filterSort === '1';
        this.options = (data.options || []).map(item => new BarFilterOption({ ...item, checked: item.checked || false }));
        this.toggle = this.filterOpen;
    }
}

export class BarFilterOption {
    cvp: string;
    cid: string;
    cn: string;
    checked: boolean;
    constructor(data) {
        if (!data) {
            return;
        }
        this.cvp = data.cvp;
        this.cid = data.cid;
        this.cn = data.cn;
        this.checked = data.checked;
    }
}

export class GenArtBarFilter {
    gaid: string;
    filter: BarFilter[];
    constructor(data) {
        if (!data) {
            return;
        }
        this.gaid = data.gaid;
        this.filter = (data.filter || []).map(item => new BarFilter(item));;
    }
}