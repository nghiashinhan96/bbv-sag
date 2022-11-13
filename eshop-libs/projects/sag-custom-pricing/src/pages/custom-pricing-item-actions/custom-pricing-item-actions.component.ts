import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { SubSink } from 'subsink';
import { GrossPriceType } from '../../enums/custom-pricing.enums';
import { CustomPrice } from '../../interfaces/custom-pricing.interface';
import { SagCustomPricingConfigService } from '../../services/custom-pricing-config.service';
import { SagCustomPricingStorageService } from '../../services/custom-pricing-storage.service';

@Component({
    selector: 'sag-custom-pricing-item-actions',
    templateUrl: './custom-pricing-item-actions.component.html'
})

export class SagCustomPricingItemActionsComponent implements OnInit, OnChanges {
    @Input() article: any;
    @Input() editable: boolean;
    @Input() priceType: string;
    @Input() customPriceBrand: any;
    @Input() isShow: boolean;

    @Output() customPriceChange = new EventEmitter<CustomPrice>();

    customPriciable = false;
    isCustomPriceShown = false;
    isAllowedPriceSelection = false;

    private subs = new SubSink();

    constructor(
        private config: SagCustomPricingConfigService,
        private storage: SagCustomPricingStorageService
    ) {

    }

    ngOnInit() {
        this.customPriciable = this.config.libUserSetting.priceDisplayChanged;
        this.isCustomPriceShown = this.storage.isCustomPriceShown;
        this.isAllowedPriceSelection = this.checkAllowedPriceSelection(this.article);
        this.subs.sink = this.storage.isCustomPriceShown$.subscribe(value => {
            this.isCustomPriceShown = value;
        });
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.article && !changes.article.firstChange) {
            this.isAllowedPriceSelection = this.checkAllowedPriceSelection(this.article);
        }
    }

    private checkAllowedPriceSelection(article) {
        if (!article || !article.price) {
            return false;
        }
        const priceType = article.price.price && article.price.price.type;
        return priceType === GrossPriceType.UVPE.toString() || priceType === GrossPriceType.OEP.toString();
    }

    onCustomPriceChange(displayedPrice: CustomPrice) {
        this.customPriceChange.emit(displayedPrice);
    }
}
