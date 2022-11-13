export interface SagMessageData {
    message: string | any;
    type: 'ERROR' | 'WARNING' | 'SUCCESS' | 'INFO';
    params?: any;
    icon?: string;
    extras?: any;
}
