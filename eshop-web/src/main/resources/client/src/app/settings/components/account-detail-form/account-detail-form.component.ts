import { Component, OnInit, Input, ChangeDetectionStrategy, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';

import { UserDetail } from 'src/app/core/models/user-detail.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { UserProfile, UpdateProfileModel } from '../../models/user-profile/user-profile.model';
import { UserProfileSalutation } from '../../models/user-profile/user-profile-salutation.model';
import { UserProfileType } from '../../models/user-profile/user-profile-type.model';
import { UserProfileLanguage } from '../../models/user-profile/user-profile-language.model';
import { SETTING_PAGE } from '../../settings.constant';
import { SagMessageData } from 'sag-common';
import { AppAuthConfigService } from 'src/app/authentication/services/app-auth.config.service';

@Component({
    selector: 'connect-account-detail-form',
    templateUrl: './account-detail-form.component.html',
    styleUrls: ['./account-detail-form.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class AccountDetailFormComponent implements OnInit {
    @Input() set userInfomation(newInfo: UserDetail) {
        this.userDetail = newInfo;
        this.setCompanyAndBranch(this.userDetail);
        this.updateThuleExternalUrlByLanguage();
    }
    @Input() set userProfiles(newProfile: UserProfile) {
        if (newProfile) {
            this.userProfile = newProfile;
            this.initForm(this.userDetail, this.userProfile);
            this.salutations = this.userProfile.salutations;
            this.types = this.userProfile.types;
            this.languages = this.userProfile.languages;
        }
    }

    @Output() updateProfileEmitter = new EventEmitter<{ request: UpdateProfileModel, langIso: string, callback: any }>();

    userProfile: UserProfile;
    userDetail: UserDetail;
    salutations: UserProfileSalutation[];
    types: UserProfileType[];
    languages: UserProfileLanguage[];
    companyInformation: string;
    branchInformation: string;
    accountDetailForm: FormGroup;
    result: SagMessageData;

    constructor(
        private fb: FormBuilder,
        private appAuthConfigService: AppAuthConfigService
    ) { }

    ngOnInit() {
        this.setCompanyAndBranch(this.userDetail);
        this.initForm(this.userDetail, this.userProfile);
    }

    onSubmit(callback) {
        if (this.accountDetailForm.valid) {
            this.accountDetailForm.disable();
            const accessUrl = `${this.appAuthConfigService.redirectUrl}${SETTING_PAGE}`;
            const langIso = this.languages
                .find(lang => lang.id === this.accountDetailForm.value.languageId)
                .langiso.toLowerCase();
            const request = new UserProfile().toUpdateProfileRequest({
                ...this.accountDetailForm.value,
                id: this.userDetail.id,
                accessUrl
            });

            this.updateProfileEmitter.emit({
                request,
                langIso,
                callback: (notifyMessage: SagMessageData) => {
                    this.reset(notifyMessage);
                    callback();
                }
            });
            return;
        }
        callback();
    }

    get userName() { return this.accountDetailForm.get('userName') as FormControl; }
    get surName() { return this.accountDetailForm.get('surName') as FormControl; }
    get firstName() { return this.accountDetailForm.get('firstName') as FormControl; }
    get email() { return this.accountDetailForm.get('email') as FormControl; }

    private reset(result: SagMessageData) {
        this.accountDetailForm.enable();
        this.result = result;
    }

    private initForm(userDetail: UserDetail, userProfile: UserProfile) {
        if (!userDetail || !userProfile) {
            return;
        }

        const isFormDisabled = userDetail.isSalesOnBeHalf;

        this.accountDetailForm = this.fb.group({
            userName: [{
                value: userDetail.username || '',
                disabled: userDetail.salesUser
            }, [Validators.required]],
            salutationIds: userProfile.salutationId || 0,
            surName: [userProfile.surName || '', [Validators.required]],
            firstName: [userProfile.firstName || '', [Validators.required]],
            email: [userProfile.email || '', [Validators.required, Validators.email]],
            phoneNumber: userProfile.phoneNumber || '',
            languageId: userProfile.languageId || 0,
            typeId: [{ value: userProfile.typeId || 0, disabled: !userDetail.isSalesOnBeHalf}],
            hourlyRate: userProfile.hourlyRate || '',
        });

        if (isFormDisabled) {
            this.accountDetailForm.disable();
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

    private updateThuleExternalUrlByLanguage() {
        if (!this.userDetail) {
            return;
        }

        const { settings } = this.userDetail;
        if (!settings || !settings.externalUrls.thule) {
            return;
        }

        const foundIdx = settings.externalUrls.thule.indexOf('-ch');
        const oldThuleDealerUrl = settings.externalUrls.thule;
        this.userDetail.settings.externalUrls.thule = oldThuleDealerUrl.substring(0, foundIdx - 2)
            + this.userDetail.language + oldThuleDealerUrl.substring(foundIdx, oldThuleDealerUrl.length - 1);
    }

    private getCompanyInfoByUser(userDetail: UserDetail): string {
        if (!userDetail) { return ''; }

        if (userDetail.finalCustomer) { return userDetail.finalCustomer.name; }

        if (userDetail.customer) { return `${userDetail.customer.companyName} - ${userDetail.custNr}`; }

        return '';
    }

}
