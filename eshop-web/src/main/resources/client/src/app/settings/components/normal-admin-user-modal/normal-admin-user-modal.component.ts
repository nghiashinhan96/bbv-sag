import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { ProfileModel } from '../../models/final-user-admin/user-profile.model';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { UserDetail } from 'src/app/core/models/user-detail.model';

@Component({
    selector: 'connect-normal-admin-user-modal',
    templateUrl: 'normal-admin-user-modal.component.html',
    styleUrls: ['normal-admin-user-modal.component.scss']
})
export class NormalAdminUserModalComponent implements OnInit {
    @Input() init: Observable<ProfileModel>;
    @Input() userId: number;
    @Input() userDetail: UserDetail;

    @Input() updateUser: any;
    @Input() updatePassword: any;

    user: ProfileModel;

    error: any;

    private readonly spinnerSelector = '.modal-body';

    constructor(
        public bsModalRef: BsModalRef
    ) { }

    ngOnInit() {
        this.initData();
    }

    initData() {
        SpinnerService.start(this.spinnerSelector);
        this.init
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe((result: ProfileModel) => {
                if (!this.userId) {
                    this.user = { ...result };
                    this.user.languages = result.languages;
                    this.user.salutations = result.salutations;
                    this.user.types = result.types;
                    this.user.languageId = (result.languages[0].id || 1).toString();
                    this.user.salutationId = (result.salutations[0].id || 1).toString();
                    this.user.typeId = (result.types[0].id || 1).toString();
                } else {
                    this.user = { ...result };
                    this.user.languageId = this.user.languageId && this.user.languageId.toString();
                    this.user.salutationId = this.user.salutationId && this.user.salutationId.toString();
                    this.user.typeId = this.user.typeId && this.user.typeId.toString();
                }
            }, err => {
                this.error = err;
            });
    }

    onSubmitPassword({ data, onSuccess, onError }) {
        SpinnerService.start(this.spinnerSelector);

        const params = {
            data,
            onSuccess: () => {
                onSuccess();
                SpinnerService.stop(this.spinnerSelector);
            },
            onError: (error) => {
                onError(error);
                SpinnerService.stop(this.spinnerSelector);
            }
        };

        this.updatePassword(params);
    }

    onSubmitProfile({ data, onSuccess, onError }) {
        SpinnerService.start(this.spinnerSelector);

        const params = {
            data,
            onSuccess: () => {
                onSuccess();
                SpinnerService.stop(this.spinnerSelector);
            },
            onError: (error) => {
                onError(error);
                SpinnerService.stop(this.spinnerSelector);
            }
        };

        if (this.userId) {
            this.updateUser(params);
        }
    }
}
