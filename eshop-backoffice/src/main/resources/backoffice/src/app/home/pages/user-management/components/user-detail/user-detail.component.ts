import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';

import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

import { NotificationModel } from 'src/app/shared/models/notification.model';
import { UserSetting } from 'src/app/home/models/user/user-setting.model';
import { BOValidator } from 'src/app/core/utils/validator';
import { UserService } from 'src/app/core/services/user.service';
import { CUSTOMER_SEARCH_MODE } from 'src/app/core/enums/enums';
import AffUtils from 'src/app/core/utils/aff-utils';
import { ON_BEHALF_ADMIN_USER } from '../../consts/user-detail.const';

@Component({
    selector: 'backoffice-user-detail',
    templateUrl: './user-detail.component.html',
    styleUrls: ['./user-detail.component.scss'],
})
export class UserDetailComponent implements OnInit {
    @ViewChild('confirmModal', { static: true }) confirmModal: ElementRef;


    confirmModalRef: BsModalRef = null;

    private user: any = { id: '' };

    isPassFormExpand = true;
    // isUserSettingFormExpand = true;
    showDiscountEnable = AffUtils.isAT() ||  AffUtils.isCH() ||  AffUtils.isCZAX10();

    public notifierPassword: NotificationModel;
    public notifierUpdateUser: NotificationModel;
    public profilePasswordForm: any;
    public profileForm: any;

    userSetting: UserSetting;

    public optionsLanguage = new Array();
    public optionsSalutation = new Array();
    public optionsType = new Array();

    public payments;
    public deliveryTypes;
    public deliveryMethods;
    // private isAuthed: boolean;

    // public collapsed1 = true;
    // public collapsed2 = true;
    // public collapsed3 = true;
    private originalProfileForm: any;
    // private confirmSaveSettingModal = 'confirm-save-setting';
    private forceRedirect = false;
    public isSb = AffUtils.isSB();
    isAffiliateApplyForPDP = AffUtils.isAffiliateApplyForPDP();

    constructor(
        private userService: UserService,
        private fb: FormBuilder,
        private translateService: TranslateService,
        private router: Router,
        private route: ActivatedRoute,
        private locationService: Location,
        private modalService: BsModalService
    ) { }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            Object.assign(this.user, params);
            this.buildPasswordForm();
            this.userService.showUserDetail(this.user.id).subscribe((result) => {
                const res: any = result;
                this.userSetting = new UserSetting(res);
                this.userSetting.userId = this.user.id;
                this.payments = res.paymentMethod;
                this.deliveryTypes = res.deliveryTypes;
                this.deliveryMethods = res.collectiveDelivery;
                this.buildSelectDropdown(res);
                this.buildProfileForm();
                this.originalProfileForm = JSON.parse(
                    JSON.stringify(this.userSetting)
                );
                this.originalProfileForm = Object.assign(
                    this.originalProfileForm,
                    this.profileForm.getRawValue()
                );
                if (res.type) {
                    if (res.type === ON_BEHALF_ADMIN_USER && this.isAffiliateApplyForPDP) {
                        this.disableFieldsIfUseIsOnbehalfAdmin();
                    }
                }
            });
        });
    }

    // togglePanel(state) {
    //     this[state] = !this[state];
    // }

    hideDetailAndShowUserList() {
        this.viewCustomerDetail();
    }

    onInvalidUserName() {
        let message = this.translateService.instant(
            'REGISTER.MESSAGES.INVALID_USER_NAME'
        );
        this.notifierUpdateUser = { messages: [message], status: false };
    }

    public updatePaymentCheckbox(event) {
        this.userSetting.paymentId = +event.target.value;
    }

    public updateDeliveryType(event) {
        this.userSetting.deliveryId = +event.target.value;
    }

    public updateDeliveryMethod(event) {
        this.userSetting.collectiveDelivery = +event.target.value;
    }

    public updatePassword() {
        const password = this.profilePasswordForm.value.pass1;
        this.userService
            .updatePassword({
                id: this.user.id,
                password,
                redirectUrl: window.location.origin,
            })
            .subscribe(
                (res) => {
                    this.notifierPassword = {
                        messages: ['PASSWORD.MESSAGE_SUCCESSFUL'],
                        status: true,
                    };
                },
                (error) => {
                    const errorCode = error && error.error && `PASSWORD.${error.error.error_code}` || error.message;

                    this.notifierPassword = { messages: [errorCode], status: false };
                }
            );
    }

    public async saveUserDetail() {
        this.notifierUpdateUser = null;
        const userForm = this.profileForm.getRawValue();
        const userName = (userForm.userName || '').trim();
        if (!userName) {
            this.onInvalidUserName();
            return;
        }
        const transformedUserValues = {
            ...userForm,
            userName,
            languageId: userForm.languageId.value
        };
        Object.assign(this.userSetting, transformedUserValues);
        await this.userService
            .updateUserSetting(this.userSetting)
            .toPromise()
            .catch((err) => {
                let message = err.message;

                const errorCode = err && err.error && err.error.error_code || err && err.error_code;

                if (errorCode === 'INVALID_USERNAME_IN_AFFILIATE') {
                    message = this.translateService.instant(
                        'REGISTER.MESSAGES.INVALID_USERNAME_IN_AFFILIATE'
                    );
                }
                this.notifierUpdateUser = { messages: [message], status: false };
            });
        if (this.notifierUpdateUser && !this.notifierUpdateUser.status) {
            return;
        }
        this.notifierUpdateUser = {
            messages: ['USER_MANAGEMENT.USER_DETAIL.MESSAGE_SUCCESSFUL'],
            status: true,
        };
        this.originalProfileForm = Object.assign(this.originalProfileForm, this.userSetting);
        this.originalProfileForm = Object.assign(
            this.originalProfileForm,
            this.profileForm.getRawValue()
        );
        this.forceRedirect = false;
    }

    public viewCustomerDetail() {
        this.router.navigate([
            '/home/search/customers/detail',
            {
                affiliate: this.userSetting.affiliate,
                customerNr: this.userSetting.orgCode,
                searchMode: CUSTOMER_SEARCH_MODE.CUSTOMER,
            },
        ]);
    }

    isSettingChanged() {
        Object.assign(this.userSetting, this.profileForm.getRawValue());
        return (
            JSON.stringify(this.originalProfileForm) !==
            JSON.stringify(this.userSetting)
        );
    }

    async saveUserDetailAndRedirect() {
        await this.saveUserDetail();
        this.closeModal();
        if (!this.notifierUpdateUser || !this.notifierUpdateUser.status) {
            return;
        }
        this.viewCustomerDetail();
    }

    closeModalAndRedirect() {
        this.forceRedirect = true;
        this.closeModal();
        this.viewCustomerDetail();
    }

    closeModal() {
        this.confirmModalRef.hide();
    }

    canDeactivate() {
        if (!this.forceRedirect && this.isSettingChanged()) {
            if (!this.confirmModalRef) {
                this.confirmModalRef = this.modalService.show(this.confirmModal, {
                    class: 'modal-user-form  modal-lg',
                    ignoreBackdropClick: false,
                });
            } else {
                this.confirmModalRef.hide();
                this.confirmModalRef = this.modalService.show(this.confirmModal);
            }

            return false;
        }
        return true;
    }

    private buildSelectDropdown(res) {
        // build language and set default
        this.optionsLanguage = this.buildLanguageSelect(res.languageDtos);

        // build salutation and set default
        this.optionsSalutation = this.buildSalutationSelect(res.salutationDtos);

        // build type and set default
        this.optionsType = this.buildTypeSelect(res.types);
    }

    private buildLanguageSelect(data) {
        const languages = new Array();
        for (const languageItem of data) {
            languages.push({
                value: languageItem.id.toString(),
                label: languageItem.description,
            });
        }
        return languages;
    }

    private buildSalutationSelect(data) {
        const salutations = new Array();
        for (const salutationItem of data) {
            salutations.push({
                value: salutationItem.id.toString(),
                label: this.translateService.instant(
                    'USER_MANAGEMENT.USER_DETAIL.SALUTATION_TITLE.' + salutationItem.code
                ),
            });
        }
        return salutations;
    }

    private buildTypeSelect(data) {
        const types = new Array();
        for (const typeItem of data) {
            types.push({
                value: typeItem.id.toString(),
                label: this.translateService.instant(
                    'COMMON.LABEL.ROLE.' + typeItem.name
                ),
            });
        }
        return types;
    }

    private buildProfileForm() {
        this.profileForm = this.fb.group({
            id: new FormControl(
                { value: this.userSetting.userId || 0 },
                Validators.required
            ),
            customerNumber: new FormControl(this.userSetting.orgCode || ''),
            companyName: new FormControl(this.userSetting.companyName || ''),
            extCustomerName: new FormControl(
                this.userSetting.externalCustomerName || ''
            ),
            extUserName: new FormControl(this.userSetting.externalUserName || ''),
            salutationId: new FormControl(this.userSetting.salutationId),
            userName: new FormControl(this.userSetting.userName || '', [
                Validators.required,
                BOValidator.userNameValidator,
            ]),
            lastName: new FormControl(this.userSetting.lastName, Validators.required),
            firstName: new FormControl(
                this.userSetting.firstName || '',
                Validators.required
            ),
            email: new FormControl(this.userSetting.email || '', [
                Validators.required,
                BOValidator.emailValidator,
            ]),
            telephone: new FormControl(this.userSetting.telephone),
            fax: new FormControl(this.userSetting.fax),
            languageId: new FormControl({
                value: this.userSetting.languageId,
                label: this.optionsLanguage.find(lang => lang.value === this.userSetting.languageId).label
            }),
            typeId: this.userSetting.typeId,
            emailNotificationOrder: new FormControl(
                this.userSetting.emailNotificationOrder
            ),
            netPriceView: new FormControl(this.userSetting.netPriceView),
            showDiscount: new FormControl(this.userSetting.showDiscount),
            isUserActive: new FormControl(this.userSetting.isUserActive),
            netPriceConfirm: new FormControl(this.userSetting.netPriceConfirm),
            hourlyRate: new FormControl(this.userSetting.hourlyRate, [
                BOValidator.validateHourlyRate,
            ]),
        });
    }

    private buildPasswordForm() {
        this.profilePasswordForm = this.fb.group(
            {
                pass1: ['', [Validators.required, BOValidator.passwordValidator]],
                pass2: ['', Validators.required],
            },
            {
                validator: BOValidator.passwordMatchesValidator,
            }
        );
    }

    disableFieldsIfUseIsOnbehalfAdmin() {
        this.profilePasswordForm.get('pass1').disable({ onlySelf: true });
        this.profilePasswordForm.get('pass2').disable({ onlySelf: true });
        this.profileForm.get('typeId').disable({ onlySelf: true });
        this.profileForm.get('netPriceConfirm').disable({ onlySelf: true });
        this.profileForm.get('isUserActive').disable({ onlySelf: true });
        this.profileForm.get('email').disable({ onlySelf: true });
        this.profileForm.get('userName').disable({ onlySelf: true });
    }
}
