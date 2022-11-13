import { Injectable, TemplateRef } from '@angular/core';

import { IAngularMyDpOptions, IMyDate, IMyDateModel } from 'angular-mydatepicker';
import { Moment } from 'moment';
import * as moment from 'moment';

import { SagTableColumn } from 'sag-table';
import { GrossPriceKeyPipe } from 'sag-common';

import { TODAY, TWO_DAYS, SEVEN_DAYS, THIRTYONE_DAYS, ADD_THIRTYONE_DAYS } from '../settings.constant';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SAG_COMMON_DATETIME_FORMAT } from 'sag-common';

@Injectable()
export class InvoiceBusinessService {
    oldInvoice: boolean;

    constructor(
        private localStorageService: AppStorageService,
        private grossPriceKey: GrossPriceKeyPipe
    ) { }

    buildInvoiceDetailColumns() {
        return [
            {
                id: 'articleNr',
                i18n: 'COMMON_LABEL.COLUMNS.ARTICLE_NUMBER',
                filterable: true,
                sortable: true,
                width: '125px'
            },
            {
                id: 'articleTitle',
                i18n: 'COMMON_LABEL.COLUMNS.INFO',
                filterable: true,
                sortable: true
            },
            {
                id: 'vehicleInfo',
                i18n: 'COMMON_LABEL.COLUMNS.VEHICLE',
                filterable: true,
                sortable: true,
                width: '125px'
            },
            {
                id: 'quantity',
                i18n: 'COMMON_LABEL.COLUMNS.NUMBER',
                filterable: true,
                cellClass: 'text-right',
                class: 'text-right',
                sortable: true
            }
        ] as SagTableColumn[];
    }

    buildInvoiceOverviewColumns(templates?: TemplateRef<any>[]) {
        return [
            {
                id: 'orderNr',
                i18n: 'COMMON_LABEL.ORDER_NUMBER',
                filterable: true,
                sortable: true,
                width: '115px'
            },
            {
                id: 'invoiceNr',
                i18n: 'COMMON_LABEL.INVOICE_NUMBER',
                filterable: true,
                sortable: true,
                width: '120px'
            },
            {
                id: 'deliveryNoteNr',
                i18n: 'COMMON_LABEL.DELIVERY_NUMBER',
                filterable: true,
                sortable: true,
                width: '135px'
            },
            {
                id: 'customerNr',
                i18n: 'COMMON_LABEL.CUSTOMER_NUMBER',
                filterable: true,
                sortable: true,
                width: '115px'
            },
            {
                id: 'name',
                i18n: 'COMMON_LABEL.CUSTOMER_NAME',
                filterable: true,
                sortable: true,
                width: '100px'
            },
            {
                id: 'zipcode',
                i18n: 'COMMON_LABEL.POSTCODE',
                filterable: true,
                sortable: true,
                width: '100px'
            },
            {
                id: 'city',
                i18n: 'COMMON_LABEL.CITY',
                filterable: true,
                sortable: true,
                width: '100px'
            },
            {
                id: 'invoiceDate',
                i18n: 'COMMON_LABEL.DATE',
                sortable: true,
                type: 'date',
                dateFormat: SAG_COMMON_DATETIME_FORMAT,
                width: '135px'
            },
            {
                id: 'amount',
                i18n: 'INVOICE.GROSS_PRICE',
                filterable: true,
                sortable: true,
                type: 'currency',
                width: '150px',
                currencyOptions: {
                    isAllowNegative: true
                }
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                width: '70px',
                cellTemplate: templates[0]
            }
        ] as SagTableColumn[];
    }

    getDatePickerOption() {
        return {
            dateRange: false,
            dateFormat: 'dd.mm.yyyy',
            sunHighlight: true,
            markCurrentDay: true,
            showSelectorArrow: true,
            showFooterToday: true,
            disableSince: this.buildDateData(moment().add(1, 'day')).singleDate.date,
            focusInputOnDateSelect: false
        } as IAngularMyDpOptions;
    }

    getDateRangeSelect() {
        return [
            {
                id: TODAY,
                code: `COMMON_LABEL.${TODAY}`
            },
            {
                id: TWO_DAYS,
                code: `COMMON_LABEL.${TWO_DAYS}`
            },
            {
                id: SEVEN_DAYS,
                code: `COMMON_LABEL.${SEVEN_DAYS}`
            },
            {
                id: THIRTYONE_DAYS,
                code: `COMMON_LABEL.${THIRTYONE_DAYS}`
            }
        ];
    }

    buildDateData(date: Moment) {
        return {
            isRange: false,
            singleDate: {
                date: {
                    year: date.year(),
                    month: date.month() + 1,
                    day: date.date()
                }
            }
        } as IMyDateModel;
    }

    buildDateFromRange(range: string) {
        switch (range) {
            case TODAY:
                return moment();
            case TWO_DAYS:
                return moment().subtract(2, 'days');
            case SEVEN_DAYS:
                return moment().subtract(7, 'days');
            case THIRTYONE_DAYS:
                return moment().subtract(31, 'days');
            case ADD_THIRTYONE_DAYS:
                return moment().add(31, 'days');
        }
    }

    openInvoicePDF(invoiceNr: string, orderNr: string, oldInvoice: boolean) {
        const token = {
            access_token: this.localStorageService.appToken,
            orderNr,
            oldInvoice
        };
        const link = `${environment.baseUrl}invoice/archives/${invoiceNr}/pdf?access_token=${token.access_token}&orderNr=${orderNr}&oldInvoice=${oldInvoice}`;
        window.open(link);
    }

    buildFullDateData(date: IMyDate) {
        return `${date.year}-${date.month}-${date.day}`;
    }
}
