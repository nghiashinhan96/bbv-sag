import { KEY_RAW } from '../enums/vehicle-list.enum';
export class SagVehicleSearchTableColumn {
    name: string;
    key: string;
    keyPayload: string;
    keyData: string;
    keyRaw: KEY_RAW;
    keyHintTitle?: string;
    text: string;
    options: Array<any>;
    isCenterText: boolean;
    isFilter: boolean;
    order: number;
}