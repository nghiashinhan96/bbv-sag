import { SubSink } from 'subsink';
import { OnInit, OnDestroy, ViewChild, TemplateRef, Component } from '@angular/core';

import { SagTableControl, SagTableColumn } from 'sag-table';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { isEmpty } from 'lodash';
import { Subscription, Observable, of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SagConfirmationBoxComponent } from 'sag-common';
import { catchError, finalize } from 'rxjs/operators';

import { FinalCustomersUsersCriteria } from '../../../settings/models/final-customer/final-customer-user-criteria.model';
import { FinalCustomerUser } from '../../../settings/models/final-customer/final-customer-user.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { FinalUserAdminService } from '../../../settings/services/final-user-admin.service';
import { SettingsService } from '../../../settings/services/settings.service';
import { Constant } from '../../../core/conts/app.constant';
import { FinalAdminUserModalComponent } from 'src/app/shared/final-customer/components/final-admin-user-modal/final-admin-user-modal.component';
import { AppModalService } from 'src/app/core/services/app-modal.service';

@Component({
    selector: 'connect-wholesaler-customers-users',
    templateUrl: 'wholesaler-customers-users.component.html',
    styleUrls: ['wholesaler-customers-users.component.scss']
})
export class WholesalerCustomersUsersComponent implements OnInit, OnDestroy, SagTableControl {
    bsModalRef: BsModalRef;
    routerParamsSub: Subscription;

    orgId: number;
    searchModel: any = {};
    columns = [];
    sortKeys = {
        userName: 'orderByUserNameDesc',
        fullName: 'orderByFullNameDesc',
        userEmail: 'orderByEmailDesc'
    };
    notFoundText = '';
    finalCustomerTypes: any[] = [];

    subs = new SubSink();

    @ViewChild('colType', { static: true }) colTypeRef: TemplateRef<any>;
    @ViewChild('colActions', { static: true }) colActionsRef: TemplateRef<any>;

    constructor(
        private modalService: BsModalService,
        private activatedRoute: ActivatedRoute,
        private settingsService: SettingsService,
        private finalUserAdminService: FinalUserAdminService,
        private router: Router,
        private appModal: AppModalService
    ) { }

    ngOnInit() {
        const profileSub = this.getProfile();
        SpinnerService.start();
        this.subs.sink = profileSub
            .pipe(
                finalize(() => {
                    SpinnerService.stop();
                })
            )
            .subscribe(data => {
                this.finalCustomerTypes = this.buildCustomerTypes(data.types);
                this.buildColumns();
            });

        this.routerParamsSub = this.activatedRoute.params.subscribe(params => {
            if (!params) {
                return;
            }
            if (params.orgId) {
                this.orgId = params.orgId;
                setTimeout(() => {
                    this.searchModel = { ...this.searchModel, orgId: this.orgId };
                }, 0);
            }
        });
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
        this.routerParamsSub.unsubscribe();
    }

    searchTableData({ request, callback }) {
        const criteria = new FinalCustomersUsersCriteria(isEmpty(request.filter) ? null : request.filter);
        criteria.page = request.page - 1;

        if (request.sort && request.sort.field) {
            const column = this.sortKeys[request.sort.field];
            const sortValue = request.sort.direction === 'desc';
            criteria.sort.reset();
            criteria.sort[column] = sortValue;
        }

        Object.assign(this.searchModel, criteria);

        this.subs.sink = this.finalUserAdminService.searchFinalCustomersUser(this.searchModel.orgId, criteria).pipe(
            catchError(err => {
                this.notFoundText = err.error.code === Constant.NOT_FOUND_CODE ? 'MESSAGES.NO_RESULTS_FOUND' : 'MESSAGES.GENERAL_ERROR';
                return of({
                    data: [],
                    totalElements: 0,
                });
            })
        ).subscribe((res) => {
            if (res) {
                callback({
                    rows: res.data,
                    totalItems: res.totalElements,
                    page: this.searchModel.page + 1
                });
            }
        });
    }

    openCreateUserModal() {
        this.appModal.modals = this.modalService.show(FinalAdminUserModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                wssOrgId: this.orgId,
                init: this.getProfile(),
                createUser: (data) => {
                    this.createUser(data);
                }
            }
        });
    }

    openEditUserModal(user: FinalCustomerUser) {
        this.appModal.modals = this.modalService.show(FinalAdminUserModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                userId: user.userId,
                wssOrgId: this.orgId,
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
        this.appModal.modals = this.modalService.show(SagConfirmationBoxComponent, {
            ignoreBackdropClick: true,
            initialState: {
                title: 'COMMON_LABEL.CONFIRMATION',
                message: 'FINAL_CUSTOMER.DELETE_FINAL_USER_MESSAGE',
                okButton: 'COMMON_LABEL.CONFIRM_BTN',
                cancelButton: 'COMMON_LABEL.CLOSE',
                close: () => new Promise((resolve) => {
                    SpinnerService.start('connect-confirmation-box');
                    this.finalUserAdminService.deleteFinalUser(user.userId)
                        .subscribe(() => {
                            resolve(null);
                            setTimeout(() => {
                                this.searchModel = { ...this.searchModel };
                            }, 500);
                            SpinnerService.stop('connect-confirmation-box');
                        });
                })
            }
        });
    }

    backToCustomers(event) {
        event.preventDefault();
        event.stopPropagation();
        SpinnerService.start();
        this.router.navigate(['wholesaler']);
    }

    handleErrorMessage(error) {
        if (error && error.error_code) {
            if (error.error_code === 'DUPLICATED_USERNAME_IN_AFFILIATE') {
                return 'FINAL_CUSTOMER.FINAL_USER_ERROR.DUPLICATED_USER';
            }
        }
        return this.settingsService.handleErrorMessage(error);
    }

    private buildColumns() {
        this.columns = [
            {
                id: 'userName',
                i18n: 'SETTINGS.USER_NAME',
                filterable: true,
                sortable: true,
                width: '200px'
            },
            {
                id: 'fullName',
                i18n: 'COMMON_LABEL.NAME',
                filterable: true,
                sortable: true,
                width: 'auto'
            },
            {
                id: 'userEmail',
                i18n: 'COMMON_LABEL.EMAIL',
                filterable: true,
                sortable: true,
                width: '200px'
            },
            {
                id: 'type',
                i18n: 'COMMON_LABEL.TYPE',
                filterable: true,
                sortable: false,
                width: '150px',
                type: 'select',
                selectSource: this.finalCustomerTypes,
                selectValue: 'value',
                selectLabel: 'label',
                cellTemplate: this.colTypeRef
            },
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                width: '70px',
                cellClass: 'align-middle',
                cellTemplate: this.colActionsRef
            }
        ] as SagTableColumn[];
    }

    private getProfile(userId?: number): Observable<any> {
        let profileSub;
        if (userId) {
            profileSub = this.finalUserAdminService.getFinalUser(userId);
        } else {
            profileSub = this.finalUserAdminService.getFinalUserProfile();
        }
        return profileSub;
    }

    private createUser({ data, onSuccess, onError }) {
        this.finalUserAdminService.createFinalUser(this.orgId, data).subscribe(
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

    private updateUser(userId, { data, onSuccess, onError }) {
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

    private updatePassword(userId, { data, onSuccess, onError }) {
        this.finalUserAdminService.changePassword(userId, data).subscribe(
            () => {
                onSuccess();
            },
            (err) => {
                onError(this.settingsService.handleErrorMessage(err));
            }
        );
    }

    private buildCustomerTypes(types) {
        const TYPE_KEY = 'SETTINGS.PROFILE.TYPE.';
        const fullOptions = [{ id: Constant.ALL_OPTION, description: 'All', name: 'ALL' }, ...types];

        return fullOptions.map((option) => {
            return {
                value: option.id.toString(),
                label: TYPE_KEY + option.name
            };
        });
    }
}
