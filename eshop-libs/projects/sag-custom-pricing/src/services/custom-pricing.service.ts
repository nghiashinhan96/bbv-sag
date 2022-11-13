import { CustomPrice, ArticleCustomPrice, CustomPriceUpdate } from '../interfaces/custom-pricing.interface';
import { HttpClient } from '@angular/common/http';
import { GrossPriceType } from '../enums/custom-pricing.enums';
import { first } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { SagCustomPricingStorageService } from './custom-pricing-storage.service';
import { Observable } from 'rxjs';
import { SagCustomPricingConfigService } from './custom-pricing-config.service';

@Injectable()
export class SagCustomPricingService {

    constructor(
        private http: HttpClient,
        private config: SagCustomPricingConfigService,
        private storage: SagCustomPricingStorageService
    ) { }

    async updateCustomPriceByBrand(
        selectedPrice: CustomPrice,
        articles: any[],
        { brand, brandId }: { brand: string, brandId: number }
    ): Promise<ArticleCustomPrice[]> {
        const body = (articles || []).map(art => {
            return {
                articleId: art.pimId,
                amount: art.amountNumber,
                brands: [{
                    brand,
                    brandId
                }]
            };
        });
        let prices = [];
        if (selectedPrice.type === GrossPriceType.OEP) {
            prices = await this.getPrices(body);
        } else {
            prices = body.map(item => {
                return {
                    articleId: item.articleId,
                    displayedPrice: null
                };
            });
        }
        return prices;
    }

    private async getPrices(body) {
        let response: any = [];
        try {
            response = await this.getCustomPrice(body).pipe(first()).toPromise();
        } catch (ex) {
            // Could not load prices
        }
        return (response || []).map(item => {
            const result = {
                articleId: item.articleId,
                displayedPrice: null
            };
            if (item && item.prices && item.prices.length > 0) {
                result.displayedPrice = item && item.prices && item.prices[0];
            }
            return result;
        });
    }


    getCustomPrice(body): Observable<any> {
        const url = `${this.config.baseUrl}articles/display-prices`;
        return this.http.post(url, body);
    }

    updateCustomPriceInBasket(body: CustomPriceUpdate[]): Observable<any> {
        const url = `${this.config.baseUrl}cart/displayed-price/update?shopType=${this.config.shopType}`;
        return this.http.post(url, body);
    }
}
