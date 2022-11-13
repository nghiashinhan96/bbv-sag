import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AffiliateAvailSettingComponent } from './affiliate-avail-setting.component';
import { TranslateModule } from '@ngx-translate/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { LMarkdownEditorModule } from 'ngx-markdown-editor';
import AffUtils from 'src/app/core/utils/aff-utils';

xdescribe('AffiliateAvailSettingComponent', () => {
  let component: AffiliateAvailSettingComponent;
  let fixture: ComponentFixture<AffiliateAvailSettingComponent>;
  let spy: any;
  let formBuilder: FormBuilder;
  let availStateFormGroup: FormGroup;
  let nonAvailStateFormGroup: FormGroup;
  const mockupData = [
    {
      availState: "SAME_DAY",
      listAvailText: [
        {
          langIso: "IT",
          content: "a"
        },
        {
          langIso: "FR",
          content: "Aujourd'hu"
        },
        {
          langIso: "DE",
          content: "b"
        }
      ],
      title: "Available same day",
      color: "rgb(2, 173, 2)",
      displayOption: "DISPLAY_TEXT"
    },
    {
      availState: "NOT_AVAILABLE",
      detailAvailText: [
        {
          langIso: "IT",
          content: "Se l'articolo non è disponibile entro 24 ore, sarete informati."
        },
        {
          langIso: "FR",
          content: "Si l'article n'est pas disponible en 24 heures, vous en serez informé."
        },
        {
          langIso: "DE",
          content: "Sollte der Artikel nicht innerhalb von 24 Std lieferbar sein, werden Sie informiert. fdfdfdfd"
        }
      ],
      listAvailText: [
        {
          langIso: "IT",
          content: "test"
        },
        {
          langIso: "FR",
          content: "test"
        },
        {
          langIso: "DE",
          content: "test to show text fdfd"
        }
      ],
      title: "Not available",
      color: "rgb(2, 173, 2)",
      displayOption: "DROP_SHIPMENT",
      confirmColor: "rgb(2, 173, 2)"
    }
  ];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AffiliateAvailSettingComponent ],
      imports: [
        TranslateModule.forRoot(),
        SharedModules,
        LMarkdownEditorModule,
        ReactiveFormsModule
      ],
      providers: [
        FormBuilder,
      ]
    })
    .compileComponents();
    formBuilder = TestBed.get(FormBuilder);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AffiliateAvailSettingComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  xdescribe('avail state section', () => {
    beforeEach(() => {
      availStateFormGroup = formBuilder.group({
        color: mockupData[0].color || '',
        displayOption: mockupData[0].displayOption,
        listAvail: formBuilder.group({
            IT_List: [mockupData[0].listAvailText[0].content, [Validators.required, Validators.maxLength(255)]],
            FR_List: [mockupData[0].listAvailText[1].content, [Validators.required, Validators.maxLength(255)]],
            DE_List: [mockupData[0].listAvailText[2].content, [Validators.required, Validators.maxLength(255)]]
        }),
        title: mockupData[0].title,
        availState: mockupData[0].availState
      });
      component.availSettingForm = availStateFormGroup;
      fixture.detectChanges();
    });

    it('should init data', () => {
      expect(component.currentEditorLangList).toBe('DE');
      expect(component.currentEditorLangDetail).toBe('');
      expect(component.color).toBe(mockupData[0].color);
      expect(component.availState).toBe(mockupData[0].availState);
    });

    it('should get ace input element for auto focusing purpose', () => {
      expect(component.aceTextInput).toBeTruthy();
    });

    it('should set color for preview display test at the first time', () => {
      const compiled: HTMLElement = fixture.debugElement.nativeElement;
      const previewDisplayTextEl: HTMLElement = compiled.querySelector(`#listAvail-language-${component.currentEditorLangList}_List .preview-panel`);
      const color = previewDisplayTextEl.style.color;
      expect(color).toBe(mockupData[0].color);
    });

    it('updateColor() should work properly', () => {
      spy = spyOn(component, 'updateColor').and.callThrough();
      const newColor = 'rgb(219, 15, 237)';
      component.updateColor(newColor);
      expect(component.updateColor).toHaveBeenCalled();
      expect(component.color).toBe(newColor);
      expect(component.availSettingForm.getRawValue().color).toBe(newColor);
      const compiled: HTMLElement = fixture.debugElement.nativeElement;
      const previewDisplayTextEl: HTMLElement = compiled.querySelector(`#listAvail-language-${component.currentEditorLangList}_List .preview-panel`);
      const color = previewDisplayTextEl.style.color;
      expect(color).toBe(newColor);
    });

    it('initAutoFocus() should work properly', fakeAsync(() => {
      spy = spyOn(component, 'initAutoFocus').and.callThrough();
      spy = spyOn<any>(component, 'setAutoFocus');
      component.initAutoFocus('FR', 1, false);
      expect(component.initAutoFocus).toHaveBeenCalled();
      expect(component.currentEditorLangList).toBe('FR');
      tick(500);
      expect(component['setAutoFocus']).toHaveBeenCalledWith(1);
    }));

    it('setAutoFocus() should work properly', () => {
      spy = spyOn<any>(component, 'setAutoFocus').and.callThrough();
      spy = spyOn<any>(component, 'setColorForPreviewPanel');
      component['setAutoFocus'](1);
      expect(component['setAutoFocus']).toHaveBeenCalled();
      expect(component['setColorForPreviewPanel']).toHaveBeenCalled();
    });

    it('isRequiredListText() should work properly', () => {
      let isRequiredListText = component.isRequiredListText;
      expect(isRequiredListText).toBe(false);
      component.availSettingForm.patchValue({listAvail: {DE_List: ''}});
      isRequiredListText = component.isRequiredListText;
      expect(isRequiredListText).toBe(true);
    });
  });

  xdescribe('not avail state section', () => {
    beforeEach(() => {
      nonAvailStateFormGroup = formBuilder.group({
        color: mockupData[1].color || '',
        displayOption: mockupData[1].displayOption,
        listAvail: formBuilder.group({
            IT_List: [mockupData[1].listAvailText[0].content, [Validators.required, Validators.maxLength(255)]],
            FR_List: [mockupData[1].listAvailText[1].content, [Validators.required, Validators.maxLength(255)]],
            DE_List: [mockupData[1].listAvailText[2].content, [Validators.required, Validators.maxLength(255)]]
        }),
        detailAvail: formBuilder.group({
          IT_List: [mockupData[1].detailAvailText[0].content, [Validators.required, Validators.maxLength(255)]],
          FR_List: [mockupData[1].detailAvailText[1].content, [Validators.required, Validators.maxLength(255)]],
          DE_List: [mockupData[1].detailAvailText[2].content, [Validators.required, Validators.maxLength(255)]]
      }),
        title: mockupData[1].title,
        availState: mockupData[1].availState,
        confirmColor: mockupData[1].confirmColor
      });
      component.availSettingForm = nonAvailStateFormGroup;
      fixture.detectChanges();
    });

    it('should init data', () => {
      expect(component.currentEditorLangList).toBe('DE');
      expect(component.currentEditorLangDetail).toBe('DE');
      expect(component.color).toBe(mockupData[1].color);
      expect(component.availState).toBe(mockupData[1].availState);
    });

    it('should set color for preview display test at the first time', () => {
      const compiled: HTMLElement = fixture.debugElement.nativeElement;
      const previewDisplayTextEl: HTMLElement = compiled.querySelector(`#listAvail-language-${component.currentEditorLangList}_List .preview-panel`);
      let color = previewDisplayTextEl.style.color;
      expect(color).toBe(mockupData[1].color);
      const dropShipmentPreviewElement: HTMLElement = compiled.querySelector(`.drop-shipment-text-preview`);
      color = dropShipmentPreviewElement.style.color;
      expect(color).toBe(mockupData[1].color);
      const dotElement: HTMLElement = compiled.querySelector(`.dot`);
      color = dotElement.style.color;
      expect(color).toBe(mockupData[1].color);
    });

    it('initAutoFocus() should work properly', fakeAsync(() => {
      spy = spyOn(component, 'initAutoFocus').and.callThrough();
      spy = spyOn<any>(component, 'setAutoFocus');
      component.initAutoFocus('FR', 1, true);
      expect(component.initAutoFocus).toHaveBeenCalled();
      expect(component.currentEditorLangDetail).toBe('FR');
      tick(500);
      expect(component['setAutoFocus']).toHaveBeenCalledWith(1);
      component.initAutoFocus('FR', 1, false);
      expect(component.currentEditorLangList).toBe('FR');
      const totalLangItem = mockupData[1].listAvailText.length;
      tick(500);
      expect(component['setAutoFocus']).toHaveBeenCalledWith(totalLangItem + 1);
    }));

    it('isRequiredDetailText() should work properly', () => {
      let isRequiredDetailText = component.isRequiredDetailText;
      expect(isRequiredDetailText).toBe(false);
      component.availSettingForm.patchValue({detailAvail: {DE_List: ''}});
      isRequiredDetailText = component.isRequiredDetailText;
      expect(isRequiredDetailText).toBe(true);
    });

    it('updateOrderConfirmationTextColor() should work properly', () => {
      spy = spyOn(component, 'updateOrderConfirmationTextColor').and.callThrough();
      const newColor = 'rgb(219, 15, 237)';
      component.updateOrderConfirmationTextColor(newColor);
      expect(component.updateOrderConfirmationTextColor).toHaveBeenCalled();
      expect(component.orderConfirmationTextcolor).toBe(newColor);
      expect(component.availSettingForm.getRawValue().confirmColor).toBe(newColor);
      const compiled: HTMLElement = fixture.debugElement.nativeElement;
      const orderConfirmationTextPreviewEl: HTMLElement = compiled.querySelector(`.order-confirmation-text-preview-box`);
      const color = orderConfirmationTextPreviewEl.style.color;
      expect(color).toBe(newColor);
    });

    it('orderConfirmationTextPreview() should work properly when country is AT', () => {
      spy = spyOn(AffUtils, 'isAT').and.callThrough().and.returnValue(true);
      let orderConfirmationTextPreview = component.orderConfirmationTextPreview;
      expect(orderConfirmationTextPreview).toBe(component.orderConfirmationTextPreviewForCH_AT);
    });

    it('orderConfirmationTextPreview() should work properly when country is CZ', () => {
      spy = spyOn(AffUtils, 'isAT').and.callThrough().and.returnValue(false);
      spy = spyOn(AffUtils, 'isCZ').and.callThrough().and.returnValue(true);
      let orderConfirmationTextPreview = component.orderConfirmationTextPreview;
      expect(orderConfirmationTextPreview).toBe(component.orderConfirmationTextPreviewForCZ);
    });

  });

  afterAll(() => {
    TestBed.resetTestingModule();
  });
});
