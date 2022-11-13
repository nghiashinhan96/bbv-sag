import { Component, OnInit, Input, Output, OnDestroy, EventEmitter } from '@angular/core';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { Observable, Subscription } from 'rxjs';

@Component({
    selector: 'connect-customer-ref-text',
    templateUrl: './customer-ref-text.component.html',
    styleUrls: ['./customer-ref-text.component.scss']
})
export class CustomerRefTextComponent implements OnInit, OnDestroy {
    @Input() keyStore: number;
    @Input() disabled: boolean;
    @Input() maxLength = 60;
    @Input() cleanUp$: Observable<any>;

    @Input() refText: string;
    @Output() refTextChange: EventEmitter<string> = new EventEmitter<string>();

    maxLengthMsg = 'SHOPPING_BASKET.REFERENCE_MAX_60_LENGTH';

    private subscription: Subscription;
    constructor(
        private appStorage: AppStorageService
    ) { }

    ngOnInit() {
        this.maxLengthMsg = `SHOPPING_BASKET.REFERENCE_MAX_${this.maxLength}_LENGTH`;

        if (this.keyStore) {
            this.subscription = this.appStorage.observeRefText().subscribe(res => {
                this.refText = res && res[this.keyStore] || '';
            });
            const refTextObj = this.appStorage.refText;
            this.refText = refTextObj && refTextObj[this.keyStore] || '';
        }
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    updateCustomerRefText() {
        if (this.keyStore) {
            this.appStorage.refText = { [this.keyStore]: this.refText };
        } else {
            this.refTextChange.emit(this.refText);
        }
    }
}
