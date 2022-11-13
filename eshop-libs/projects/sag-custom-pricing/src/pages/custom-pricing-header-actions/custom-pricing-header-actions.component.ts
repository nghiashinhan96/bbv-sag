import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SubSink } from 'subsink';
import { CustomPrice } from '../../interfaces/custom-pricing.interface';
import { SagCustomPricingConfigService } from '../../services/custom-pricing-config.service';
import { SagCustomPricingStorageService } from '../../services/custom-pricing-storage.service';

@Component({
    selector: 'sag-custom-pricing-header-actions',
    templateUrl: './custom-pricing-header-actions.component.html'
})

export class SagCustomPricingHeaderActionsComponent implements OnInit {
    @Input() editable = false;
    @Input() toggleable = true;

    @Output() customPriceChange = new EventEmitter<CustomPrice>();

    customPriciable = false;
    isCustomPriceShown = false;

    private subs = new SubSink();

    customPriceOptions = [{
        type: 'UVPE',
        brand: '',
        label: 'CUSTOM_PRICE.UVPE'
    }, {
        type: 'OEP',
        brand: '',
        label: 'CUSTOM_PRICE.OEP'
    }];

    constructor(
        private config: SagCustomPricingConfigService,
        private storage: SagCustomPricingStorageService
    ) { }

    ngOnInit() {
        this.customPriciable = this.config.libUserSetting.priceDisplayChanged;
        this.isCustomPriceShown = this.storage.isCustomPriceShown;
        this.subs.sink = this.storage.isCustomPriceShown$.subscribe(value => {
            this.isCustomPriceShown = value;
        });
    }

    onCustomPriceChange(displayedPrice: CustomPrice) {
        this.customPriceChange.emit(displayedPrice);
    }

    onOffCustomPrice() {
        this.storage.isCustomPriceShown = !this.storage.isCustomPriceShown;
    }
}
