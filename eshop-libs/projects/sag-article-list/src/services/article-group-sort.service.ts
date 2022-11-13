import { Injectable } from '@angular/core';
import { uniq } from 'lodash';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { first } from 'rxjs/internal/operators/first';

import {
    ArticlesService,
    ArticleModel,
    CategoryModel,
    CateBrand,
    AvailabilityUtil,
    ArticleUtil
} from 'sag-article-detail';

import { NO_BRAND, BRAND, NUMBER_ITEMS_REQUEST_AVAILABILITY, DF_BRAND_PRIOR_VALUE } from '../consts/article-list.const';
import { ArticleSortUtil } from '../utils/article-sort.util';
import { SortedArticleGroup } from '../models/sorted-article-group.model';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { ArticleListConfigService } from './article-list-config.service';
import { AffiliateUtil } from 'sag-common';

@Injectable()
export class ArticleGroupSortService {
    isCz = AffiliateUtil.isCz(this.config.affiliate);
    isEhCz = AffiliateUtil.isEhCz9(this.config.affiliate);
    isAffiliateApplyDeliverableStock = AffiliateUtil.isAffiliateApplyDeliverableStock(this.config.affiliate);

    private disabledBrandPriority = false;

    get disabledBrandPriorityAvailability() {
        return this.disabledBrandPriority;
    }

    set disabledBrandPriorityAvailability(value) {
        this.disabledBrandPriority = value;
    }

    constructor(
        private articleService: ArticlesService,
        private config: ArticleListConfigService
    ) { }

    async filterBrandAndSortByPriority(articles: ArticleModel[], category: CategoryModel): Promise<SortedArticleGroup> {
        // reset data;
        // articles.forEach(art => {
        //     delete art.availabilities;
        // });

        let categoryBrands = category.getCateBrands() || [];
        categoryBrands = this.filterBrand(categoryBrands);
        const { articlesWithBrand, articlesWithoutBrand, defaultPrioArticles } =
            this.groupArticlesByBrandAndWithoutBrand(articles, categoryBrands, category);
        if (articlesWithBrand.length || defaultPrioArticles.length) {
            let { displayedArticles, nonDisplayedArticles } = await this.sortAndGetDisplayedArticles(articlesWithBrand);
            if (displayedArticles.length) {
                displayedArticles = [...displayedArticles, ...defaultPrioArticles];
                nonDisplayedArticles = [...nonDisplayedArticles, ...articlesWithoutBrand];
            } else {
                if (articlesWithoutBrand.length) {
                    await this.requestErpStock([...articlesWithoutBrand]);
                    const sortedData = await this.groupBySupplierSortByBestand(articlesWithoutBrand);
                    if (sortedData.displayedArticles.length || defaultPrioArticles.length) {
                        displayedArticles = [...sortedData.displayedArticles, ...defaultPrioArticles];
                        nonDisplayedArticles = [...nonDisplayedArticles, ...sortedData.nonDisplayedArticles];
                    } else {
                        return this.fallBackToDefaultRule(articlesWithBrand, articlesWithoutBrand, defaultPrioArticles);
                    }
                } else {
                    return this.fallBackToDefaultRule(articlesWithBrand, articlesWithoutBrand, defaultPrioArticles);
                }
            }
            return this.brandArticles(displayedArticles, nonDisplayedArticles);
        } else {
            await this.requestErpStock([...articlesWithoutBrand]);
            const sortedData = await this.groupBySupplierSortByBestand(articlesWithoutBrand);
            const displayedArticles = sortedData.displayedArticles;
            const nonDisplayedArticles = sortedData.nonDisplayedArticles;
            if (displayedArticles.length > 0) {
                return this.brandArticles(displayedArticles, nonDisplayedArticles);
            } else {
                return this.nonBrandArticles(nonDisplayedArticles);
            }
        }
    }

    private filterBrand(allBrands) {
        let brands = allBrands.filter(brand => !!brand.collection);
        if (brands.length === 0) {
            brands = allBrands.filter(brand => !!brand.affiliate);
        }

        return brands;
    }

    private async fallBackToDefaultRule(articlesWithBrand, articlesWithoutBrand, defaultPrioArticles) {
        const sorted = await this.sortAndGetDisplayedArticles(articlesWithBrand, false);
        if (sorted.displayedArticles.length || defaultPrioArticles.length) {
            const displayedArticles = [...sorted.displayedArticles, ...defaultPrioArticles];
            const nonDisplayedArticles = [...sorted.nonDisplayedArticles, ...articlesWithoutBrand];
            return this.brandArticles(displayedArticles, nonDisplayedArticles);
        } else {
            return this.nonBrandArticles([...sorted.nonDisplayedArticles, ...defaultPrioArticles, ...articlesWithoutBrand]);
        }
    }

    private nonBrandArticles(displayedArticles = [], nonDisplayedArticles = []) {
        return {
            displayedArticles,
            nonDisplayedArticles,
            type: NO_BRAND
        } as SortedArticleGroup;
    }

    private brandArticles(displayedArticles = [], nonDisplayedArticles = []) {
        return {
            displayedArticles,
            nonDisplayedArticles,
            type: BRAND
        } as SortedArticleGroup;
    }

    /**
     * groupArticlesByBrandAndWithoutBrand: Split to 2 array {articlesWithBrand, articlesWithoutBrand}
     */
    private groupArticlesByBrandAndWithoutBrand(articles: ArticleModel[], categoryBrands: CateBrand[], category: CategoryModel) {

        const articlesWithBrand = [];
        const articlesWithoutBrand = [];
        const defaultPrioArticles = [];
        // group article by brand and without brand
        articles.forEach(article => {
            const brands = categoryBrands.filter(element => element.gaId === article.gaID);
            if (brands && brands.length > 0) {
                const brand = brands.find(element => element.brand.toString() === article.dlnrId);
                if (!!brand) {
                    article.prio = brand.prio;
                    article.subPrio = brand.subPrio;
                    articlesWithBrand.push(article);
                } else {
                    articlesWithoutBrand.push(article);
                }
                // articleWithBrandArtIds.push(article.idPim);
            } else if (article.isBelongDefaultGroup(category)) {
                article.prio = DF_BRAND_PRIOR_VALUE;
                defaultPrioArticles.push(article);
            } else {
                articlesWithoutBrand.push(article);
            }
        });
        return {
            articlesWithBrand,
            articlesWithoutBrand,
            defaultPrioArticles
        };
    }

    private async groupBySupplierSortByBestand(articles: ArticleModel[]) {
        const groups = ArticleSortUtil.groupBy(articles, article => [article.supplier]);
        groups.forEach(g => {
            g.sort((item1, item2) => ArticleSortUtil.sortByBestand(item1, item2, this.isAffiliateApplyDeliverableStock));
        });
        groups.sort((g1, g2) => ArticleSortUtil.sortByBestand(g1[0], g2[0], this.isAffiliateApplyDeliverableStock));
        const topArticles = [];
        const hidenArticles = [];

        groups.forEach((g) => {
            g.forEach(art => {
                const hasAvail = AvailabilityUtil.hasAvailByStock(art, this.isAffiliateApplyDeliverableStock) || this.disabledBrandPriorityAvailability;
                if (!!hasAvail) {
                    topArticles.push(art);
                } else {
                    hidenArticles.push(art);
                }
            });
        });
        return {
            displayedArticles: topArticles,
            nonDisplayedArticles: hidenArticles
        };
    }
    /**
     * Split to 2 group {displayedArticles, nonDisplayedArticles}
     * These list will be grouped by supplier then sort by prio
     */
    private async sortAndGetDisplayedArticles(articles, byAvail = true) {
        if (articles.length === 0) {
            return {
                displayedArticles: [],
                nonDisplayedArticles: []
            };
        }
        // group & sort data
        const groups = ArticleSortUtil.groupBy(articles, article => [article.prio]);
        groups.sort((g1, g2) => ArticleSortUtil.sortByPrioAndBestand(g1[0], g2[0], this.isAffiliateApplyDeliverableStock));
        // tear down to get avail
        let lowerArticles = [];
        let topArticles = [];
        if (byAvail) {
            // get topPrio -1 -> no avail -> no top prio
            const { index, displayedArticles, hidenArticles } = await this.requestTopPrioAvail(groups, 0);
            if (index !== -1) {
                topArticles = displayedArticles;
                lowerArticles = hidenArticles;
                // The case for other groups in the big group to be bypassed by requestTopPrioAvail
                groups.forEach((g, i) => {
                    if (i !== index) {
                        lowerArticles = [...lowerArticles, ...g];
                    }
                });
            } else {
                groups.forEach(g => {
                    lowerArticles = [...lowerArticles, ...g];
                });
            }
        } else {
            const topPriorityValue = groups[0][0].prio;
            groups.forEach(g => {
                if (g[0].prio === topPriorityValue) {
                    topArticles = [...topArticles, ...g];
                } else {
                    lowerArticles = [...lowerArticles, ...g];
                }
            });
        }

        const { hasAvailArticles, noAvailArticles } = this.groupBySupplierSortByPrioAndStock(topArticles);
        lowerArticles = [...lowerArticles, ...noAvailArticles];
        return {
            displayedArticles: hasAvailArticles,
            nonDisplayedArticles: lowerArticles
        };
    }

    private groupBySupplierSortByPrioAndStock(articles) {
        let noAvailArticles = [];
        let hasAvailArticles = [];
        const groups = ArticleSortUtil.groupBy(articles, article => [article.supplier]);
        groups.forEach(g => {
            g.sort((item1, item2) => ArticleSortUtil.sortByPrioAndBestand(item1, item2, this.isAffiliateApplyDeliverableStock));
        });
        groups.sort((g1, g2) => ArticleSortUtil.sortByPrioAndBestand(g1[0], g2[0], this.isAffiliateApplyDeliverableStock));
        groups.forEach(g => {
            const hasAvail = g.some(art => AvailabilityUtil.hasAvailByStock(art, this.isAffiliateApplyDeliverableStock) || this.disabledBrandPriorityAvailability);
            if (hasAvail) {
                hasAvailArticles = [...hasAvailArticles, ...g];
            } else {
                noAvailArticles = [...noAvailArticles, ...g];
            }
        });
        return {
            hasAvailArticles,
            noAvailArticles
        };
    }
    /**
     * Grouped list by prio and supplier then sorted by prio > vail > stock
     */
     sortBySubPrio(articles) {
        // prepare avail sort state
        articles.forEach(article => {
            const availabilities = article.availabilities;

            if (!availabilities) {
                article.availSortState = -1;
            } else {
                AvailabilityUtil.sortAvailWithLatestTime(availabilities);
                article.availSortState = AvailabilityUtil.intialAvailSortState(availabilities, true, this.isCz);
            }
        });

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
                    topArticles = [...topArticles, ...this.sortByPrioAndAvailAndGroupSupplier(group)];
                });
            }
        });

        return [...topArticles, ...this.sortByPrioAndAvailAndGroupSupplier(otherArticles)];
    }

    private sortByPrioAndAvailAndGroupSupplier(articles) {
        const groups = ArticleSortUtil.groupBy(articles, article => [article.supplier]);
        groups.forEach(g => {
            if (this.isCz) {
                g.sort((item1, item2) => ArticleSortUtil.comparatorAvailAndPrioAndStockCz(item1, item2));
            } else {
                g.sort((item1, item2) => ArticleSortUtil.comparatorAvailAndPrioAndStock(item1, item2));
            }
        });
        if (this.isCz) {
            groups.sort((g1, g2) => ArticleSortUtil.comparatorPrioAndStockCz(g1[0], g2[0]));
            groups.sort((g1, g2) => ArticleSortUtil.comparatorPrioHasStockCz(g1[0], g2[0]));
        } else {
            groups.sort((g1, g2) => ArticleSortUtil.comparatorAvailAndPrioAndStock(g1[0], g2[0]));
            groups.sort((g1, g2) => ArticleSortUtil.comparatorAvailAndPrioHasStock(g1[0], g2[0]));
        }
        
        let result = [];
        groups.forEach(g => {
            result = [...result, ...g];
        });
        return result;
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
            g.sort((item1, item2) => ArticleSortUtil.comparatorStockAndPrio(item1, item2, this.isAffiliateApplyDeliverableStock));
        });
        groups.sort((g1, g2) => ArticleSortUtil.comparatorStockAndPrio(g1[0], g2[0], this.isAffiliateApplyDeliverableStock));
        
        let result = [];
        groups.forEach(g => {
            result = [...result, ...g];
        });
        return result;
    }

    private async requestTopPrioAvail(groups, gIndex) {
        if (gIndex === groups.length) {
            return { index: -1 };
        }
        const group = groups[gIndex];
        let topArticles = [];
        let lowerArticles = [];
        const suppliers: ArticleModel[][] = ArticleSortUtil.groupBy(group, article => [article.supplier]);
        const arts = group.length > 0 ? [group] : [];
        await this.requestStock(arts, 0, true);
        suppliers.forEach(g => {
            const hasAvail = g.find(art => AvailabilityUtil.hasAvailByStock(art, this.isAffiliateApplyDeliverableStock) || this.disabledBrandPriorityAvailability);
            if (!!hasAvail) {
                topArticles = [...topArticles, ...g];
            } else {
                lowerArticles = [...lowerArticles, ...g];
            }
        });
        if (topArticles.length > 0) {
            return {
                index: gIndex,
                displayedArticles: topArticles,
                hidenArticles: lowerArticles
            };
        }

        gIndex += 1;
        return this.requestTopPrioAvail(groups, gIndex);
    }

    private async requestStock(groups: ArticleModel[][], gIndex: number, isAll = false) {
        if (gIndex === groups.length) {
            return -1;
        }
        const requestStockItems = (groups[gIndex] || []).filter(art => !art.pseudo);
        if (requestStockItems[0] && requestStockItems[0].prio === DF_BRAND_PRIOR_VALUE) {
            gIndex += 1;
            return this.requestStock(groups, gIndex, isAll);
        }
        const requests = [];
        const erpRequest = {
            stockRequested: true
        };
        const requestItemsBatch = requestStockItems.filter(item => !item.stockRequested);
        if (requestItemsBatch.length) {
            requests.push(this.articleService.getArticlesInfoWithBatch(requestItemsBatch, requestItemsBatch.length, erpRequest));
        }
        if (requests.length) {
            let allInfos = [];
            try {
                const respones = await forkJoin(requests).pipe(first(), catchError(() => of([]))).toPromise();

                respones.forEach(res => {
                    const infos = this.articleService.getDisplayedInfo(res.items);
                    allInfos = [...allInfos, ...infos];
                });
                // assign avail to article:
                groups[gIndex].forEach((article: ArticleModel) => {
                    // assign stock to article for sorting purpose;
                    const artInfo = allInfos.find(info => info.key === article.pimId);
                    if (artInfo) {
                        article.stock = artInfo.stock;
                        article.totalAxStock = artInfo.totalAxStock;
                        article.stockRequested = true;
                        article.deliverableStock = artInfo.deliverableStock;
                    }
                });
                if (!isAll) {
                    const hasAvail = allInfos.find(art => AvailabilityUtil.hasAvailByStock(art, this.isAffiliateApplyDeliverableStock) || this.disabledBrandPriorityAvailability);
                    if (!!hasAvail) {
                        return (gIndex);
                    }
                }
                gIndex += 1;
                return this.requestStock(groups, gIndex, isAll);
            } catch (error) {
                gIndex += 1;
                return this.requestStock(groups, gIndex, isAll);
            }
        } else {
            gIndex += 1;
            return this.requestStock(groups, gIndex, isAll);
        }
    }

    async requestErpStock(articles: ArticleModel[], callback?) {
        if (this.isCz || this.isEhCz) {
            return;
        }
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

    assignArticleErpInfo(article: ArticleModel, artInfo: any, erpRequest: any, assignAvail = true) {
        ArticleUtil.assignArticleErpInfo(article, artInfo, erpRequest, assignAvail, this.isEhCz);
    }
}
