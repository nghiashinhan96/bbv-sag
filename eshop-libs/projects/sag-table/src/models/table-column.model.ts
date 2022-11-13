import { TemplateRef } from '@angular/core';

export class SagTableColumn {
    id: string;
    i18n: string;
    type?: 'input' | 'select' | 'currency' | 'number' | 'datepicker' | 'date' | 'daterangepicker' | '';
    filterable: boolean;
    sortable: boolean;
    headerTemplate?: TemplateRef<any>;
    cellTemplate?: TemplateRef<any>;
    filterTemplate?: TemplateRef<any>;
    width?: string;
    class?: string;
    cellClass?: string;
    disabled?: boolean;
    selectSource?: any[];
    selectValue?: string;
    selectLabel?: string;
    defaultValue?: string;
    // only work if type is Date
    dateFormat?: string;
    ellipsis?: boolean;
    sortKey?: string;
    currencyOptions?: any;
}
