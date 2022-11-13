import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

import { UserDetail } from 'src/app/core/models/user-detail.model';
import { UserProfile, UpdateProfileModel } from '../../models/user-profile/user-profile.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { UserProfileLanguage } from '../../models/user-profile/user-profile-language.model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserProfileType } from '../../models/user-profile/user-profile-type.model';
import { UserProfileSalutation } from '../../models/user-profile/user-profile-salutation.model';
import { SETTING_PAGE } from '../../settings.constant';
import { NormalAdminService } from '../../services/normal-admin.service';
import { AppAuthConfigService } from 'src/app/authentication/services/app-auth.config.service';
import { finalize } from 'rxjs/operators';
import { Validator } from 'src/app/core/utils/validator';
import { SagMessageData } from 'sag-common';
import { SettingsService } from '../../services/settings.service';
import { FormUtil } from 'src/app/core/utils/form.util';

@Component({
    selector: 'connect-create-user-modal-form',
    templateUrl: './create-user-modal-form.component.html',
    styleUrls: ['./create-user-modal-form.component.scss']
})
export class CreateUserModalFormComponent implements OnInit {
    @Input() onSuccess: any;

    userDetail: UserDetail;
    userProfile: UserProfile;

    companyInformation: string;
    branchInformation: string;

    salutations: UserProfileSalutation[];
    types: UserProfileType[];
    languages: UserProfileLanguage[];
    accountDetailForm: FormGroup;
    isUpdating: boolean;
    notifyMessage: SagMessageData;

    constructor(
        public bsModalRef: BsModalRef,
        private fb: FormBuilder,
        private normalAdminService: NormalAdminService,
        private appAuthConfigService: AppAuthConfigService,
        private settingService: SettingsService,
    ) { }

    ngOnInit() {
        this.setCompanyAndBranch(this.userDetail);
        this.initForm(this.userDetail, this.userProfile);
        this.salutations = this.userProfile.salutations;
        this.types = this.userProfile.types;
        this.languages = this.userProfile.languages;
    }

    private initForm(userDetail: UserDetail, userProfile: UserProfile) {
        this.accountDetailForm = this.fb.group({
            userName: [(userDetail && userDetail.username) || '', [Validators.required]],
            salutationIds: [(userProfile && userProfile.salutationId) || 0],
            surName: [(userProfile && userProfile.surName) || '', [Validators.required]],
            firstName: [(userProfile && userProfile.firstName) || '', [Validators.required]],
            email: [(userProfile && userProfile.email) || '', [Validators.required, Validator.emailValidator]],
            phoneNumber: [(userProfile && userProfile.phoneNumber) || ''],
            languageId: [(userProfile && userProfile.languageId) || 0],
            typeId: [(userProfile && userProfile.typeId) || 0],
            hourlyRate: [(userProfile && userProfile.hourlyRate) || '']
        });
    }

    onSubmit(callback?) {
        if (this.accountDetailForm.invalid) {
            FormUtil.markFormAsDirty(this.accountDetailForm);
            if (callback) {
                callback();
            }
            return;
        }
        if (this.accountDetailForm.valid) {
            const accessUrl = `${this.appAuthConfigService.redirectUrl}${SETTING_PAGE}`;
            const request = {
                id: this.userProfile.id,
                salutationId: this.accountDetailForm.value.salutationIds,
                userName: this.accountDetailForm.value.userName,
                surName: this.accountDetailForm.value.surName,
                firstName: this.accountDetailForm.value.firstName,
                email: this.accountDetailForm.value.email,
                phoneNumber: this.accountDetailForm.value.phoneNumber,
                languageId: this.accountDetailForm.value.languageId,
                typeId: this.accountDetailForm.value.typeId,
                hourlyRate: this.accountDetailForm.value.hourlyRate,
                accessUrl
            } as UpdateProfileModel;

            this.isUpdating = true;

            this.normalAdminService.createUser(request)
                .pipe(
                    finalize(() => this.isUpdating = false)
                )
                .subscribe(() => {
                    this.bsModalRef.hide();
                    if (this.onSuccess) {
                        this.onSuccess();
                    }
                }, ({ error }) => {
                    this.notifyMessage = { type: 'ERROR', message: this.settingService.handleErrorMessage(error) };
                });
        }
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
