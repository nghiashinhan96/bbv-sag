import { MessageLocationModel } from './message-location.model';
import { SagEditorLanguageModel } from 'sag-common';

export class MessageSavingModel {
    title: string;
    messageLocation: MessageLocationModel;
    locationTypeId: number;
    locationValue: string;
    accessRightId: number;
    typeId: string;
    subAreaId: number;
    styleId: number;
    visibilityId: number;
    active: boolean;
    dateValidFrom: string;
    dateValidTo: string;
    messageLanguages: Array<SagEditorLanguageModel>;
    ssoTraining: boolean;
    constructor(data?) {
        if (!data) {
            return;
        }
        this.title = data.title;
        this.messageLocation = data.messageLocation;
        this.locationTypeId = data.locationTypeId;
        this.locationValue = data.locationValue;
        this.accessRightId = data.accessRightId;
        this.typeId = data.typeId;
        this.subAreaId = data.subAreaId;
        this.styleId = data.styleId;
        this.visibilityId = data.visibilityId;
        this.active = data.active;
        this.dateValidFrom = data.dateValidFrom;
        this.dateValidTo = data.dateValidTo;
        this.messageLanguages = data.messageLanguages;
        this.ssoTraining = data.ssoTraining;
    }
}
