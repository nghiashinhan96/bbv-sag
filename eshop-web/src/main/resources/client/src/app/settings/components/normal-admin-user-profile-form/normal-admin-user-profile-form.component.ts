import { OnInit, Component, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, Validators, FormControl, FormBuilder } from '@angular/forms';

import { Validator } from 'src/app/core/utils/validator';
import { Constant } from 'src/app/core/conts/app.constant';
import { ProfileModel } from '../../models/final-user-admin/user-profile.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { SagMessageData } from 'sag-common';
import { SETTING_PAGE } from '../../settings.constant';
import { AppAuthConfigService } from 'src/app/authentication/services/app-auth.config.service';

@Component({
    selector: 'connect-normal-admin-user-profile-form',
    templateUrl: 'normal-admin-user-profile-form.component.html',
    styleUrls: ['normal-admin-user-profile-form.component.scss']
})
export class NormalAdminUserProfileFormComponent implements OnInit {
    @Input() user: ProfileModel;
    @Input() userDetail: UserDetail;
    @Output() save = new EventEmitter();

    isUpdating = false;
    result: SagMessageData;

    profileForm: FormGroup;

    salutationOptions = [];
    langOptions = [];
    typeOptions = [];

    companyInformation: string;
    branchInformation: string;

    constructor(
        private fb: FormBuilder,
        private appAuthConfigService: AppAuthConfigService
    ) {

    }

    ngOnInit() {
        this.profileForm = this.fb.group({
            id: '',
            userName: ['', [Validators.required, Validator.userNameValidator]],
            salutationId: null,
            surName: ['', Validators.required],
            firstName: ['', Validators.required],
            email: ['', [Validators.required, Validator.emailValidator]],
            phoneNumber: '',
            languageId: null,
            typeId: null,
            hourlyRate: '',
            accessUrl: ''
        });
        this.intialData(this.user);
        this.setCompanyAndBranch(this.userDetail);
    }

    get userName() { return this.profileForm.get('userName') as FormControl; }
    get surName() { return this.profileForm.get('surName') as FormControl; }
    get firstName() { return this.profileForm.get('firstName') as FormControl; }
    get email() { return this.profileForm.get('email') as FormControl; }

    private intialData(profile: ProfileModel) {
        this.profileForm.patchValue(profile);
        this.profileForm.get('accessUrl').setValue(`${this.appAuthConfigService.redirectUrl}${SETTING_PAGE}`, { emitEvent: false });
        const SALUTATION_KEY = 'SETTINGS.PROFILE.SALUTATION.';
        const TYPE_KEY = 'SETTINGS.PROFILE.TYPE.';
        const LANGUAGE_KEY = 'SETTINGS.PROFILE.LANGUAGE.';
        this.salutationOptions = this.buildSelectOptions(profile.salutations, SALUTATION_KEY, 'code');
        this.typeOptions = this.buildSelectOptions(profile.types, TYPE_KEY, 'name');
        this.langOptions = this.buildSelectOptions(profile.languages, LANGUAGE_KEY, 'langcode');
    }

    onSubmit() {
        if (this.profileForm.invalid) {
            return;
        }

        this.isUpdating = true;
        const savedData = this.profileForm.getRawValue();
        this.save.emit({
            data: savedData,
            onSuccess: () => {
                this.result = { type: 'SUCCESS', message: 'SETTINGS.PROFILE.MESSAGE_SUCCESSFUL' } as SagMessageData;
                this.isUpdating = false;
            },
            onError: (error) => {
                this.result = { type: 'ERROR', message: error } as SagMessageData;
                this.isUpdating = false;
            }
        });
    }

    private buildSelectOptions(selectObjects, prefix: string, key: string) {
        return selectObjects.map((option) => {
            return {
                value: option.id.toString(),
                label: prefix + option[key]
            };
        });
    }

    private setCompanyAndBranch(userDetail: UserDetail) {
        if (userDetail) {
            this.companyInformation = userDetail.salesUser ? Constant.SALE_COMPANY_INFO : this.getCompanyInfoByUser(userDetail);
            this.branchInformation = userDetail.defaultBranchId ?
                `${userDetail.defaultBranchId} - ${userDetail.defaultBranchName}` :
                '';
        }
    }

    private getCompanyInfoByUser(userDetail: UserDetail): string {
        if (!userDetail) { return ''; }

        if (userDetail.finalCustomer) { return userDetail.finalCustomer.name; }

        if (userDetail.customer) { return `${userDetail.customer.companyName} - ${userDetail.custNr}`; }

        return '';
    }
}
