import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { SagCurrencyModule } from 'sag-currency';
import { SagCustomPricingStorageService } from './services/custom-pricing-storage.service';
import { SagCustomPricingService } from './services/custom-pricing.service';
import { SagCustomPricingHeaderActionsComponent } from './pages/custom-pricing-header-actions/custom-pricing-header-actions.component';
import { SagCustomPricingItemActionsComponent } from './pages/custom-pricing-item-actions/custom-pricing-item-actions.component';
import { SagCustomPricingPopoverComponent } from './pages/custom-pricing-popover/custom-pricing-popover.component';
import { SagCustomPricingConfigService } from './services/custom-pricing-config.service';
import { PopoverModule } from 'ngx-bootstrap/popover';

@NgModule({
    declarations: [
        SagCustomPricingHeaderActionsComponent,
        SagCustomPricingItemActionsComponent,
        SagCustomPricingPopoverComponent
    ],
    exports: [
        SagCustomPricingHeaderActionsComponent,
        SagCustomPricingItemActionsComponent,
        SagCustomPricingPopoverComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        PopoverModule.forRoot(),
        SagCurrencyModule
    ]
})
export class SagCustomPricingModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagCustomPricingModule,
            providers: [
                SagCustomPricingConfigService,
                SagCustomPricingStorageService,
                SagCustomPricingService
            ]
        };
    }
}
