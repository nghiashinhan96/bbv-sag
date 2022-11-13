import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { debounceTime, filter, tap, switchMap, finalize } from 'rxjs/operators';

import { DeliveryProfileModel } from '../../models/delivery-profile.model';
import { DeliveryProfileService } from '../../services/delivery-profile.service';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { ApiUtil } from 'src/app/core/utils/api.util';

@Component({
    selector: 'backoffice-delivery-profile-detail',
    templateUrl: './delivery-profile-detail.component.html',
    styleUrls: ['./delivery-profile-detail.component.scss']
})
export class DeliveryProfileDetailComponent implements OnInit {

    profileForm: FormGroup;
    // profile$: Observable<any>;
    profile: any;

    notifier: NotificationModel;
    countryOptions = [];
    distributionOptions = [];
    deliveryBranchOptions = [];

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private activeRoute: ActivatedRoute,
        private deliveryProfileService: DeliveryProfileService
    ) { }

    ngOnInit() {
        this.buildProfileForm();
    }

    buildProfileForm() {
        this.profileForm = this.formBuilder.group({
            id: null,
            country: ['', Validators.required],
            deliveryProfileId: [null, Validators.required],
            deliveryProfileName: [{ value: '', disabled: true }, Validators.required],
            distributionBranchId: [null, Validators.required],
            deliveryBranchId: [null, Validators.required],
            vendorCutOffTime: [null, Validators.required],
            latestTime: [null, Validators.required],
            lastDelivery: [null, Validators.required],
            deliveryDuration: [null, Validators.required],
            nextDay: null
        });

        this.getProfileData();

        this.profileForm.get('deliveryProfileId').valueChanges.pipe(
            debounceTime(600),
            filter(val => !!val),
            tap(() => {
                this.profileForm.get('deliveryProfileId').disable({ emitEvent: false });
                this.profileForm.get('deliveryProfileName').disable();
            }),
            switchMap(val => this.deliveryProfileService.searchDeliveryProfile(val)),
            tap(() => this.profileForm.get('deliveryProfileId').enable({ emitEvent: false }))
        ).subscribe(deliveryProfileName => {
            this.profileForm.get('deliveryProfileName').setValue(deliveryProfileName || '');
            if (!deliveryProfileName) {
                this.profileForm.get('deliveryProfileName').enable();
            }
        });
    }

    getProfileData() {
        const profileId = this.activeRoute.snapshot.paramMap.get('profileId');
        if (!profileId) {
            this.handleProfileData(new DeliveryProfileModel());
            return;
        }
        this.deliveryProfileService.getDeliveryProfile(parseInt(profileId, 10)).pipe(
            finalize(() => SpinnerService.stop())
        ).subscribe(res => {
            this.handleProfileData(res);
        });
    }

    handleProfileData(profile: DeliveryProfileModel) {
        this.profileForm.patchValue(profile, { emitEvent: false });
        this.profile = profile;
        this.initProfileDropdownData();
    }

    initProfileDropdownData() {
        this.deliveryProfileService.initDropdownData().subscribe(result => {
            const res: any = result;
            this.countryOptions = (res.countries || []).map(
                item => ({ value: item.code.toString(), label: item.description.toString() }));
            this.distributionOptions = (res.branchNr || []).map(
                item => ({ value: item.branchNr.toString(), label: item.branchCode.toString() }));
            this.deliveryBranchOptions = (res.branchNr || []).map(
                item => ({ value: item.branchNr.toString(), label: item.branchCode.toString() }));

            const countryValue = this.countryOptions.find(country => country.value === this.profile.country) ||
            {
                value: '',
                label: ''
            };
            this.profileForm.get('country').setValue(countryValue);

            const defaultValue = {
                value: null,
                label: ''
            };

            const distributionBranchIdValue = this.distributionOptions.find(opt => opt.value === this.profile.distributionBranchId)
                || defaultValue;
            this.profileForm.get('distributionBranchId').setValue(distributionBranchIdValue);

            const deliveryBranchIdValue = this.deliveryBranchOptions.find(br => br.value === this.profile.deliveryBranchId) || defaultValue;
            this.profileForm.get('deliveryBranchId').setValue(deliveryBranchIdValue);
        });
    }

    backToList() {
        this.router.navigate(['../'], { relativeTo: this.activeRoute });
    }

    saveProfile() {
        if (this.profileForm.invalid) {
            return;
        }

        const rawProfileData = this.profileForm.getRawValue();
        const transformProfileData = {
            ...rawProfileData,
            country: rawProfileData.country.value,
            distributionBranchId: rawProfileData.distributionBranchId.value,
            deliveryBranchId: rawProfileData.deliveryBranchId.value
        };
        const formData: DeliveryProfileModel = new DeliveryProfileModel(transformProfileData);
        this.deliveryProfileService
            .createOrupdateDeliveryProfile(formData.dto).pipe(
                finalize(() => SpinnerService.stop())
            )
            .subscribe(res => {
                this.notifier = { messages: ['COMMON.MESSAGE.SAVE_SUCCESSFULLY'], status: true };
                if (!formData.id) {
                    setTimeout(() => {
                        this.backToList();
                    }, 500);
                }
            }, ({error}) => {
                const errorMess = ApiUtil.handleErrorReponse(error);
                this.notifier = { messages: [errorMess], status: false };
            });
    }
}
