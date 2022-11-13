import { Component, OnInit, Input, ViewChild, OnDestroy, Output, EventEmitter, ElementRef, AfterViewInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';


import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs/internal/observable/of';
import { catchError, map, tap } from 'rxjs/internal/operators';
import { finalize, debounceTime, distinctUntilChanged, filter } from 'rxjs/operators';
import { PopoverDirective } from 'ngx-bootstrap/popover';

import { UserDetail } from 'src/app/core/models/user-detail.model';
import { environment } from 'src/environments/environment';
import { APP_DEFAULT_PAGE_SIZE } from 'src/app/core/conts/app.constant';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { CustomerDataModel } from 'src/app/core/models/customer-data.model';
import { UserService } from 'src/app/core/services/user.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { CustomerModel } from 'src/app/core/models/customer.model';
import { CustomerSearchService } from '../../service/customer-search.service';

import { SagCurrencyPipe, SagNumericService } from 'sag-currency';
import { Subscription } from 'rxjs';
import { CustomerUtil } from 'src/app/core/utils/customer.util';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { OrderHistoryService } from 'src/app/settings/services/order-history.service';
import { OrderHistoryDetailRequest } from 'src/app/settings/models/order-history/order-history-detail.model';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { Validator } from 'src/app/core/utils/validator';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';

const MAX_CUSTOMER_TAB_SIZE = 6;
const EPSILON_HEIGHT = 20;
const RECENT_ORDERS_SIZE = 5;

@Component({
    selector: 'connect-sale-control',
    templateUrl: './sale-control.component.html',
    styleUrls: ['./sale-control.component.scss']
})
export class SaleControlComponent implements OnInit, OnDestroy, AfterViewInit {
    @Input() user: UserDetail;
    @Input() activeSection: string;
    @Output() activeSectionChange = new EventEmitter<string>();

    searchForm: FormGroup;

    orderHistoriesLoading = false;
    orders: any[];

    isLoading = false;
    isSearchFreeText = false;
    total: number;
    customers: any[];
    customerTabs: CustomerModel[];
    errorMessage: string;

    customerInfoHeader = '';
    private customerSubscription: Subscription;

    @ViewChild('pop', { static: true }) resultPopup: PopoverDirective;
    @ViewChild('searchInput', { static: true }) searchInput: ElementRef;
    private isAutoSearch = false;
    private searchCustomerSub: Subscription;
    constructor(
        private fb: FormBuilder,
        private customerSearchService: CustomerSearchService,
        private userService: UserService,
        private appContextService: AppContextService,
        private appStore: AppStorageService,
        private currencyPipe: SagCurrencyPipe,
        private currencyService: SagNumericService,
        private fbRecordingService: FeedbackRecordingService,
        private translateService: TranslateService,
        private orderHistoryService: OrderHistoryService,
        private shoppingBasketService: ShoppingBasketService
    ) { }

    typeof(val) {
        return typeof (val);
    }
    ngOnInit() {
        this.searchForm = this.fb.group({
            affiliate: environment.affiliate,
            text: '',
            customerNr: '',
            telephone: '',
            offset: 0,
            size: APP_DEFAULT_PAGE_SIZE
        });
        this.searchForm.get('text').valueChanges.pipe(
            debounceTime(600),
            map((val: string) => (val || '').replace(/\s\s+/g, ' ').trim()),
            distinctUntilChanged(),
            filter((val: string) => {
                const text = val.replace(/\s/g, '');
                return text.length > 2;
            })
        ).subscribe(val => {
            if (val) {
                this.isSearchFreeText = true;
                this.searchCustomers(() => {
                    this.isSearchFreeText = false;
                }, true);
            }
        });

        this.customerSubscription = this.appStore.observeCustomers().subscribe(tabs => {
            this.customerTabs = tabs || [];
        });
        this.userService.userDetail$.subscribe(userDetail => {
            this.customerInfoHeader = '';
            if (userDetail && userDetail.isSalesOnBeHalf) {
                this.customerInfoHeader = CustomerUtil.buildCustomerInfo(userDetail);
            }
        });

    }

    ngAfterViewInit(): void {
        setTimeout(() => {
            this.searchInput.nativeElement.focus();
        });
    }

    ngOnDestroy(): void {
        if (this.customerSubscription) {
            this.customerSubscription.unsubscribe();
        }
    }

    get isOpenInfoBox() {
        if (this.customerTabs && this.customerTabs.length > 0) {
            const customer = this.customerTabs.find(cus => cus.nr.toString() === this.user.custNr);
            if (customer) {
                return customer.isOpenInfoBox;
            }
        }

        return false;
    }

    cleanOtherDataOnFocus(event, focusControlName) {
        const tmpVal = this.searchForm.get(focusControlName).value;
        this.searchForm.patchValue({
            text: '',
            customerNr: '',
            telephone: ''
        });
        this.searchForm.get(focusControlName).setValue(tmpVal);
    }

    removeCustomer(event, customerNr: number, customerIndex: number) {
        event.preventDefault();
        const customers = this.appStore.removeCustomer(customerNr);
        this.fbRecordingService.resetModel();
        this.appContextService.clearContextInCache().subscribe(() => { });
        if (customers.length === 0) {
            const spinner = SpinnerService.startApp();
            this.errorMessage = null;
            this.userService.backToSale().subscribe(() => {
                SpinnerService.stop(spinner);
                setTimeout(() => {
                    this.searchInput.nativeElement.focus();
                });
            });
            // Hide minibasket popover
            this.shoppingBasketService.toggleMiniBasket(true);
        } else {
            const nextCustomerIndex = customerIndex > customers.length - 1 ? customers.length - 1 : customerIndex;
            const nextCustomer = customers[nextCustomerIndex];
            if (Number(customerNr) === Number(this.user.custNr)) {
                this.selectCustomer(nextCustomer.nr);
            }
        }
    }

    onSelectCustomerFromHistory({ customerNr, orderId }) {
        this.searchForm.get('customerNr').setValue(customerNr);
        this.searchForm.get('text').setValue('');
        this.selectCustomer(customerNr, orderId);
    }

    onSelectCustomerFromSearch(customerNr: number) {
        this.searchForm.get('customerNr').setValue(customerNr);
        this.searchForm.get('text').setValue('');
        this.selectCustomer(customerNr);
        this.resultPopup.hide();
    }

    selectCustomer(customerNr: number, orderId?: number) {
        // Hide minibasket popover
        this.shoppingBasketService.toggleMiniBasket(true);
        if (Number(customerNr) === Number(this.user.custNr)) {
            if (orderId) {
                const addBasketSpinner = SpinnerService.startApp();
                this.addOrderToBasket(orderId).pipe(
                    finalize(() => SpinnerService.stop(addBasketSpinner))
                ).subscribe();
            }
            return;
        }
        const spinner = SpinnerService.startApp();
        // check is existed customer
        let customerSearchObserve$;
        this.errorMessage = null;
        const foundCustomer = this.customerTabs.find(c => Number(c.nr) === Number(customerNr));
        if (!!foundCustomer) {
            customerSearchObserve$ = this.userService.authorizeCustomer(foundCustomer);
        } else {
            if (this.customerTabs.length < MAX_CUSTOMER_TAB_SIZE) {
                customerSearchObserve$ = this.switchUser(customerNr);
            } else {
                customerSearchObserve$ = of(null);
                this.errorMessage = this.translateService.instant('AX_CONNECTION.ERROR.CUSTOMER_LIMIT',
                    {
                        size: MAX_CUSTOMER_TAB_SIZE
                    }
                );
            }
        }

        customerSearchObserve$.pipe(
            finalize(() => SpinnerService.stop(spinner))
        ).subscribe(async (res) => {
            if (res) {
                await this.addOrderToBasket(orderId).toPromise();
                this.searchForm.patchValue({
                    text: '',
                    customerNr: '',
                    telephone: ''
                });
                this.customers = [];
                this.activeSection = 'connect';
                this.activeSectionChange.emit('connect');
                this.errorMessage = null;
                this.appStore.resetOpenInfoBox(customerNr.toString(), true);
            }
        });
    }

    onScroll(event) {
        const container = event.currentTarget;
        if ((container.scrollHeight - container.scrollTop) >= (container.clientHeight - EPSILON_HEIGHT)) {
            this.onScrollToEnd();
        }
    }

    onScrollToEnd() {
        if (this.isLoading || this.customers.length >= this.total) {
            return;
        }
        let offset = this.searchForm.get('offset').value;
        offset = offset + 1;
        this.searchForm.get('offset').setValue(offset);
        this.searchCustomerData().subscribe(res => {
            this.customers = [...this.customers, ...res];
        });
    }

    searchCustomers(callback, isAutoSearch = false) {
        this.isAutoSearch = isAutoSearch;
        this.searchForm.get('offset').setValue(0);
        const body = this.searchForm.value;
        const customerNr = Number(body.customerNr);
        if (!!customerNr) {
            this.selectCustomer(customerNr);
            callback();
        } else if (body.text || body.telephone) {
            if (this.searchCustomerSub) {
                this.searchCustomerSub.unsubscribe();
            }
            this.searchCustomerSub = this.searchCustomerData()
                .subscribe(customers => {
                    callback();
                    if (this.isAutoSearch) {
                        this.customers = customers;
                        this.resultPopup.show();
                    } else {
                        if (customers.length > 0) {
                            this.customers = customers;
                            this.resultPopup.show();
                        } else {
                            this.resultPopup.hide();
                            this.errorMessage = this.errorHandler({
                                error_code: 'NOT_FOUND_CUSTOMER'
                            });
                        }
                    }

                });
        } else {
            callback();
        }
    }

    getOrderHistories() {
        if (this.orders) {
            return this.orders;
        }
        const body = {
            page: 0,
            size: RECENT_ORDERS_SIZE
        };
        this.orderHistoriesLoading = true;
        this.customerSearchService.getSaleOrders(body).pipe(
            map((orders: any) => {
                return (orders && orders.content || []).map(order => this.buildRecentOrdersList(order));
            }),
            catchError((error) => {
                return of([]);
            }),
            finalize(() => {
                this.orderHistoriesLoading = false;
            })
        ).subscribe(res => {
            this.orders = res;
        });
    }

    allowNumber(event) {
        return Validator.allowNumber(event);
    }

    private buildRecentOrdersList(order) {
        const totalPrice = this.currencyPipe.transform(order.totalPrice);
        const orderDate = order.createdDateDisp;
        const currencyUnit = this.currencyService.getCurrency();
        const label = `${orderDate} - ${order.nr} - ${currencyUnit}${totalPrice} ${order.customerNr}`;
        return {
            label,
            orderId: Number(order.id),
            customerNr: Number(order.customerNr)
        };
    }

    private switchUser(customerNr) {
        return this.customerSearchService.switchUser(customerNr).pipe(
            catchError((err) => {
                this.errorMessage = this.errorHandler(err.error);
                return of(false);
            }));
    }

    private searchCustomerData() {
        this.isLoading = true;
        this.errorMessage = '';
        const body = this.searchForm.value;
        body.text = (body.text || '').replace(/\s\s+/g, ' ').trim();
        body.telephone = (body.telephone || '').replace(/[\(\)]/g, '').trim();
        return this.customerSearchService.getCustomerInfo(body).pipe(
            map((res: CustomerDataModel) => {
                this.total = res.recommendCustomers.totalElements;
                return res.recommendCustomers.content.map(r => this.convertToDisplayedCustomer(r));
            }),
            catchError((err) => {
                this.errorMessage = this.errorHandler(err.error);
                return of([]);
            }), // empty list on error
            tap(() => this.isLoading = false)
        );
    }

    private convertToDisplayedCustomer(customer) {
        const list = [];
        if (customer.shortName) {
            list.push(customer.shortName);
        }
        if (customer.name) {
            list.push(customer.name);
        }
        if (customer.zipCode) {
            list.push(customer.zipCode);
        }
        if (customer.city) {
            list.push(customer.city);
        }
        const additionalInfo = list.join(', ');
        return {
            value: customer.nr,
            label: `${customer.affiliateInitials} - <span class="color-primary">${customer.nr}</span> - ${additionalInfo}`
        };
    }

    private errorHandler(error: any) {
        const code = error.error_code;
        switch (code) {
            case 'NOT_FOUND_CUSTOMER':
                return 'AX_CONNECTION.CUSTOMER_NOT_FOUND';
            case 'NOT_FOUND_USER_ON_BEHALF':
                return 'AX_CONNECTION.SALES_ADMIN_NOT_FOUND';
            case 'AC_BLOCKED_CUSTOMER_001':
                return this.translateService.instant('AX_CONNECTION.BLOCKED_CUSTOMER',
                    { customerNr: this.searchForm.get('customerNr').value }
                );
            case 'AC_ISE_001':
                return 'ERROR_500';
            default:
                const body = this.searchForm.value;
                if (body.telephone) {
                    return 'AX_CONNECTION.CUSTOMER_TELEPHONE_NOT_FOUND';
                }
                if (body.text) {
                    return 'AX_CONNECTION.CUSTOMER_FREE_TEXT_NOT_FOUND';
                }
                return 'AX_CONNECTION.CUSTOMER_NOT_FOUND';
        }
    }

    private addOrderToBasket(orderId: any) {
        const body = {
            orderId,
            orderNumber: null
        } as OrderHistoryDetailRequest;

        if (!body.orderId && !body.orderNumber) {
            return of(null);
        }

        return this.orderHistoryService.addOrderHistoryToCart(body);
    }
}
