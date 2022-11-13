import { Injectable, Injector } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SysNotificationComponent } from '../components/sys-notification/sys-notification.component';
import { AppStorageService } from './app-storage.service';
import { TranslateService } from '@ngx-translate/core';
import { SagConfirmationBoxComponent, MarkedHtmlPipe, AffiliateUtil } from 'sag-common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { environment } from 'src/environments/environment';
@Injectable({
    providedIn: 'root'
})
export class AppCommonService {
    private modal;
    private isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
    constructor (
        private injector: Injector
    ) { }

    get markedHtmlPipe() {
        return this.injector.get(MarkedHtmlPipe);
    }

    get dialogService() {
        return this.injector.get(BsModalService);
    }

    get appStorage() {
        return this.injector.get(AppStorageService);
    }

    get translateService() {
        return this.injector.get(TranslateService);
    }

    get router() {
        return this.injector.get(Router);
    }

    get http() {
        return this.injector.get(HttpClient);
    }

    getDeliveryTextApplyAffiliateSetting() {
        const settings = this.appStorage.userPrice || null;
        const langCode = this.appStorage.appLangCode;

        if (settings && !this.isEhCz) {
            const availText = (settings.detailAvailText || []).find(item => item.langIso === langCode);
            if (availText) {
                return this.markedHtmlPipe.transform(availText.content);
            }
        }

        return this.translateService.instant('ARTICLE.24_HOURS');
    }

    logError(body) {
        if (!this.appStorage.appToken || !navigator.onLine) {
            return of(null);
        }
        const url = `${environment.baseUrl}log/jserror`;
        return this.http.post(url, body).pipe(catchError(err => {
            console.error(err);
            return of(null);
        }));
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
            ignoreBackdropClick: true,
            keyboard: false
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
            ignoreBackdropClick: true,
            keyboard: false
        });
    }

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

    getNotReferencedText() {
        return this.translateService.instant('PDP.NOT_REFERENCED');
    }

    getPromotionText() {
        return this.translateService.instant('PDP.PROMOTION');
    }
}
