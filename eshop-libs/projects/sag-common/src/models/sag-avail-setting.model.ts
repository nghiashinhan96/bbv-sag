import { SagEditorLanguageModel } from './sag-editor-language.model';

export class SagAvailDisplaySettingModel {
    availIcon: boolean;
    dropShipmentAvail: boolean;

    availState: string;
    detailAvailText: SagEditorLanguageModel[] = [];
    listAvailText: SagEditorLanguageModel[] = [];
    title: string;
    color: string;
    displayOption: string;
    confirmColor?: string;

    constructor (data?: any) {
        if (data) {
            this.availIcon = data.availIcon;
            this.dropShipmentAvail = data.dropShipmentAvail;

            if(data.detailAvailText) {
                data.detailAvailText.forEach(element => {
                    this.detailAvailText.push(new SagEditorLanguageModel(element));
                });
            }

            if(data.listAvailText) {
                data.listAvailText.forEach(element => {
                    this.listAvailText.push(new SagEditorLanguageModel(element));
                });
            }

            this.availState = data.availState;
            this.title = data.title;
            this.color = data.color;
            this.displayOption = data.displayOption;
            this.confirmColor = data.confirmColor;
        }
    }
}