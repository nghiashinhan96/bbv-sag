import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { SagMessageData } from 'sag-common';
import { ProfileModel } from 'src/app/settings/models/final-user-admin/user-profile.model';

@Component({
    selector: 'connect-final-admin-user-modal',
    templateUrl: 'final-admin-user-modal.component.html',
    styleUrls: ['final-admin-user-modal.component.scss']
})
export class FinalAdminUserModalComponent implements OnInit {
    @Input() init: Observable<ProfileModel>;
    @Input() finalCustomerOrgId: number;
    @Input() userId: number;
    @Input() wssOrgId: number;

    @Input() createUser: any;
    @Input() updateUser: any;
    @Input() updatePassword: any;

    profile: ProfileModel;

    result: SagMessageData;

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
                if (this.userId) {
                    this.profile = { ...result };
                    this.profile.languageId = this.profile.languageId && this.profile.languageId.toString();
                    this.profile.salutationId = this.profile.salutationId && this.profile.salutationId.toString();
                    this.profile.typeId = this.profile.typeId && this.profile.typeId.toString();
                } else {
                    this.profile = new ProfileModel();
                    this.profile.languages = result.languages;
                    this.profile.salutations = result.salutations;
                    this.profile.types = result.types;
                    this.profile.languageId = (result.languages[0].id || 1).toString();
                    this.profile.salutationId = (result.salutations[0].id || 1).toString();
                    this.profile.typeId = (result.types[0].id || 1).toString();
                    this.profile.netPriceView = false;
                }
            }, () => {
                this.result = { type: 'WARNING', message: 'SEARCH.NO_RESULTS_FOUND' } as SagMessageData;
            });
    }

    onSubmitPassword({ data, onSuccess, onError }) {
        SpinnerService.start(this.spinnerSelector);

        const params = {
            data,
            onSuccess: () => {
                onSuccess();
                SpinnerService.stop(this.spinnerSelector);
                setTimeout(() => {
                    this.bsModalRef.hide();
                }, 300);
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
                setTimeout(() => {
                    this.bsModalRef.hide();
                }, 300);
            },
            onError: (error) => {
                onError(error);
                SpinnerService.stop(this.spinnerSelector);
            }
        };

        if (this.userId) {
            this.updateUser(params);
        } else {
            this.createUser(params);
        }
    }
}
