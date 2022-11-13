import { Injectable } from '@angular/core';
import { DmsService } from '../services/dms.service';
import { ExportOrder } from '../models/export-order.model';
import { CollectionUtil } from 'src/app/dms/utils/collection.util';
import { ReturnBasket } from '../models/return-basket.model';
import { DmsConstant } from '../constants/dms.constant';
import { ExportOrderDetail } from '../models/export-order-detail.model';
import { ReturnBasketPart } from '../models/return-basket-part.model';
import { ReturnBasketTime } from '../models/return-basket-time.model';
import { DmsProcessor } from './dms-processor';
import { map } from 'rxjs/operators';
import { SagConfirmationBoxComponent } from 'sag-common';


@Injectable({
    providedIn: 'root'
})
export class CloudDmsProcessor extends DmsService implements DmsProcessor {
    private exportResolver = [];
    public async export(exportOrder: ExportOrder) {
        try {
            await this.doExport(exportOrder);
        } catch (ex) {
            return this.showRetryReturnBasket(exportOrder);
        }
    }

    private async doExport(exportOrder: ExportOrder) {
        const returnBasket = this.convertExportOrderToReturnBasket(exportOrder);
        return await this.postReturnBasket(returnBasket, exportOrder.hookUrl).pipe(map(() => {
            this.appStorage.cleanRefText(exportOrder.customerNr);
            this.basketService.removeAllBasket().subscribe();
            this.removeDmsInfo();
            this.userService.logout();
            return '';
        })).toPromise();
    }

    private resolveAllExportResolvers(mess?) {
        while (this.exportResolver.length) {
            const resolve = this.exportResolver.pop();
            resolve(mess);
        }
    }

    private showRetryReturnBasket(exportOrder: ExportOrder) {
        return new Promise(resolve => {
            this.exportResolver.push(resolve);
            this.modalService.show(SagConfirmationBoxComponent, {
                class: 'modal-confirm',
                ignoreBackdropClick: true,
                initialState: {
                    message: 'DMS.MESSAGE.RETRY',
                    close: () => {
                        return new Promise(res => {
                            this.doExport(exportOrder)
                                .then(() => {
                                    res('');
                                    this.resolveAllExportResolvers();
                                })
                                .catch(() => {
                                    res();
                                    return this.showRetryReturnBasket(exportOrder);
                                });
                        });
                    },
                    cancel: () => {
                        this.resolveAllExportResolvers(this.getReturnBaksetErrorMessage());
                    }
                }
            });
        });
    }

    private convertExportOrderToReturnBasket(exportOrder: ExportOrder): ReturnBasket {
        let parts = [];
        let times = [];
        if (!CollectionUtil.isEmpty(exportOrder.orders)) {
            const orders = exportOrder.orders;
            parts = orders.filter(order => !order.articleNumber.startsWith(DmsConstant.LABOUR_TIME_PREFIX))
                .map(order => this.convertExportOrderItemToReturnBasketPart(order));

            times = orders.filter(order => order.articleNumber.startsWith(DmsConstant.LABOUR_TIME_PREFIX))
                .map(order => this.convertExportOrderItemToReturnBasketTime(order));
        }
        return {
            parts,
            times,
            customer_nr: exportOrder.customerNr,
            order_type: exportOrder.requestType,
            order_number: exportOrder.orderNumber,
            order_date: exportOrder.orderDate,
            order_total_with_vat: exportOrder.totalPriceInclVat,
            order_total_no_vat: exportOrder.totalPrice,
            delivery_type: exportOrder.deliveryType,
            payment_type: exportOrder.paymentMethod,
            address_company: exportOrder.companyName,
            address_street: exportOrder.street,
            address_postcode: exportOrder.postCode,
            address_city: exportOrder.city,
            order_note: exportOrder.note,
        } as ReturnBasket;
    }

    private convertExportOrderItemToReturnBasketPart(order: ExportOrderDetail): ReturnBasketPart {
        return {
            art_nr: order.articleNumber,
            art_id: order.articleId,
            total_price_gross_with_vat: order.totalGrossPriceIncl,
            total_price_gross_no_vat: order.totalGrossPrice,
            price_gross_no_vat: order.grossPrice,
            art_description: order.description,
            quantity: order.quantity,

            total_price_net_with_vat: order.totalNetPriceInclVat,
            total_price_net_no_vat: order.totalNetPrice,
            price_net_no_vat: order.netPrice,
            total_price_uvpe_with_vat: order.totalUvpeIncl,
            total_price_uvpe_no_vat: order.totalUvpe,
            price_uvpe_no_vat: order.uvpe,
            vin: '',
            brand: order.brand,
            art_additional_description: order.artAdditionalDescription,
            from_branch_time: order.fromBranchTime,
            delivery_status: order.deliveryStatus,
            delivery_note: order.deliveryNote
        } as ReturnBasketPart;
    }

    private convertExportOrderItemToReturnBasketTime(order: ExportOrderDetail): ReturnBasketTime {
        return {
            art_nr: order.articleNumber,
            total_price_with_vat: order.totalGrossPriceIncl,
            total_price_no_vat: order.totalGrossPrice,
            hourly_rate: order.grossPrice,
            quantity: order.quantity,
            position_description: order.description,
            vin: '',
        } as ReturnBasketTime;
    }

    public getReturnBaksetErrorMessage() {
        return 'DMS.ERROR.SUBMIT_FAILED';
    }

}
