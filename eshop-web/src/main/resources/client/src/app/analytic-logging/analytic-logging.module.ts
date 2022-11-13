import { NgModule } from '@angular/core';
import { EncryptDecryptService } from '../core/services/encrypt-decrypt.service';
import { CustomerOrderTypeService } from '../shopping-basket/services/customer-order-type.service';
import { SalesOrderTypeService } from '../shopping-basket/services/sales-order-type.service';

@NgModule({
    providers: [
        EncryptDecryptService,
        SalesOrderTypeService,
        CustomerOrderTypeService
    ]
})
export class AnalyticLoggingModule { }
