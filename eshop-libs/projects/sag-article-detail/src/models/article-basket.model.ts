import { CategoryModel } from './category.model';
import { ArticleModel } from './article.model';

export class ArticleBasketModel {
    action: 'LOADED' | 'ADD' | 'INCREASE' | 'DESCREASE' | 'REMOVE' | 'CUSTOM_PRICE_CHANGE';
    pimId: string;
    amount?: number;
    artnr?: string;
    stock?: number;
    category?: CategoryModel;
    vehicle?: any;
    cartKey?: string;
    article?: ArticleModel;
    basket?: any;
    uuid?: string;
    isAccessoryItem?: boolean;
    isPartsItem?: boolean;
    index?: number;
    rootModalName?: string;
    callback?: (data: any) => void;
    addToCart?: (amount?: number) => void;
    confirm?: (artid: string) => void;
    done?: (artid: string) => void;
}
