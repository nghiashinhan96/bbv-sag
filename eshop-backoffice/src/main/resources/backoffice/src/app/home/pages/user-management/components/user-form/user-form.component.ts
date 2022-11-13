import {
    Component,
    OnInit,
    Input,
    AfterViewInit,
    Output,
    EventEmitter,
    OnChanges,
} from '@angular/core';
import {
    FormBuilder,
    FormControl,
    Validators,
    FormGroup,
} from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { UserFormModel } from '../../models/user-form.model';
import { UserFormDataManagementService } from '../../../../services/user-form-data-management.service';
import { CustomerQuery } from '../../models/customer-query.model';
import { ProfileInfoFormModel } from '../../models/profile-info-form.model';
import { BOValidator } from 'src/app/core/utils/validator';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { UserService } from 'src/app/core/services/user.service';
import { EMPTY_STRING } from 'src/app/core/conts/app.constant';

@Component({
    selector: 'backoffice-user-form',
    templateUrl: './user-form.component.html',
    styleUrls: ['./user-form.component.scss'],
})
export class UserFormComponent implements OnInit, AfterViewInit, OnChanges {
    @Input() model: UserFormModel;
    @Output() onSaved = new EventEmitter();

    // profileLoaded: boolean;
    profileFormModel: ProfileInfoFormModel;
    profileForm: any;
    notifier: NotificationModel;
    accountNotifier: NotificationModel;
    formDisable = false;

    salutationOptions: any;
    languageOptions: any;
    typeOptions: any;

    constructor(
        private translateService: TranslateService,
        private fb: FormBuilder,
        private userService: UserService,
        private dataManagement: UserFormDataManagementService
    ) {
    }

    ngOnInit() { }
    ngOnChanges() {
        this.fetchComponentData();
    }

    ngAfterViewInit() { }

    fetchComponentData() {
        if (!this.model) {
            this.model = new UserFormModel();
        }
        // build view for first time
        this.buildOrUpdateView();
        this.dataManagement.setCustomerQuery(
            new CustomerQuery({
                customerNr: this.model.customerNumber,
                affiliate: this.model.affiliate,
            })
        );
        this.dataManagement.fetchCustomer().subscribe((customer) => {
            this.model.companyName = customer.companyName;
            this.model.addressesLink = customer.addressesLink;
            this.model.sendMethodCode = customer.sendMethodCode;
            this.model.defaultBranchName = customer.defaultBranchName;
            this.model.defaultBranchId = customer.defaultBranchId;
        });
    }

    createUserProfileDto() {
        const { value } = this.profileForm;
        const languageId = value.languageId.value;
        const salutationId = value.salutationId.value;
        const typeId = value.typeId.value;
        return {
            ...value,
            languageId,
            salutationId,
            typeId,
        };
    }

    onSave(): void {
        const buildUserFormModel = (): UserFormModel => {
            const formModel = new UserFormModel();
            formModel.userProfileDto = this.createUserProfileDto();
            formModel.addressesLink = this.model.addressesLink;
            formModel.affiliateShortName = this.model.affiliate;
            formModel.customerNumber = this.model.customerNumber;
            formModel.sendMethodCode = this.model.sendMethodCode;
            return formModel;
        };

        this.onDisplayMessageOnTopNotifier(EMPTY_STRING, true);
        this.onDisplayMessageOnAccountNotifier(EMPTY_STRING, true);

        if (!this.profileForm.valid) {
            this.markFormGroupTouched(this.profileForm);
            this.profileForm.markAsTouched();
            this.onDisplayMessageOnAccountNotifier(
                'USER_MANAGEMENT.USER_DETAIL.MESSAGE_PROFILE.MESSAGE_INVALID_MANDATORY_FIELDS',
                false
            );
            return;
        }
        const userDto = buildUserFormModel();
        this.model.onSaving();
        this.userService.createUser(userDto).subscribe(
            () => {
                this.buildOrUpdateView();
                this.model.onSaved();
                this.onSaved.emit();
            },
            (error) => {
                let message = error.message;
                let hasErrorCorde = false;
                if (error.error_code === 'DUPLICATED_USERNAME_IN_AFFILIATE') {
                    message = 'REGISTER.MESSAGES.INVALID_USERNAME_IN_AFFILIATE';
                    hasErrorCorde = true;
                } else if (error.error_code === 'ORGANISATION_NOT_FOUND') {
                    message =
                        'USER_MANAGEMENT.USER_DETAIL.MESSAGE_PROFILE.ORGANISATION_NOT_FOUND';
                    hasErrorCorde = true;
                }
                if (hasErrorCorde) {
                    this.onDisplayMessageOnTopNotifier(message, false);
                } else {
                    this.onDisplayMessageOnAccountNotifier(
                        'COMMON.MESSAGE.SAVE_UNSUCCESSFULLY',
                        false
                    );
                }
                this.model.onSaved();
            }
        );
    }

    onDisplayMessageOnAccountNotifier(message: string, status: boolean): void {
        this.accountNotifier = message
            ? { messages: [message], status }
            : null;
    }

    onDisplayMessageOnTopNotifier(message: string, status: boolean): void {
        this.notifier = message ? { messages: [message], status } : null;
    }

    onDisableForm(value = true): void {
        for (const key in this.profileForm.controls) {
            if (this.profileForm.controls.hasOwnProperty(key)) {
                const control = this.profileForm.controls[key];
                if (value) {
                    control.disable();
                } else {
                    control.enable();
                }
            }
        }
        this.formDisable = value;
    }

    markFormGroupTouched(formGroup: FormGroup) {
        (Object as any).values(formGroup.controls).forEach((control) => {
            control.markAsTouched();

            if (control.controls) {
                this.markFormGroupTouched(control);
            }
        });
    }

    buildOrUpdateView() {
        this.buildUserForm();
        this.buildOptions();
    }

    buildOptions(): void {
        const buildTypeSelect = (data: any) => {
            const types = [];
            data.forEach((typeItem) => {
                types.push({
                    value: typeItem.id.toString(),
                    label: this.translateService.instant(
                        'USER_MANAGEMENT.USER_DETAIL.TYPE_OPTIONS.' + typeItem.name
                    ),
                });
            });
            return types;
        };
        const buildSalutationSelect = (data: any) => {
            const salutations = [];
            data.forEach((salutationItem) => {
                salutations.push({
                    value: salutationItem.id.toString(),
                    label: this.translateService.instant(
                        'USER_MANAGEMENT.USER_DETAIL.SALUTATION_TITLE.' +
                        salutationItem.code
                    ),
                });
            });
            return salutations;
        };
        const buildLanguageSelect = (data: any) => {
            const languages = [];
            data.forEach((languageItem) =>
                languages.push({
                    value: languageItem.id.toString(),
                    label: languageItem.description,
                })
            );
            return languages;
        };
        this.model.onFetchingOtions();
        this.userService
            .getUserDefaultInfo(this.model && this.model.affiliate)
            .subscribe((res) => {
                const resData: any = res;
                this.salutationOptions = buildSalutationSelect(resData.salutations);
                this.languageOptions = buildLanguageSelect(resData.languages);
                this.typeOptions = buildTypeSelect(resData.types);
                this.profileFormModel.languageId = this.languageOptions[0].value;
                this.profileFormModel.typeId = this.typeOptions[0].value;
                this.profileFormModel.salutationId = this.salutationOptions[0];

                this.profileForm.patchValue({
                    salutationId: this.salutationOptions[0],
                    languageId: this.languageOptions[0],
                    typeId: this.typeOptions[0],
                });
                this.model.onFetchedOtions();
            });
    }

    buildUserForm(): void {
        this.profileFormModel = new ProfileInfoFormModel();
        this.profileForm = this.fb.group({
            userName: new FormControl({ value: EMPTY_STRING, disabled: false }, [
                Validators.required,
                BOValidator.userNameValidator,
            ]),
            salutationId: new FormControl({
                value: EMPTY_STRING,
                disabled: false,
            }),
            surName: new FormControl({ value: EMPTY_STRING, disabled: false }, [
                Validators.required,
            ]),
            firstName: new FormControl({ value: EMPTY_STRING, disabled: false }, [
                Validators.required,
            ]),
            email: new FormControl({ value: EMPTY_STRING, disabled: false }, [
                Validators.required,
                BOValidator.emailValidator,
            ]),
            phoneNumber: new FormControl({
                value: EMPTY_STRING,
                disabled: false,
            }),
            languageId: new FormControl({
                value: EMPTY_STRING,
                disabled: false,
            }),
            typeId: new FormControl({
                value: EMPTY_STRING,
                disabled: false,
            }),
            hourlyRate: new FormControl({
                value: EMPTY_STRING,
                disabled: false,
            }),
            accessUrl: window.location.origin,
        });
        this.onDisableForm(false);
    }
}
