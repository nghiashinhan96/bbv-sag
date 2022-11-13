import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CurrencyUtil } from '../../public-api';
import {
    SAG_CURRENCY_INPUT_DEFAULT_MODE,
    SAG_CURRENCY_INPUT_HORIZONTAL_MODE,
    SAG_CURRENCY_FORMATTED_MODE
} from '../../currency.constant';
import { SagCurrencySettingModel } from '../../models/currency-setting.model';

@Component({
    selector: 'sag-currency-amount-input',
    templateUrl: './amount-input.component.html',
    styleUrls: ['./amount-input.component.scss']
})
export class SagCurrencyAmountInputComponent implements OnInit, OnChanges {
    @Input() amountNumber: number = null;
    @Input() salesQuantity: number;
    @Input() qtyMultiple: number;
    @Input() amountDisabled: boolean;
    @Input() mode = SAG_CURRENCY_INPUT_DEFAULT_MODE;
    @Input() min = 0;
    @Input() max = 0;
    @Output() amountNumberChange = new EventEmitter<number>();
    // @Output() amountFocusOut = new EventEmitter<number>();
    // @Output() amountKeyEnter = new EventEmitter<number>();
    // @Output() amountIncrease = new EventEmitter<number>();
    // @Output() amountDecrease = new EventEmitter<number>();



    isBlurred: boolean;
    inputHorizontalMode = SAG_CURRENCY_INPUT_HORIZONTAL_MODE;
    numberFormatMode = SAG_CURRENCY_FORMATTED_MODE;
    numberFormatSetting = {
        digits: 0
    } as SagCurrencySettingModel;
    private oldVal;
    constructor() { }

    ngOnInit(): void { }

    ngOnChanges(change: SimpleChanges) {
        if(change.amountNumber && !change.amountNumber.firstChange) {
            this.amountNumber = CurrencyUtil.getMaxQuantityValid(change.amountNumber.currentValue, this.qtyMultiple);
        }
    }

    amountFocusIn(event) {
        this.oldVal = this.amountNumber;
        if (!this.amountNumber) {
            this.amountNumber = this.salesQuantity;
        }

        setTimeout(() => {
            if (event && event.target) {
                event.target.select();
            }
        }, 0)
    }

    emitAmountFocusOut(event) {
        if (this.isBlurred) {
            this.isBlurred = false;
            return;
        }
        const amount = this.correctAmountNumber(+(this.amountNumber || 0));
        const oldVal = +(this.oldVal || 0);
        const isValidRange = (this.min === 0 || amount >= this.min) && (this.max === 0 || amount <= this.max);
        if (amount !== oldVal) {
            if (isValidRange) {
                this.amountNumber = amount;
            } else {
                this.amountNumber = CurrencyUtil.getMaxQuantityValid(oldVal, this.qtyMultiple);
            }
        } else {
            this.amountNumber = CurrencyUtil.getMaxQuantityValid(oldVal, this.qtyMultiple);
        }
        this.amountNumberChange.emit(this.amountNumber);
    }

    emitAmountKeyEnter(event) {
        const target = event.target as HTMLElement;
        const amount = this.correctAmountNumber(+(this.amountNumber || 0));
        const oldVal = +(this.oldVal || 0);
        const isValidRange = (this.min === 0 || amount >= this.min) && (this.max === 0 || amount <= this.max);
        if (amount !== oldVal) {
            if (isValidRange) {
                this.amountNumber = amount;
                this.amountNumberChange.emit(this.amountNumber);
                this.isBlurred = true;
                target.blur();
            } else {
                this.amountNumber = CurrencyUtil.getMaxQuantityValid(oldVal, this.qtyMultiple);
            }
        } else {
            this.amountNumber = CurrencyUtil.getMaxQuantityValid(oldVal, this.qtyMultiple);
        }
    }

    increase() {
        if (!this.amountNumber || (this.max === 0 || this.amountNumber < this.max)) {
            if (!this.amountNumber) {
                this.amountNumber = this.salesQuantity;
            } else {
                this.amountNumber = this.getIncreaseAmount(+this.amountNumber, this.qtyMultiple);
            }
            if (this.amountNumber) {
                this.amountNumberChange.emit(this.amountNumber);
            }
        }
    }

    decrease() {
        if ((this.min === 0 || (!!this.amountNumber && this.amountNumber > this.min))) {
            if (!this.amountNumber) {
                this.amountNumber = this.salesQuantity;
            } else {
                const amountNumber = this.getDecreaseAmount(+this.amountNumber, this.qtyMultiple);
                if (this.amountNumber !== amountNumber) {
                    this.amountNumber = amountNumber;
                }
            }
            if (this.amountNumber) {
                this.amountNumberChange.emit(this.amountNumber);
            }
        }
    }

    private correctAmountNumber(amountNumber: number) {
        if (amountNumber < 1) {
            amountNumber = this.salesQuantity;
        } else if (amountNumber % this.qtyMultiple !== 0) {
            amountNumber = Math.ceil(amountNumber / this.qtyMultiple) * this.qtyMultiple;
        }
        return amountNumber;
    }

    private getIncreaseAmount(amount: number, qtyMultiple: number) {
        let result: number;
        if (amount % qtyMultiple === 0) {
            result = amount + qtyMultiple;
        } else {
            result = Math.ceil(amount / qtyMultiple) * qtyMultiple;
        }
        return CurrencyUtil.getMaxQuantityValid(result, qtyMultiple);
    }

    private getDecreaseAmount(amount: number, qtyMultiple: number) {
        let result: number;
        if (amount > qtyMultiple) {
            result = amount - qtyMultiple;
        } else {
            result = qtyMultiple;
        }
        return CurrencyUtil.getMaxQuantityValid(result, qtyMultiple);
    }
}
