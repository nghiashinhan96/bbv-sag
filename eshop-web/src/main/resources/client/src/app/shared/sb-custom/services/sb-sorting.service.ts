import { Injectable } from '@angular/core';
import { partition, uniq } from 'lodash';
import { of } from 'rxjs';
import { catchError, first } from 'rxjs/operators';
import { ArticleModel, CategoryModel, ArticlesService, ArticleUtil } from 'sag-article-detail';
import { ArticleSortUtil, BRAND, NO_BRAND } from 'sag-article-list';

@Injectable()
export class SbSortingService {

    constructor(
        private articleService: ArticlesService
    ) { }

    async filterBrandAndSortByPriority(articles: ArticleModel[], category: CategoryModel): Promise<any> {
        let categoryBrands = category.getCateBrands();
        categoryBrands = this.filterBrand(categoryBrands);
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

        let supplierGroup = ArticleSortUtil.groupBy(articles, (art) => [art.supplier]);
        supplierGroup = supplierGroup.map(group => {
            return group.sort((item1, item2) => this.sortByAxStock(item1, item2));
        });

        const [groupHasStock, groupNonStock] = partition(supplierGroup, g => g[0].totalAxStock > 0);

        if (groupHasStock.length === 0) {
            let displayedArticles = this.refinedGroupAndSortByPrioAndSupplier(groupNonStock);
            return {
                displayedArticles,
                nonDisplayedArticles: [],
                type: NO_BRAND
            };
        }

        const displayedArticles = this.refinedGroupAndSortByPrioAndSupplier(groupHasStock)
        const nonDisplayedArticles = this.refinedGroupAndSortByPrioAndSupplier(groupNonStock)

        return {
            displayedArticles,
            nonDisplayedArticles,
            type: BRAND
        };
    }

    refinedGroupAndSortByPrioAndSupplier(groups: [][]) {
        let articles = groups.reduce((arts, g) => {
            return [...arts, ...g];
        }, []);
        return this.groupByPrioAndSortBySupplier(articles);
    }

    groupByPrioAndSortBySupplier(articles: ArticleModel[]) {
        const groups = this.groupByPrioAndNonPrio(articles);
        groups[0] = this.sortByPrioAndSupplier(groups[0]);
        groups[1].sort((item1, item2) => { return ArticleSortUtil.comparatorGroupSupplierAndSortLocation(item1, item2, true) });
        return [...groups[0], ...groups[1]];
    }

    groupBySupplierSortBySubPrio(articles: ArticleModel[]) {
        const groups = ArticleSortUtil.groupBy(articles, article => [article.supplier]);
        groups.forEach(g => {
            g.sort((item1, item2) => item1.subPrio - item2.subPrio);
        });
        return groups.reduce((arts, g) => {
            return [...arts, ...g];
        }, []);
    }

    sortBySubPrio(articles: ArticleModel[]) {
        return articles.sort((item1, item2) => item1.subPrio - item2.subPrio);
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

    private groupByStockAndNonStock(articles) {
        return partition(articles, art => art.totalAxStock > 0);
    }

    private groupByPrioAndNonPrio(articles) {
        return partition(articles, art => !!art.prio);
    }

    private filterBrand(allBrands) {
        let brands = allBrands.filter(brand => !!brand.collection);
        if (brands.length === 0) {
            brands = allBrands.filter(brand => !!brand.affiliate);
        }

        return brands;
    }

    private sortByPrioAndSupplier(articles) {
        const groups = ArticleSortUtil.groupBy(articles, article => [article.prio]);
        groups.sort((g1, g2) => ArticleSortUtil.sortByPrio(g1[0], g2[0]));
        groups.forEach(g => {
            g.sort((item1, item2) => ArticleSortUtil.comparatorGroupSupplierAndSortLocation(item1, item2, true));
        });
        return groups.reduce((arts, g) => {
            return [...arts, ...g];
        }, []);
    }

    sortByAxStock(item1, item2) {
        return item2.totalAxStock - item1.totalAxStock;
    }

    assignArticleErpInfo(article: ArticleModel, artInfo: any, erpRequest: any, assignAvail = true) {
        ArticleUtil.assignArticleErpInfo(article, artInfo, erpRequest, assignAvail, false);
    }

    async requestErpStock(articles: ArticleModel[], callback?) {
        const erpRequest = {
            stockRequested: true
        };
        const requestStockItems = articles.filter(art => !art.pseudo && !art.stockRequested);
        if (requestStockItems.length > 0) {
            const response = await this.articleService.getArticlesInfoWithBatch(requestStockItems, requestStockItems.length, erpRequest)
                .pipe(first(), catchError(() => of([])))
                .toPromise();
            const infos = this.articleService.getDisplayedInfo(response.items);
            articles.forEach((article: ArticleModel) => {
                const artInfo = infos.find(avail => avail.key === article.pimId);
                this.assignArticleErpInfo(article, artInfo, erpRequest);
            });
            if (callback) {
                callback();
            }
        } else {
            if (callback) {
                callback();
            }
        }
    }

    sortBySubPrioAndStock(articles) {
        const prioGroups = ArticleSortUtil.groupBy(articles, article => [article.prio]);
        let otherArticles = [];
        let topArticles = [];
        prioGroups.forEach(g => {
            const subPrios = uniq(g.map(item => item.subPrio));

            if (subPrios.length === 1) {
                otherArticles = [...otherArticles, ...g];
            } else {
                g.sort((item1, item2) => item1.subPrio - item2.subPrio);
                const groupBySubPrio = ArticleSortUtil.groupBy(g, article => [article.subPrio]);

                groupBySubPrio.forEach(group => {
                    topArticles = [...topArticles, ...this.sortByPrioAndStockAndGroupSupplier(group)];
                });
            }
        });

        return [...topArticles, ...this.sortByPrioAndStockAndGroupSupplier(otherArticles)];
    }

    private sortByPrioAndStockAndGroupSupplier(articles) {
        const groups = ArticleSortUtil.groupBy(articles, article => [article.supplier]);
        groups.forEach(g => {
            g.sort((item1, item2) => ArticleSortUtil.comparatorStockAndPrio(item1, item2));
        });
        groups.sort((g1, g2) => ArticleSortUtil.comparatorStockAndPrio(g1[0], g2[0]));
        
        let result = [];
        groups.forEach(g => {
            result = [...result, ...g];
        });
        return result;
    }
}
