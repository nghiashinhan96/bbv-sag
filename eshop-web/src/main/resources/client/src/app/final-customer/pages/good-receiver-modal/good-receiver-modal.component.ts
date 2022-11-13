import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FinalCustomerService } from '../../services/final-customer.service';
import { SagTableColumn, SagTableControl, SagTableRequestModel, SagTableResponseModel } from 'sag-table';
import { FinalCustomerModel } from '../../models/final-customer.model';
import { FinalCustomerSearchRequestModel } from '../../models/final-customer-search-request.model';
import { APP_DEFAULT_PAGE_SIZE } from 'src/app/core/conts/app.constant';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FINAL_CUSTOMER_TYPE } from '../../enums/final-customer-type.enum';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Component({
    selector: 'connect-good-receiver-modal',
    templateUrl: './good-receiver-modal.component.html',
    styleUrls: ['./good-receiver-modal.component.scss']
})
export class GoodReceiverModalComponent implements OnInit, SagTableControl {
    columns: SagTableColumn[] = [];
    sort = {
        field: 'name',
        direction: 'desc',
        force: true
    };
    selectedCustomer: FinalCustomerModel = null;
    CUSTOMER_TYPE = FINAL_CUSTOMER_TYPE;
    close: () => void;
    @ViewChild('actionRef', { static: true }) actionRef: TemplateRef<any>;
    @ViewChild('statusRef', { static: true }) statusRef: TemplateRef<any>;
    constructor(
        private finalCustomerService: FinalCustomerService,
        public bsModalRef: BsModalRef,
        private appStorage: AppStorageService
    ) { }

    ngOnInit() {
        this.columns = [
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                width: '40px',
                cellTemplate: this.actionRef
            },
            {
                id: 'name',
                i18n: 'COMMON_LABEL.COMPANY_NAME',
                filterable: true,
                sortable: true
            },
            {
                id: 'finalCustomerType',
                i18n: 'ORDER_DASHBOARD.COMPANY_TYPE',
                filterable: true,
                sortable: false,
                type: 'select',
                cellTemplate: this.statusRef,
                selectSource: [
                    {
                        value: null,
                        label: 'COMMON_LABEL.ALL'
                    },
                    {
                        value: 'ONLINE',
                        label: FINAL_CUSTOMER_TYPE.ONLINE
                    },
                    {
                        value: 'PASSANT',
                        label: FINAL_CUSTOMER_TYPE.PASSANT
                    }
                ],
                selectValue: 'value',
                selectLabel: 'label',
                width: '120px',
                defaultValue: null
            },
            {
                id: 'addressInfo',
                i18n: 'COMMON_LABEL.ADDRESS',
                filterable: true,
                sortable: false
            },
            {
                id: 'contactInfo',
                i18n: 'ORDER_DASHBOARD.CONTACT_INFO',
                filterable: true,
                sortable: false
            }
        ];
    }

    searchTableData({ request, callback }: { request: SagTableRequestModel; callback(data: SagTableResponseModel): void; }): void {
        const requestObj = new FinalCustomerSearchRequestModel({
            pageNr: request.page - 1,
            size: APP_DEFAULT_PAGE_SIZE,
            criteria: {
                term: Object.assign((request.filter || {}), { status: 'ACTIVE' }),
                sort: request.sort
            }
        } as FinalCustomerSearchRequestModel);

        this.finalCustomerService.searchFinalCustomers(requestObj).subscribe((res: any) => {
            callback({
                rows: (res && res.content || []).map(item => new FinalCustomerModel(item)),
                totalItems: res.totalElements,
                itemsPerPage: res.size,
                page: res.number + 1
            });
        });
    }

    take() {
        SpinnerService.start('connect-good-receiver-modal');
        this.finalCustomerService.getFinalCustomer(this.selectedCustomer.orgId).subscribe(res => {
            this.appStorage.goodReceiver = res;
            SpinnerService.stop('connect-good-receiver-modal');
            this.bsModalRef.hide();
        });
    }

    removeCustomer() {
        this.selectedCustomer = null;
        this.appStorage.goodReceiver = null;
        this.bsModalRef.hide();
    }

    onSelect(row) {
        this.selectedCustomer = row;
    }
}
