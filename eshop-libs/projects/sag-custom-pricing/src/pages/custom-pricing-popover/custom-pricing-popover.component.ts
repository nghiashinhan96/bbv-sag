import { Component, OnInit, Input, Renderer2, HostListener, ElementRef, OnDestroy, Output, EventEmitter } from '@angular/core';
import { CustomPrice } from '../../interfaces/custom-pricing.interface';
import { SagCustomPricingService } from '../../services/custom-pricing.service';
import { Subscription } from 'rxjs';

@Component({
    selector: 'sag-custom-pricing-popover',
    templateUrl: './custom-pricing-popover.component.html'
})

export class SagCustomPricingPopoverComponent implements OnInit, OnDestroy {
    @Input() article: any;
    @Input() popupRef: any;
    @Input() brand: {
        brand: string,
        brandId: string
    };
    @Output() selected = new EventEmitter();
    @Input() list: CustomPrice[];

    private documentListener: any;
    private priceSubscription: Subscription;

    constructor(
        private renderer: Renderer2,
        private ref: ElementRef,
        private customPriceService: SagCustomPricingService
    ) { }

    @HostListener('click', ['$event'])
    onClickElement(event: any) {
        const checkClick = this.ref.nativeElement.contains(event.target);
        if (checkClick) {
            // do not excute document click;
            event.stopPropagation();
        }
    }

    ngOnInit() {
        if (!this.list) {
            // call to get data
            const uvpePrce: CustomPrice = {
                type: 'UVPE',
                brand: '',
                price: this.article.price && this.article.price.price && this.article.price.price.grossPrice || null,
            };
            const requestBody = [{
                articleId: this.article.pimId,
                amount: this.article.amountNumber,
                brands: this.brand && [this.brand] || []
            }];
            this.customPriceService.getCustomPrice(requestBody)
                .subscribe(res => {
                    if (res && res.length > 0) {
                        this.list = [uvpePrce, ...(res[0].prices || [])];
                    } else {
                        this.list = [uvpePrce];
                    }
                }, error => {
                    this.list = [uvpePrce];
                });
        }
        // Because the article input can be accepted any type, so we should new article model for article input
        setTimeout(() => {
            this.bindDocumentEditListener();
        });
    }

    ngOnDestroy(): void {
        if (this.priceSubscription) {
            this.priceSubscription.unsubscribe();
        }
    }

    onSelectPrice(item) {
        if (this.article && item.type === 'UVPE') {
            this.selected.emit(null);
        } else {
            this.selected.emit(item);
        }
        this.closePopup();
    }

    closePopup() {
        this.unbindDocumentEditListener();
        this.popupRef.hide();
    }

    private bindDocumentEditListener() {
        if (!this.documentListener) {
            this.documentListener = this.renderer.listen('document', 'click', () => {
                this.unbindDocumentEditListener();
                this.popupRef.hide();
            });
        }
    }

    private unbindDocumentEditListener() {
        if (this.documentListener) {
            this.documentListener();
            this.documentListener = null;
        }
    }
}
