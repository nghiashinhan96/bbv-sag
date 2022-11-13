export interface BasketPriceSummaryModel {
    subTotal: number;
    vatTotal: number;
    totalExclVat: number;
    total: number;
    existedCouponCode: string;
    discount: number;
    subTotalExlVat: number;
    subTotalWithVat: number;
}
