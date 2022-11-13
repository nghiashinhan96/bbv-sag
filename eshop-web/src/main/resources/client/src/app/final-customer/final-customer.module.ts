import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GoodReceiverComponent } from './pages/good-receiver/good-receiver.component';
import { GoodReceiverModalComponent } from './pages/good-receiver-modal/good-receiver-modal.component';
import { SagTableModule } from 'sag-table';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TranslateModule } from '@ngx-translate/core';
import { FinalCustomerAddressComponent } from './pages/final-customer-address/final-customer-address.component';



@NgModule({
    declarations: [
        GoodReceiverComponent,
        GoodReceiverModalComponent,
        FinalCustomerAddressComponent
    ],
    entryComponents: [
        GoodReceiverModalComponent
    ],
    exports: [
        GoodReceiverComponent,
        FinalCustomerAddressComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        ModalModule.forRoot(),
        SagTableModule
    ]
})
export class FinalCustomerModule { }
