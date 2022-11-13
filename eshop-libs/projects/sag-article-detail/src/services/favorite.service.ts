import { ArticleDetailConfigService } from './article-detail-config.service';
import { FavoriteItem, FavoriteRequest } from '../models/favorite-item.model';
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, finalize, map, tap } from "rxjs/operators";
import { BroadcastService } from "sag-common";
import { TranslateService } from '@ngx-translate/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { FAVORITE_BROADCAST_EVENT, FAVORITE_PROCESS_TYPE, FAVORITE_SEARCH_TYPE } from '../consts/article-detail.const';
import { ArticleBroadcastKey } from '../enums/article-broadcast-key.enum';
import { ArticleModel } from '../models/article.model';
import { of } from 'rxjs';
import { SagArticleDetailIntegrationService } from './article-detail-integration.service';

@Injectable({
    providedIn: 'root'
})
export class FavoriteService {
    favoriteList: FavoriteItem[] = [];
    currentGenArtStatus: FavoriteItem;

    constructor(
        private http: HttpClient,
        private broadcastService: BroadcastService,
        private config: ArticleDetailConfigService,
        private translateService: TranslateService,
        private integrationService: SagArticleDetailIntegrationService
    ) { }

    onProcessFavoriteItemSuccess(item: FavoriteItem, mode, rootModalName = '') {
        switch (mode) {
            case FAVORITE_BROADCAST_EVENT.REMOVE_ARTICLE:
            case FAVORITE_BROADCAST_EVENT.ADD_ARTICLE: {
                this.broadcastService.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
                    action: FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_ARTICLE,
                    data: item,
                    mode
                });
                this.integrationService.sendWishlistGaData(item, rootModalName);
                break;
            }

            case FAVORITE_BROADCAST_EVENT.REMOVE_LEAF: {
                this.currentGenArtStatus = null;
                this.broadcastService.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
                    action: FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_LEAF,
                    data: item,
                    mode
                });
                break;
            }
            case FAVORITE_BROADCAST_EVENT.ADD_LEAF: {
                this.currentGenArtStatus = item;
                this.broadcastService.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
                    action: FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_LEAF,
                    data: item
                });
                break;
            }

            case FAVORITE_BROADCAST_EVENT.ADD_VEHICLE:
            case FAVORITE_BROADCAST_EVENT.REMOVE_VEHICLE: {
                this.broadcastService.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
                    action: FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_VEHICLE,
                    data: item,
                    mode
                });
                break;
            }
            default:
                break;
        }

    }

    processFavoriteItem(item: FavoriteItem) {
        if (!item) {
            return;
        }
        const url = `${this.config.baseUrl}favorite/process-item`;
        return this.http.post(url, item);
    }

    updateFavoriteItem(item: FavoriteItem) {
        const url = `${this.config.baseUrl}favorite/item`;
        return this.http.put(url, item).pipe(
            finalize(() => {
                switch (item.type) {
                    case FAVORITE_PROCESS_TYPE.ARTICLE:
                        this.broadcastService.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
                            action: FAVORITE_BROADCAST_EVENT.EDIT_ARTICLE,
                            data: item
                        });
                        break;
                    case FAVORITE_PROCESS_TYPE.LEAF_NODE:
                        this.broadcastService.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
                            action: FAVORITE_BROADCAST_EVENT.EDIT_LEAF_COMMENT,
                            data: item
                        });
                        this.currentGenArtStatus = item;
                        break;
                    case FAVORITE_PROCESS_TYPE.VEHICLE:
                        this.broadcastService.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
                            action: FAVORITE_BROADCAST_EVENT.EDIT_VEHICLE,
                            data: item
                        });
                        break;
                    default:
                        break;
                }
            })
        );
    }

    removeFavortieItem(item: FavoriteItem, onRemoved?) {
        item.addItem = false;
        let removeMode = '';
        switch (item.type) {
            case FAVORITE_PROCESS_TYPE.ARTICLE:
                removeMode = FAVORITE_BROADCAST_EVENT.REMOVE_ARTICLE;
                break;
            case FAVORITE_PROCESS_TYPE.LEAF_NODE:
                removeMode = FAVORITE_BROADCAST_EVENT.REMOVE_LEAF;
                break;
            case FAVORITE_PROCESS_TYPE.VEHICLE:
                removeMode = FAVORITE_BROADCAST_EVENT.REMOVE_VEHICLE;
                break;
            default:
                break;
        }
        this.processFavoriteItem(item).subscribe(res => {
            if (removeMode !== '') {
                this.onProcessFavoriteItemSuccess(item, removeMode);
            }
            if (onRemoved) {
                onRemoved();
            }
        }, error => {
            console.log(error);
        })
    }

    addFavoriteItem(item: FavoriteItem, mode, rootModalName = '') {
        item.addItem = true;
        const body = {
            ...item,
            articleItem: undefined
        };
        this.processFavoriteItem(body).subscribe(res => {
            this.onProcessFavoriteItemSuccess(item, mode, rootModalName);
        }, error => {
            console.log(error);
        })
    }

    getGenArtStatus(treeId, leafId, gaId) {
        const item = new FavoriteItem({
            treeId,
            leafId: (leafId || '').toString(),
            gaId,
            type: FAVORITE_PROCESS_TYPE.LEAF_NODE
        });
        const url = `${this.config.baseUrl}favorite/info-items`;
        return this.http.post(url, [item]).pipe(
            tap((res: any[]) => {
                if (res && res.length > 0) {
                    const statusList = res.map(status => new FavoriteItem(status));
                    this.currentGenArtStatus = statusList[0];
                } else {
                    this.currentGenArtStatus = null;
                }
            })
        );
    }

    searchFavorite(request: FavoriteRequest) {
        const url = `${this.config.baseUrl}favorite/search`;
        return this.http.post(url, request)
            .pipe(
                map((res: any) => {
                    let data = [];
                    if (res || res.content) {
                        data = (res.content || []).map(item => {
                            const fItem = new FavoriteItem(item);
                            return fItem;
                        })
                    }
                    return { data, totalPages: res.totalPages };
                })
            )
    }

    generateFavoriteItemFromArticle(article: ArticleModel) {
        if (!article) {
            return;
        }
        const title = this.generatArticleTitle(article);
        const favoriteItem = new FavoriteItem({
            articleItem: article,
            articleId: article.artid,
            comment: article.favoriteComment || '',
            title,
            type: FAVORITE_PROCESS_TYPE.ARTICLE
        });
        return favoriteItem;
    }


    generatArticleTitle(article: ArticleModel) {
        if (!article) {
            return '';
        }
        const genArtDescription = article.genArtTxts.length > 0 ? (article.genArtTxts[0].gatxtdech || '') : '';
        const supplier = article.supplier || '';
        const productAddon = article.productAddon || '';
        const articleNumberText = this.translateService.instant('COMMON_LABEL.ARTICLE_NUMBER_LABEL');
        const artNr = article.artnr ? `${articleNumberText} ${article.artnrDisplay}` : '';

        const general = [genArtDescription, supplier].join(' ').trim();
        const details = [productAddon, artNr].join(' ').trim();
        if (general) {
            return [general, details].join(', ').trim();
        }
        return details;
    }

    getListOfLatestComment() {
        const url = `${this.config.baseUrl}favorite/latest-comment`;
        return this.http.get(url)
    }

    searchForComment(text) {
        const url = `${this.config.baseUrl}favorite/search/comment?text=${text}`;
        return this.http.get(url).pipe(catchError(error => of([])));
    }

    generateFavoriteItemFromVehicle(vehicle: any) {
        if (!vehicle) {
            return;
        }
        const title = vehicle.vehicleInfo;
        const favoriteItem = new FavoriteItem({
            vehicleId: vehicle.vehid,
            vinId: vehicle.vin,
            comment: vehicle.favoriteComment || '',
            title,
            type: FAVORITE_PROCESS_TYPE.VEHICLE
        });
        return favoriteItem;
    }

}