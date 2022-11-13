import { Component, OnInit, Input, ViewChild, TemplateRef, EventEmitter, Output } from '@angular/core';
import { SagTableColumn } from 'sag-table';

import { NormalAdminBusinessService } from '../../services/normal-admin-business.service';
import { Observable } from 'rxjs';
import { AdminManagementUser } from '../../models/normal-admin/user-admin-magement.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { groupBy } from 'lodash';
import { TranslateService } from '@ngx-translate/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { NormalAdminService } from '../../services/normal-admin.service';
import { NormalAdminUserModalComponent } from '../normal-admin-user-modal/normal-admin-user-modal.component';
import { ProfileService } from '../../services/profile.service';
import { SettingsService } from '../../services/settings.service';
import { SagConfirmationBoxComponent, SagMessageData } from 'sag-common';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Component({
    selector: 'connect-normal-admin-user-list',
    templateUrl: './normal-admin-user-list.component.html',
    styleUrls: ['./normal-admin-user-list.component.scss']
})
export class NormalAdminUserListComponent implements OnInit {
    @ViewChild('salutation', { static: true }) salutation: TemplateRef<any>;
    @ViewChild('role', { static: true }) role: TemplateRef<any>;
    @ViewChild('actions', { static: true }) actions: TemplateRef<any>;

    @Input() userList: Observable<AdminManagementUser[]>;
    @Input() currentUser: UserDetail;
    @Output() refreshUserList = new EventEmitter<any>();

    columns: SagTableColumn[] = [];

    private salutationSelect = [];
    private roleSelect = [];

    constructor(
        private businessSevice: NormalAdminBusinessService,
        private translateService: TranslateService,
        private bsModelService: BsModalService,
        private settingsService: SettingsService,
        private profileService: ProfileService,
        private normalAdminService: NormalAdminService
    ) { }

    async ngOnInit() {
        this.userList.subscribe(list => {
            const groupsSalutation = groupBy(list, 'salutation');
            const groupsRole = groupBy(list, 'role');

            this.salutationSelect = Object.keys(groupsSalutation)
                .map(salutationKey => ({
                    value: salutationKey,
                    label: this.translateService.instant(`SETTINGS.PROFILE.SALUTATION.${salutationKey}`)
                }));
            this.roleSelect = Object.keys(groupsRole).map(role => ({
                value: role,
                label: this.translateService.instant(`SETTINGS.PROFILE.TYPE.${role}`)
            }));

            this.columns = this.businessSevice.buildUserListColumns(
                [this.salutation, this.role, this.actions],
                [this.salutationSelect, this.roleSelect]
            );
        });
    }

    updatePassword({ data, onSuccess, onError }) {
        this.profileService.updatePasswordForAdmin(data).subscribe(
            () => {
                onSuccess();
            },
            (err) => {
                onError(this.settingsService.handleErrorMessage(err));
            }
        );
    }

    updateUser({ data, onSuccess, onError }) {
        this.profileService.updateUserProfile(data).subscribe(
            () => {
                this.refreshUserList.emit();
                onSuccess();
            },
            (err) => {
                onError(this.settingsService.handleErrorMessage(err));
            }
        );
    }

    editUser(userInfo: AdminManagementUser) {
        this.bsModelService.show(NormalAdminUserModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                userId: userInfo.id,
                userDetail: this.currentUser,
                init: this.profileService.getUserProfileById(userInfo.id),
                updateUser: (data) => {
                    this.updateUser(data);
                },
                updatePassword: (data) => {
                    this.updatePassword(data);
                }
            }
        });
    }

    deleteUser(userInfo: AdminManagementUser) {
        this.bsModelService.show(SagConfirmationBoxComponent, {
            ignoreBackdropClick: true,
            initialState: {
                title: '',
                message: 'SETTINGS.DELETE_CONFIRM_MESSAGE',
                messageParams: { value: `${userInfo.firstName} ${userInfo.lastName}` },
                okButton: 'SETTINGS.DELETE',
                cancelButton: 'SETTINGS.CANCEL',
                bodyIcon: 'fa-exclamation-triangle',
                close: () => new Promise((resolve) => {
                    SpinnerService.start('connect-confirmation-box', { containerMinHeight: 0 });
                    this.normalAdminService.deleteUser(userInfo.id).subscribe(() => {
                        resolve(null);
                        SpinnerService.stop('connect-confirmation-box');
                        this.userList = this.normalAdminService.getAllUsers();
                    }, error => {
                        resolve({
                            type: 'ERROR',
                            message: 'SETTINGS.DELETE_FAILURE'
                        } as SagMessageData);
                    });
                })
            }
        });
    }

}
