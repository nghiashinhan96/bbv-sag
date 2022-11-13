import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { SagNumericService } from './services/numeric.service';
import { SagNumericDirective } from './directives/numeric.directive';
import { SagNumericPipe } from './pipes/numeric.pipe';
import { SagCurrencyPipe } from './pipes/currency.pipe';
import { FixedNumericPipe } from './pipes/fixed-numeric.pipe';
import { SagCurrencyAmountInputComponent } from './components/amount-input/amount-input.component';
import { SagCurrencyStorageService } from './services/currency-storage.service';

@NgModule({
    declarations: [
        SagNumericDirective,
        SagNumericPipe,
        SagCurrencyPipe,
        FixedNumericPipe,
        SagCurrencyAmountInputComponent
    ],
    exports: [
        SagNumericDirective,
        SagNumericPipe,
        SagCurrencyPipe,
        FixedNumericPipe,
        SagCurrencyAmountInputComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        NgxWebstorageModule
    ]
})
export class SagCurrencyModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagCurrencyModule,
            providers: [
                SagCurrencyStorageService,
                SagNumericPipe,
                SagCurrencyPipe,
                FixedNumericPipe,
                SagNumericService
            ]
        };
    }
}
