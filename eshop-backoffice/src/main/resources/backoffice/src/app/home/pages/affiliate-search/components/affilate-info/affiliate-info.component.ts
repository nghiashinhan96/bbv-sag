import {
    Component,
    OnInit,
    Input,
    ElementRef
} from '@angular/core';

import {
    FormGroup,
    FormBuilder,
    FormControl,
    Validators,
} from '@angular/forms';

import { VatTypeDisplayUtil, BrandPriorityAvailabilityUtil, SagEditorLanguageModel, SAG_AVAIL_DISPLAY_STATES, SagAvailDisplaySettingModel, ExternalPartSettings, SAG_AVAIL_DISPLAY_OPTIONS } from 'sag-common';
import { Subscription } from 'rxjs';
import { OnDestroy } from '@angular/core';
import AffUtils from 'src/app/core/utils/aff-utils';
import { AffiliateModel } from 'src/app/home/models/affiliate/affiliate.model';
import { BOValidator } from 'src/app/core/utils/validator';

@Component({
    selector: 'backoffice-affiliate-info',
    templateUrl: './affiliate-info.component.html',
    styleUrls: ['./affiliate-info.component.scss']
})
export class AffiliateInfoComponent implements OnInit, OnDestroy {
    @Input() affiliateSetting: AffiliateModel;
    @Input() languages: SagEditorLanguageModel[] = [];

    public isFormExpanded = true;
    public model: FormGroup;
    submitted = false;

    // detailAvailText: Array<SagEditorLanguageModel> = [];
    // listAvailText: Array<SagEditorLanguageModel> = [];

    // currentEditorLangDetail: string;
    // currentEditorLangList: string;
    vatTypeDisplayShow = AffUtils.isAT() ||  AffUtils.isCH() ||  AffUtils.isCZAX10();
    aceTextInput;


    subs = new Subscription();
    isSb = AffUtils.isSB();

    constructor (
        private formBuilder: FormBuilder,
        private el: ElementRef
    ) { }

    ngOnInit(): void {
        this.model = this.buildForm(this.affiliateSetting);
        setTimeout(() => {
            this.aceTextInput = this.el.nativeElement.querySelectorAll('.ace_text-input');
            this.setAutoFocus(0);
        });

    }

    ngOnDestroy() {
      this.subs.unsubscribe();
    }

    private setAutoFocus(index = 0) {
        if (this.aceTextInput && this.aceTextInput[index]) {
          (this.aceTextInput[index] as any).focus();
        }
    }

    public getSetting() {
        this.submitted = true;
        if (this.model.valid) {
            return this.model;
        }
        return null;
    }

    private buildForm(model: any) {
        const vat = VatTypeDisplayUtil.convertResponseData(model.vatTypeDisplay);
        const cs4 = BrandPriorityAvailabilityUtil.convertResponseData(model.customerBrandPriorityAvailFilter);
        const c4s = BrandPriorityAvailabilityUtil.convertResponseData(model.c4sBrandPriorityAvailFilter);

        const externalPartSettingsForm = this.buildExternalPartSettingsForm(this.affiliateSetting.externalPartSettings);
        const availDisplaySettingsForm = this.buildAvailDisplaySettingsForm(this.affiliateSetting.availDisplaySettings || []);

        return this.formBuilder.group({
            name: new FormControl(model.name),
            shortName: new FormControl(model.shortName),
            orgCode: new FormControl(model.orgCode),
            description: new FormControl(model.description),
            customerAbsEnabled: new FormControl(model.customerAbsEnabled),
            salesAbsEnabled: new FormControl(model.salesAbsEnabled),
            ksoEnabled: new FormControl(model.ksoEnabled),
            vat: new FormControl(model.vat, [
                Validators.required,
                BOValidator.validateForVatNumber,
            ]),
            vtList: new FormControl(vat && vat.list || false),
            vtDetail: new FormControl(vat && vat.detail || false),
            cs4P1: new FormControl(cs4.p1),
            cs4P2: new FormControl(cs4.p2),
            cs4P3: new FormControl(cs4.p3),
            c4sP1: new FormControl(c4s.p1),
            c4sP2: new FormControl(c4s.p2),
            c4sP3: new FormControl(c4s.p3),
            disabledBrandPriorityAvailability: new FormControl(model.disabledBrandPriorityAvailability),
            customerBrandFilterEnabled: new FormControl(model.customerBrandFilterEnabled),
            salesBrandFilterEnabled: new FormControl(model.salesBrandFilterEnabled),
            availDisplaySettings: this.formBuilder.array([...availDisplaySettingsForm]),
            invoiceRequestAllowed: [model.invoiceRequestAllowed || false],
            invoiceRequestEmail: [model.invoiceRequestEmail || "", [BOValidator.emailValidator]],
            externalPartSettings: externalPartSettingsForm
        },
        {
            validator:  BOValidator.requiredFieldWithConditionsValidator('invoiceRequestEmail', (formGroup: FormGroup) => {
                return formGroup.controls.invoiceRequestAllowed.value;
            })
        }
        );
    }

    private getDetailAvailForm(detailAvailText: SagEditorLanguageModel[]) {
        const contentDetail = [];
        detailAvailText = detailAvailText && detailAvailText.length > 0 ? detailAvailText : this.languages;
        detailAvailText.map(lang => {
            contentDetail[lang.langIso + '_Detail'] = [lang.content, [Validators.required, Validators.maxLength(255), BOValidator.noWhitespaceValidator]];
        });

        return contentDetail;
    }

    private getListAvailForm(listAvailText: SagEditorLanguageModel[]) {
        const contentList = [];
        listAvailText = listAvailText && listAvailText.length > 0 ? listAvailText : this.languages;
        listAvailText.map(lang => {
            let control = [];

            control = [lang.content, [Validators.required, Validators.maxLength(255), BOValidator.noWhitespaceValidator]];

            contentList[lang.langIso + '_List'] = control;
        });

        return contentList;
    }

    initAvailDisplaySettings(data: SagAvailDisplaySettingModel) {
        const contentList = this.getListAvailForm(data.listAvailText);
        const contentDetail = this.getDetailAvailForm(data.detailAvailText);
        const displayOption = this.getDisplayOption(data);
        const formGroup = this.formBuilder.group({
          color: data.color || '',
          displayOption: displayOption,
          listAvail: this.formBuilder.group({
              ...contentList
          }),
          detailAvail: this.formBuilder.group({
              ...contentDetail
          }),
          title: data.title,
          availState: data.availState,
          confirmColor: data.confirmColor || ''
        });
        if (data.availState !== SAG_AVAIL_DISPLAY_STATES.NOT_AVAILABLE) {
          formGroup.removeControl('detailAvail');
          formGroup.removeControl('confirmColor');
        }
        return formGroup;
    }

    buildAvailDisplaySettingsForm(data: SagAvailDisplaySettingModel[]) {
        const availDisplaySettingsForm: FormGroup[] = [];
        data.forEach(element => {
            const availForm = this.initAvailDisplaySettings(element);
            availDisplaySettingsForm.push(availForm);
        });
        return availDisplaySettingsForm;
    }

    buildExternalPartSettingsForm(data: ExternalPartSettings) {
        const headerNames = this.getMultilinguaForm(data.headerNames, data.useExternalParts);
        const orderTexts = this.getMultilinguaForm(data.orderTexts, data.useExternalParts);
        const form = this.formBuilder.group({
            useExternalParts: data.useExternalParts,
            showInReferenceGroup: [{value: data.showInReferenceGroup, disabled: !data.useExternalParts}],
            headerNames: this.formBuilder.group({
                ...headerNames
            }),
            orderTexts: this.formBuilder.group({
                ...orderTexts
            })
        });

        form.get('useExternalParts').valueChanges.subscribe(value => {
            if (value) {
                form.get('showInReferenceGroup').enable();
                this.setValidatorForMultilinguaControls(form, 'headerNames', [Validators.required]);
                this.setValidatorForMultilinguaControls(form, 'orderTexts', [Validators.required]);
            } else {
                form.get('showInReferenceGroup').disable();
                this.setValidatorForMultilinguaControls(form, 'headerNames');
                this.setValidatorForMultilinguaControls(form, 'orderTexts');
            }
        });

        return form;
    }

    private getMultilinguaForm(data: SagEditorLanguageModel[], isRequired: boolean) {
        const contentList = [];
        data = data && data.length > 0 ? data : this.languages;
        data.map(lang => {
            let control = [];

            if (isRequired) {
                control = [lang.content, [Validators.required]];
            } else {
                control = [lang.content];
            }

            contentList[lang.langIso] = control;
        });

        return contentList;
    }

    getTitle(data) {
        let title = '';
        switch (data.availState) {
            case SAG_AVAIL_DISPLAY_STATES.SAME_DAY:
                title = 'AVAIL_SETTINGS.AVAILABILITY_DISPLAY_SAME_DAY';
                break;
            case SAG_AVAIL_DISPLAY_STATES.NEXT_DAY:
                title = 'AVAIL_SETTINGS.AVAILABILITY_DISPLAY_NEXT_DAY';
                break;
            case SAG_AVAIL_DISPLAY_STATES.PARTIALLY_AVAILABLE:
                title = 'AVAIL_SETTINGS.AVAILABILITY_DISPLAY_PARTIALLY';
                break;
            case SAG_AVAIL_DISPLAY_STATES.NOT_AVAILABLE:
                title = 'AVAIL_SETTINGS.AVAILABILITY_DISPLAY_NOT_AVAILABLE';
                break;
            case SAG_AVAIL_DISPLAY_STATES.NOT_ORDERABLE:
                title = 'AVAIL_SETTINGS.AVAILABILITY_DISPLAY_NOT_ORDERABLE';
                break;
            case SAG_AVAIL_DISPLAY_STATES.IN_STOCK:
                title = 'AVAIL_SETTINGS.AVAILABILITY_DISPLAY_IN_STOCK';
                break;
            default:
                break;
        }
        return title;
    }

    getIsShowExceptionalCasesHintText(data) {
        return data.availState === SAG_AVAIL_DISPLAY_STATES.SAME_DAY;
    }

    getDisplayOption(data: SagAvailDisplaySettingModel) {
        let displayOption = null;
        if (data.availState === SAG_AVAIL_DISPLAY_STATES.NOT_AVAILABLE) {
            displayOption = data.displayOption;
        } else {
            displayOption = data.displayOption === SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT ? true : false;
        }
        return displayOption;
    }

    private setValidatorForMultilinguaControls(form: FormGroup, childFormGroupName: string, value?) {
        const childFormGroup = form.get(childFormGroupName) as FormGroup
        const controls = childFormGroup.controls;
        if (value) {
            Object.keys(controls).forEach(key => {
                childFormGroup.controls[key].setValidators(value);
                childFormGroup.controls[key].updateValueAndValidity();
            });
        } else {
            Object.keys(controls).forEach(key => {
                childFormGroup.controls[key].clearValidators();
                childFormGroup.controls[key].updateValueAndValidity();
            });
        }
    }
}
