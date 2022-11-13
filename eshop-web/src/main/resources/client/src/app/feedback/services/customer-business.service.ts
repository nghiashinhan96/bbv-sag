import { Injectable } from '@angular/core';
import { FeedbackUserType } from '../enums/feedback.enum';
import { FeedbackData } from '../models/feedback-data.model';
import { FeedbackBusinessService } from './feedback-business.service';
import { FeedbackSavingRequest } from '../models/feedback-saving-request.model';

@Injectable()
export class CustomerBusinessService extends FeedbackBusinessService {
    getFormTitle() {
        return 'FEEDBACK.CUSTOMER_FEEDBACK';
    }

    getGroup() {
        return FeedbackUserType[FeedbackUserType.CUSTOMER];
    }

    getMasterData() {
        return this.feedbackService.getCustomerUserData();
    }

    getTechnicalData(): FeedbackData {
        return this.getNormalUserTechnicalData(this.fbRecordingService.getTechnicalData());
    }

    getUserData(data) {
        return this.getNormalUserData(data);
    }

    getSavingRequestModel(model: FeedbackSavingRequest): FeedbackSavingRequest {
        return new FeedbackSavingRequest(model);
    }

    createFeedback(model: FormData, rawModel?: any) {
        let modelToConsume: any;
        // IE don't support FormData get()
        if (typeof model.get === 'function') {
            modelToConsume = model.get('feedbackModel');
        } else if (rawModel) {
            modelToConsume = rawModel;
        }
        return this.feedbackService.createCustomerFeedback(modelToConsume);
    }
}
