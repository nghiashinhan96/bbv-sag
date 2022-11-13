import { OilOption } from './oil-cate-option.model';

export class OilChoice {
    var: string;
    displayName: null;
    option: OilOption[];
    constructor(json) {
        if (!json) {
            return;
        }
        this.var = json.var;
        this.displayName = json.displayName;
        this.option = (json.option || []).map(op => new OilOption(op));
    }
}
