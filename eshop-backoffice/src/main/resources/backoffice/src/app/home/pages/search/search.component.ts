import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup } from '@angular/forms';

import { AffiliateService } from 'src/app/core/services/affiliate.service';
import { UserService } from 'src/app/core/services/user.service';

import { AffiliateInfoModel, AffiliateRequestModel } from '../affiliate-search/models/affiliate-info.model';
import { UserRequestModel } from '../../models/user-request.model';
import { CustomerRequestModel } from '../customer-search/model/customer-request.model';
import { CUSTOMER_SEARCH_MODE } from 'src/app/core/enums/enums';
import { DEFAULT_SELECTOR_VALUE } from 'src/app/core/conts/app.constant';
import { CustomerService } from 'src/app/home/services/customer/customer.service';
import { LicenseRequestModel } from '../license-search/models/license-request.model';

@Component({
    selector: 'backoffice-home-search',
    templateUrl: 'search.component.html',
})
export class HomeSearchComponent implements OnInit {
    constructor(
        private affiliateService: AffiliateService,
        private router: Router,
        private route: ActivatedRoute,
        private userService: UserService,
        private customerService: CustomerService
    ) { }

    isAuthed: boolean;

    // Customer search models
    public affiliates;

    // License search data
    licensesData = [];

    // affiliateModels: Array<AffiliateInfoModel>;
    // affiliateSearchModel = new AffiliateRequestModel();
    searchData = new CustomerRequestModel();
    userSearchModel = new UserRequestModel();
    licenseSearchData = new LicenseRequestModel();

    // Form
    // affiliateSearchForm: FormGroup;

    ngOnInit() {
        this.userService.isAuthed.subscribe((authed) => {
            this.isAuthed = authed;
            if (authed) {
            } else {
                this.router.navigateByUrl('/login');
            }
        });

        this.getAffiliates();
        this.getLicensesData();
    }

    searchAffiliate(value) {
        this.router.navigate(['affiliates'], {
            queryParams: { affShortName: value.value },
            relativeTo: this.route,
        });
    }

    searchCustomerGroup(value) {
        this.router.navigate(['/home/search/customer-groups'], {
            queryParams: {
                affiliate: value.affiliate.value,
                collectionName: value.customerGroupName,
            },
        });
    }

    searchCustomer(value) {
        this.searchData.searchMode = CUSTOMER_SEARCH_MODE.CUSTOMER.toString();
        this.searchData.affiliate = value.affiliate.value;
        this.searchData.customerNr = value.customerNr;
        this.searchData.companyName = value.companyName;

        this.getSearchData();
    }

    getUserResultSearch(value) {
        this.userSearchModel.affiliate = value.affiliate.value;
        this.userSearchModel.customerNumber = value.customerNumber;
        this.userSearchModel.userName = value.userName;
        this.userSearchModel.email = value.email;
        this.userSearchModel.telephone = value.telephone;
        this.userSearchModel.name = value.name || '';
        this.router.navigate([
            '/home/search/users',
            { queryParam: JSON.stringify(this.userSearchModel) },
        ]);
    }

    searchLicenses(data) {
        this.licenseSearchData.affiliate = data.affiliate;
        this.licenseSearchData.customerNr = data.customerNr;
        this.licenseSearchData.packName = data.packName;
        this.licenseSearchData.beginDate = data.beginDate;
        this.licenseSearchData.endDate = data.endDate;
        this.router.navigate([
            '/home/search/licenses',
            { queryParam: JSON.stringify(this.licenseSearchData) },
        ]);
    }

    getSearchData() {
        this.router.navigate([
            '/home/search/customers',
            { queryParam: JSON.stringify(this.searchData) },
        ]);
    }

    private getAffiliates() {
        this.affiliateService.getShortInfos().subscribe((data) => {
            if (!data) {
                return;
            }
            
            const res: any = data;
            const affiliates = [];
            affiliates.push(DEFAULT_SELECTOR_VALUE);
            this.affiliates = affiliates.concat(
                res.map((item) => {
                    return { value: item.shortName, label: item.name };
                })
            );
        });
    }

    private getLicensesData() {
        this.customerService.getAllLicenseTypes().subscribe((data) => {
            if (!data) {
                return;
            }
            
            const res: any = data;
            const licensesData = [];
            licensesData.push(DEFAULT_SELECTOR_VALUE);
            this.licensesData = licensesData.concat(
                res.map((item) => {
                    return { value: item.packName.trim(), label: item.packName.trim() };
                })
            );
        });
    }
}
