import { OilType } from '../enums/oil-type.enum';
import { OilChoice } from './oil-cate-choice.model';

export class OilOption {
    value: string;
    type: OilType;
    displayValue: string;
    label?: string;
    applicationId?: string;
    cateId?: string;
    cateName?: string;
    guid?: string;
    choice?: OilChoice;
    markedAsDeleted?: boolean;
    displayName: string;
    constructor(json) {
        if (!json) {
            return;
        }
        this.value = json.value;
        this.type = json.type;
        this.displayValue = json.display_value;
        this.label = json.display_value;
        this.applicationId = json.application_id;
        this.cateId = json.cateId;
        this.guid = json.guid;
        this.markedAsDeleted = json.markedAsDeleted || false;
        this.displayName = json.displayName;
        if (json.choice) {
            this.choice = new OilChoice(json.choice);
        }
    }
}