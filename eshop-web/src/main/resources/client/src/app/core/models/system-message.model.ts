export interface SystemMessage {
    id: number;
    area: string;
    content: string;
    style: string;
    subArea: string;
    type: string;
    visibility: string;
    ssoTraining?: boolean;
    ssoInfo?: any;
}
