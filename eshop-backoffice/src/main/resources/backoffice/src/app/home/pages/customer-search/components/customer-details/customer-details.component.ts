import {
    Component,
    OnInit,
    EventEmitter,
    Output,
    ViewChild,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { CustomerDetailUsers } from '../../model/customer-detail-users.model';
import { CustomerDetailLicenceComponent } from '../customer-detail-licence/customer-detail-licence.component';
import { CustomerDetailUsersComponent } from '../customer-detail-users/customer-detail-users.component';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { CustomerService } from 'src/app/home/services/customer/customer.service';
import { UserRequestModel } from 'src/app/home/models/user-request.model';
import { CUSTOMER_SEARCH_MODE } from 'src/app/core/enums/enums';
import { EMPTY_STRING } from 'src/app/core/conts/app.constant';

@Component({
    selector: 'backoffice-customer-details',
    templateUrl: './customer-details.component.html',
    styleUrls: ['./customer-details.component.scss'],
})
export class CustomerDetailsComponent implements OnInit {
    @Output() customEvent = new EventEmitter();

    @ViewChild(CustomerDetailLicenceComponent, { static: true })
    customerDetailLicenceComponent: CustomerDetailLicenceComponent;
    @ViewChild(CustomerDetailUsersComponent, { static: true })
    customerDetailUsersComponent: CustomerDetailUsersComponent;

    public selectedCustomer = { affiliate: '', customerNr: '' };
    customerSetting: any;
    public settingUpdated: any;
    private customerLicence: any;
    public collapsed1 = true;
    public collapsed2 = true;
    public collapsed3 = true;
    customerInfo;

    // public isEdit = false; // indicate whether user can edit customer information
    public notifier: NotificationModel;

    customerDetailUsers: CustomerDetailUsers;
    private searchMode: string;

    constructor(
        private customerService: CustomerService,
        private route: ActivatedRoute,
        private locationService: Location
    ) {
        this.customerDetailUsers = new CustomerDetailUsers();
    }

    ngOnInit(): void {
        this.initData();
        setTimeout(() => {
            this.openTab(this.searchMode);
        });
    }

    initData() {
        this.route.params.subscribe((params) => {
            Object.assign(this.selectedCustomer, params);
            this.searchMode = params ? params.searchMode : EMPTY_STRING;
        });

        if (this.selectedCustomer) {
            this.getCustomerInfo(this.selectedCustomer);
            this.getCustomerSetting(this.selectedCustomer.customerNr);
            this.buildCustomerDetailUsersView();
        }
    }

    buildCustomerDetailUsersView() {
        this.customerDetailUsers.userQuery = new UserRequestModel({
            affiliate: this.selectedCustomer.affiliate,
            customerNumber: this.selectedCustomer.customerNr,
        });
    }

    getCustomerInfo(selectedCustomer) {
        this.customerService.getCustomerInfo(selectedCustomer).subscribe(
            (res) => {
                this.customerInfo = res;
            },
            (err) => {
            }
        );
    }

    getCustomerSetting(customerNr) {
        this.customerService.getCustomerSetting(customerNr).subscribe(
            (setting) => {
                this.customerSetting = setting;
            },
            (error) => {
            }
        );
    }

    // getCustomerLicence(customerNr, pageSize) {
    //     this.customerLicence = this.customerService.getCustomerLicence(
    //         customerNr,
    //         pageSize
    //     );
    // }

    saveCustomerSetting(settings) {
        if (!this.validateSettings(settings)) {
            this.notifier = {
                messages: ['CUSTOMER.ERROR.SESSION_TIMEOUT'],
                status: false,
            };
            return;
        }
        this.customerService.updateCustomerSetting(settings).subscribe(
            (response) => {
                this.notifier = {
                    messages: ['COMMON.MESSAGE.UPDATE_SUCCESSFULLY'],
                    status: true,
                };
            },
            (error) => {
                this.notifier = {
                    messages: ['COMMON.MESSAGE.UPDATE_FAILED'],
                    status: false,
                };
            }
        );
    }

    abortCustomerDetail() {
        this.locationService.back();
    }

    validateSettings(settings): boolean {
        return CustomerService.validateTimeout(settings.sessionTimeoutSeconds);
    }

    private openTab(searchMode: string) {
        switch (searchMode) {
            case CUSTOMER_SEARCH_MODE.CUSTOMER.toString():
                this.customerDetailUsersComponent.open();
                break;
            case CUSTOMER_SEARCH_MODE.LICENCES.toString():
                this.customerDetailLicenceComponent.open();
                break;
            default:
                break;
        }
    }
}
