import {
    Component,
    Input,
    Output,
    EventEmitter,
    ViewChild,
    ElementRef,
    OnChanges,
    SimpleChanges,
    OnInit,
} from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';

import { IMyDateModel } from 'angular-mydatepicker';

import { LicenceService } from '../../services/licence.service';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: "licence",
    templateUrl: './licence.component.html',
    providers: [LicenceService],
})
export class LicenceComponent implements OnChanges, OnInit {
    @ViewChild('closeBtn', { static: true }) closeBtn: ElementRef;

    @Input() isNewModeInput: boolean;
    @Input() licenceModelInput: any;

    @Output() saveEvent = new EventEmitter();
    @Output() closeModal = new EventEmitter();

    public licenceTypes;
    public selectedLicenceId;
    public selectedLicenceType;
    public fromDate;
    public toDate;
    public quantity: number;
    public isNewMode;
    public customerNr;
    public modalId;

    public errMessage;
    public licenceForm;
    public datePickerFromSetting;
    public datePickerToSetting;

    locale = '';

    constructor(
        private formBuilder: FormBuilder,
        private service: LicenceService,
        private translateService: TranslateService
    ) {
        this.locale = this.translateService.currentLang;
    }

    ngOnInit(): void {
        this.buildLicenceForm();
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.initValue();
        if (this.licenceForm) {
            this.resetFormValue(
                this.selectedLicenceType.value,
                this.fromDate,
                this.toDate,
                this.quantity
            );
        }
        this.initDateValidityConstraints();
    }
    onFromDateChanged(event: IMyDateModel) {
        this.datePickerToSetting = this.service.changeMaxDate(
            this.datePickerToSetting,
            event.singleDate.date
        );
    }

    onToDateChanged(event: IMyDateModel) {
        this.datePickerFromSetting = this.service.changeMinDate(
            this.datePickerFromSetting,
            event.singleDate.date
        );
    }

    selectLicenceType(event) {
        const quantity = this.service.findQuantityLicenceByValue(
            this.licenceTypes,
            event.value
        );
        if (quantity) {
            this.licenceForm.controls.quantity.setValue(quantity);
        }
    }

    createCustomerLicense() {
        if (!this.licenceForm.valid) {
            return;
        }

        const creatingLicence = (this.licenceModelInput.licenceTypes.filter(
            (x) => x.id.toString() === this.licenceForm.controls.licenceType.value.value
        ) || [])[0];

        if (creatingLicence) {
            const data = Object.assign({
                typeOfLicense: creatingLicence.typeOfLicense,
                packName: creatingLicence.packName,
                customerNr: this.customerNr,
                beginDate: DatePickerUtil.getDateFromToDatePicker(
                    this.licenceForm.controls.fromDate.value.singleDate.date
                ),
                endDate: DatePickerUtil.getDateFromToDatePicker(
                    this.licenceForm.controls.toDate.value.singleDate.date
                ),
                quantity: this.licenceForm.controls.quantity.value,
            });
            this.service.createCustomerLicense(data).subscribe(
                (res) => {
                    this.closeThisModal();
                    this.saveEvent.emit();
                },
                (err) => {
                    this.errMessage = err;
                }
            );
        }
    }

    updateCustomerLicense() {
        if (!this.licenceForm.valid) {
            return;
        }
        const editingLicence = (this.licenceModelInput.licenceTypes.filter(
            x => x.id.toString() === this.licenceForm.controls.licenceType.value.value
        ) || [])[0];
        const data = {
            packName: editingLicence.packName,
            beginDate: DatePickerUtil.getDateFromToDatePicker(
                this.licenceForm.controls.fromDate.value.singleDate.date
            ),
            endDate: DatePickerUtil.getDateFromToDatePicker(
                this.licenceForm.controls.toDate.value.singleDate.date
            ),
            quantity: this.licenceForm.controls.quantity.value,
        };
        this.service.updateLicense(data, this.selectedLicenceId).subscribe(
            (res) => {
                this.closeThisModal();
                this.saveEvent.emit();
            },
            (err) => {
                this.errMessage = err;
            }
        );
    }

    closeThisModal() {
        this.closeModal.emit();
    }

    private initValue() {
        this.initValueForForm();
        this.isNewMode = this.isNewModeInput;
        this.modalId = this.licenceModelInput.modalId;
        if (this.licenceModelInput.selectedLicenceId) {
            this.selectedLicenceId = this.licenceModelInput.selectedLicenceId;
        }
        this.customerNr = this.licenceModelInput.customerNr;
        this.datePickerFromSetting = {};
        this.datePickerToSetting = {};
        this.errMessage = null;
    }

    private initValueForForm() {
        this.initLicenceTypes();
        this.initSelectedLicenceType();
        this.fromDate = this.buildDataDatePicker(this.licenceModelInput.fromDate);
        this.toDate = this.buildDataDatePicker(this.licenceModelInput.toDate);
        this.quantity = this.licenceModelInput.quantity;
    }

    private buildDataDatePicker({ date }) {
        return {
            isRange: false,
            singleDate: {
                date:
                {
                    year: date.year,
                    month: date.month,
                    day: date.day
                }
            }
        } as IMyDateModel;
    }

    private initLicenceTypes() {
        const arr = this.licenceModelInput.licenceTypes.map((item) => {
            return {
                value: item.id.toString(),
                label: item.packName,
                quantity: item.quantity,
            };
        });
        this.licenceTypes = Object.assign([], arr);
    }

    private initSelectedLicenceType() {
        if (this.licenceModelInput.selectedType) {
            const type = {
                value: this.licenceModelInput.selectedType.id.toString(),
                label: this.licenceModelInput.selectedType.packName,
                quantity: this.licenceModelInput.selectedType.quantity,
            };
            this.selectedLicenceType = type;
        } else {
            this.selectedLicenceType = Object.assign({}, this.licenceTypes[0]);
        }
    }

    private buildLicenceForm() {
        this.licenceForm = this.service.buildLicenceForm(
            this.selectedLicenceType,
            this.fromDate,
            this.toDate,
            this.quantity
        );
    }

    private initDateValidityConstraints() {
        this.datePickerFromSetting = this.service.initFromDateValidityConstraints(
            this.toDate
        );
        this.datePickerToSetting = this.service.initToDateValidityConstraints(
            this.fromDate
        );
    }

    private resetFormValue(licenceType, fromDate, toDate, quantity) {
        this.licenceForm.controls.licenceType.setValue(licenceType);
        this.licenceForm.controls.fromDate.setValue(fromDate);
        this.licenceForm.controls.toDate.setValue(toDate);
        this.licenceForm.controls.quantity.setValue(quantity);
    }
}
