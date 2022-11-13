import { Injectable } from '@angular/core';
import { ArticleBasketModel } from 'sag-article-detail';
import { SagArticleListIntegrationInterface } from 'sag-article-list';

@Injectable({
  providedIn: 'root'
})
export class ArticleListIntegrationService implements SagArticleListIntegrationInterface {

constructor() { }
  getArticleListAreAddedToCart(data: ArticleBasketModel[]): Promise<any[]> {
    return Promise.resolve([]);
  }

}
