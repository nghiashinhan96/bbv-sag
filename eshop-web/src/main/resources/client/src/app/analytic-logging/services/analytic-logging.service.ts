import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { catchError, first } from 'rxjs/operators';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { EncryptDecryptService } from 'src/app/core/services/encrypt-decrypt.service';
import { UserService } from 'src/app/core/services/user.service';
import { DateUtil } from 'src/app/core/utils/date.util';
import { environment } from 'src/environments/environment';
import { AnalyticEventType, ShoppingBasketEventType } from '../enums/event-type.enums';
import { ArticleCategoryEvent } from '../models/article-category.event.model';
import { ArticleListEvent } from '../models/article-list.event.model';
import { ArticleNrSearchEvent } from '../models/article-nr-search.event.model';
import { ArticleResultEvent } from '../models/article-result.model';
import { BasketItemSource } from '../models/basket-item-source.model';
import { BatteriesEvent } from '../models/battery.event.model';
import { BulbEvent } from '../models/bulb.event.model';
import { CouponEvent } from '../models/coupon.event.model';
import { FulltextSearchEvent } from '../models/fulltext-search.event.model';
import { LoginLogoutEvent } from '../models/login-logout.event.model';
import { MakeModelTypeSearchEvent } from '../models/make-model-type-search.event.model';
import { OilEvent } from '../models/oil.event.model';
import { OrderEvent } from '../models/order.event.model';
import { ReturnArticleEvent } from '../models/return-article.event.model';
import { ShoppingBasketEvent } from '../models/shopping-basket.event.model';
import { TyreEvent } from '../models/tyre.event.model';
import { VehicleDescSearchEvent } from '../models/vehicle-desc-search.event.model';
import { VehicleSearchEvent } from '../models/vehicle-search.event.model';
import { VinSearchEvent } from '../models/vin-search.event.model';
import { WspCatalogEvent } from '../models/wsp-catalog.event.model';
import { EventUtil } from '../utils/event.util';
import uuid from 'uuid/v4';
import { BasketItemSourceDesc } from '../enums/basket-item-source-desc.enum';
import { AffiliateUtil } from 'sag-common';
import { ARTICLE_SEARCH_MODE } from 'sag-article-search';
import { HeaderSearchTypeEnum } from 'src/app/layout/components/header/components/header-search/header-search-type.enum';
import { FavoriteArticleEvent } from '../models/favorite-article.event.model';
import { FavoriteVehicleEvent } from '../models/favorite-vehicle.event.model';

const EVENT_LOG_URL = `${environment.baseUrl}analytics/receive`;

@Injectable({ providedIn: 'root' })
export class AnalyticLoggingService {
    isChExcludeEh = AffiliateUtil.isAffiliateCH(environment.affiliate) && !AffiliateUtil.isEhCh(environment.affiliate);
    isAtExcludeEh = AffiliateUtil.isAffiliateAT(environment.affiliate) && !AffiliateUtil.isEhAt(environment.affiliate);
    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);

    constructor(
        private http: HttpClient,
        private encryptDecryptService: EncryptDecryptService,
        private userService: UserService,
        private appStorage: AppStorageService
    ) { }

    postEventFulltextSearch(data, eventType: AnalyticEventType) {
        const request = { ...data };
        request['@timestamp'] = DateUtil.getTimeInSeconds();
        if (this.isAllowedEventToSend(eventType)) {
            return this.http
                .post(`${EVENT_LOG_URL}?event_type=${eventType.toString()}`, request)
                .pipe(catchError(error => of('JSON event error: ', error)));
        }
        return of(null);
    }

    sendAdsEvent(eventType: AnalyticEventType, selectedTyre?: string) {
        const user = this.userService.userDetail;
        let event: any;
        let request: any;
        const metadata = {
            sessionId: this.encryptDecryptService.encrypt().toString()
        };
        switch (eventType) {
            case AnalyticEventType.ARTICLE_LIST_EVENT:
                event = new ArticleListEvent(metadata, user, null);
                event.artlistAdvertisementClicked = true;
                break;
            case AnalyticEventType.TYRE_EVENT:
                event = new TyreEvent(metadata, user, null);
                event.tyresAdvertisementClicked = true;
                event.tyresSearchTypeSelected = selectedTyre;
                break;
            case AnalyticEventType.OIL_EVENT:
                event = new OilEvent(metadata, user, null);
                event.oilAdvertisementClicked = true;
                break;
            case AnalyticEventType.BULB_EVENT:
                event = new BulbEvent(metadata, user, null);
                event.bulbsAdvertisementClicked = true;
                break;
            case AnalyticEventType.BATTERIES_EVENT:
                event = new BatteriesEvent(metadata, user, null);
                event.batAdvertisementClicked = true;
                break;
        }

        request = event.toAdsEventRequest();
        this.postEventFulltextSearch(request, eventType).pipe(first()).toPromise();
    }

    createLoginLogoutEventData(data: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        const event = new LoginLogoutEvent(metadata, user, data);
        return event.toEventRequest();
    }

    createBatteryEventData(data: any, extras?: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        let basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.BATTERY_SEARCH);
        const event = new BatteriesEvent(metadata, user, data, extras.totalItems, basketItemSource);
        return EventUtil.normalize(event.toEventRequest());
    }

    createBulbEventData(data: any, extras?: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        let basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.BULB_SEARCH);
        const event = new BulbEvent(metadata, user, data, extras.totalItems, basketItemSource);
        return EventUtil.normalize(event.toEventRequest());
    }

    createOilEventData(data: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        let basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.OIL_SEARCH);
        const event = new OilEvent(metadata, user, data, basketItemSource);
        return EventUtil.normalize(event.toEventRequest());
    }

    createTyreEventData(data: any, extras?: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        let basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.TYRE_SEARCH);
        const event = new TyreEvent(metadata, user, data, extras.numberOfHits, extras.selectedTab, basketItemSource);
        if (extras.matchCodeSearch) {
            return EventUtil.normalize(event.toMatchCodeEventRequest());
        } else {
            return EventUtil.normalize(event.toEventRequest());
        }
    }

    createArticleListEventData(data: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        const event = new ArticleListEvent(metadata, user, data);
        return EventUtil.normalize(event.toEventRequest());
    }

    createCouponEventData(data: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        const event = new CouponEvent(metadata, user, data);
        return EventUtil.normalize(event.toEventRequest());
    }

    createShoppingOrderEventData(data: any, extras?: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        extras.isNettoPriceDisplay = this.userService.userPrice.currentStateNetPriceView;
        const event = new OrderEvent(metadata, user, data, extras);
        return EventUtil.normalize(event.toEventRequest());
    }

    createFullTextSearchEventData(data: any, extras?: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        if (extras.isReturnArticleSearchMode) {
            const event = new ReturnArticleEvent(metadata, user, data);
            return EventUtil.normalize(event.toEventRequest());
        } else {
            let basketItemSource: BasketItemSource = {};
            switch (data.ftsFilterSelected) {
                case HeaderSearchTypeEnum.ARTICLES.toUpperCase():
                    if (extras.isSelectedItemEvent) {
                        basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.FULL_TEXT_SEARCH);
                    }
                    break;
                case HeaderSearchTypeEnum.VEHICLES.toUpperCase():
                    if (extras.isSelectedItemEvent) {
                        basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.VEHICLE_FULL_TEXT_SEARCH);
                    }
                    break;
                case HeaderSearchTypeEnum.ALL.toUpperCase():
                    if (extras.isSelectedItemEvent) {
                        let basketItemSourceDesc = BasketItemSourceDesc.VEHICLE_FULL_TEXT_SEARCH;
                        if (extras.isArticle) {
                            basketItemSourceDesc = BasketItemSourceDesc.FULL_TEXT_SEARCH;
                        }
                        basketItemSource = this.createBasketItemSource(basketItemSourceDesc);
                    }
                    break;
                case HeaderSearchTypeEnum.FASTSCAN_ARTICLES:
                    basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.FAST_SCAN);
                    break;
                default:
                    break;
            }
            const event = new FulltextSearchEvent(metadata, user, data, basketItemSource);
            if (extras.isSelectedItemEvent) {
                return event.toSelectedItemEventRequest(extras.isArticle, extras.isShowMore);
            } else {
                return event.toEventRequest();
            }
        }
    }

    createFullTextSearchArticleEventData(data: any, extras?: any) {
        const user = this.userService.userDetail;
        const metadata = {};
        let basketItemSource: BasketItemSource = {};
        if (extras) {
            switch (extras.articleSearchMode) {
                case ARTICLE_SEARCH_MODE.SHOPPING_LIST:
                    let basketItemSourceDesc = BasketItemSourceDesc.SHOPPING_LIST_SEARCH;
                    if (extras.isSubBasket) {
                        basketItemSourceDesc = BasketItemSourceDesc.WSS_FC_SHOPPING_LIST_SEARCH;
                    }
                    basketItemSource = this.createBasketItemSource(basketItemSourceDesc);
                    break;
                case ARTICLE_SEARCH_MODE.DEEP_LINK:
                    basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.ARTICLE_DEEP_LINK);
                    break;
                default:
                    break;
            }
        } else {
            basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.ARTICLE_SEARCH);
        }
        const event = new ArticleNrSearchEvent(metadata, user, data, basketItemSource);
        return EventUtil.normalize(event.toEventRequest());
    }

    createFavoriteArticleEventData(data: any) {
        const user = this.userService.userDetail;
        const metadata = {};
        let basketItemSource: BasketItemSource = this.createBasketItemSource(BasketItemSourceDesc.ARTICLE_FAVORITE);
        const event = new FavoriteArticleEvent(metadata, user, data, basketItemSource);
        return EventUtil.normalize(event.toEventRequest());
    }

    createFavoriteVehicleEventData(data: any) {
        const user = this.userService.userDetail;
        const metadata = {};
        let basketItemSource: BasketItemSource = this.createBasketItemSource(BasketItemSourceDesc.VEHICLE_FAVORITE);
        const event = new FavoriteVehicleEvent(metadata, user, data, basketItemSource);
        return EventUtil.normalize(event.toEventRequest());
    }

    createArticleCategoryEventData(data: any) {
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        const event = new ArticleCategoryEvent(metadata, user, data);
        return EventUtil.normalize(event.toArticleCategoryRequest());
    }

    createVehicleDescSearchEventData(data: any, isCameFromHistorySearching?: boolean) {
        const user = this.userService.userDetail;
        const metadata = {};
        const basketItemSourceDesc = isCameFromHistorySearching ? BasketItemSourceDesc.VEHICLE_ID_SEARCH : BasketItemSourceDesc.VEHICLE_DESC_SEARCH;
        let basketItemSource = this.createBasketItemSource(basketItemSourceDesc);
        const event = new VehicleDescSearchEvent(metadata, user, data, basketItemSource);
        return event.toEventRequest();
    }

    createVehicleSearchEventData(data: any, isCameFromHistorySearching?: boolean) {
        const user = this.userService.userDetail;
        const metadata = {};
        const basketItemSourceDesc = isCameFromHistorySearching ? BasketItemSourceDesc.VEHICLE_ID_SEARCH : BasketItemSourceDesc.VEHICLE_CODE_SEARCH;
        let basketItemSource = this.createBasketItemSource(basketItemSourceDesc);
        const event = new VehicleSearchEvent(metadata, user, data, basketItemSource);
        return event.toEventRequest();
    }

    createVinSearchEventData(data: any) {
        const user = this.userService.userDetail;
        const metadata = {};
        let basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.VIN_SEARCH);
        const event = new VinSearchEvent(metadata, user, data, basketItemSource);
        return event.toEventRequest();
    }

    createMakeModelTypeSearchEventData(data: any, isCameFromHistorySearching?: boolean, isCameFromAdvanceVehicleSearch: boolean = false) {
        const user = this.userService.userDetail;
        const metadata = {};
        let basketItemSourceDesc = '';
        if (isCameFromAdvanceVehicleSearch) {
            basketItemSourceDesc = BasketItemSourceDesc.VEHICLE_MAKEMODELTYPE_SEARCH_PAGED;
        } else {
            basketItemSourceDesc = isCameFromHistorySearching ? BasketItemSourceDesc.VEHICLE_ID_SEARCH : BasketItemSourceDesc.VEHICLE_MAKEMODELTYPE_SEARCH;
        }
        let basketItemSource = this.createBasketItemSource(basketItemSourceDesc);
        const event = new MakeModelTypeSearchEvent(metadata, user, data, basketItemSource);
        return event.toEventRequest();
    }

    createMotoMakeModelSearchEventData(data: any, isCameFromHistorySearching?: boolean) {
        const user = this.userService.userDetail;
        const metadata = {};
        const basketItemSourceDesc = isCameFromHistorySearching ? BasketItemSourceDesc.VEHICLE_ID_SEARCH : BasketItemSourceDesc.VEHICLE_MOTO_MAKEMODEL_SEARCH;
        let basketItemSource = this.createBasketItemSource(basketItemSourceDesc);
        const event = new MakeModelTypeSearchEvent(metadata, user, data, basketItemSource);
        return event.toEventRequest();
    }

    createTransferBasketEventData(data: any, extras?: any) {
        const user = this.userService.userDetail;
        const metadata = {};
        extras.isNettoPriceDisplay = this.userService.userPrice.currentStateNetPriceView;
        const event = new OrderEvent(metadata, user, data, extras);
        return event.toTransferBasketEventRequest();
    }

    createTransferOfferEventData(data: any, extras?: any) {
        const user = this.userService.userDetail;
        const metadata = {};
        extras.isNettoPriceDisplay = this.userService.userPrice.currentStateNetPriceView;
        const event = new OrderEvent(metadata, user, data, extras);
        return event.toTransferOfferEventRequest();
    }

    createArticleResultsEventData(articles: any, vehicleId?: any) {
        const user = this.userService.userDetail;
        const metadata = {};
        let basketItemSource: BasketItemSource = {};
        basketItemSource = this.appStorage.basketItemSource;
        const event = new ArticleResultEvent(metadata, user, { articles, vehicleId }, basketItemSource);
        return event.toEventRequest();
    }

    createShoppingBasketEventData(data: any, extras?: any) {
        let event: ShoppingBasketEvent;
        const user = this.userService.userDetail;
        const metadata = this.createMetadata();
        const isNettoPriceDisplay = this.userService.userPrice.currentStateNetPriceView;
        switch (extras && extras.source) {
            case ShoppingBasketEventType.BASKET:
                event = new ShoppingBasketEvent(metadata, user)
                    .buildShoppingBasketEventData(data, isNettoPriceDisplay);
                return EventUtil.normalize(event.toWholeShoppingBasketEventRequest(isNettoPriceDisplay));
            case ShoppingBasketEventType.SAVED_BASKETS:
                event = new ShoppingBasketEvent(metadata, user)
                    .buildSavedBasketEventData(data, ShoppingBasketEventType.SAVED_BASKETS, isNettoPriceDisplay);
                return EventUtil.normalize(event.toShoppingBasketEventRequest());
            case ShoppingBasketEventType.INVOICE:
                let basketItemSourceOfInvoice: BasketItemSource = {};
                basketItemSourceOfInvoice = this.appStorage.basketItemSource;
                this.appStorage.clearBasketItemSource();
                event = new ShoppingBasketEvent(metadata, user, null, null, null, null, null, basketItemSourceOfInvoice)
                    .buildInvoiceEventData(extras.selectedArticles, data, ShoppingBasketEventType.INVOICE, isNettoPriceDisplay);
                return EventUtil.normalize(event.toShoppingBasketEventRequest());
            case ShoppingBasketEventType.OFFER:
                event = new ShoppingBasketEvent(metadata, user, null, ShoppingBasketEventType.OFFER)
                    .buildOfferEventData(data, isNettoPriceDisplay);
                return EventUtil.normalize(event.toShoppingBasketEventRequest());
            case ShoppingBasketEventType.ARTICLE_LIST:
            case ShoppingBasketEventType.GTMOTIVE:
                event = new ShoppingBasketEvent(metadata, user, data, null, null, null, isNettoPriceDisplay)
                    .buildArticleListEventData(
                        extras.addedArticle,
                        extras.source,
                        isNettoPriceDisplay,
                        extras.customBrand,
                        this.appStorage.basketItemSource
                    );
                return EventUtil.normalize(event.toShoppingBasketEventRequest());
            case ShoppingBasketEventType.HAYNESPRO:
            case ShoppingBasketEventType.DVSE:
                let basketItemSourceOfDVSE: BasketItemSource = {};
                if (extras.source === ShoppingBasketEventType.DVSE && extras.addedArticle) {
                    basketItemSourceOfDVSE.basketItemSourceId = extras.addedArticle.basketItemSourceId;
                    basketItemSourceOfDVSE.basketItemSourceDesc = extras.addedArticle.basketItemSourceDesc;
                }
                event = new ShoppingBasketEvent(metadata, user, data, null, null, null, isNettoPriceDisplay, basketItemSourceOfDVSE)
                    .buildArticleListEventData(
                        extras.addedArticle,
                        extras.source,
                        isNettoPriceDisplay,
                        extras.customBrand
                    );
                return EventUtil.normalize(event.toShoppingBasketEventRequest());
            case ShoppingBasketEventType.SHOPPING_BASKET_PAGE:
                event = new ShoppingBasketEvent(metadata, user)
                    .buildShoppingBasketPageEventData(data, isNettoPriceDisplay, extras.customBrand);
                return EventUtil.normalize(event.toShoppingBasketEventRequest());
            case ShoppingBasketEventType.THULE:
                let basketItemSourceOfThule: BasketItemSource = {};
                basketItemSourceOfThule = this.appStorage.basketItemSource;
                this.appStorage.clearBasketItemSource();
                event = new ShoppingBasketEvent(metadata, user, null, null, null, null, null, basketItemSourceOfThule)
                        .buildThulePageEventData(data, isNettoPriceDisplay);
                    return EventUtil.normalize(event.toShoppingBasketEventRequest());
            case ShoppingBasketEventType.FAST_SCAN:
                event = new ShoppingBasketEvent(metadata, user)
                    .buildFastScanEventData(data, isNettoPriceDisplay);
                return EventUtil.normalize(event.toShoppingBasketEventRequest());
            case ShoppingBasketEventType.OCI:
                event = new ShoppingBasketEvent(metadata, user, null, extras && extras.source)
                    .buildShoppingBasketEventData(data, isNettoPriceDisplay);
                return EventUtil.normalize(event.toWholeShoppingBasketEventRequest(isNettoPriceDisplay));
            default:
                let basketItemSourceOfOrderHistory: BasketItemSource = {};
                if (extras.source === ShoppingBasketEventType.ORDER_HISTORY) {
                    basketItemSourceOfOrderHistory = this.appStorage.basketItemSource;
                    this.appStorage.clearBasketItemSource();
                }
                event = new ShoppingBasketEvent(
                    metadata,
                    user,
                    data,
                    extras && extras.source,
                    extras.selectedArticles,
                    extras.totalPrice,
                    isNettoPriceDisplay,
                    basketItemSourceOfOrderHistory
                );
                return EventUtil.normalize(event.toShoppingBasketEventRequest());
        }
    }

    createWspCatalogEventData(data, isCameFromFavoriteSearch? : boolean, isCameFromGenartBreadcrumbClick?: boolean) {
        const user = this.userService.userDetail;
        const metadata = {};
        let basketItemSource: BasketItemSource = {};
        if ((data.wspLeafNodeClicked && !isCameFromGenartBreadcrumbClick) || data.wspStdTileLinkClicked || isCameFromFavoriteSearch) {
            basketItemSource = this.createBasketItemSource(BasketItemSourceDesc.WSP_CATALOG);
        } else if (data.wspGenartTileLinkClicked || isCameFromGenartBreadcrumbClick) {
            basketItemSource = this.appStorage.basketItemSource;
        }
        const event = new WspCatalogEvent(metadata, user, data, basketItemSource);
        return EventUtil.normalize(event.toEventRequest());
    }

    private createMetadata() {
        return {
            sessionId: this.encryptDecryptService.encrypt().toString()
        };
    }

    private isAllowedEventToSend(eventType: AnalyticEventType): boolean {
        const list = this.appStorage.jsonEventList || {};
        return list[eventType];
    }
    
    generateBasketItemSourceId(desc: string) {
        const id = uuid();
        const basketItemSource: BasketItemSource = {
            basketItemSourceId: id,
            basketItemSourceDesc: desc
        };
        return basketItemSource;
    }

    createBasketItemSource(basketItemSourceDesc: string): BasketItemSource {
        let basketItemSource: BasketItemSource = {};
        if(this.isChExcludeEh || this.isAtExcludeEh || this.isAxCz) {
            basketItemSource = this.generateBasketItemSourceId(basketItemSourceDesc);
            this.appStorage.basketItemSource = basketItemSource;
        }
        return basketItemSource;
    }
}
