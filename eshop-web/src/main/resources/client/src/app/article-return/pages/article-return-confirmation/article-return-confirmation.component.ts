import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SagMessageData } from 'sag-common';
import { RETURN_ORDER_MESSAGE_TYPE } from '../../enums/return-order.enum';

@Component({
    selector: 'connect-article-return-confirmation',
    templateUrl: './article-return-confirmation.component.html',
    styleUrls: ['./article-return-confirmation.component.scss']
})
export class ArticleReturnConfirmationComponent implements OnInit {

    nonQuarantineOrderPositions;
    quarantineOrderPositions;
    errorMessage: string;
    batchJobId: string;
    showWarningTitle: boolean;
    messenger: SagMessageData;
    messageType: RETURN_ORDER_MESSAGE_TYPE;
    RETURN_ORDER_MESSAGE_TYPE = RETURN_ORDER_MESSAGE_TYPE;

    constructor(private activate: ActivatedRoute) { }

    ngOnInit() {
        const { nonQuarantineOrderPositions, quarantineOrderPositions, batchJobId, errorMessage, messageType, showWarningTitle } = this.activate.snapshot.queryParams;
        this.nonQuarantineOrderPositions = this.formatResult(nonQuarantineOrderPositions);
        this.quarantineOrderPositions = this.formatResult(quarantineOrderPositions);
        this.errorMessage = errorMessage;
        this.showWarningTitle = showWarningTitle;
        this.batchJobId = batchJobId;

        if (messageType && batchJobId) {
            this.messageType = messageType;
            this.messenger = {
                type: messageType,
                message: this.errorMessage,
                params: {
                    batchJobId
                }
            } as SagMessageData;

        }
    }

    private formatResult(input) {
        if (!input) {
            return '';
        }
        let formatText = '';
        if (input.length <= 5) {
            return input.join(', ');
        }

        for (let index = 0; index < input.length; index++) {
            const element = input[index];
            formatText += element;
            if (index % 5 === 0 && index !== 0) {
                formatText += ', <br/>';
            } else if (index !== input.length - 1) {
                formatText += ', ';
            }
        }
        return formatText;
    }


}
