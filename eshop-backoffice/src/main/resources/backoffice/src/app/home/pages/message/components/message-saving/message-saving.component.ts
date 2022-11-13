import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

import { IMyDateModel, IAngularMyDpOptions } from 'angular-mydatepicker';

import { MessageSavingService } from '../../services/message-saving.service';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { URL_MESSAGE_PAGE } from 'src/app/core/conts/app.constant';
import { DateUtil } from 'src/app/core/utils/date.util';
import { MessageLocationModel } from '../../models/message-location.model';
import { MessageFormModel } from '../../models/message-form.model';
import { MessageSavingModel } from '../../models/message-saving.model';
import { MessageService } from '../../services/message.service';
import { NotificationModel } from 'src/app/shared/models/notification.model';

import * as ace from 'ace-builds/src-noconflict/ace';
import { SagEditorLanguageModel } from 'sag-common';
ace.config.set('basePath', '/assets/ui/');
ace.config.set('modePath', '');
ace.config.set('themePath', '');

@Component({
    selector: 'backoffice-message-saving',
    styleUrls: ['./message-saving.component.scss'],
    templateUrl: './message-saving.component.html',
    providers: [
        MessageSavingService
    ]
})
export class MessageSavingComponent implements OnInit {

    isUpdateMode: boolean;

    // this part for master data options
    locationTypes: Array<any>;
    supportedAffiliates: Array<any>;
    supportedLanguages: Array<any>;
    roleTypes: Array<any>;
    userGroups: Array<any>;
    types: Array<any>;
    visibilities: Array<any>;
    styles: Array<any>;
    areas: Array<any>;
    subAreas: Array<any>;
    // end

    // start-date
    dateFromModel: any;
    dateToModel: any;
    datePickerFromSetting: IAngularMyDpOptions = {};
    datePickerToSetting: IAngularMyDpOptions = {};
    locale: string;

    private datePickerCommonSetting: any;
    // end-date

    messageSettingCollapsed = true;
    messageContentCollapsed = true;
    messageModel: MessageFormModel;
    languages: Array<SagEditorLanguageModel>;
    attemptedSubmit: boolean;
    messageNotifier: NotificationModel;
    editingMessageId;
    markdownOptions = {
        hideIcons: ['FullScreen']
    };

    private masterData: any;

    isFormEditExpand = true;
    isFormSettingExpand = true;
    formEditMess: FormGroup;
    formSettingMess: FormGroup;
    currentEditorLang: string;

    constructor(
        private translateService: TranslateService,
        private messageService: MessageService,
        private internalService: MessageSavingService,
        private router: Router,
        private fb: FormBuilder,
        private route: ActivatedRoute) {
        this.datePickerCommonSetting = DatePickerUtil.commonSetting;
    }

    ngOnInit(): void {
        this.initData();
    }

    onDateToChanged(event: IMyDateModel) {
        this.datePickerFromSetting = DateUtil.changeMinDate(
            this.datePickerToSetting,
            event.singleDate.date
        );
    }

    onDateFromChanged(event: IMyDateModel) {
        this.datePickerToSetting = DateUtil.changeMaxDate(
            this.datePickerToSetting,
            event.singleDate.date
        );
    }

    backToMessageListPage() {
        this.router.navigate([URL_MESSAGE_PAGE]);
    }

    // togglePanel(key) {
    //     switch (key) {
    //         case 'MESSAGE_CONTENT':
    //             this.messageContentCollapsed = !this.messageContentCollapsed;
    //             break;
    //         case 'MESSAGE_SETTING':
    //             this.messageSettingCollapsed = !this.messageSettingCollapsed;
    //             break;
    //     }
    // }

    initAutoFocus(langiso) {
        this.currentEditorLang = langiso;
        let selector = `#language-${langiso} textarea`;
        let markdown: any = document.querySelector(selector);
        if (markdown) {
            setTimeout(() => {
                markdown.focus();
            });
        }
        
    }

    getMessageContentsFromForm(): Array<SagEditorLanguageModel> {
        const formValue = this.formEditMess.value;
        const contents = this.languages.map(lang => new SagEditorLanguageModel({ content: formValue[lang.langIso], langIso: lang.langIso }));
        return contents;
    }

    onChangeSupportedAffiliates(event: any, affiliate: any) {
        if (!affiliate) {
            return;
        }
        const index = this.supportedAffiliates.findIndex((affl) => affl.value === affiliate.value);
        if (event.currentTarget.checked) {
            this.supportedAffiliates[index].selected = true;
        } else {
            this.supportedAffiliates[index].selected = false;
        }
    }

    createMessagse() {
        this.messageNotifier = null;
        this.attemptedSubmit = true;
        if (!this.title || this.isInvalidLanguages || this.isInvalidCustomerNr) {
            return;
        }

        const dateFrom = DatePickerUtil.getDateFromToDatePicker(this.formSettingMess.get('dateFrom').value.singleDate.date, true);
        const dateTo = DatePickerUtil.getDateFromToDatePicker(this.formSettingMess.get('dateTo').value.singleDate.date, true);
        const messageLocationModel: MessageLocationModel = new MessageLocationModel({
            locationTypeId: this.messageModel.locationTypeId,
            locationValues: this.isSelectedCustomerAsLocationType ?
                [this.customerNr] :
                (this.supportedAffiliates.filter((affiliate: any) => affiliate.selected)).map((validAffiliate: any) => validAffiliate.value)
        });

        const languages = this.getMessageContentsFromForm();
        const savingModel: MessageSavingModel = new MessageSavingModel({
            title: this.title,
            messageLocation: messageLocationModel,
            locationTypeId: this.messageModel.locationTypeId,
            locationValue: this.isSelectedCustomerAsLocationType ? this.customerNr :
                this.messageModel.supportedAffiliates.length ? this.messageModel.supportedAffiliates[0].value : '',
            accessRightId: this.internalService.getAccessRightId(this.roleTypes, this.messageModel, this.messageModel.roleTypeId),
            typeId: this.messageModel.typeId,
            styleId: this.messageModel.styleId,
            visibilityId: this.messageModel.visibilityId,
            active: this.activeMessage,
            subAreaId: this.internalService
                .getSubAreaId(this.masterData, this.messageModel, this.messageModel.areaId, !this.isSelectedPanelAsBlockType),
            dateValidFrom: dateFrom,
            dateValidTo: dateTo,
            messageLanguages: languages,
            ssoTraining: this.formSettingMess.value.ssoTraining
        });
        this.isUpdateMode ? this.handelForUpdateMessage(this.editingMessageId, savingModel) : this.handelForCreateMessage(savingModel);

    }

    selectLocationType() {
        if (!this.formSettingMess) {
            return;
        }
        this.messageModel.locationTypeId = this.formSettingMess.get('locationTypeId').value;
        this.resetRoleTypes(this.messageModel.locationTypeId);
    }

    selectUserRoleType() {
        if (!this.formSettingMess) {
            return;
        }
        this.messageModel.roleTypeId = this.formSettingMess.get('roleTypeId').value;
        this.resetUserGroups(this.messageModel.roleTypeId);
    }

    selectAccessRightId() {
        this.messageModel.accessRightId = this.formSettingMess.get('accessRightId').value;
    }

    selectStyleId() {
        this.messageModel.styleId = this.formSettingMess.get('styleId').value;
    }

    selectVisibilities() {
        this.messageModel.visibilityId = this.formSettingMess.get('visibilityId').value;
    }

    selectTypeId() {
        this.messageModel.typeId = this.formSettingMess.get('typeId').value;
    }

    selectArea() {
        this.messageModel.areaId = this.formSettingMess.get('areaId').value;
        this.resetSubAreas(this.messageModel.areaId);
    }

    selectSubArea() {
        this.messageModel.subAreaId = this.formSettingMess.get('subAreaId').value;
    }

    get isInvalidLanguages() {
        const languages = this.getMessageContentsFromForm();
        return this.hasAnyLanguageWithNoContent(languages);
    }

    get isInvalidCustomerNr() {
        return this.isSelectedCustomerAsLocationType && !this.customerNr;
    }

    get supportedAffiliatesSelectCtrs() {
        return this.formSettingMess.get('supportedAffiliatesSelectCtrs') as FormGroup;
    }

    get isDataReady() {
        return this.locationTypes
            && this.supportedAffiliates
            && this.roleTypes
            && this.userGroups
            && this.types
            && this.visibilities
            && this.styles
            && this.areas
            && this.subAreas
            && this.supportedLanguages
            && this.messageModel
            && this.languages;
    }

    get isSelectedAffiliateAsLocationType() {
        const locationTypeId = this.formSettingMess.get('locationTypeId').value;
        return this.internalService.isSelectedAffiliateAsLocationType(this.masterData, locationTypeId);
    }

    get isSelectedCustomerAsLocationType() {
        const locationTypeId = this.formSettingMess.get('locationTypeId').value;
        return this.internalService.isSelectedCustomerAsLocationType(this.masterData, locationTypeId);
    }

    get isSelectedRoleHasMoreThanOneGroup() {
        return this.internalService.isSelectedRoleHasMoreThanOneGroup(this.userGroups);
    }

    get isSelectedAreaHasMoreThanOneSubArea() {
        return this.internalService.isSelectedAreaHasMoreThanOneSubArea(this.subAreas);
    }

    get isSelectedPanelAsBlockType() {
        return this.internalService.isSelectedPanelAsBlockType(this.masterData, this.messageModel.typeId);
    }

    get title() {
        return this.formEditMess ? this.formEditMess.get('title').value : '';
    }

    get customerNr() {
        return this.formSettingMess ? this.formSettingMess.get('customerNr').value : null;
    }

    get activeMessage() {
        return this.formSettingMess ? this.formSettingMess.get('active').value : false;
    }

    private intiSettingForm() {
        if (!this.messageModel) {
            return;
        }

        this.formSettingMess = this.fb.group({
            active: this.messageModel.active,
            locationTypeId: this.messageModel.locationTypeId,
            supportedAffiliatesSelectCtrs: this.buildSupportedAffiliatesControls(),
            customerNr: [this.messageModel.customerNr, Validators.required],
            roleTypeId: this.messageModel.roleTypeId,
            accessRightId: this.messageModel.accessRightId,
            dateFrom: this.dateFromModel,
            dateTo: this.dateToModel,
            styleId: this.messageModel.styleId,
            visibilityId: this.messageModel.visibilityId,
            typeId: this.messageModel.typeId,
            areaId: this.messageModel.areaId,
            subAreaId: this.messageModel.subAreaId,
            ssoTraining: this.messageModel.ssoTraining
        });
    }

    private buildSupportedAffiliatesControls() {
        const supportAffCtrList: any[] = [];
        this.supportedAffiliates.map(aff => supportAffCtrList.push(this.fb.control(aff.selected)));
        return this.fb.array(supportAffCtrList);
    }

    private initLanguage() {
        const languages = [];
        this.supportedLanguages.forEach(item => {
            languages.push(new SagEditorLanguageModel({ langIso: item.langiso }));
        });
        return languages;
    }

    private initEditForm() {
        if (!this.messageModel || !this.languages) {
            return;
        }

        this.currentEditorLang = this.languages[0].langIso;
        const contentList = [];
        this.languages.map(lang => {
            contentList[lang.langIso] = [lang.content, Validators.required];
        });

        this.formEditMess = this.fb.group({
            title: [this.messageModel.title, Validators.required],
            content: [this.languages[0].content, Validators.required],
            ...contentList
        });
    }

    private getDefaultModel(): MessageFormModel {
        return new MessageFormModel({
            active: true,
            locationTypeId: this.locationTypes[0].value,
            affiliateShortName: this.supportedAffiliates.map((x: any) => x.value),
            customerNr: null,
            roleTypeId: this.roleTypes[0].value,
            accessRightId: this.userGroups[0].value,
            typeId: this.types[0].value,
            visibilityId: this.visibilities[0].value,
            styleId: this.styles[0].value,
            areaId: this.areas[0].value,
            subAreaId: this.subAreas[0].value,
            supportedAffiliates: [],
            ssoTraining: false
        });
    }

    private initData() {
        this.messageService.getMasterData().subscribe(result => {
            const res: any = result;
            this.masterData = res;
            this.locationTypes = this.internalService.getLocationTypes(res.locationTypes);
            this.supportedAffiliates = this.internalService.getSupportedAffiliates(res.supportedAffiliates);
            this.roleTypes = this.internalService.getRoleTypes(this.locationTypes[0].roleTypes);
            this.userGroups = this.internalService.getUserGroupRole(this.roleTypes[0].accessRights);
            this.types = this.internalService.getTypes(res.types);
            this.visibilities = this.internalService.getVisibilities(res.visibilities);
            this.styles = this.internalService.getStyles(res.styles);
            this.areas = this.internalService.getAreas(this.userGroups[0].areas);
            this.subAreas = this.internalService.getSubAreas(this.areas, this.areas[0].value);
            this.supportedLanguages = res.supportedLanguages;

            this.route.params.subscribe((params) => {
                this.languages = this.initLanguage();
                if (params && params.id) {
                    this.isUpdateMode = true;
                    this.editingMessageId = params.id;
                    this.messageService.findById(this.editingMessageId).subscribe((foundMessage: any) => {

                        const message = foundMessage;
                        this.messageModel = this.getModel(message);
                        this.languages = [];
                        message.messageLanguages.forEach(item => {
                            this.languages.push(new SagEditorLanguageModel({ langIso: item.langIso, content: item.content }));
                        });

                        this.initCalenda(this.messageModel.dateValidFrom, this.messageModel.dateValidTo);
                        this.intiSettingForm();
                        this.initEditForm();
                        this.selectLocationType();
                    });
                } else {
                    this.initDefaultCalenda();
                    this.messageModel = this.getDefaultModel();
                    this.initEditForm();
                    this.intiSettingForm();
                }
            });

        }, err => { });
    }

    private getModel(message): MessageFormModel {
        if (message.messageLocation) {
            const supportedAffiliatesValues = message.messageLocation.locationValues || [];

            const selectedAffilicates = this.supportedAffiliates.map((affiliate: any) => {
                if (supportedAffiliatesValues.includes(affiliate.value)) {
                    return {
                        selected: true,
                        value: affiliate.value,
                        label: affiliate.label
                    };
                }
                return {
                    selected: false,
                    value: affiliate.value,
                    label: affiliate.label
                };
            });
            this.supportedAffiliates = [...selectedAffilicates];
        }

        return new MessageFormModel({
            title: message.title,
            active: message.active,
            locationTypeId: message.locationTypeId.toString(),
            affiliateShortName: message.affiliateShortName,
            customerNr: message.customerNr,
            roleTypeId: message.roleTypeId.toString(),
            accessRightId: message.accessRightId.toString(),
            typeId: message.typeId.toString(),
            visibilityId: message.visibilityId.toString(),
            styleId: message.styleId.toString(),
            areaId: message.areaId.toString(),
            subAreaId: message.subAreaId.toString(),
            dateValidFrom: message.dateValidFrom,
            dateValidTo: message.dateValidTo,
            supportedAffiliates: this.supportedAffiliates,
            ssoTraining: message.ssoTraining
        });
    }

    private resetRoleTypes(selectedLocationTypeId: number) {
        const selectedLocationType = this.masterData.locationTypes.find(item => item.id == selectedLocationTypeId);
        this.roleTypes = this.internalService.getRoleTypes(selectedLocationType.roleTypes);
        this.messageModel.roleTypeId = this.containsChild(this.messageModel.roleTypeId, this.roleTypes) || this.roleTypes[0].value;
        this.formSettingMess.patchValue({
            roleTypeId: this.messageModel.roleTypeId
        });
        this.resetUserGroups(this.messageModel.roleTypeId);
    }

    private resetUserGroups(selectedRoleTypeId: number) {
        const selectedUsertype = this.roleTypes.find(item => item.value == selectedRoleTypeId);
        this.userGroups = this.internalService.getUserGroupRole(selectedUsertype.accessRights);
        this.messageModel.accessRightId = this.containsChild(this.messageModel.accessRightId, this.userGroups) || this.userGroups[0].value;
        this.formSettingMess.patchValue({
            accessRightId: this.messageModel.accessRightId
        });
        this.resetAreas(this.messageModel.accessRightId);
    }

    private resetAreas(selectedUserGroupId: number) {
        const selectedUserGroup = this.userGroups.find(item => item.value == selectedUserGroupId);
        this.areas = this.internalService.getAreas(selectedUserGroup.areas);
        this.messageModel.areaId = this.containsChild(this.messageModel.areaId, this.areas) || this.areas[0].value;
        this.formSettingMess.patchValue({
            areaId: this.messageModel.areaId
        });
        this.resetSubAreas(this.messageModel.areaId);
    }

    private resetSubAreas(selectedAreaId: number) {
        this.subAreas = this.internalService.getSubAreas(this.areas, selectedAreaId);
        this.messageModel.subAreaId = this.containsChild(this.messageModel.subAreaId, this.subAreas) || this.subAreas[0].value;
        this.formSettingMess.patchValue({
            subAreaId: this.messageModel.subAreaId
        });
    }

    private containsChild(id: number, arr: Array<any>) {
        return arr.find(item => item.value === id) ? id : null;
    }

    private initDefaultCalenda() {
        this.locale = this.translateService.currentLang;
        const currentDate = new Date();

        // build date from
        const dateAsOneMonthFromNow = new Date(currentDate);
        this.dateFromModel = this.buildDataDatePicker(dateAsOneMonthFromNow);

        // build date To
        currentDate.setMonth(currentDate.getMonth() + 1);
        this.dateToModel = this.buildDataDatePicker(currentDate);

        // validate range date from can not be over date to
        Object.assign(this.datePickerFromSetting, this.datePickerCommonSetting);
        this.datePickerFromSetting.disableSince = this.dateToModel.singleDate.date;

        // validate range date to can not be before date from
        Object.assign(this.datePickerToSetting, this.datePickerCommonSetting);
        this.datePickerToSetting.disableUntil = this.dateFromModel.singleDate.date;
    }

    private initCalenda(dateValidFrom, dateValidTo) {
        this.locale = this.translateService.currentLang;
        this.dateFromModel = this.buildDataDatePicker(new Date(dateValidFrom));
        this.dateToModel = this.buildDataDatePicker(new Date(dateValidTo));

        Object.assign(this.datePickerFromSetting, this.datePickerCommonSetting);
        this.datePickerFromSetting.disableSince = this.dateToModel.singleDate.date;
        Object.assign(this.datePickerToSetting, this.datePickerCommonSetting);
        this.datePickerToSetting.disableUntil = this.dateFromModel.singleDate.date;
    }

    private buildDataDatePicker(dateInJavascript) {
        return {
            isRange: false,
            singleDate: {
                date:
                {
                    year: dateInJavascript.getFullYear(),
                    month: dateInJavascript.getMonth() + 1,
                    day: dateInJavascript.getDate()
                }
            }
        } as IMyDateModel;
    }

    private hasAnyLanguageWithNoContent(languages: Array<SagEditorLanguageModel>) {
        return languages.find(item => !item.content);
    }

    private handelForCreateMessage(savingModel: MessageSavingModel) {
        this.messageService.create(savingModel).subscribe(
            res => {
                this.messageNotifier = { messages: ['COMMON.MESSAGE.SAVE_SUCCESSFULLY'], status: true };
                setTimeout(() => {
                    this.router.navigateByUrl(URL_MESSAGE_PAGE);
                }, 1000);
            }, ({ error })  => {
                this.handleError(error);
            });
    }

    private handelForUpdateMessage(messageId, savingModel: MessageSavingModel) {
        this.messageService.update(messageId, savingModel).subscribe(
            res => {
                this.messageNotifier = { messages: ['COMMON.MESSAGE.SAVE_SUCCESSFULLY'], status: true };
            }, ({ error }) => {
                this.handleError(error);
            });
    }

    private handleError(err) {
        switch (err.error_code) {
            case 'CUSTOMER_NOT_FOUND':
                this.messageNotifier = { messages: ['MESSAGE.CUSTOMER_NOT_FOUND'], status: false };
                break;
            case 'MESSAGE_TIME_OVERLAP':
                this.messageNotifier = { messages: ['MESSAGE.MESSAGE_TIME_OVERLAP'], status: false };
                break;
            default:
                this.messageNotifier = { messages: [err.message], status: false };
                break;
        }
    }
}
