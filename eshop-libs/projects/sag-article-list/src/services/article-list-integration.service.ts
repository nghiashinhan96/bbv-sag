import { Injectable } from '@angular/core';
import { ArticleBasketModel } from 'sag-article-detail/public-api';
import { SagArticleListIntegrationInterface } from '../interfaces/article-list-integration.interface';

@Injectable()
export class SagArticleListIntegrationService implements SagArticleListIntegrationInterface {

constructor() { }
  getArticleListAreAddedToCart(data: ArticleBasketModel[]): Promise<any[]> {
    throw new Error('Method not implemented.');
  }
}
