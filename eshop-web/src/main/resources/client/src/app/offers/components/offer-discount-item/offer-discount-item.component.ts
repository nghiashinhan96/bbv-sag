import { Component, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { OfferPositionT } from '../../models/offer-position.model';
import { OfferDiscountService } from '../../services/offer-discount.service';

@Component({
    selector: 'connect-offer-discount-item',
    templateUrl: './offer-discount-item.component.html',
    styleUrls: ['../../offers.component.scss', './offer-discount-item.component.scss']
})

export class OfferDiscountItemComponent implements OnChanges {
    @Input() items: Array<OfferPositionT>;
    @Output() eventDeleteItem = new EventEmitter();

    constructor(private offerDiscoutService: OfferDiscountService) {

    }

    ngOnChanges(): void {
        this.items = JSON.parse(JSON.stringify(this.items));
        if (!this.items) {
            this.items = [];
            return;
        }

        this.items.map(item => item as OfferPositionT).forEach(item => {
            item.discountDescription = this.offerDiscoutService.buildDiscountDescriptionAsString(item);
            item.discountValue = this.offerDiscoutService.buildDiscountAmountWithSignAsString(item);
        });
    }

    deleteItem(item: OfferPositionT) {
        delete item.discountDescription;
        delete item.discountValue;
        this.eventDeleteItem.emit(item);
    }
}
