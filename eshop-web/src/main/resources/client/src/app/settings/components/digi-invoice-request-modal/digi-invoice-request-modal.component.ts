import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Validator } from 'src/app/core/utils/validator';
import { SagMessageData } from 'sag-common';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { SubSink } from 'subsink';
import { finalize } from 'rxjs/operators';
import { DIGI_INVOICE_MODE } from '../../enums/digi-invoice/digi-invoice.enum';
import { DigiInvoiceService } from '../../services/digi-invoice.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { TOKEN_STATE } from 'src/app/core/enums/token-state.enum';
import { UserDetail } from 'src/app/core/models/user-detail.model';

@Component({
    selector: 'connect-digi-invoice-request-modal',
    templateUrl: './digi-invoice-request-modal.component.html',
    styleUrls: ['./digi-invoice-request-modal.component.scss']
})
export class DigiInvoiceRequestModalComponent implements OnInit, OnDestroy {
    @Input() userDetail: UserDetail;

    mode = DIGI_INVOICE_MODE.INPUT_EMAIL;
    isEmailValid = false;
    requestEmailForm: FormGroup;
    DIGI_INVOICE_MODE = DIGI_INVOICE_MODE;
    notifier: SagMessageData;
    private readonly spinnerSelector = '.modal-body';
    private readonly INVALID_CODE = 'FORGOT_PASSWORD.INVALID_CODE';
    private readonly TOKEN_EXPIRED = 'FORGOT_PASSWORD.TOKEN_EXPIRED';
    private subs = new SubSink();

    constructor(
        public bsModalRef: BsModalRef,
        private fb: FormBuilder,
        private digiInvoiceService: DigiInvoiceService,
        private appStorage: AppStorageService
    ) { }

    ngOnInit() {
        this.buildForm();
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    private buildForm() {
        this.requestEmailForm = this.fb.group({
            inputEmail: ['', [Validators.required, Validator.emailValidator]],
            inputCode: ['']
        });
    }

    onSendCode() {
        this.sendCode();
    }

    sendCode() {
        SpinnerService.start(this.spinnerSelector);
        const email = this.requestEmailForm.getRawValue().inputEmail;
        this.subs.sink = this.digiInvoiceService.sendCode(email).pipe(finalize(() => SpinnerService.stop(this.spinnerSelector))).subscribe(rs => {
            this.appStorage.digiInvoiceHashCode = rs;
            this.mode = DIGI_INVOICE_MODE.VERIFY_CODE;
            this.notifier = { message: 'FORGOT_PASSWORD.CODE_IN_EMAIL', type: 'SUCCESS' };
        }, () => {
            SpinnerService.stop(this.spinnerSelector);
        });
    }

    onVerifyCode() {
        this.verifyCode();
    }

    verifyCode() {
        SpinnerService.start(this.spinnerSelector);
        const code = this.requestEmailForm.getRawValue().inputCode;
        const hashCode = this.appStorage.digiInvoiceHashCode;
        if (!hashCode) {
            SpinnerService.stop(this.spinnerSelector);
            this.notifier = { message: this.INVALID_CODE, type: 'ERROR' };
            return;
        }
        this.subs.sink = this.digiInvoiceService.checkCode(code, hashCode).subscribe(rs => {
            switch (rs) {
                case TOKEN_STATE.VALID:
                    this.notifier = null;
                    this.requestToChangeEmail();
                    break;
                case TOKEN_STATE.INVALID:
                    this.notifier = { message: this.INVALID_CODE, type: 'ERROR' };
                    SpinnerService.stop(this.spinnerSelector)
                    break;
                case TOKEN_STATE.EXPIRED:
                    this.notifier = { message: this.TOKEN_EXPIRED, type: 'ERROR' };
                    SpinnerService.stop(this.spinnerSelector)
                    break;
            }
        }, () => {
            SpinnerService.stop(this.spinnerSelector);
            this.notifier = { message: this.INVALID_CODE, type: 'ERROR' };
        });
    }

    requestToChangeEmail() {
        const code = this.requestEmailForm.getRawValue().inputCode;
        const hashCode = this.appStorage.digiInvoiceHashCode;
        const invoiceRequestEmail = this.userDetail && this.userDetail.settings && this.userDetail.settings.invoiceRequestEmail;
        const invoiceRecipientEmail = this.requestEmailForm.getRawValue().inputEmail;
        this.subs.sink = this.digiInvoiceService.requestToChangeEmail(code, hashCode, invoiceRecipientEmail, invoiceRequestEmail).pipe(finalize(() => SpinnerService.stop(this.spinnerSelector))).subscribe(() => {
            this.mode = DIGI_INVOICE_MODE.SUBMITTED;
            this.notifier = { message: 'SETTINGS.SETTING.INVOICE_MODAL.SUBMITTED_SUCCESSFUL_MSG', type: 'SUCCESS' };
            this.appStorage.clearDigiInvoiceHashCode();
        }, () => {
            SpinnerService.stop(this.spinnerSelector);
        });
    }
}
