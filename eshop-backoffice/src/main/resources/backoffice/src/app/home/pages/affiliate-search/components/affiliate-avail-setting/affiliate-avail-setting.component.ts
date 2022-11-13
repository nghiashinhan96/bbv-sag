import { AfterViewInit, Component, ElementRef, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { SAG_AVAIL_DISPLAY_STATES, SAG_AVAIL_DISPLAY_OPTIONS } from 'sag-common';
import AffUtils from 'src/app/core/utils/aff-utils';
import { ORDER_CONFIRMATION_TEXT_PREVIEW } from '../../enums/affiliate-avail-setting.enum';

@Component({
  selector: 'backoffice-affiliate-avail-setting',
  templateUrl: './affiliate-avail-setting.component.html',
  styleUrls: ['./affiliate-avail-setting.component.scss']
})
export class AffiliateAvailSettingComponent implements OnInit, AfterViewInit {
  @Input('group') public availSettingForm: FormGroup;
  @Input() isShowExceptionalCasesHintText: boolean = false;
  @Input() title: string = '';
  @Input() submitted = false;

  color: string = "";
  isFormExpanded = true;
  currentEditorLangList: string;
  aceTextInput;
  markdownOptions = {
    hideIcons: ['FullScreen']
  };
  availState = '';
  SAG_AVAIL_DISPLAY_STATES = SAG_AVAIL_DISPLAY_STATES;
  SAG_AVAIL_DISPLAY_OPTIONS = SAG_AVAIL_DISPLAY_OPTIONS;
  // Not-available variables section
  currentEditorLangDetail: string;
  orderConfirmationTextcolor: string = "";
  orderConfirmationTextPreviewForCH_AT = ORDER_CONFIRMATION_TEXT_PREVIEW.ORDER_CONFIRMATION_CH_AT;
  orderConfirmationTextPreviewForCZ = ORDER_CONFIRMATION_TEXT_PREVIEW.ORDER_CONFIRMATION_CZ;
  
  constructor(
    private el: ElementRef
  ) { }

  ngOnInit() {
    this.initData();
  }

  ngAfterViewInit() {
    this.aceTextInput = this.el.nativeElement.querySelectorAll('.ace_text-input');
    this.setColorForPreviewPanel();
  }

  private setColorForPreviewPanel() {
    const previewPanelElement: HTMLElement = this.el.nativeElement.querySelector(`#listAvail-language-${this.currentEditorLangList}_List .preview-panel`);
    if (previewPanelElement) {
      previewPanelElement.style.color = this.color;
    }
    const dropShipmentPreviewElement: HTMLElement = this.el.nativeElement.querySelector(`.drop-shipment-text-preview`);
    if (dropShipmentPreviewElement) {
      dropShipmentPreviewElement.style.color = this.color;
    }
    const dotElement: HTMLElement = this.el.nativeElement.querySelector(`.dot`);
    if (dotElement) {
      dotElement.style.color = this.color;
    }
    const orderConfirmationTextPreviewEl: HTMLElement = this.el.nativeElement.querySelector(`.order-confirmation-text-preview-box`);
    if (orderConfirmationTextPreviewEl) {
      orderConfirmationTextPreviewEl.style.backgroundColor = this.color;
      orderConfirmationTextPreviewEl.style.color = this.orderConfirmationTextcolor;
    }
  }

  updateColor(color) {
    this.color = color;
    this.availSettingForm.patchValue({color});
    this.setColorForPreviewPanel();
  }

  updateOrderConfirmationTextColor(orderConfirmationTextcolor) {
    this.orderConfirmationTextcolor = orderConfirmationTextcolor;
    this.availSettingForm.patchValue({confirmColor: orderConfirmationTextcolor});
    this.setColorForPreviewPanel();
  }

  initAutoFocus(langiso, index, isDetail: boolean) {
    let indexApply = 0;
    if (isDetail) {
      this.currentEditorLangDetail = langiso;
      indexApply = index;
    } else {
      this.currentEditorLangList = langiso;
      indexApply = index + (Object.keys(this.listAvailForm.controls).length || 0);
      if (this.availState !== SAG_AVAIL_DISPLAY_STATES.NOT_AVAILABLE) {
        indexApply = index;
      }
    }
    setTimeout(() => {
      this.setAutoFocus(indexApply);
    });
  }

  private setAutoFocus(index = 0) {
    if (this.aceTextInput && this.aceTextInput[index]) {
      (this.aceTextInput[index] as any).focus();
      this.setColorForPreviewPanel();
    }
  }

  private initData() {
    const availSetting = this.availSettingForm && this.availSettingForm.getRawValue() || {};
    this.currentEditorLangList = this.getCurrentEditorLang(availSetting.listAvail);
    this.currentEditorLangDetail = this.getCurrentEditorLang(availSetting.detailAvail);
    this.color = availSetting.color || this.color;
    this.availState =  availSetting.availState || this.availState;
    this.orderConfirmationTextcolor = availSetting.confirmColor || this.orderConfirmationTextcolor;
  }

  private getCurrentEditorLang(availObj) {
    return availObj && Object.keys(availObj).length && Object.keys(availObj).length > 0 && Object.keys(availObj).sort()[0].split('_')[0] || '';
  }

  get isRequiredListText() {
    if (this.availSettingForm) {
      const listAvailCtrls = (this.availSettingForm.get('listAvail') as FormGroup).controls;
      const ctrls = Object.keys(listAvailCtrls).filter(ctrl => listAvailCtrls[ctrl].getError('required') || listAvailCtrls[ctrl].getError('whitespace'));
      if (ctrls.length > 0) {
        return true;
      }
    }

    return false;
  }

  get isMaxLengthListText() {
    if (this.availSettingForm) {
      const listAvailCtrls = (this.availSettingForm.get('listAvail') as FormGroup).controls;
      const ctrls = Object.keys(listAvailCtrls).filter(ctrl => listAvailCtrls[ctrl].getError('maxlength'));
      if (ctrls.length > 0) {
        return true;
      }
    }
    return false;
  }

  get isRequiredDetailText() {
    if (this.availSettingForm) {
        const detailAvailCtrls = (this.availSettingForm.get('detailAvail') as FormGroup).controls;
        const ctrls = Object.keys(detailAvailCtrls).filter(ctrl => detailAvailCtrls[ctrl].getError('required') || detailAvailCtrls[ctrl].getError('whitespace'));
        if (ctrls.length > 0) {
            return true;
        }
    }
    return false;
  }

  get isMaxLengthDetailText() {
      if (this.availSettingForm) {
          const detailAvailCtrls = (this.availSettingForm.get('detailAvail') as FormGroup).controls;
          const ctrls = Object.keys(detailAvailCtrls).filter(ctrl => detailAvailCtrls[ctrl].getError('maxlength'));
          if (ctrls.length > 0) {
              return true;
          }
      }
      return false;
  }

  get listAvailForm() {
    return this.availSettingForm ? (this.availSettingForm.get('listAvail') as FormGroup) : null;
  }

  get detailAvailForm() {
    return this.availSettingForm ? (this.availSettingForm.get('detailAvail') as FormGroup) : null;
  }

  get orderConfirmationTextPreview() {
    let textPreview = '';
    if (AffUtils.isAT() || AffUtils.isCH()) {
      textPreview = this.orderConfirmationTextPreviewForCH_AT;
    } else if (AffUtils.isCZ() || AffUtils.isCZAX10()){
      textPreview = this.orderConfirmationTextPreviewForCZ;
    }
    return textPreview;
  }
}
