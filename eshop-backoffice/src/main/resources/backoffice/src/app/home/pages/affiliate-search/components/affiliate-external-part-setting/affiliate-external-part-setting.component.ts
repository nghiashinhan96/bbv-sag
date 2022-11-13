import { Component, ElementRef, Input, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'backoffice-affiliate-external-part-setting',
  templateUrl: './affiliate-external-part-setting.component.html',
  styleUrls: ['./affiliate-external-part-setting.component.scss']
})
export class AffiliateExternalPartSettingComponent implements OnInit {
  @Input('form') public externalPartSettingForm: FormGroup;
  @Input() title: string = '';
  @Input() submitted = false;

  @ViewChildren('headerNameInput') headerNamesInput: QueryList<ElementRef>;
  @ViewChildren('orderTextInput') orderTextsInput: QueryList<ElementRef>;

  isFormExpanded = true;
  currentEditorLangHeaderNames: string;
  currentEditorLangOrderTexts: string;
  
  constructor() { }

  ngOnInit() {
    this.initData();
  }

  initAutoFocus(langiso, index, fieldName) {
    switch(fieldName) {
      case 'header-names':
        this.currentEditorLangHeaderNames = langiso;
        setTimeout(() => {
        this.setAutoFocus(this.headerNamesInput, index);
      });
        break;
      case 'order-texts':
        this.currentEditorLangOrderTexts = langiso;
        setTimeout(() => {
        this.setAutoFocus(this.orderTextsInput, index);
      });
        break;
    }
  }

  private setAutoFocus(input, index = 0) {
    const el = input && input.find((_, i) => i === index);
    if (el && el.nativeElement) {
      el.nativeElement.focus();
    }
  }

  private initData() {
    const settings = this.externalPartSettingForm && this.externalPartSettingForm.getRawValue() || {};
    this.currentEditorLangHeaderNames = this.getCurrentEditorLang(settings.headerNames);
    this.currentEditorLangOrderTexts = this.getCurrentEditorLang(settings.orderTexts);
  }

  private getCurrentEditorLang(obj) {
    return obj && Object.keys(obj).length && Object.keys(obj).length > 0 && Object.keys(obj).sort()[0] || '';
  }

  get isRequiredHeaderNames() {
    if (this.externalPartSettingForm) {
      const headerNamesCtrls = (this.externalPartSettingForm.get('headerNames') as FormGroup).controls;
      const ctrls = Object.keys(headerNamesCtrls).filter(ctrl => headerNamesCtrls[ctrl].getError('required'));
      if (ctrls.length > 0) {
        return true;
      }
    }

    return false;
  }

  get isRequiredOrderTexts() {
    if (this.externalPartSettingForm) {
        const orderTextsCtrls = (this.externalPartSettingForm.get('orderTexts') as FormGroup).controls;
        const ctrls = Object.keys(orderTextsCtrls).filter(ctrl => orderTextsCtrls[ctrl].getError('required'));
        if (ctrls.length > 0) {
            return true;
        }
    }
    return false;
  }

  get headerNamesForm() {
    return this.externalPartSettingForm ? (this.externalPartSettingForm.get('headerNames') as FormGroup) : null;
  }

  get orderTextsForm() {
    return this.externalPartSettingForm ? (this.externalPartSettingForm.get('orderTexts') as FormGroup) : null;
  }
}
