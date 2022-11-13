import { environment } from 'src/environments/environment';
import { Injectable, TemplateRef } from '@angular/core';
import * as moment from 'moment';
import { SagTableColumn } from 'sag-table';
import { AffiliateUtil } from 'sag-common';
import { DateUtil } from 'src/app/core/utils/date.util';
import { OrderCondition } from '../models/order-history/order-condition.model';
import { ArticleAvailabilityModel } from 'sag-article-detail';
import { GrossPriceKeyPipe } from 'sag-common';
import { UserService } from 'src/app/core/services/user.service';

@Injectable({
    providedIn: 'root'
})
export class OrderHistoryBusinessService {
    isCz = AffiliateUtil.isCz(environment.affiliate);
    isCzBased = AffiliateUtil.isAffiliateCZ(environment.affiliate) || AffiliateUtil.isAxCz(environment.affiliate);

    constructor(
        private grossPriceKey: GrossPriceKeyPipe,
        private userService: UserService
    ) { }

    buildTableColumns(templates: TemplateRef<any>[]) {
        const tableColumn = [
            {
                id: 'date',
                sortKey: 'dateNumber',
                i18n: 'SETTINGS.MY_ORDER.HEADER.ORDER_DATE',
                filterable: true,
                sortable: true,
                width: '135px'
            },
            {
                id: 'status',
                i18n: 'SETTINGS.MY_ORDER.HEADER.ORDER_STATUS',
                filterable: true,
                sortable: true,
                width: '100px'
            },
            {
                id: 'nr',
                i18n: 'SETTINGS.MY_ORDER.HEADER.ORDER_NUMBER',
                filterable: true,
                sortable: true,
                width: '130px'
            },
            {
                id: 'vehicleInfos',
                i18n: 'SETTINGS.MY_ORDER.HEADER.VEHICLE',
                filterable: true,
                sortable: true,
                width: '240px'
            },
            {
                id: 'username',
                i18n: 'SETTINGS.MY_ORDER.HEADER.USERNAME',
                filterable: true,
                sortable: true,
                width: '100px'
            },
            {
                id: 'source',
                i18n: 'SETTINGS.MY_ORDER.HEADER.SOURCE',
                filterable: true,
                sortable: true,
                width: '100px',
                type: 'input'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                width: '50px',
                cellClass: 'text-center',
                cellTemplate: templates[0]
            }
        ] as SagTableColumn[];
        if (this.userService.userDetail.hasWholesalerPermission) {
            tableColumn.splice(6, 0, {
                id: 'goodsReceiverName',
                i18n: 'SETTINGS.MY_ORDER.HEADER.CUSTOMER_NAME',
                filterable: true,
                sortable: true,
                width: '130px',
            });
        }
        if (this.isCz) {
            return tableColumn.filter(item => ['status', 'username'].indexOf(item.id) === -1);

        }
        return tableColumn;
    }

    buildOrderDetailItemsColumn(templateRef: TemplateRef<any>[]) {
        return [
            {
                id: 'articleNumber',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.ARTICLE_NUMBER',
                width: '225px',
                cellTemplate: templateRef[0]
            },
            {
                id: 'vehicleInfo',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.VEHICLE',
                filterable: true,
                sortable: true,
                cellTemplate: templateRef[1],
                width: '200px'
            },
            {
                id: 'grossPrice',
                i18n: this.isCz ? 'SETTINGS.MY_ORDER.DETAIL.HEADER.GROSS_PRICE' : this.grossPriceKey.transform(environment.affiliate),
                filterable: true,
                sortable: true,
                width: '100px',
                cellTemplate: templateRef[2],
                cellClass: 'text-right',
                class: 'text-right'
            },
            {
                id: 'quantity',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.QUANTITY',
                filterable: true,
                sortable: true,
                width: '80px',
                cellClass: 'text-right',
                class: 'text-right',
                cellTemplate: templateRef[3]
            },
            {
                id: 'deliveryInformation',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.DELIVERY_INFORMATION',
                filterable: true,
                sortable: true,
                width: '200px',
                cellTemplate: templateRef[4]
            },
            {
                id: 'additionalText',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.REFERENCE',
                filterable: true,
                sortable: true,
                width: '133px',
                cellTemplate: templateRef[5]
            }
        ] as SagTableColumn[];
    }

    buildFinalUserAdminOrderDetailItemsColumn(templateRef: TemplateRef<any>[]) {
        return [
            {
                id: 'articleNumber',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.ARTICLE_NUMBER',
                width: '230px',
                cellTemplate: templateRef[0]
            },
            {
                id: 'vehicleDesc',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.VEHICLE',
                filterable: true,
                sortable: true,
                width: '200px'
            },
            {
                id: 'grossPrice',
                i18n: this.grossPriceKey.transform(environment.affiliate),
                filterable: true,
                sortable: true,
                width: '100px',
                type: 'currency'
            },
            {
                id: 'quantity',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.QUANTITY',
                filterable: true,
                sortable: true,
                width: '80px',
                cellClass: 'text-right',
                class: 'text-right'
            },
            {
                id: 'deliveryInformation',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.DELIVERY_INFORMATION',
                filterable: true,
                sortable: false,
                width: '200px',
                cellTemplate: templateRef[1]
            },
            {
                id: 'reference',
                i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.REFERENCE',
                filterable: true,
                sortable: true,
                width: '133px'
            }
        ] as SagTableColumn[];
    }

    buildSaleOrderHistoryColumn(templateRef: TemplateRef<any>[], typeList: any[]) {
        return [
            {
                id: 'customerNr',
                i18n: 'COMMON_LABEL.COLUMNS.CUSTOMER_NR',
                width: '80px',
                filterable: true,
                sortable: true,
            },
            {
                id: 'customerName',
                i18n: 'COMMON_LABEL.COLUMNS.CUSTOMER_NAME',
                width: '160px',
                filterable: true,
                sortable: true,
            },
            {
                id: 'orderNumber',
                i18n: 'ORDER_HISTORY.COLUMNS.ORDER_NUMBER',
                width: '100px',
                filterable: true,
                sortable: true,
            },
            {
                id: 'createdDateDisp',
                i18n: 'COMMON_LABEL.COLUMNS.SAVING_DATE',
                width: '100px',
                filterable: true,
                sortable: true,
                type: 'datepicker',
                filterTemplate: templateRef[0]
            },
            {
                id: 'type',
                i18n: 'ORDER_HISTORY.COLUMNS.TYPE',
                width: '90px',
                cellTemplate: templateRef[1],
                filterable: true,
                sortable: true,
                type: 'select',
                selectValue: 'value',
                selectLabel: 'label',
                selectSource: typeList,
                defaultValue: typeList[0].value
            },
            {
                id: 'totalPrice',
                i18n: 'COMMON_LABEL.COLUMNS.TOTAL',
                width: '110px',
                filterable: true,
                sortable: true,
                cellClass: 'text-right'
            },
        ] as SagTableColumn[];
    }

    buildDeliveryInfo(availabilities: ArticleAvailabilityModel[] = [], sendMethodCode = '') {
        let message = [];
        if (availabilities && !availabilities.length) {
            if (this.isCz && sendMethodCode) {
                const orderCondition = new OrderCondition();
                orderCondition.setStateForSendMethodCode(sendMethodCode);
                if (orderCondition.isTourMode) {
                    return [
                        'CONDITION.DELIVERY_TYPE_TOUR'
                    ];
                } else {
                    return [
                        'CONDITION.DELIVERY_TYPE_PICKUP'
                    ];
                }
            }
            return message;
        }
        const latestTimeAvail = availabilities.map(avail => ({
            tourName: avail.tourName,
            sendMethodCode: avail.sendMethodCode,
            arrivalTime: avail.arrivalTime
        }))
            .reduce((preTime, curTime) => {
                const pre = moment(preTime.arrivalTime || 0);
                const cur = moment(curTime.arrivalTime || 0);
                return pre.isAfter(cur) ? preTime : curTime;
            });
        const orderCondition = new OrderCondition();
        orderCondition.setStateForSendMethodCode(latestTimeAvail.sendMethodCode || sendMethodCode);
        const deliveryTime = [
            DateUtil.formatDateInDate(latestTimeAvail.arrivalTime),
            'SETTINGS.MY_ORDER.DETAIL.AROUND',
            DateUtil.formatDateInTime(latestTimeAvail.arrivalTime)
        ]

        if (orderCondition.isTourMode) {
            orderCondition.tourName = latestTimeAvail.tourName ? latestTimeAvail.tourName : '';
            message = [
                'CONDITION.DELIVERY_TYPE_TOUR'
            ];
            if (orderCondition.tourName) {
                message = [
                    ...message,
                    'CONDITION.WITH_TOUR',
                    orderCondition.tourName
                ];
            }
            if (latestTimeAvail.arrivalTime) {
                message = [
                    ...message,
                    'CONDITION.TOUR_TIME',
                    'CONDITION.AT_THE',
                    ...deliveryTime
                ];
            }
        } else if (orderCondition.isPickupMode) {
            message = [
                'CONDITION.DELIVERY_TYPE_PICKUP'
            ];
            if (latestTimeAvail.arrivalTime) {
                message = [
                    ...message,
                    'CONDITION.PICKUP_TIME',
                    'CONDITION.AT_THE',
                    ...deliveryTime
                ];
            } else {
                if (!this.isCz) {
                    message = [
                        ...message,
                        'ARTICLE.DELIVERY_IMMEDIATE'
                    ];
                }
            }
        } else if (orderCondition.isCourier) {
            message = [
                'CONDITION.DELIVERY_TYPE.TITLE',
                ': ',
                'CONDITION.DELIVERY_TYPE.COURIER'
            ];
            if (latestTimeAvail.arrivalTime) {
                message = [
                    ...message,
                    ...deliveryTime
                ];
            }
        }

        return message;
    }

    buildFCDeliveryInfo(availabilities: ArticleAvailabilityModel[] = []): string[] {
        let message = [];
        if (availabilities && !availabilities.length) {
            return message;
        }

        if (!this.userService.userDetail.hasAvailabilityPermission) {
            return message;
        }

        const latestTimeAvail = availabilities.map(avail => ({
            tourName: avail.tourName,
            sendMethodCode: avail.sendMethodCode,
            arrivalTime: avail.arrivalTime
        }))
            .reduce((preTime, curTime) => {
                const pre = moment(preTime.arrivalTime);
                const cur = moment(curTime.arrivalTime);
                return pre.isAfter(cur) ? preTime : curTime;
            });
        const orderCondition = new OrderCondition();
        orderCondition.setStateForSendMethodCode(latestTimeAvail.sendMethodCode);
        const deliveryTime = [
            DateUtil.formatDateInDate(latestTimeAvail.arrivalTime),
            'SETTINGS.MY_ORDER.DETAIL.AROUND',
            DateUtil.formatDateInTime(latestTimeAvail.arrivalTime)
        ]

        if (orderCondition.isTourMode) {
            orderCondition.tourName = latestTimeAvail.tourName ? latestTimeAvail.tourName : '';
            message = [
                'CONDITION.DELIVERY_TYPE_TOUR',
                'CONDITION.WITH_TOUR',
                orderCondition.tourName,
                'CONDITION.FC_TOUR_TIME',
                'CONDITION.AT_THE',
                ...deliveryTime,
                'CONDITION.AT_THE_SUFFIX'
            ];
        } else {
            message = [
                'CONDITION.DELIVERY_TYPE_PICKUP',
                'CONDITION.FC_PICKUP_TIME',
                'CONDITION.AT_THE'
            ];
            message = message.concat(latestTimeAvail.arrivalTime ? [...deliveryTime, 'CONDITION.AT_THE_SUFFIX'] : ['ARTICLE.DELIVERY_IMMEDIATE']);
        }
        return message;
    }

    buildSaleOrderDetailTable() {
        return [
            {
                id: 'quantity',
                i18n: 'COMMON_LABEL.COLUMNS.NUMBER',
                width: '10%',
                filterable: true,
                sortable: true,
            },
            {
                id: 'articleNr',
                i18n: 'COMMON_LABEL.COLUMNS.ARTICLE_NR',
                width: '20%',
                filterable: true,
                sortable: true,
            },
            {
                id: 'info',
                i18n: 'COMMON_LABEL.COLUMNS.INFO',
                width: '35%',
                filterable: true,
                sortable: true,
            },
            {
                id: 'vehicle',
                i18n: 'COMMON_LABEL.COLUMNS.VEHICLE',
                width: '35%',
                filterable: true,
                sortable: true,
            }
        ] as SagTableColumn[];
    }
}
