import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { BsModalRef, ModalModule } from 'ngx-bootstrap/modal';
import { ConnectCommonModule } from 'src/app/shared/connect-common/connect-common.module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DigiInvoiceRequestModalComponent } from './digi-invoice-request-modal.component';
import { DigiInvoiceService } from '../../services/digi-invoice.service';
import { of } from 'rxjs';
import { DIGI_INVOICE_MODE } from '../../enums/digi-invoice/digi-invoice.enum';
import { TOKEN_STATE } from 'src/app/core/enums/token-state.enum';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { LocalStorageService } from 'ngx-webstorage';

describe('DigiInvoiceRequestModalComponent', () => {
    let component: DigiInvoiceRequestModalComponent;
    let fixture: ComponentFixture<DigiInvoiceRequestModalComponent>;
    let spy: any;
    let bsModalRefSpy = jasmine.createSpyObj('BsModalRef', ['hide']);
    let digiInvoiceService: DigiInvoiceService;
    let appStorageService: AppStorageService;
    let localStorageService = jasmine.createSpyObj('LocalStorageService', ['']);
    const hashCode = 'testing hash code';

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [DigiInvoiceRequestModalComponent],
            imports: [
                ModalModule.forRoot(),
                TranslateModule.forRoot(),
                ConnectCommonModule,
                ReactiveFormsModule,
                HttpClientTestingModule
            ],
            providers: [
                {
                    provide: BsModalRef,
                    useValue: bsModalRefSpy
                },
                {
                    provide: LocalStorageService,
                    useValue: localStorageService
                },
                FormBuilder,
                DigiInvoiceService,
                AppStorageService
            ]
        })
            .compileComponents();
            digiInvoiceService = TestBed.get(DigiInvoiceService);
            appStorageService = TestBed.get(AppStorageService);
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(DigiInvoiceRequestModalComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should init data', () => {
        expect(component.requestEmailForm).toBeDefined();
    });

    it('should disable send code button', () => {
        const compiled: HTMLElement = fixture.debugElement.nativeElement;
        const btnElement = compiled.querySelector("button[data-automation='invoice-request-send-code-btn'][disabled]");
        expect(btnElement).toBeTruthy();
    });

    it('should enable send code button', () => {
        component.requestEmailForm.patchValue({inputEmail: 'testing_email@gmail.com'})
        fixture.detectChanges();
        const compiled: HTMLElement = fixture.debugElement.nativeElement;
        const btnElement = compiled.querySelector("button[data-automation='invoice-request-send-code-btn'][disabled]");
        expect(btnElement).toBeFalsy();
    });

    it('should call onSendCode()', fakeAsync(() => {
        spy = spyOn(component, 'onSendCode');
        component.requestEmailForm.patchValue({inputEmail: 'testing_email@gmail.com'})
        fixture.detectChanges();
        const compiled: HTMLElement = fixture.debugElement.nativeElement;
        const btnElement: any = compiled.querySelector("button[data-automation='invoice-request-send-code-btn']");
        btnElement.click();
        tick();
        expect(component.onSendCode).toHaveBeenCalled();
    }));

    it('sendCode() should work properly', fakeAsync(() => {
        spyOnProperty(appStorageService,'digiInvoiceHashCode', 'set');
        spy = spyOn(component, 'sendCode').and.callThrough();
        spy = spyOn(digiInvoiceService, 'sendCode').and.returnValue(of(hashCode));
        component.sendCode();
        tick();
        expect(digiInvoiceService.sendCode).toHaveBeenCalled();
        expect(component.mode).toBe(DIGI_INVOICE_MODE.VERIFY_CODE);
        expect(component.notifier).toBeDefined();
        expect(component.notifier.message).toBe('FORGOT_PASSWORD.CODE_IN_EMAIL');
    }));

    it('should call onVerifyCode()', fakeAsync(() => {
        spy = spyOn(component, 'onVerifyCode');
        component.mode = DIGI_INVOICE_MODE.VERIFY_CODE;
        fixture.detectChanges();
        const compiled: HTMLElement = fixture.debugElement.nativeElement;
        const btnElement: any = compiled.querySelector("button[data-automation='invoice-request-verify-code-btn']");
        btnElement.click();
        tick();
        expect(component.onVerifyCode).toHaveBeenCalled();
    }));

    it('verifyCode() should work properly in case of hash code missing ', fakeAsync(() => {
        spyOnProperty(appStorageService,'digiInvoiceHashCode', 'get').and.returnValue(null);
        const responseSate = TOKEN_STATE.VALID;
        spy = spyOn(component, 'verifyCode').and.callThrough();
        spy = spyOn(component, 'requestToChangeEmail');
        spy = spyOn(digiInvoiceService, 'checkCode').and.returnValue(of(responseSate));
        component.verifyCode();
        tick();
        expect(digiInvoiceService.checkCode).not.toHaveBeenCalled();
        expect(component.requestToChangeEmail).not.toHaveBeenCalled();
        expect(component.notifier.message).toBe(component['INVALID_CODE']);
    }));

    it('verifyCode() should work properly in case of response state is valid', fakeAsync(() => {
        spyOnProperty(appStorageService,'digiInvoiceHashCode', 'get').and.returnValue(hashCode);
        const responseSate = TOKEN_STATE.VALID;
        spy = spyOn(component, 'verifyCode').and.callThrough();
        spy = spyOn(component, 'requestToChangeEmail');
        spy = spyOn(digiInvoiceService, 'checkCode').and.returnValue(of(responseSate));
        component.verifyCode();
        tick();
        expect(digiInvoiceService.checkCode).toHaveBeenCalled();
        expect(component.requestToChangeEmail).toHaveBeenCalled();
    }));

    it('verifyCode() should work properly in case of response state is invalid', fakeAsync(() => {
        spyOnProperty(appStorageService,'digiInvoiceHashCode', 'get').and.returnValue(hashCode);
        const responseSate = TOKEN_STATE.INVALID;
        spy = spyOn(component, 'verifyCode').and.callThrough();
        spy = spyOn(component, 'requestToChangeEmail');
        spy = spyOn(digiInvoiceService, 'checkCode').and.returnValue(of(responseSate));
        component.verifyCode();
        tick();
        expect(digiInvoiceService.checkCode).toHaveBeenCalled();
        expect(component.requestToChangeEmail).not.toHaveBeenCalled();
        expect(component.notifier.message).toBe(component['INVALID_CODE']);
    }));

    it('verifyCode() should work properly in case of response state is expired', fakeAsync(() => {
        spyOnProperty(appStorageService,'digiInvoiceHashCode', 'get').and.returnValue(hashCode);
        const responseSate = TOKEN_STATE.EXPIRED;
        spy = spyOn(component, 'verifyCode').and.callThrough();
        spy = spyOn(component, 'requestToChangeEmail');
        spy = spyOn(digiInvoiceService, 'checkCode').and.returnValue(of(responseSate));
        component.verifyCode();
        tick();
        expect(digiInvoiceService.checkCode).toHaveBeenCalled();
        expect(component.requestToChangeEmail).not.toHaveBeenCalled();
        expect(component.notifier.message).toBe(component['TOKEN_EXPIRED']);
    }));

    it('requestToChangeEmail() should work properly', fakeAsync(() => {
        spyOnProperty(appStorageService,'digiInvoiceHashCode', 'get').and.returnValue(hashCode);
        spy = spyOn(appStorageService,'clearDigiInvoiceHashCode');
        spy = spyOn(component, 'requestToChangeEmail').and.callThrough();
        spy = spyOn(digiInvoiceService, 'requestToChangeEmail').and.returnValue(of(null));
        component.requestToChangeEmail();
        tick();
        expect(digiInvoiceService.requestToChangeEmail).toHaveBeenCalled();
        expect(appStorageService.clearDigiInvoiceHashCode).toHaveBeenCalled();
        expect(component.notifier.message).toBe('SETTINGS.SETTING.INVOICE_MODAL.SUBMITTED_SUCCESSFUL_MSG');
        expect(component.mode).toBe(DIGI_INVOICE_MODE.SUBMITTED);
    }));

    afterAll(() => {
        TestBed.resetTestingModule();
    });
});
