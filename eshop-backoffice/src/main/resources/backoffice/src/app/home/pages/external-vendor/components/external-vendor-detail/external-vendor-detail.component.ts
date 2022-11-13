import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { timer, Subscription } from 'rxjs';

import { ExternalVendorService } from '../../services/external-vendor.service';
import { ExternalVendorDetailRequest } from '../../model/external-vendor-item.model';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { ExternalVendorConstants } from '../../external-vendor.constant';

@Component({
    selector: 'backoffice-app-external-vendor-detail',
    templateUrl: './external-vendor-detail.component.html',
    styleUrls: ['./external-vendor-detail.component.scss']
})
export class ExternalVendorDetailComponent implements OnInit {

    countries: any[];
    externalVendorForm: FormGroup = null;

    sagArticleGroup: any[];

    brands: any[];
    profiles: any[];
    availTypes: any[];
    notifier: NotificationModel;

    // selectedCountry: string;
    // selectedAvailType: string;
    // selectedBrand: string;
    // selectedProfileId: string;

    private id: number;
    private request: ExternalVendorDetailRequest;
    private subscription: Subscription;

    constructor(
        private router: Router,
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private externalVendorService: ExternalVendorService) { }

    ngOnInit() {
        this.externalVendorForm = this.createExternalVendorDetailForm();

        this.route.data.subscribe((data: { externalVendor: ExternalVendorDetailRequest }) => {
            if (!data.externalVendor.id) {
                this.getInitData();
                return;
            }
            const vendor = data.externalVendor;
            this.id = vendor.id;
            this.externalVendorForm.patchValue(vendor, { emitEvent: false });

            this.getInitData(vendor);
        });
    }

    onSubmit() {
        this.notifier = null;
        if (this.externalVendorForm.valid) {
            this.request = this.setRequestData(this.externalVendorForm.value);
            this.request.id = this.id;
            if (this.id) {
                this.externalVendorService.update(this.request)
                    .subscribe(() => {
                        this.goBack();
                    },
                        ({error}) => this.notifier = { messages: [ApiUtil.handleErrorReponse(error)], status: false });
            } else {
                this.externalVendorService.create(this.request)
                    .subscribe(() => {
                        this.goBack();
                    },
                        ({error}) => this.notifier = { messages: [ApiUtil.handleErrorReponse(error)], status: false });
            }
        }
    }

    onBack() {
        this.router.navigate(['home/external-vendors']);
    }

    get vendorId() { return this.externalVendorForm.get('vendorId'); }
    get name() { return this.externalVendorForm.get('vendorName'); }
    get priority() { return this.externalVendorForm.get('vendorPriority'); }

    private setRequestData(data): any {
        const requestData = { ...data };
        requestData.country = data.country.value ? data.country.value.toString().toLowerCase()
            : data.country.toLowerCase();
        requestData.deliveryProfileId = data.deliveryProfileId.value ? data.deliveryProfileId.value.toString()
            : data.deliveryProfileId.toString();

        let brandId = data.brandId;
        if (brandId && brandId.value) {
            brandId = brandId.value.toString();
        } else {
            brandId = '';
        }
        requestData.brandId = brandId;
        requestData.availabilityTypeId = data.availabilityTypeId.value ? data.availabilityTypeId.value : data.availabilityTypeId;
        return requestData;
    }

    private createExternalVendorDetailForm(): FormGroup {
        return this.formBuilder.group({
            country: ['', Validators.required],
            sagArticleGroup: '',
            brandId: '',
            vendorId: ['', Validators.maxLength(9)],
            vendorName: ['', Validators.maxLength(ExternalVendorConstants.EXTERNAL_VENDOR_NAME_MAX_LENGTH)],
            vendorPriority: ['', Validators.compose([Validators.required, Validators.min(1), Validators.pattern(/^\d*$/)])],
            deliveryProfileId: ['', Validators.required],
            availabilityTypeId: ['', Validators.required]
        });
    }

    private getInitData(vendorData?: ExternalVendorDetailRequest) {

        this.externalVendorService.getInitData().subscribe(data => {
            this.countries = data.countries.map((country) => ({ label: country.description, value: country.code }));
            this.availTypes = data.availabilityType.map((type) => ({ value: type, label: type }));

            this.profiles = data.deliveryProfile
                .map((profile) => ({ value: profile.deliveryProfileId.toString(), label: profile.deliveryProfileName }));

            this.brands = data.brands.map(brand => ({ value: brand.dlnrid, label: brand.suppname }));
            if (vendorData) {
                this.initSelectorsValue(vendorData);
            }
        });
    }

    private initSelectorsValue(vendorData?: ExternalVendorDetailRequest) {
        const emptyValue = {
            value: '',
            label: ''
        };

        const countryValue = this.countries.find(country => country.value === vendorData.country) || emptyValue;
        this.externalVendorForm.get('country').setValue(countryValue);

        const brandValue = this.brands.find(brand => brand.value === vendorData.brandId) || emptyValue;
        this.externalVendorForm.get('brandId').setValue(brandValue);

        const profileValue = this.profiles.find(profile => profile.value === vendorData.deliveryProfileId) || emptyValue;
        this.externalVendorForm.get('deliveryProfileId').setValue(profileValue);

        const typeValue = this.availTypes.find(type => type.value === vendorData.availabilityTypeId) || emptyValue;
        this.externalVendorForm.get('availabilityTypeId').setValue(typeValue);
    }

    private goBack() {
        this.notifier = { messages: ['COMMON.MESSAGE.SAVE_SUCCESSFULLY'], status: true };
        this.subscription = timer(1000).subscribe(() => {
            this.onBack();
            this.subscription.unsubscribe();
        });
    }
}
