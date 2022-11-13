import { CategoryModel } from 'sag-article-detail';
export class QuickClickModel {
    id: string;
    description: string;
    sagCode: string;
    children: CategoryModel[];
    parentId: string;
    rootDescription: string;
    sort: string;
    open: string;
    qcol: number;
    qrow: number;
    qflag: number;
    qlevel: number;
    qshow: boolean;
    ignoredOpen: boolean;
    oilCate: boolean;
    check: boolean;
    constructor(json?: QuickClickModel | any) {
        if (!json) {
            return;
        }
        this.id = json.id;
        this.description = json.description;
        this.sagCode = json.sagCode;
        this.children = (json.children || []).map(item => new CategoryModel(item));
        this.parentId = json.parentId;
        this.rootDescription = json.rootDescription;
        this.sort = json.sort;
        this.open = json.open;
        this.qcol = json.qcol;
        this.qrow = json.qrow;
        this.qflag = json.qflag;
        this.qlevel = json.qlevel;
        this.qshow = Boolean(json.qshow);
        this.ignoredOpen = Boolean(json.ignoredOpen);
        this.oilCate = Boolean(json.oilCate);
        this.check = Boolean(json.check);
    }
}
