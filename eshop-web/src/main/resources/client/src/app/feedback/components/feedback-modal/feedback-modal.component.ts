import { Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgOption } from '@ng-select/ng-select';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { finalize, takeUntil, tap } from 'rxjs/operators';
import * as FileSaver from 'file-saver';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { UserService } from 'src/app/core/services/user.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { FeedbackUserType } from '../../enums/feedback.enum';
import { FeedbackData } from '../../models/feedback-data.model';
import { CustomerBusinessService } from '../../services/customer-business.service';
import { FeedbackBusinessService } from '../../services/feedback-business.service';
import { FeedbackService } from '../../services/feedback.service';
import { SalesNotOnBehalfBusinessService } from '../../services/sales-not-onbehalf-business.service';
import { SalesOnBehalfBusinessService } from '../../services/sales-onbehalf-business.service';
import { FeedbackDataItem } from '../../models/feedback-data-item.model';
import { TranslateService } from '@ngx-translate/core';
import { FeedbackTopic } from '../../models/feedback-topic.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { FeedbackSavingRequest } from '../../models/feedback-saving-request.model';
import { SagMessageData, BrowserUtil } from 'sag-common';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';

const LIMIT_FILE_SIZE = 5 * 1024 * 1024;
const EXECUTABLE_FILE_EXTENSIONS = ['exe', 'bat', 'com', 'cmd', 'inf', 'ipa', 'osx', 'pif', 'run', 'wsh', 'dll'];

@Component({
    selector: 'connect-feedback-modal',
    templateUrl: 'feedback-modal.component.html'
})
export class FeedbackModalComponent implements OnInit, OnDestroy {
    title: string;
    form: FormGroup;
    userType: FeedbackUserType;
    topics: NgOption[];
    source: string;
    userData: FeedbackData;
    userEmail: string;
    technicalData: FeedbackData;
    attachments = [];

    showAttachmentArea = false;

    isUserDataOpened = true;
    isAttachmentOpened = true;
    isTechnicalDataOpened = true;
    isExceedLimitedFilesSize = false;

    readonly noticeTimeout = 3000;

    result: SagMessageData;

    private feedbackBusinessService: FeedbackBusinessService;
    private destroy$ = new Subject();

    readonly spinnerSelector = 'connect-feedback-modal .modal-body';

    @ViewChild('uploader', { static: false }) uploader: ElementRef;
    @ViewChild('refDiv', { static: false }) refDiv: ElementRef;
    @ViewChild('feedbackMessRef', { static: false }) feedbackMessRef: ElementRef;

    constructor(
        private fb: FormBuilder,
        private bsModalRef: BsModalRef,
        private translateService: TranslateService,
        private appStorage: AppStorageService,
        private userService: UserService,
        private customerBusinessService: CustomerBusinessService,
        private salesOnBehalfBusinessService: SalesOnBehalfBusinessService,
        private salesNotOnBehalfBusinessService: SalesNotOnBehalfBusinessService,
        private articleShoppingBasketService: ArticleShoppingBasketService,
    ) { }

    @HostListener('window:keydown', ['$event'])
    onKeyPress(event: KeyboardEvent) {
        const isTextArea = event.target === this.feedbackMessRef.nativeElement;
        if (isTextArea && (event.ctrlKey || event.metaKey) && event.keyCode === 86) {
            if (!!this.refDiv && !!this.refDiv.nativeElement && this.showAttachmentArea && BrowserUtil.isIE()) {
                this.refDiv.nativeElement.focus();
            }
        }
    }

    ngOnInit() {
        this.form = this.fb.group({
            selectedTopic: [null, Validators.required],
            custContact: '',
            saleGroup: '',
            feedbackMessage: ['', Validators.required]
        });

        SpinnerService.start(this.spinnerSelector);
        this.userService.getPaymentSetting()
            .pipe(takeUntil(this.destroy$))
            .subscribe(() => {
                this.getMasterData(this.userService.userDetail);
            }, () => {
                SpinnerService.stop(this.spinnerSelector);
            });
    }

    ngOnDestroy() {
        this.destroy$.next(true);
        this.destroy$.complete();
    }

    get selectedTopicControl() {
        return this.form.get('selectedTopic') as FormControl;
    }

    get feedbackMessageControl() {
        return this.form.get('feedbackMessage') as FormControl;
    }

    get saleGroupControl() {
        return this.form.get('saleGroup') as FormControl;
    }

    get shortTechnicalDatas(): FeedbackDataItem[] {
        if (!this.technicalData || !this.technicalData.items || !this.technicalData.items.length) {
            return [];
        }
        return this.technicalData.items.filter(item => item.isShortTechnicalData);
    }

    onPaste(event) {
        // tslint:disable-next-line: no-string-literal
        const clipboardData = event.clipboardData || window['clipboardData'];
        const items = clipboardData.items || clipboardData.files || [];
        Array.from(items).forEach((item: DataTransferItem) => {
            // Skip content if not image
            if (item.type.indexOf('image') !== -1 && this.showAttachmentArea) {
                // Retrieve image on clipboard as blob
                let file: File;
                // for IE checking
                if (item instanceof File) {
                    file = item;
                } else {
                    file = item.getAsFile();
                }

                const fileName = `screenshot.${file.type.split('/')[1]}`;
                const preview: any = {
                    name: fileName,
                    date: new Date(),
                    file
                };
                preview.type = 'image';
                this.readImage(preview, file, this.attachments);
                event.preventDefault();
                event.stopPropagation();
            }
        });

        if (!!this.feedbackMessRef && !!this.feedbackMessRef.nativeElement && BrowserUtil.isIE()) {
            this.feedbackMessRef.nativeElement.focus();
            setTimeout(() => {
                this.feedbackMessRef.nativeElement.focus();
            });
        }
    }

    onSubmit() {
        this.updateControlValidity(this.selectedTopicControl);
        this.updateControlValidity(this.feedbackMessageControl);

        if (this.isExceedLimitedFilesSize) {
            return;
        }
        if (this.form.invalid) {
            return;
        }

        const spinner = SpinnerService.start(this.spinnerSelector);
        this.articleShoppingBasketService.loadMiniBasket().then(() => {
            this.technicalData = this.feedbackBusinessService.getTechnicalData();
            const value = this.form.value;
            const custContact = this.feedbackBusinessService.getCustomerContact(value.custContact);
            const source = this.feedbackBusinessService.getSource(value.saleGroup);
            const message = this.feedbackBusinessService.getFeedbackMessage(value.feedbackMessage);
            const selectedTopic = this.topics.find(item => item.value === value.selectedTopic);
            const topic = new FeedbackTopic({
                title: this.translateService.instant('FEEDBACK.FEEDBACK_THEME'),
                topicCode: selectedTopic.value,
                topic: this.translateService.instant(selectedTopic.label)
            });

            const requestModel = new FeedbackSavingRequest({
                lang: this.appStorage.appLangCode,
                topic,
                affiliateStore: this.appStorage.defaultSetting ? this.appStorage.defaultSetting.title : '',
                customerContact: custContact,
                source,
                message,
                userData: this.userData,
                technicalData: this.technicalData
            });

            const formData = new FormData();
            const rawModel = JSON.stringify(this.feedbackBusinessService.getSavingRequestModel(requestModel));
            formData.append('feedbackModel', rawModel);
            this.attachments.forEach(file => {
                formData.append('files', file.file, file.name);
            });

            this.feedbackBusinessService.createFeedback(formData, rawModel)
                .pipe(finalize(() => SpinnerService.stop(spinner)))
                .subscribe(
                    (res: any) => {
                        this.result = this.getSuccessfulMessage(this.userData);
                        setTimeout(() => {
                            this.bsModalRef.hide();
                        }, this.noticeTimeout);
                    }, () => {
                        this.result = { type: 'ERROR', message: 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY' } as SagMessageData;
                    });
        });
    }

    getSuccessfulMessage(userData): SagMessageData {
        if (!this.userService.userDetail.isSalesOnBeHalf) {
            return { type: 'SUCCESS', message: 'FEEDBACK.SUCCESSFUL_MESSAGE_WITH_EMAIL', params: { userEmail: this.userEmail } };
        }
        return { type: 'SUCCESS', message: 'FEEDBACK.SUCCESSFUL_MESSAGE' };
    }

    toggleUserData() {
        this.isUserDataOpened = !this.isUserDataOpened;
    }

    addImages() {
        this.uploader.nativeElement.click();
    }

    onChange() {
        const files: FileList = this.uploader.nativeElement.files;
        for (const file of Array.from(files)) {
            const preview: any = {
                name: file.name,
                date: new Date(),
                file
            };
            if (file && file.type.indexOf('image') !== -1) {
                preview.type = 'image';
                this.readImage(preview, file, this.attachments);
            } else if (!this.isExecutableFile(file)) {
                preview.src = '';
                preview.type = file.name.split('.').pop();
                preview.customClass = this.getCustomClass(preview.type);
                this.attachments.push(preview);
                this.trackingSize();
            }
        }
        this.uploader.nativeElement.value = '';
    }

    remove(index) {
        this.attachments.splice(index, 1);
        this.trackingSize();
    }

    showInNewTab(attachment) {
        if (!BrowserUtil.isIE() && (attachment.type === 'image' || attachment.type === 'pdf')) {
            window.open(URL.createObjectURL(attachment.file));
        } else {
            FileSaver.saveAs(attachment.file, attachment.name);
        }
    }

    close() {
        this.bsModalRef.hide();
    }

    private getMasterData(user: UserDetail) {
        this.showAttachmentArea = user.salesUser || user.isSalesOnBeHalf;
        this.feedbackBusinessService = this.getFeedbackBusinessService(user);
        this.title = this.feedbackBusinessService.getFormTitle();
        this.source = this.feedbackBusinessService.getGroup();
        this.saleGroupControl.setValue(this.source);
        this.feedbackBusinessService.getMasterData()
            .pipe(
                takeUntil(this.destroy$),
                tap((res) => {
                    this.userEmail = res.userData.userEmail;
                    this.topics = this.feedbackBusinessService.getTopicsByCode(res.topicCodes);
                    this.userData = this.feedbackBusinessService.getUserData(res.userData);
                    this.technicalData = this.feedbackBusinessService.getTechnicalData();
                }),
                finalize(() => SpinnerService.stop(this.spinnerSelector))
            )
            .subscribe();
    }

    private getFeedbackBusinessService(user: UserDetail) {
        if (user.isSalesOnBeHalf) {
            return this.salesOnBehalfBusinessService;
        }
        if (user.salesUser) {
            return this.salesNotOnBehalfBusinessService;
        }
        return this.customerBusinessService;
    }

    private readImage(obj, file, images) {
        const reader = new FileReader();
        reader.onloadend = () => {
            obj.src = reader.result;
            images.push(obj);
            this.trackingSize();
        };
        reader.readAsDataURL(file);
    }

    private trackingSize() {
        let totalSize = 0;
        this.attachments.forEach(attachment => {
            totalSize += attachment.file.size;
        });
        this.isExceedLimitedFilesSize = totalSize > LIMIT_FILE_SIZE;
    }

    private updateControlValidity(control: FormControl) {
        control.markAsDirty();
        control.updateValueAndValidity({ onlySelf: true, emitEvent: true });
    }

    private getCustomClass(type: string) {
        switch (type) {
            case 'pdf':
                return 'fa-file-pdf-o';
            case 'doc':
            case 'docx':
                return 'fa-file-word-o';
            case 'xls':
            case 'xlsx':
                return 'fa-file-excel-o';
            case 'ppt':
            case 'pptx':
                return 'fa-file-powerpoint-o';
            case 'zip':
            case 'rar':
            case '7z':
                return 'fa-file-archive-o';
        }
        return 'fa-file-text-o';
    }

    private isExecutableFile(file: File) {
        const ext = (file && file.name || '').split('.').pop().toLowerCase();
        if (!ext) {
            return false;
        }
        return EXECUTABLE_FILE_EXTENSIONS.indexOf(ext) !== -1;
    }
}
