import { Component, OnInit, Input, OnDestroy, ChangeDetectorRef, ViewChild, ElementRef } from '@angular/core';
import { SystemMessagesService } from 'src/app/core/services/system-messages.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SystemMessage } from 'src/app/core/models/system-message.model';
import { Subject } from 'rxjs';
import { Constant } from 'src/app/core/conts/app.constant';
import { UserService } from 'src/app/core/services/user.service';
import { takeUntil } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import uuid from 'uuid/v4';
import { environment } from 'src/environments/environment';
import { AffiliateUtil } from 'sag-common';
import { FormUtil } from 'src/app/core/utils/form.util';

@Component({
    selector: 'connect-system-message',
    templateUrl: 'system-message.component.html',
    styleUrls: ['system-message.component.scss']
})
export class SystemMessageComponent implements OnInit, OnDestroy {
    message: SystemMessage;
    showMessage = false;
    isAuthed = false;
    ssoInfo = false;
    id = uuid();
    affiliate: string;
    @Input() subArea: string;

    private onDestroy$ = new Subject();
    @ViewChild('dataForm', { static: true }) dataForm: ElementRef;

    constructor(
        private cdRef: ChangeDetectorRef,
        private userService: UserService,
        private storageService: AppStorageService,
        private systemMessageService: SystemMessagesService,
        private translateService: TranslateService
    ) { }

    ngOnInit() {
        this.affiliate = environment.affiliate;

        this.systemMessageService.messages$
            .pipe(takeUntil(this.onDestroy$))
            .subscribe(messages => {
                this.getMessages(messages);
            });

        this.userService.userDetail$
            .pipe(takeUntil(this.onDestroy$))
            .subscribe(user => {
                this.isAuthed = !!user;
            });
    }

    hideMessage() {
        if (this.isAuthed && this.message.visibility === Constant.MESSAGE_METHOD_ONCE) {
            this.systemMessageService.setHideMessageById(this.message.id)
                .pipe(takeUntil(this.onDestroy$))
                .subscribe();
        }
    }

    ngOnDestroy() {
        this.updateBannerTypeLayout(true);
        this.onDestroy$.next();
        this.onDestroy$.complete();
    }

    closeMessage() {
        this.showMessage = false;
        if (this.message.visibility === Constant.MESSAGE_METHOD_SECTION_HIDING) {
            this.storageService.hiddenSystemMessages = this.message.id.toString();
        }
        this.updateBannerTypeLayout(true);
    }

    private getMessages(messages: SystemMessage[]) {
        const message = (messages || []).find(item => this.canShowMessage(this.subArea, item));
        if (message) {
            this.message = message;
            this.showMessage = true;
            this.hideMessage();
            this.ssoInfo = !!message.ssoInfo;
            if (!!message.ssoInfo) {
                this.buildFormData(message.ssoInfo);
            }
            this.cdRef.detectChanges();
            setTimeout(() => {
                this.updateBannerTypeLayout();
            });
        }
    }

    private canShowMessage(subArea, message) {
        if (subArea !== message.subArea) {
            return false;
        }

        const hiddenMessages = this.storageService.hiddenSystemMessages;

        const isHidden = (hiddenMessages || []).some(id => id === message.id.toString());

        return !isHidden;
    }

    private updateBannerTypeLayout(reset = false) {
        if (!this.message) {
            return;
        }

        if (this.message.type !== Constant.MESSAGE_TYPE_BANNER || this.message.subArea !== 'ALL') {
            return;
        }

        let height = 0;
        if (!reset) {
            const banner = document.querySelector('.system-message .banner') as HTMLElement;
            height = banner && banner.clientHeight || 0;
        }

        document.body.style.paddingTop = `${height}px`;
        document.documentElement.style.setProperty('--msg-height', `${height}px`);
        const header = document.querySelector('connect-header .header') as HTMLElement;
        if (header) {
            header.style.top = `${height}px`;
        }

        const gtmotive = document.querySelector('.gte-wrapper') as HTMLElement;
        if (gtmotive) {
            gtmotive.style.top = `${parseInt(document.body.style.paddingTop, 10)}px`;
        }
    }

    executeAdvert() {
        if (!!this.ssoInfo) {
            const btn = this.dataForm.nativeElement.querySelector('input[type=submit]') as any;
            if (btn) {
                btn.click();
            }
        }
    }

    private buildFormData(json) {
        const langCode = this.translateService.currentLang.toUpperCase();
        let actionLink = '';
        if (AffiliateUtil.isBaseAT(environment.affiliate)) {
            actionLink = `https://www.svs-digital.at/sagauth/authenticate/?language=${langCode}`;
        } else {
            actionLink = `https://www.swissautomotiveshow.ch/sagauth/authenticate/?language=${langCode}`;
        }
        const f = FormUtil.buildFormData(json, actionLink);
        this.dataForm.nativeElement.innerHTML = '';
        this.dataForm.nativeElement.appendChild(f);
    }
}
