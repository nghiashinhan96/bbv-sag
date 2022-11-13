import { Injectable } from '@angular/core';
import { SalesBusinessService } from './sales-business.service';
import { cloneDeep } from 'lodash';
import { FeedbackData } from '../models/feedback-data.model';
import { FeedbackSavingRequest } from '../models/feedback-saving-request.model';
import { FeedbackDataItem } from '../models/feedback-data-item.model';
import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { FeedbackDataItemKey, FeedbackUserType } from '../enums/feedback.enum';

@Injectable()
export class SalesNotOnBehalfBusinessService extends SalesBusinessService {
    getGroup() {
        return FeedbackUserType[FeedbackUserType.SALES_NOT_ON_BEHALF];
    }

    getMasterData() {
        return this.feedbackService.getSaleNotOnBehalfUserData();
    }

    getUserData(data) {
        return this.getSalesUserData(data);
    }

    getTechnicalData(): FeedbackData {
        return this.getSalesUserTechnicalData(this.fbRecordingService.getTechnicalData());
    }

    getSavingRequestModel(model: FeedbackSavingRequest): FeedbackSavingRequest {
        const userDataItem = cloneDeep(model.userData);
        const salesDataItem = userDataItem.items.shift();
        const salesInfo = new FeedbackData({
            title: this.translateService.instant('FEEDBACK.SALES_DATA'),
            items: Array.of(salesDataItem)
        });

        const dataItems: FeedbackDataItem[] = [];
        const customerInfoItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CUSTOMER_INFO.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.CUSTOMER'),
            value: NOT_AVAILABLE
        });
        dataItems.push(customerInfoItem);

        const customerEmailItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CUSTOMER_EMAIL.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.EMAIL'),
            value: NOT_AVAILABLE
        });
        dataItems.push(customerEmailItem);

        const customerPhoneItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CUSTOMER_PHONE.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.PHONE'),
            value: NOT_AVAILABLE
        });
        dataItems.push(customerPhoneItem);

        const defaultBranchItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.DEFAULT_BRANCH.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.BRANCH'),
            value: NOT_AVAILABLE
        });
        dataItems.push(defaultBranchItem);

        const customerContact = model.customerContact;
        const customerContactItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CUSTOMER_CONTACT.toString(),
            title: this.translateService.instant('FEEDBACK.CUSTOMER_CONTACT'),
            value: customerContact ? customerContact.contact : NOT_AVAILABLE
        });
        dataItems.push(customerContactItem);

        const newUserDataItem = new FeedbackData({
            title: this.translateService.instant('FEEDBACK.YOUR_DATA'),
            items: dataItems
        });

        const finalModel: FeedbackSavingRequest = new FeedbackSavingRequest({
            ...model,
            salesInfo,
            userData: newUserDataItem
        });

        return finalModel;
    }

    createFeedback(formData: FormData) {
        return this.feedbackService.createSaleNotOnBehalfFeedback(formData);
    }
}
