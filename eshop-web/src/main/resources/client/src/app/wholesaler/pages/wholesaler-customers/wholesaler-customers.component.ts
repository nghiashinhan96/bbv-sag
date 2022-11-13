import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { get, isEmpty, keys } from 'lodash';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Subscription, of } from 'rxjs';
import { filter, map, tap, catchError, finalize } from 'rxjs/operators';
import { SagTableColumn, SagTableControl, TablePage } from 'sag-table';
import { SagConfirmationBoxComponent } from 'sag-common';
import { Constant } from 'src/app/core/conts/app.constant';
import { SpinnerService } from 'src/app/core/utils/spinner';
// tslint:disable-next-line: max-line-length
import { FinalCustomerSearchCriteria } from '../../../settings/models/final-customer/final-customer-criteria.model';
import { FinalCustomer, FinalCustomerTypes } from '../../../settings/models/final-customer/final-customer.model';
import { FinalCustomerService } from '../../../settings/services/final-customer.service';
import { UserService } from 'src/app/core/services/user.service';
import { UserDetail } from 'src/app/core/models/user-detail.model';
// tslint:disable-next-line: max-line-length
import { FinalCustomerFormModalComponent } from 'src/app/shared/final-customer/components/final-customer-form-modal/final-customer-form-modal.component';
import { AppModalService } from 'src/app/core/services/app-modal.service';

@Component({
    selector: 'connect-wholesaler-customers',
    templateUrl: 'wholesaler-customers.component.html',
    styleUrls: ['wholesaler-customers.component.scss']
})
export class WholesalerCustomersComponent implements OnInit, OnDestroy, SagTableControl {
    bsModalRef: BsModalRef;
    routerEventsSub: Subscription;
    userDetailSub: Subscription;

    user: UserDetail;

    searchModel: any = {
        orderDescByName: false
    };
    page: TablePage = new TablePage();
    sort = {
        field: 'name',
        direction: 'asc',
        force: true
    };

    columns = [];
    sortKeys = {
        name: 'orderDescByName'
    };
    notFoundText = '';

    @ViewChild('colTypeName', { static: true }) colTypeNameRef: TemplateRef<any>;
    @ViewChild('colActions', { static: true }) colActionsRef: TemplateRef<any>;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private modalService: BsModalService,
        private userService: UserService,
        private finalCustomerService: FinalCustomerService,
        private appModal: AppModalService
    ) {
        this.routerEventsSub = this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(() => {
                const navigation = router.getCurrentNavigation();
                if (get(navigation, 'previousNavigation.extras.state')) {
                    this.searchModel = navigation.previousNavigation.extras.state;
                }

                setTimeout(() => {
                    this.sort.field = keys(this.sortKeys).find(key => {
                        const sortKey = this.sortKeys[key];
                        return this.searchModel[sortKey] === false || this.searchModel[sortKey] === true;
                    });
                    this.sort.direction = this.searchModel[this.sortKeys[this.sort.field]] ? 'desc' : 'asc';
                    this.page.currentPage = (this.searchModel.page || 0) + 1;
                    this.searchModel = { ...this.searchModel };
                }, 0);

            });

        this.userDetailSub = this.userService.userDetail$.subscribe(user => {
            this.user = user;
        });
    }

    ngOnInit() {
        this.buildColumns();
    }

    ngOnDestroy() {
        this.routerEventsSub.unsubscribe();
        this.userDetailSub.unsubscribe();
    }

    searchTableData({ request, callback }) {
        const criteria = new FinalCustomerSearchCriteria(isEmpty(request.filter) ? null : request.filter);
        const page = request.page - 1;

        if (request.sort && request.sort.field) {
            const column = this.sortKeys[request.sort.field];
            const sortValue = request.sort.direction === 'desc';
            criteria.resetSort();
            criteria.sort[column] = sortValue;
        }

        Object.assign(this.searchModel, criteria.term, criteria.sort);

        this.finalCustomerService.searchFinalCustomers(page, Constant.DEFAULT_PAGE_SIZE, criteria).pipe(
            catchError(err => {
                this.notFoundText = err.error.code === Constant.NOT_FOUND_CODE ? 'MESSAGES.NO_RESULTS_FOUND' : 'MESSAGES.GENERAL_ERROR';
                return of({
                    finalCustomers: [],
                    total: 0
                });
            })
        ).subscribe((res) => {
            callback({
                rows: res.finalCustomers,
                totalItems: res.total,
                page: this.searchModel.page + 1
            });
        });
    }

    showUserList(event, item: FinalCustomer) {
        SpinnerService.start();
        event.preventDefault();
        event.stopPropagation();
        this.router.navigate([item.orgId], { relativeTo: this.route, state: {} });
    }

    openCreateCustomerModal() {
        this.appModal.modals = this.modalService.show(FinalCustomerFormModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                title: 'FINAL_CUSTOMER.ADD_NEW_CUSTOMER',
                user: this.user,
                init: this.finalCustomerService.getCustomerTemplate(),
                submit: (data) => {
                    this.createCustomer(data);
                }
            }
        });
    }

    openUpdateCustomerModal(user: FinalCustomer) {
        this.appModal.modals = this.modalService.show(FinalCustomerFormModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                title: 'WSS.EDIT_CUSTOMER_TITLE',
                user: this.user,
                init: this.getSelectedCustomer(user.orgId),
                submit: (data) => {
                    this.updateCustomer(user.orgId, data);
                }
            }
        });
    }

    openDeleteCustomerModal(customer: FinalCustomer) {
        this.appModal.modals = this.modalService.show(SagConfirmationBoxComponent, {
            ignoreBackdropClick: true,
            initialState: {
                title: 'COMMON_LABEL.CONFIRMATION',
                message: 'FINAL_CUSTOMER.DELETE_MESSAGE',
                okButton: 'COMMON_LABEL.CONFIRM_BTN',
                cancelButton: 'COMMON_LABEL.CLOSE',
                close: () => new Promise((resolve) => {
                    SpinnerService.start('connect-confirmation-box');
                    this.finalCustomerService.deleteFinalCustomer(customer.orgId)
                        .pipe(
                            finalize(() => {
                                SpinnerService.stop('connect-confirmation-box');
                            })
                        )
                        .subscribe(() => {
                            resolve(null);
                            setTimeout(() => {
                                this.searchModel = { ...this.searchModel };
                            }, 500);
                        }, ({ error }) => {
                            resolve(null);
                            this.openDeleteErrorModal();
                        });
                })
            }
        });
    }

    openDeleteErrorModal() {
        this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'FINAL_CUSTOMER.CUSTOMER_COULD_NOT_DELETE',
                okButton: 'COMMON_LABEL.YES',
                showCancelButton: false,
                showHeaderIcon: false,
                showCloseButton: true,
            }
        });
    }

    private buildColumns() {
        this.columns = [
            {
                id: 'name',
                i18n: 'COMMON_LABEL.COMPANY_NAME',
                filterable: true,
                sortable: true,
                width: '180px'
            },
            {
                id: 'finalCustomerType',
                i18n: 'ORDER_DASHBOARD.COMPANY_TYPE',
                filterable: true,
                sortable: false,
                width: '180px',
                type: 'select',
                selectSource: this.buildCompanyTypes(),
                selectValue: 'value',
                selectLabel: 'label',
                cellTemplate: this.colTypeNameRef
            },
            {
                id: 'addressInfo',
                i18n: 'COMMON_LABEL.ADDRESS',
                filterable: true,
                sortable: false,
                width: 'auto'
            },
            {
                id: 'contactInfo',
                i18n: 'ORDER_DASHBOARD.CONTACT_INFO',
                filterable: true,
                sortable: false,
                width: '180px'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                width: '100px',
                cellClass: 'align-middle',
                cellTemplate: this.colActionsRef
            }
        ] as SagTableColumn[];
    }

    private buildCompanyTypes() {
        return [
            {
                value: null,
                label: 'COMMON_LABEL.ALL'
            },
            {
                value: 'ONLINE',
                label: FinalCustomerTypes.ONLINE
            },
            {
                value: 'PASSANT',
                label: FinalCustomerTypes.PASSANT
            }
        ];
    }

    private getSelectedCustomer(id: number) {
        return this.finalCustomerService.getSelectedCustomer(id)
            .pipe(tap(res => {
                if (res.selectedFinalCustomer) {
                    res.permissions = res.selectedFinalCustomer.perms;
                }
            }));
    }

    private createCustomer({ data, onSuccess, onError }) {
        this.finalCustomerService.createFinalCustomer(data).subscribe(
            () => {
                onSuccess();
                setTimeout(() => {
                    this.searchModel = { ...this.searchModel };
                }, 500);
            },
            () => {
                onError();
            }
        );
    }

    private updateCustomer(customerId: number, { data, onSuccess, onError }) {
        this.finalCustomerService.updateFinalCustomer(customerId, data).subscribe(
            () => {
                onSuccess();
                setTimeout(() => {
                    this.searchModel = { ...this.searchModel };
                }, 500);
            },
            () => {
                onError();
            }
        );
    }
}
