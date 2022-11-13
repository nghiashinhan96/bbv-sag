import { Injectable } from '@angular/core';
import { cloneDeep } from 'lodash';
import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { FeedbackDataItemKey, FeedbackUserType } from '../enums/feedback.enum';
import { FeedbackDataItem } from '../models/feedback-data-item.model';
import { FeedbackData } from '../models/feedback-data.model';
import { FeedbackSavingRequest } from '../models/feedback-saving-request.model';
import { SalesBusinessService } from './sales-business.service';

@Injectable()
export class SalesOnBehalfBusinessService extends SalesBusinessService {
    getGroup() {
        return FeedbackUserType[FeedbackUserType.SALES_ON_BEHALF];
    }

    getMasterData() {
        return this.feedbackService.getSaleOnBehalfUserData();
    }

    getUserData(data) {
        return this.getNormalUserData(data);
    }

    getTechnicalData(): FeedbackData {
        return this.getNormalUserTechnicalData(this.fbRecordingService.getTechnicalData());
    }

    getSavingRequestModel(model: FeedbackSavingRequest): FeedbackSavingRequest {
        const userData: FeedbackData = cloneDeep(model.userData);
        const salesDataItem = userData.items.shift();
        const salesInfo = new FeedbackData({
            title: this.translateService.instant('FEEDBACK.SALES_DATA'),
            items: Array.of(salesDataItem)
        });
        const customerContact = model.customerContact;
        const customerContactItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CUSTOMER_CONTACT.toString(),
            title: this.translateService.instant('FEEDBACK.CUSTOMER_CONTACT'),
            value: customerContact ? customerContact.contact : NOT_AVAILABLE
        });
        userData.items.push(customerContactItem);

        const finalModel = new FeedbackSavingRequest({
            ...model,
            salesInfo,
            userData
        });

        return finalModel;
    }

    createFeedback(model: FormData) {
        return this.feedbackService.createSaleOnBehalfFeedback(model);
    }
}
