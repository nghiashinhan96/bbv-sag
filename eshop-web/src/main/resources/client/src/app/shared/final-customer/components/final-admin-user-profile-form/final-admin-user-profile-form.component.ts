import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { AppAuthConfigService } from 'src/app/authentication/services/app-auth.config.service';
import { Validator } from 'src/app/core/utils/validator';
import { AffiliateEnum, AffiliateUtil, SagMessageData } from 'sag-common';
import { ProfileModel } from 'src/app/settings/models/final-user-admin/user-profile.model';
import { SETTING_PAGE } from 'src/app/settings/settings.constant';
import { FormUtil } from 'src/app/core/utils/form.util';
import { UserService } from 'src/app/core/services/user.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { FinalUserAdminService } from 'src/app/settings/services/final-user-admin.service';
import { finalize } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { SubSink } from 'subsink';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
    selector: 'connect-final-admin-user-profile-form',
    templateUrl: 'final-admin-user-profile-form.component.html',
    styleUrls: ['final-admin-user-profile-form.component.scss']
})
export class FinalAdminUserProfileFormComponent implements OnInit, OnDestroy {
    @Input() profile: ProfileModel;
    @Input() wssOrgId: number;
    @Output() save = new EventEmitter();

    isUpdating = false;
    result: SagMessageData;

    profileForm: FormGroup;

    salutationOptions = [];
    langOptions = [];
    typeOptions = [];
    sb = AffiliateEnum.SB;

    subs = new SubSink();
    isEhCh = AffiliateUtil.isEhCh(environment.affiliate);
    isWbb = AffiliateUtil.isWbb(environment.affiliate);
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
    isStCz = AffiliateUtil.isCz(environment.affiliate);
    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);
    constructor(
        private fb: FormBuilder,
        private appAuthConfigService: AppAuthConfigService,
        private userService: UserService,
        private finalUserAdminService: FinalUserAdminService,
        private activatedRoute: ActivatedRoute,
    ) { }

    ngOnInit() {
        this.profileForm = this.fb.group({
            id: '',
            userName: ['', Validators.required],
            salutationId: null,
            surName: ['', Validators.required],
            firstName: ['', Validators.required],
            email: ['', [Validators.required, Validator.emailValidator]],
            phoneNumber: '',
            languageId: null,
            typeId: null,
            hourlyRate: '',
            emailConfirmation: false,
            netPriceView: { value: false, disabled: true },
            netPriceConfirm: { value: false, disabled: true },
            accessUrl: ''
        });

        this.netPriceView.valueChanges.subscribe(value => {
            if (!value) {
                this.netPriceConfirm.disable();
                this.netPriceConfirm.setValue(false);
            } else {
                this.netPriceConfirm.enable();
            }
        });
        this.intialData(this.profile);
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    get netPriceView() { return this.profileForm.get('netPriceView'); }
    get netPriceConfirm() { return this.profileForm.get('netPriceConfirm'); }
    get userName() { return this.profileForm.get('userName') as FormControl; }
    get surName() { return this.profileForm.get('surName') as FormControl; }
    get firstName() { return this.profileForm.get('firstName') as FormControl; }
    get email() { return this.profileForm.get('email') as FormControl; }

    private intialData(profile: ProfileModel) {
        this.profileForm.patchValue(profile);
        this.profileForm.get('netPriceView').setValue(profile.netPriceView);
        if (profile.netPriceView) {
            this.profileForm.get('netPriceConfirm').enable();
        } else {
            this.profileForm.get('netPriceConfirm').disable();
        }
        this.profileForm.get('accessUrl').setValue(`${this.appAuthConfigService.redirectUrl}${SETTING_PAGE}`, { emitEvent: false });
        const SALUTATION_KEY = 'SETTINGS.PROFILE.SALUTATION.';
        const TYPE_KEY = 'SETTINGS.PROFILE.TYPE.';
        const LANGUAGE_KEY = 'SETTINGS.PROFILE.LANGUAGE.';
        this.salutationOptions = this.buildSelectOptions(profile.salutations, SALUTATION_KEY, 'code');
        this.typeOptions = this.buildSelectOptions(profile.types, TYPE_KEY, 'name');
        this.langOptions = this.buildSelectOptions(profile.languages, LANGUAGE_KEY, 'langcode');

        let getProfileOfFCAdmin: Observable<any>;
        if (this.isEhCh || this.isEhCz) {
            getProfileOfFCAdmin = this.finalUserAdminService.getFinalUserProfile();
        }

        if (this.isWbb || this.isStCz || this.isAxCz) {
            if (this.wssOrgId) {
                getProfileOfFCAdmin = this.finalUserAdminService.getWssProfile(this.wssOrgId);
            }
        }

        if (getProfileOfFCAdmin) {
            SpinnerService.start('connect-final-admin-user-profile-form form');
            this.subs.sink = getProfileOfFCAdmin
                .pipe(finalize(() => {
                    SpinnerService.stop('connect-final-admin-user-profile-form form')
                }))
                .subscribe(res => {
                    this.enableShowNetPrice(res);
                });
        }
    }

    enableShowNetPrice(data) {
        if (!data) {
            return;
        }
        const { showNetPriceEnabled } = data as any;
        let isNetPriceViewDisable = !(showNetPriceEnabled);
        if (isNetPriceViewDisable) {
            this.profileForm.get('netPriceView').disable();
            this.profileForm.get('netPriceConfirm').disable();
        } else {
            this.profileForm.get('netPriceView').enable();
        }
    }

    onSubmit(callback?) {
        if (this.profileForm.invalid) {
            FormUtil.markFormAsDirty(this.profileForm);
            if (callback) {
                callback();
            }
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
}
