import { Component } from '@angular/core';

@Component({
    selector: 'connect-return-basket-information-modal',
    templateUrl: 'return-order-information-modal.component.html',
    styleUrls: ['return-order-information-modal.component.scss']
})
export class ReturnOrderInformationModalComponent {

    onProcessInBackground;
    isDisable = false;
    
    constructor(
    ) { }

    handleProcessInBackground(){
        this.isDisable = true;
        this.onProcessInBackground();
    }
}
