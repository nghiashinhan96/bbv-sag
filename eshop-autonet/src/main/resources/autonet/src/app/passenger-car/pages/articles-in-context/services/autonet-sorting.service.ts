import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { catchError, first } from 'rxjs/operators';
import { ArticleModel, ArticlesService, CategoryModel } from 'sag-article-detail';
import { ArticleSortUtil, NO_BRAND } from 'sag-article-list';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';

@Injectable()
export class AutonetSortingService {

    constructor(
        private articleService: ArticlesService
    ) { }

    async filterBrandAndSortByPriority(articles: ArticleModel[], category: CategoryModel): Promise<any> {
        const categoryBrands = this.getCateBrands(category);
        articles.forEach(article => {
            const brands = categoryBrands.filter(element => element.gaId === article.gaID);
            if (brands && brands.length > 0) {
                const brand = brands.find(element => element.brand.toString() === article.dlnrId);
                if (!!brand) {
                    article.prio = brand.prio;
                    article.subPrio = brand.subPrio;
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
        /**
         * Debug only
         */
        // console.log(displayedArticles.map((a: ArticleModel) => {
        //     return { id: a.artDesc, prio: a.prio };
        // }));
        return {
            displayedArticles,
            nonDisplayedArticles: [],
            type: NO_BRAND
        };
    }

    sortBySubPrio(articles: ArticleModel[]) {
        // overrite service
        // Autonet does not sort by subPrio
        return [...articles];
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
            const sub = (genArt.sorts || []).reduce((subBrands, item) => {
                return [...subBrands, ...item.brands || []];
            }, []);
            return [...brands, ...sub];
        }, []);
    }

    assignArticleErpInfo(article: any, artInfo: any) {
        if (!artInfo) {
            return;
        }
        if (!article.autonetRequested) {
            article.autonetInfos = artInfo.autonetInfos;
            article.autonetRequested = true;
            const convertedArt = AppHelperUtil.convertAutonetData([article])[0];
            Object.assign(article, convertedArt);
        }
    }
}
