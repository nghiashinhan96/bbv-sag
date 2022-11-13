export interface CustomPrice {
    type: 'UVPE' | 'OEP';
    brand: string;
    brandId?: string;
    price: number;
    totalPrice?: number;
    label?: string;
    priceWithVat?: number;
    totalPriceWithVat?: number;
}

export interface ArticleCustomPrice {
    articleId: string;
    displayedPrice: CustomPrice;
}

export interface CustomPriceEmitter {
    vehicleId: string;
    selectedPrice: CustomPrice;
    allSelectedPrices: ArticleCustomPrice[];
}

export interface CustomPriceUpdate {
    cartKey: string;
    displayedPrice: CustomPrice;
}
