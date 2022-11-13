import { Injectable } from '@angular/core';
import { ArticleModel, CategoryModel, ArticleUtil } from 'sag-article-detail';
import { ArticleSortUtil, BRAND } from 'sag-article-list';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Injectable()
export class WspSortingService {
  isEhCz = AffiliateUtil.isEhCz(environment.affiliate);

  constructor() { }

  async filterBrandAndSortByPriority(articles: ArticleModel[], category: CategoryModel): Promise<any> {
    const categoryBrands = this.getCateBrands(category);
    articles.forEach(article => {
      const brands = categoryBrands.filter(element => element.gaId === article.gaID);
      if (brands && brands.length > 0) {
        const brand = brands.find(element => element.brand.toString() === article.dlnrId);
        if (!!brand) {
          article.prio = brand.prio;
        }
      }
    });
    const groups = ArticleSortUtil.groupBy(articles, article => [article.prio]);
    groups.forEach(g => {
      g.sort((item1, item2) => ArticleSortUtil.sortByBestand(item1, item2));
    });
    groups.sort((g1, g2) => ArticleSortUtil.sortByPrioAndBestand(g1[0], g2[0]));
    groups.sort((g1, g2) => ArticleSortUtil.sortByPrioHasBestand(g1[0], g2[0]));
    const displayedArticles = groups.reduce((arts, g) => {
      return [...arts, ...this.groupBySupplierSortByPrioAndStock(g)];
    }, []);
    return {
      displayedArticles,
      nonDisplayedArticles: [],
      type: BRAND
    };
  }

  private groupBySupplierSortByPrioAndStock(articles) {
    const groups = ArticleSortUtil.groupBy(articles, article => [article.supplier]);
    groups.forEach(g => {
      g.sort((item1, item2) => ArticleSortUtil.sortByPrioAndBestand(item1, item2));
    });
    groups.sort((g1, g2) => ArticleSortUtil.sortByPrioAndBestand(g1[0], g2[0]));
    groups.sort((g1, g2) => {
      return g1[0].supplier > g2[0].supplier ? 1 : -1;
    });
    return groups.reduce((arts, g) => {
      return [...arts, ...g];
    }, []);
  }

  private getCateBrands(cate: any) {
    return (cate.genArts || []).reduce((brands, genArt, i) => {
      return [...brands, ...genArt.brands];
    }, []);
  }

  assignArticleErpInfo(article: ArticleModel, artInfo: any, erpRequest: any, assignAvail = true) {
    ArticleUtil.assignArticleErpInfo(article, artInfo, erpRequest, assignAvail, this.isEhCz);
  }
}
