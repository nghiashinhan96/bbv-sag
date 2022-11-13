import { Component, OnInit, AfterViewInit } from '@angular/core';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { ChangePassword } from '../../models/change-password.model';
import { ProfileService } from '../../services/profile.service';
import { UserService } from 'src/app/core/services/user.service';
import { switchMap, map, finalize } from 'rxjs/operators';
import { UserProfile } from '../../models/user-profile/user-profile.model';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { ResponseMessage } from 'src/app/core/models/response-message.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { SettingsService } from '../../services/settings.service';
import { SagMessageData } from 'sag-common';
import { OciService } from 'src/app/oci/services/oci.service';

@Component({
    selector: 'connect-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, AfterViewInit {
    notification: SagMessageData;
    userDetail: UserDetail;
    userProfile: UserProfile;

    isOciFlow = false;

    constructor(
        private settingService: SettingsService,
        private profileService: ProfileService,
        private userService: UserService,
        private appStorageService: AppStorageService,
        private ociService: OciService,
    ) { }

    ngOnInit() {
        SpinnerService.start();
        this.profileService.getUserProfile().pipe(
            map(userProfile => this.userProfile = new UserProfile(userProfile)),
            switchMap(() => this.userService.userDetail$)
        ).subscribe(userDetail => { this.userDetail = { ...userDetail }; SpinnerService.stop(); });
    }

    ngAfterViewInit(): void {
        this.isOciFlow = this.ociService.getOciState();
    }

    updatePassword({ request, callback }) {
        if (!request) {
            return;
        }

        if (this.userDetail.isFinalUserRole) {
            request.id = this.userDetail.id;
            this.updatePasswordForFinalUser(request, callback);
        } else {
            this.updatePasswordForUser(request, callback);
        }
    }

    updateProfile({ request, langIso, callback }) {
        SpinnerService.start();
        if (request) {
            this.profileService.updateProfile(request).pipe(finalize(() => SpinnerService.stop())).subscribe(() => {
                this.notification = { type: 'SUCCESS', message: 'SETTINGS.PROFILE.MESSAGE_SUCCESSFUL' } as SagMessageData;

                if(langIso !== this.appStorageService.appLangCode) {
                    this.appStorageService.uniTrees = null;
                }

                this.appStorageService.appLangCode = langIso;
                callback(this.notification);
            }, error => {
                this.notification = this.handleErrorMessage(error);
                callback(this.notification);
            });
        }
    }

    private updatePasswordForFinalUser(data: ChangePassword, callback: any) {
        this.profileService.updatePasswordForFinalUser(data).pipe(finalize(() => SpinnerService.stop())).subscribe(response => {
            this.notification = { type: 'SUCCESS', message: 'SETTINGS.PROFILE.MESSAGE_SUCCESSFUL' } as SagMessageData;
            callback(this.notification);
        }, ({error}) => {
            this.notification = this.handleErrorMessage(error);
            callback(this.notification);
        });
    }

    private updatePasswordForUser(data: ChangePassword, callback: any) {
        this.profileService.updatePasswordForUser(data).pipe(finalize(() => SpinnerService.stop())).subscribe(response => {
            this.notification = { type: 'SUCCESS', message: 'SETTINGS.PROFILE.MESSAGE_SUCCESSFUL' } as SagMessageData;
            callback(this.notification);
        }, ({error}) => {
            this.notification = this.handleErrorMessage(error);
            callback(this.notification);
        });
    }

    private handleErrorMessage(error: any) {
        this.notification = { type: 'ERROR', message: this.settingService.handleErrorMessage(error) };
        return this.notification;
    }
}
