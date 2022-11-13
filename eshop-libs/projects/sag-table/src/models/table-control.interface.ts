import { SagTableRequestModel } from './table-request.model';
import { SagTableResponseModel } from './table-respose.model';

export interface SagTableControl {
    searchTableData({ request, callback }: { request: SagTableRequestModel; callback(data: SagTableResponseModel): void }): void;
}
