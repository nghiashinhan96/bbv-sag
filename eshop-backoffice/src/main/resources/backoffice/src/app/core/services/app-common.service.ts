import { Injectable, Injector } from '@angular/core';
import { AppStorageService } from './custom-local-storage.service';
import { SysNotificationComponent } from '../components/sys-notification/sys-notification.component';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { SagConfirmationBoxComponent } from 'sag-common';
import { BsModalService } from 'ngx-bootstrap/modal';

@Injectable({
    providedIn: 'root'
})
export class AppCommonService {
    private modal;
    constructor(
        private injector: Injector,
        private appStorage: AppStorageService,
        private dialogService: BsModalService,
        private translateService: TranslateService,
        private router: Router
    ) { }


    invalidVersionMessage() {
        if (!!this.modal) {
            return;
        }
        this.modal = this.dialogService.show(SysNotificationComponent, {
            initialState: {
                message: 'MESSAGES.VERSION_CHECKER_INFORM',
                close: () => {
                    this.modal = null;
                    this.appStorage.appVersion = null;
                    this.appStorage.removeAll();
                    window.location.reload();
                }
            },
            class: 'modal-sm sys-notification',
            keyboard: false,
            ignoreBackdropClick: true
        });
    }

    authError() {
        if (!!this.modal) {
            return;
        }
        this.modal = this.dialogService.show(SysNotificationComponent, {
            initialState: {
                message: 'MESSAGES.SECTION_EXPIRED'
            },
            class: 'modal-sm sys-notification',
            keyboard: false,
            ignoreBackdropClick: true
        });
        this.modal.content.close = () => {
            this.modal = null;
            this.appStorage.removeAll();
            this.router.navigateByUrl('/login');
        };
    }

    offlineMessage() {
        if (!!this.modal) {
            return;
        }
        const message =
            `<h5>${this.translateService.instant('COMMON_LABEL.YOU_ARE_OFFLINE')}</h5> <h4>${this.translateService.instant('COMMON_LABEL.GLOBAL_ERROR_CONTENT')}</h4>`;
        this.modal = this.dialogService.show(SagConfirmationBoxComponent, {
            class: 'offline-modal',
            initialState: {
                title: 'COMMON_LABEL.GLOBAL_ERROR_TITLE',
                message,
                okButton: 'COMMON_LABEL.RETRY',
                showHeaderIcon: false,
                showCloseButton: true,
                showCancelButton: false,
                confirm: () => {
                    this.modal = null;
                    window.location.reload();
                }
            },
            keyboard: false,
            ignoreBackdropClick: true
        });
    }
}
