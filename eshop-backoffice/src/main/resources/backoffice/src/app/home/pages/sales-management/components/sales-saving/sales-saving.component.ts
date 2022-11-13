import { OnInit, Component, Output, Input, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormControl, FormGroup } from '@angular/forms';
import { SalesSavingItemModel } from '../../model/sales-saving-item.model';
import { Router, NavigationStart } from '@angular/router';
import { SalesManagementConstant } from '../../sales-management-constant';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { BOValidator } from 'src/app/core/utils/validator';
import { TranslateService } from '@ngx-translate/core';
import { GENDER } from 'src/app/core/enums/enums';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'backoffice-sales-saving',
    templateUrl: './sales-saving.component.html',
    styleUrls: ['./sales-saving.component.scss']
})
export class SalesSavingComponent implements OnInit {

    public form: FormGroup;
    public genders: Array<any>;
    public attemptedSubmit: boolean;
    public notifier: NotificationModel;
    private isUserWannaLeave: boolean;
    public duplicatedEmailMesg: string;
    public redirectUrl: string;
    public confirmModalRef: BsModalRef;

    @Input() pageTitle: string;
    @Input() model: SalesSavingItemModel;
    @Output() saveEvent: EventEmitter<SalesSavingItemModel> = new EventEmitter();

    @ViewChild('confirmModal', { static: true }) confirmModal: ElementRef;

    constructor(
        private formBuilder: FormBuilder,
        private translateService: TranslateService,
        private router: Router,
        private modalService: BsModalService) {
    }
    ngOnInit(): void {
        this.genders = this.getGenderOptions();
        this.form = this.buildForm(this.model);
        this.router.events.subscribe(event => {
            if (event instanceof NavigationStart) {
                this.redirectUrl = event.url;
            }
        });
    }

    save(form: FormGroup) {
        this.attemptedSubmit = true;
        if (!form.valid) {
            return;
        }
        this.saveEvent.emit(this.getLastestModel());
    }

    reset() {
        this.form = this.buildForm(this.model);
        this.notifier = null;
        this.attemptedSubmit = false;
    }

    back() {
        this.router.navigateByUrl(SalesManagementConstant.URL_SALES_LIST_PAGE);
    }

    getLastestModel(): SalesSavingItemModel {
        return new SalesSavingItemModel({
            firstName: this.form.controls.firstName.value,
            lastName: this.form.controls.lastName.value,
            primaryContactEmail: this.form.controls.email.value,
            personalNumber: this.form.controls.personalNumber.value,
            gender: this.form.controls.gender.value.value,
            legalEntityId: this.form.controls.legalEntityId.value
        });
    }

    closeModalAndSaving() {
        this.closeConfirmModal();
        this.isUserWannaLeave = true;
        this.save(this.form);
    }

    closeModalAndRedirect() {
        this.closeConfirmModal();
        this.model = this.getLastestModel();
        this.router.navigateByUrl(this.redirectUrl);
    }

    handleSavingSuccessfullyCase() {
        this.notifier = { messages: ['COMMON.MESSAGE.SAVE_SUCCESSFULLY'], status: true };
        this.duplicatedEmailMesg = null;
        this.model = this.getLastestModel();
        setTimeout(() => {
            if (this.isUserWannaLeave) {
                this.router.navigateByUrl(this.redirectUrl);
            } else {
                this.router.navigateByUrl(SalesManagementConstant.URL_SALES_LIST_PAGE);
            }
        }, 1000);
    }

    handleSavingFailedCase(err) {
        if (err.error_code === 'DUPLICATED_EMAIL') {
            this.duplicatedEmailMesg = this.translateService.instant('SALES_MANAGEMENT.MESSAGE.DUPLICATED_EMAIL');
        } else {
            this.notifier = { messages: ['COMMON.MESSAGE.SAVE_UNSUCCESSFULLY'], status: false };
        }
    }

    handleDataChanged() {
        if (this.isDataChanged() && (!this.attemptedSubmit || (this.attemptedSubmit && this.form.valid))) {
            this.showConfirmModal();
            return false;
        }
        return true;
    }

    buildForm(model: SalesSavingItemModel): FormGroup {
        const genderFormValue = this.genders.find(gender => gender.value === model.gender);
        return this.formBuilder.group({
            firstName: [model.firstName, Validators.required],
            lastName: [model.lastName, Validators.required],
            email: [model.primaryContactEmail, [Validators.required, BOValidator.emailValidator]],
            personalNumber: [model.personalNumber, Validators.required],
            gender: [genderFormValue],
            legalEntityId: [model.legalEntityId]
        });
    }

    getGenderOptions() {
        const gender = new Array();
        gender.push({ value: GENDER.MALE, label: this.translateService.instant('COMMON.LABEL.GENDER.MALE') });
        gender.push({ value: GENDER.FEMALE, label: this.translateService.instant('COMMON.LABEL.GENDER.FEMALE') });
        return gender;
    }

    showConfirmModal() {
        this.confirmModalRef = this.modalService.show(this.confirmModal, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    closeConfirmModal() {
        this.confirmModalRef.hide();
    }

    isDataChanged(): boolean {
        return !this.model.equals(this.getLastestModel());
    }
}
