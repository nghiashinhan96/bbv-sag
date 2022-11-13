import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NgOption } from '@ng-select/ng-select';
import { isEmpty } from 'lodash';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { SagTableColumn, SagTableControl, TablePage } from 'sag-table';
import { SagConfirmationBoxComponent, SagMessageData } from 'sag-common';
import { Constant } from 'src/app/core/conts/app.constant';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { FinalCustomerUserResponse } from '../../models/final-customer/final-customer-user-response.model';
import { FinalCustomerUser } from '../../models/final-customer/final-customer-user.model';
import { FinalUserSearchCriteria } from '../../models/final-user-admin/final-user-search-criteria.model';
import { ProfileModel } from '../../models/final-user-admin/user-profile.model';
import { FinalUserAdminService } from '../../services/final-user-admin.service';
import { SettingsService } from '../../services/settings.service';
import { FinalAdminUserModalComponent } from 'src/app/shared/final-customer/components/final-admin-user-modal/final-admin-user-modal.component';

@Component({
    selector: 'connect-final-user-admin',
    templateUrl: './final-user-admin.component.html',
    styleUrls: ['./final-user-admin.component.scss']
})
export class FinalUserAdminComponent implements OnInit, OnDestroy, SagTableControl {
    bsModalRef: BsModalRef;
    userProfile: ProfileModel;

    searchModel: any = {};
    page: TablePage = new TablePage();
    sort = {};

    finalCustomerTypes: NgOption[] = [];
    salutationTypes: NgOption[] = [];

    columns = [];
    sortKeys = {
        userName: 'orderByUserNameDesc',
        firstName: 'orderByFirstNameDesc',
        lastName: 'orderByLastNameDesc'
    };

    data: FinalCustomerUserResponse = new FinalCustomerUserResponse();

    notFoundText = '';

    @ViewChild('colSalutation', { static: true }) colSaluationRef: TemplateRef<any>;
    @ViewChild('colType', { static: true }) colTypeRef: TemplateRef<any>;
    @ViewChild('colActions', { static: true }) colActionsRef: TemplateRef<any>;

    constructor(
        private modalService: BsModalService,
        private settingsService: SettingsService,
        private finalUserAdminService: FinalUserAdminService
    ) { }

    ngOnInit() {
        this.buildOfferColumns();
        this.finalUserAdminService.getFinalUserProfile().subscribe((res: ProfileModel) => {
            this.userProfile = res;
            const TYPE_KEY = 'SETTINGS.PROFILE.TYPE.';
            const SALUTATION_KEY = 'SETTINGS.PROFILE.SALUTATION.';

            const fullOptions = [{ id: Constant.ALL_OPTION, description: 'All', name: 'ALL' }, ...res.types];
            const salutationOptions = [{ id: Constant.ALL_OPTION, description: 'All', code: 'ALL' }, ...res.salutations];

            this.finalCustomerTypes = this.buildSelectOptions(fullOptions, TYPE_KEY, 'name');
            this.salutationTypes = this.buildSelectOptions(salutationOptions, SALUTATION_KEY, 'code');
            this.columns[1].selectSource = this.salutationTypes;
            this.columns[4].selectSource = this.finalCustomerTypes;
        });

    }

    ngOnDestroy() {
    }

    buildOfferColumns() {
        this.columns = [
            {
                id: 'userName',
                i18n: 'SETTINGS.USER_NAME',
                filterable: true,
                sortable: true,
                width: '200px'
            },
            {
                id: 'salutation',
                i18n: 'COMMON_LABEL.SALUTATION',
                filterable: true,
                sortable: false,
                cellTemplate: this.colSaluationRef,
                type: 'select',
                selectSource: this.salutationTypes,
                selectValue: 'value',
                selectLabel: 'label',
                width: '115px'
            },
            {
                id: 'firstName',
                i18n: 'COMMON_LABEL.FIRST_NAME',
                filterable: true,
                sortable: true,
                width: 'auto'
            },
            {
                id: 'lastName',
                i18n: 'COMMON_LABEL.LAST_NAME',
                filterable: true,
                sortable: true,
                width: '180px'
            },
            {
                id: 'type',
                i18n: 'COMMON_LABEL.TYPE',
                filterable: true,
                sortable: false,
                cellTemplate: this.colTypeRef,
                type: 'select',
                selectValue: 'value',
                selectLabel: 'label',
                selectSource: this.finalCustomerTypes,
                width: '210px'
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                cellTemplate: this.colActionsRef,
                width: '100px'
            }
        ] as SagTableColumn[];
    }

    searchTableData({ request, callback }) {
        const criteria = new FinalUserSearchCriteria(isEmpty(request.filter) ? null : request.filter);
        criteria.page = request.page - 1;

        if (request.sort && request.sort.field) {
            const column = this.sortKeys[request.sort.field];
            const value = request.sort.direction === 'asc';
            criteria.sort.reset();
            criteria.sort[column] = value;
        }

        Object.assign(this.searchModel, criteria);

        this.finalUserAdminService.getFinalCustomerUserList(criteria)
            .subscribe((res) => {
                if (!res) {
                    return;
                }
                if (callback) {
                    callback({
                        rows: res.data,
                        totalItems: res.totalElements,
                        page: this.searchModel.page + 1
                    });
                }
            }, (err) => {
                if (callback) {
                    callback({
                        rows: [],
                        totalItems: 0
                    });
                }
                this.notFoundText = err.error.code === Constant.NOT_FOUND_CODE ? 'MESSAGES.NO_RESULTS_FOUND' : 'MESSAGES.GENERAL_ERROR';
            });
    }

    buildSelectOptions(selectObjects, prefix: string, key: string) {
        return selectObjects.map((option) => {
            return {
                value: option.id.toString(),
                label: prefix + option[key]
            };
        });
    }

    getProfile(userId?: number) {
        let profileSub;
        if (userId) {
            profileSub = this.finalUserAdminService.getFinalUser(userId);
        } else {
            profileSub = this.finalUserAdminService.getFinalUserProfile();
        }
        return profileSub;
    }

    createUser({ data, onSuccess, onError }) {
        this.finalUserAdminService.createFinalCustomerUser(data).subscribe(
            () => {
                onSuccess();
                setTimeout(() => {
                    this.searchModel = { ...this.searchModel };
                }, 500);
            },
            ({ error }) => {
                onError(this.handleErrorMessage(error));
            }
        );
    }

    updateUser(userId, { data, onSuccess, onError }) {
        this.finalUserAdminService.updateFinalUser(userId, data).subscribe(
            () => {
                onSuccess();
                setTimeout(() => {
                    this.searchModel = { ...this.searchModel };
                }, 500);
            },
            ({ error }) => {
                onError(this.handleErrorMessage(error));
            }
        );
    }

    handleErrorMessage(error) {
        if (error && error.error_code) {
            if (error.error_code === 'DUPLICATED_USERNAME_IN_AFFILIATE') {
                return 'FINAL_CUSTOMER.FINAL_USER_ERROR.DUPLICATED_USER';
            }
        }
        return this.settingsService.handleErrorMessage(error);
    }

    updatePassword(userId, { data, onSuccess, onError }) {
        this.finalUserAdminService.changePassword(userId, data).subscribe(
            () => {
                onSuccess();
            },
            (err) => {
                onError(this.settingsService.handleErrorMessage(err));
            }
        );
    }

    openCreateUserModal() {
        this.modalService.show(FinalAdminUserModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                init: this.getProfile(),
                createUser: (data) => {
                    this.createUser(data);
                }
            }
        });
    }

    openEditUserModal(user: FinalCustomerUser) {
        this.modalService.show(FinalAdminUserModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                userId: user.userId,
                init: this.getProfile(user.userId),
                updateUser: (data) => {
                    this.updateUser(user.userId, data);
                },
                updatePassword: (data) => {
                    this.updatePassword(user.userId, data);
                }
            }
        });
    }

    openDeleteUserModal(user: FinalCustomerUser) {
        this.modalService.show(SagConfirmationBoxComponent, {
            ignoreBackdropClick: true,
            initialState: {
                title: 'COMMON_LABEL.CONFIRMATION',
                message: 'FINAL_CUSTOMER.DELETE_MESSAGE',
                okButton: 'COMMON_LABEL.CONFIRM_BTN',
                cancelButton: 'COMMON_LABEL.CLOSE',
                close: () => new Promise((resolve) => {
                    SpinnerService.start('connect-confirmation-box');
                    this.finalUserAdminService.deleteFinalUser(user.userId)
                        .subscribe(() => {
                            resolve(null);
                            this.searchModel = { ...this.searchModel };
                            SpinnerService.stop('connect-confirmation-box');
                        }, () => {
                            resolve({
                                type: 'ERROR',
                                message: 'SETTINGS.USER_MANAGEMENT.MESSAGE.DELETE_UNSUCCESFULLY'
                            } as SagMessageData);
                        });
                })
            }
        });
    }
}
