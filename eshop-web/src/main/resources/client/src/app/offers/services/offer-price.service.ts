export class OfferPriceService {
    public static getTotalExclVAT(totalArticle, totalWork, totalDiscount) {
        return totalArticle + totalWork + totalDiscount;
    }

    public static getTotalVAT(totalExclVAT, vat) {
        return totalExclVAT * vat;
    }

    public static getTotalInclVAT(totalExclVAT, totalVAT) {
        return totalExclVAT + totalVAT;
    }

    public static getTotalGrossPriceItem(grossPrice, quantity) {
        return grossPrice * quantity;
    }
}
