import { MAX_QUANTITY } from "../currency.constant";
export class CurrencyUtil {
    public static getMaxQuantityValid(quantity: number, qtyMultiple: number) {
        let validMaxQuantityInCaseQuantityMultiple: number = Math.floor(MAX_QUANTITY / qtyMultiple) * qtyMultiple;

        if (quantity === 0) {
            if (qtyMultiple <= 1) {
                return MAX_QUANTITY;
            } else {
                return validMaxQuantityInCaseQuantityMultiple;
            }
        }
        
        if (qtyMultiple <= 1) {
            return quantity <= MAX_QUANTITY ? quantity : MAX_QUANTITY;
        }
        
        return quantity <= MAX_QUANTITY ? quantity : validMaxQuantityInCaseQuantityMultiple;

    }
    public static roundHalfEvenTo2digits(num: number) {
        if (!num) {
            return num;
        }
        const tmp = num * 100;
        const floor = Math.floor(tmp);
        const ceil = Math.ceil(tmp);
        if (tmp - floor < 0.5) {
            return floor / 100;
        }
        if (ceil - tmp < 0.5) {
            return ceil / 100;
        }
        return (floor % 2 === 0) ? floor / 100 : ceil / 100;
    }
    
    public static round2Digits(num: number, useThousandDelimiter = true) {
        const roundedNumber = this.roundHalfEvenTo2digits(num);
        const formatStr = roundedNumber.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
        return useThousandDelimiter ? formatStr.replace(/,/g, '\'') :  formatStr.replace(/,/g, '');
    }

    public static formatCommaNumber(num: string) {
        const newNum = num.replace(/,/g, '.');
        return parseFloat(newNum);
    }
}