import { Injectable } from '@angular/core';
import { ArticleModel } from '../models/article.model';
import { HttpClient } from '@angular/common/http';
import { ArticleDetailConfigService } from './article-detail-config.service';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { of } from 'rxjs/internal/observable/of';
import { SagArticleDetailIntegrationService } from './article-detail-integration.service';
import { SagArticleDetailStorageService } from './article-detail-storage.service';
import { get } from 'lodash';
import { AffiliateUtil, ProjectId } from 'sag-common';
@Injectable()
export class ArticlesService {
    isEhCz = AffiliateUtil.isEhCz(this.config.affiliate);
    isAutonet = this.config.projectId === ProjectId.AUTONET;

    constructor (
        private http: HttpClient,
        private config: ArticleDetailConfigService,
        private integrationService: SagArticleDetailIntegrationService,
        private storage: SagArticleDetailStorageService
    ) { }

    getArticlesAvailabilityWithBatch(requestItems: ArticleModel[], batchNumber: number) {
        const body = {
            availabilityRequestItemList: requestItems.map(item => ({
                idPim: item.pimId,
                quantity: item.amountNumber,
                stock: item.stock || null,
                totalAxStock: item.totalAxStock
            })),
            numberOfRequestedItems: batchNumber
        };
        const url = `${this.config.baseUrl}articles/availabilities`;
        return this.http.post(url, body, { observe: 'body' }).pipe(
            tap((res: any) => {
                const avaiItem = res.items;
                if (avaiItem) {
                    for (const key in avaiItem) {
                        if (avaiItem.hasOwnProperty(key)) {
                            const avai = avaiItem[key].availabilities;
                            this.integrationService.recordFeedbackAvailability(key, avai, true);
                        }

                    }
                }
            })
        );
    }

    getArticlesInfoWithBatch(requestItems: ArticleModel[], batchNumber: number, erpInfoRequest = {}) {
        const body = {
            articleInformationRequestItems: (requestItems || []).map(item => ({
                idPim: item.pimId,
                quantity: item.amountNumber,
                stock: item.stock || null,
                totalAxStock: item.totalAxStock
            })),
            erpInfoRequest,
            numberOfRequestedItems: batchNumber
        };
        const url = `${this.config.baseUrl}articles/erp-sync`;
        return this.http.post(url, body, { observe: 'body' }).pipe(
            catchError(err => of({ items: [] })),
            tap((res: any) => {
                const avaiItem = res && res.items || null;
                if (avaiItem) {
                    for (const key in avaiItem) {
                        const avail = avaiItem[key].availabilities;
                        if (avail) {
                            this.integrationService.recordFeedbackAvailability(key, avail, true);
                        }

                    }
                }
            }),
            map((res: any) => {
                if (this.isAutonet) {
                    if (!res || !res.items) {
                        res = {
                            items: {}
                        };
                    }
                    body.articleInformationRequestItems.forEach(item => {
                        if (!res.items[item.idPim]) {
                            res.items[item.idPim] = {
                                autonetInfos: null
                            }
                        }
                    })
                }
                return res;
            })
        );
    }

    getDisplayedAvailabilities(responseAvail) {
        const items = [];
        if (!responseAvail) {
            return items;
        }
        Object.keys(responseAvail).forEach(key => {
            const item = { key, value: responseAvail[key].availabilities };
            items.push(item);
        });
        return items;
    }

    getDisplayedInfo(responseAvail) {
        const items = [];
        if (!responseAvail) {
            return items;
        }
        Object.keys(responseAvail).forEach(key => {
            const item: any = {
                key,
                stock: responseAvail[key].stock,
                totalAxStock: responseAvail[key].totalAxStock,
                price: responseAvail[key].price,
                availabilities: responseAvail[key].availabilities,
                allowedAddToShoppingCart: responseAvail[key].allowedAddToShoppingCart,
                finalCustomerNetPrice: responseAvail[key].finalCustomerNetPrice,
                totalFinalCustomerNetPrice: responseAvail[key].totalFinalCustomerNetPrice,
                autonetInfos: responseAvail[key].autonetInfos,
                depositArticle: responseAvail[key].depositArticle,
                vocArticle: responseAvail[key].vocArticle,
                vrgArticle: responseAvail[key].vrgArticle,
                pfandArticle: responseAvail[key].pfandArticle,
                memos: responseAvail[key].memos,
                deliverableStock: responseAvail[key].deliverableStock
            };
            item.finalCustomerNetPriceWithVat = responseAvail[key].finalCustomerNetPriceWithVat;
            item.totalFinalCustomerNetPriceWithVat = responseAvail[key].totalFinalCustomerNetPriceWithVat;
            items.push(item);
        });
        return items;
    }

    getArticleVehicleUsages(artId: string) {
        const url = `${this.config.baseUrl}search/articles/usages?artId=${artId}`;
        return this.http.get(url, { observe: 'body' }).pipe(
            catchError(error => {
                return of([]);
            })
        );
    }

    getArticleByUpdatedAmount(body: { amount: number, pimId: string, finalCustomerId?: number }, getRaw?: boolean) {
        const url = `${this.config.baseUrl}articles/price`;

        const { subOrderBasket } = this.storage;
        const orgId = get(subOrderBasket, 'finalOrder.orgId');
        if (orgId) {
            body = { ...body, finalCustomerId: orgId };
        }
        return this.http.post(url, body, { observe: 'body' }).pipe(
            map((res: any) => {
                const art = res && res.content[0];
                if (getRaw) {
                    return art;
                }
                return new ArticleModel(art);
            }),
            catchError((error: any) => {
                if (error.status === 404) {
                    return of({
                        notFoundInAx: true
                    });
                }
                return of(null);
            })
        );
    }

    getArticleAvailability(body: {
        pimId: string,
        amount: number,
        stock: any,
        totalAxStock: number
    }) {
        const url = `${this.config.baseUrl}articles/availability`;
        return this.http.post(url, body, { observe: 'body' }).pipe(
            tap((res: any) => {
                const avaiItem = res.content[0];
                this.integrationService.recordFeedbackAvailability(avaiItem.artid, avaiItem.availabilities, true);
            }),
            map((res: any) => res && res.content[0])
        );
    }

    syncArticle({ amount, pimId }, getRaw?: boolean) {
        // get raw data to update
        return this.getArticleByUpdatedAmount({ amount, pimId }, getRaw).pipe(
            switchMap((art: any) => {
                if (!art) {
                    return of(null);
                }
                if (art.notFoundInAx) {
                    return of(art);
                }
                return this.getArticleAvailability({ pimId, amount, stock: art.stock || null, totalAxStock: art.totalAxStock }).pipe(map(res => {
                    art.availabilities = res.availabilities;
                    return art;
                }));
            })
        );
    }

    async getArticleAvailabilityForCheckPrimaryLocation(body: {
        pimId: string,
        amount: number,
        stock: any
    }) {
        const url = `${this.config.baseUrl}articles/availability`;
        return await this.http.post(url, body, { observe: 'body' }).pipe(
            catchError((err) => of(null)),
            map((res: any) => res && res.content[0] || null)
        ).toPromise();
    }
}
