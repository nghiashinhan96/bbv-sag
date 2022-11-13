import { Injectable } from '@angular/core';
import { ArticleBasketModel } from 'sag-article-detail';
import { SagArticleListIntegrationInterface } from 'sag-article-list/interfaces/article-list-integration.interface';
import { ArticleShoppingBasketService } from './article-shopping-basket.service';

@Injectable({
  providedIn: 'root'
})
export class ArticleListIntegrationService implements SagArticleListIntegrationInterface {

  constructor(
    private articleShoppingBasketService: ArticleShoppingBasketService
  ) { }

  getArticleListAreAddedToCart(data: ArticleBasketModel[]): Promise<any[]> {
    return this.articleShoppingBasketService.getArticleListAreAddedToCart(data);
  }

}
