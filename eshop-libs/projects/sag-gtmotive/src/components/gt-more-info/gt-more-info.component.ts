import { Component, Input } from '@angular/core';

@Component({
    selector: 'sag-gtmotive-more-info',
    templateUrl: './gt-more-info.component.html'
})
export class SagGtmotiveMoreInfoComponent {
    @Input() public gtNonMatchedOperations: any;
    @Input() public normauto: boolean;
    @Input() public mailContent: any;

    isShow = true;

    constructor() { }

    sendOfferOrderToNormauto() {
        window.location.href = this.mailContent;
    }
}
