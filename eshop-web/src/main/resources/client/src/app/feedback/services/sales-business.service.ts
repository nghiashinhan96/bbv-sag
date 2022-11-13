import { Injectable } from '@angular/core';
import { FeedbackBusinessService } from './feedback-business.service';

@Injectable()
export abstract class SalesBusinessService extends FeedbackBusinessService {
    getFormTitle() {
        return 'FEEDBACK.SALES_FEEDBACK';
    }
}
