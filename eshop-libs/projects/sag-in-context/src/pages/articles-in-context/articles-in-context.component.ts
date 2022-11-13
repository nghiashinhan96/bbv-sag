import { Component, OnInit, OnDestroy, Input, TemplateRef, Output, EventEmitter } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { Subject } from 'rxjs/internal/Subject';
import { map } from 'rxjs/internal/operators/map';
import { tap } from 'rxjs/internal/operators/tap';
import { debounceTime } from 'rxjs/internal/operators/debounceTime';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { takeUntil } from 'rxjs/internal/operators/takeUntil';
import { of } from 'rxjs/internal/observable/of';
import { catchError, finalize } from 'rxjs/operators';
import { difference, get, uniq, uniqBy, omitBy, isEmpty,isEqual, uniqWith, intersection } from 'lodash';
import { SubSink } from 'subsink';

import {
    BroadcastService,
    SagConfirmationBoxComponent,
    UrlUtil,
    ARTICLE_EVENT_SOURCE,
    SAG_COMMON_RESPONSE_CODE,
    AffiliateUtil,
    AffiliateEnum,
    VEHICLE_CLASS
} from 'sag-common';
import { ArticleBroadcastKey, ArticleBasketModel } from 'sag-article-detail';
import {
    ARTICLE_LIST_TYPE,
    SagArticleListOlyslagerPopupComponent,
    ArticleListStorageService,
    SEARCH_MODE,
    CategoryTreeService,
    VehicleSearchService,
    ClassicCategoryService,
    VEHICLE_SOURCE
} from 'sag-article-list';
import {
    GT_TYPE,
    GtmotiveService,
    SagGtmotiveMultiGraphicModalComponent,
    SagGtmotiveVinWarningModalComponent
} from 'sag-gtmotive';
import { SagInContextSearchRequest } from '../../interfaces/article-in-context-search-request.interface';
import { ArticleInContextService } from '../../services/articles-in-context.service';
import { SagInContextConfigService } from '../../services/articles-in-context-config.service';
import { SagInContextStorageService } from '../../services/articles-in-context-storage.service';
import { SagInContextIntegrationService } from '../../services/articles-in-context-integration.service';
import { ADS_TARGET_NAME, CVP_VALUE_IS_MISSING_ERR_MSG } from 'sag-common';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
@Component({
    selector: 'sag-in-context-articles-result-list',
    templateUrl: './articles-in-context.component.html',
    styleUrls: ['./articles-in-context.component.scss']
})
export class SagInContextArticleResultListComponent implements OnInit, OnDestroy {
    @Input() memoTemplateRef: TemplateRef<any>;

    @Output() selectFavoriteItemEmit = new EventEmitter<any>();

    user: any;
    articleListType = ARTICLE_LIST_TYPE.IN_CONTEXT;
    vehicle$: Observable<any>;
    vehicle: any;
    vehid: string;
    lang: string;
    queryParams;
    resetTreeOnDestroy = true;
    adsTargetName = ADS_TARGET_NAME.LIST_PARTS;
    adsZoneId = 4;
    adsSiteArea = 'MAIN_LIST_PARTS_AREA';
    cz = AffiliateEnum.CZ;
    ehcz = AffiliateEnum.EH_CZ;
    isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate) || AffiliateUtil.isAxCz(this.config.affiliate);
    isSb = AffiliateUtil.isSb(this.config.affiliate);
    axcz = AffiliateEnum.AXCZ;
    ehaxcz = AffiliateEnum.EH_AX_CZ;

    // GT Motive
    GT_TYPE = GT_TYPE;
    searchByVin: boolean;
    gtType: GT_TYPE;
    gtNonMatchedOperations: any[];
    normauto: any;
    mailContent: any;
    errorMessage: string;
    hasHaynesproPermission: boolean;
    onHaynesProError = false;
    keywords = '';
    currentStateSingleSelectMode: boolean;
    isSyncCategories = false;
    selectedCategoriesRequest: any;
    oilCateRequest: any;
    gtRequest: any;
    isUpdateAvail: boolean;
    isLoadedVehicleDetail: boolean = false;

    private subs = new SubSink();
    private destroy$ = new Subject();
    private olyslagerRef: BsModalRef;
    private categoryTreeReadyObserve: Subscription;
    constructor(
        private bsModalService: BsModalService,
        private router: Router,
        private articleInContextService: ArticleInContextService,
        private activatedRoute: ActivatedRoute,
        private broadcastService: BroadcastService,
        private config: SagInContextConfigService,
        private storage: SagInContextStorageService,
        private libStorage: ArticleListStorageService,
        private categoryTreeService: CategoryTreeService,
        private gtmotiveService: GtmotiveService,
        private vehicleSearchService: VehicleSearchService,
        private classicCategoryService: ClassicCategoryService,
        public integrationService: SagInContextIntegrationService
    ) {

    }

    ngOnInit() {
        this.integrationService.hasPermission(['HAYNESPRO']).subscribe(data => {
            this.hasHaynesproPermission = data;
        });
        this.integrationService.loadMiniBasket().then(() => { });
        // trigger on articles removed
        this.integrationService.observeArticleRemove();
        this.integrationService.observeArticleUpdate();
        this.vehicle = this.storage.selectedVehicle;
        this.user = this.integrationService.userDetail;
        // reset
        this.vehicle$ = this.activatedRoute.data.pipe(
            takeUntil(this.destroy$),
            map(({ data }) => {
                const { params, queryParams } = data;
                this.queryParams = queryParams;

                if(params.vehicleId && this.vehid !== params.vehicleId) {
                    this.vehid = params.vehicleId;
                    this.isSyncCategories = true;
                } else {
                    this.isSyncCategories = false;
                }

                if (get(this.vehicle, 'id') !== params.vehicleId) {
                    this.libStorage.removeAllSelectedOil();
                    this.gtmotiveService.resetData();
                    this.resetTree();
                }

                this.searchByVin = UrlUtil.toBoolean(this.queryParams.searchByVin);
                const isGtmotive = UrlUtil.toBoolean(this.queryParams.isGtmotive);
                const vinCode = this.queryParams.vinCode || '';
                const {searchMode} =  queryParams ;
                let searchKey = decodeURIComponent(this.queryParams.keywords || vinCode || '')
                if (searchMode !== SEARCH_MODE.MAKE_MODEL_TYPE) {
                    this.keywords = searchKey;
                }
                const selectedCateIds = this.libStorage.selectedCateIds;
                if (!isGtmotive && !!this.vehid && (!selectedCateIds || selectedCateIds.length === 0)) {
                    this.setDefaultTab();
                }

                this.gtmotiveService.gteCodes = {};

                // set search context
                this.vehicleSearchService.getVehiclesByVehId(this.vehid).pipe(
                    switchMap((vehicle: any) => {
                        vehicle.vin = vinCode;
                        vehicle.keywords = searchKey;
                        vehicle.vinSearch = this.searchByVin || false;
                        this.vehicle = vehicle;
                        this.storage.selectedVehicle = this.vehicle;
                        this.gtmotiveService.handleVehicleResponse(this.vehicle);
                        if (vehicle && vehicle.vehicleInfo) {
                            this.integrationService.recordFeedbackVehicleInfo(vehicle.vehicleInfo);
                        }
                        this.setVehicleSearchMode(queryParams);
                        const fromOffer = UrlUtil.toBoolean(queryParams.fromOffer);
                        const vehicleClass = vehicle && vehicle.vehicle_class || '';
                        this.updateADSInputForMotorbike(vehicleClass);
                        this.isLoadedVehicleDetail = true;
                        return this.setVehicleContext({ ...this.vehicle, fromOffer });
                    }),
                    catchError(error => {
                        this.isLoadedVehicleDetail = true;
                        throw error;
                    })
                    ).subscribe();
                return { vehicleId: params.vehicleId, isGtmotive, syncCategories: this.isSyncCategories};
            }));

        let spinner;
        this.subs.sink = this.gtmotiveService.gtRequestObservable.pipe(
            tap((request) => {
                if (!request) {
                    return;
                }

                spinner = this.config.spinner.start('sag-in-context-articles-result-list');

                if(this.gtRequest !== request) {
                    this.gtRequest = request;
                }

                this.colectArticlesFromGtmotive(request);
            }),
            finalize(() => this.config.spinner.stop(spinner))
        ).subscribe();

        this.subs.sink = this.categoryTreeService.selectedCategoriesObservable.pipe(
            tap(() => {
                this.switchTab();
            }),
            debounceTime(600),
            switchMap(request => {
                if (this.selectedCategoriesRequest !== request) {
                    this.selectedCategoriesRequest = request;
                }
                spinner = this.config.spinner.start('sag-in-context-articles-result-list .result', {
                    containerMinHeight: 200,
                    class: 'fixed-top'
                });
                return this.requestArticles(request);
            })
        ).subscribe(() => {
            this.config.spinner.stop(spinner);
            this.isUpdateAvail = false;
        });

        this.subs.sink = this.categoryTreeService.checkOilCateObservable.subscribe((data: any) => {
            if(this.oilCateRequest !== data) {
                this.oilCateRequest = data;
            }

            this.oilChecked(data);
        });

        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .subscribe((code: string) => {
                this.router.navigate(['article', 'result'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code }
                });
            });

        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
            .subscribe((vehicleId: string) => {
                if (vehicleId && vehicleId !== this.vehid) {
                    if (this.categoryTreeReadyObserve) {
                        this.categoryTreeReadyObserve.unsubscribe();
                    }
                    this.gtmotiveService.resetData();
                    this.resetTree(true);
                    this.router.navigate(['../', vehicleId], { relativeTo: this.activatedRoute });
                }
            });

        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'LOADED':
                        this.integrationService.isAddedInCart(data);
                        const article = this.articleInContextService.articles.find(art => art.pimId === data.article.pimId);
                        if (article) {
                            article.availabilities = data.article.availabilities;
                        }
                        break;
                    case 'ADD':
                        if (data.uuid) {
                            this.config.spinner.start(`#part-detail-${data.uuid}`,
                                { containerMinHeight: 0 }
                            );
                        }
                        this.articleInContextService.isGlassBodyWork(data, this.bsModalService)
                            .then((vinCode: string) => {
                                if (vinCode) {
                                    this.vehicle.vin = vinCode;
                                    this.storage.selectedVehicle = this.vehicle;
                                }
                                this.integrationService.addItemToCart(data);
                            });
                        break;
                    case 'CUSTOM_PRICE_CHANGE':
                        this.integrationService.updateOtherProcess(data.basket);
                        break;
                }
            });
        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.TOGGLE_SPECIAL_DETAIL)
            .subscribe((data: any) => {
                this.integrationService.sendArticleListEventData(data);
            });

        this.subs.sink = this.integrationService.userSetting$.subscribe(settings => {
            this.currentStateSingleSelectMode = settings && settings.currentStateSingleSelectMode;
        });

        this.subs.sink = this.broadcastService.on(ArticleBroadcastKey.MINI_BASKET_CONDITION_EVENT)
            .subscribe((data: any) => {
                this.articleInContextService.resetResultList();

                if(this.gtRequest) {
                    this.colectArticlesFromGtmotive(this.gtRequest);
                    return;
                }

                if(this.selectedCategoriesRequest || this.oilCateRequest) {
                    this.isUpdateAvail = true;
                    this.categoryTreeService.updateSelectedCategories(this.selectedCategoriesRequest);
                }
            });
    }

    private updateADSInputForMotorbike(vehicleClass: string) {
        if (AffiliateUtil.isAffiliateApplyMotorbikeShop(this.config.affiliate) && vehicleClass === VEHICLE_CLASS.MB) {
            this.adsZoneId = 20;
            this.adsSiteArea = 'MOTO';
            this.adsTargetName = ADS_TARGET_NAME.MOTOBIKES;
        }
    }

    async ngOnDestroy() {
        this.subs.unsubscribe();
        this.destroy$.next(true);
        this.destroy$.complete();
        if (this.resetTreeOnDestroy) {
            this.libStorage.removeAllSelectedOil();
            this.gtmotiveService.resetData();
        }
        this.resetTree();
        await this.setVehicleContext().toPromise();
    }

    get hideGtmotive() {
        return isEmpty(this.gtmotiveService.gteCodes);
    }

    private resetTree(stateTree = false) {
        this.libStorage.accessoryArticle = null;
        this.libStorage.selectedCateIds = null;
        this.articleInContextService.resetResultList();
        this.categoryTreeService.resetCategoryTree();
        this.gtmotiveService.emitSearch(null);
        this.categoryTreeService.destroyCategoryTree();
        this.categoryTreeService.markedTreeState(stateTree);
        this.storage.selectedVehicle = null;
        this.classicCategoryService.resetAll();
        this.articleInContextService.emitBrandFilterData([]);
        this.articleInContextService.emitBarFilterData([]);
    }

    setVehicleSearchMode(query) {
        const { searchMode } = query;
        if (searchMode) {
            this.vehicle.searchMode = searchMode;
            return;
        }
        this.vehicle.searchMode = this.vehicle.vinSearch ? SEARCH_MODE.VIN_CODE : SEARCH_MODE.VEHICLE_DESC;
    }

    openGtmotiveModal(type: GT_TYPE) {
        if (this.searchByVin) {
            this.gotoSearchVin(type, true);
            return;
        }
        this.gtType = type;
        this.bsModalService.show(SagGtmotiveVinWarningModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                gtType: this.gtType,
                openGtmotive: () => this.openGtmotive(type),
                enterVin: () => this.router.navigate(['home']),
                cancel: () => { }
            }
        });
    }

    onHaynesProSearch(result) {
        this.integrationService.eventSource = ARTICLE_EVENT_SOURCE.EVENT_SOURCE_HP;
        this.switchTab();
        this.onHaynesProError = !result;
    }

    private gotoSearchVin(type: GT_TYPE, searchByVin = false) {
        this.resetTreeOnDestroy = false;
        this.gtmotiveService.allArticles = this.articleInContextService.articles;

        const selectedCategoryIds = this.categoryTreeService.getSelectedCategoryIds();
        const currentSelectedCategiries = [...this.categoryTreeService.currentSelectedCategiries];
        this.gtmotiveService.allSelectedCategoryIds = intersection(selectedCategoryIds, currentSelectedCategiries);
        this.gtmotiveService.queuedCategoryIds = difference(selectedCategoryIds, currentSelectedCategiries);

        const params = {
            vinCode: this.queryParams.vinCode,
            estimateId: this.queryParams.estimateId || this.gtmotiveService.estimateId,
            searchByVin,
            type,
            vehicleId: this.vehid,
            returnUrl: get(this.activatedRoute, 'snapshot.queryParams.returnUrl')
        };
        this.router.navigate(['vin', omitBy(params, p => p === undefined || p === null)]);
    }

    private openGtmotive(type: GT_TYPE) {
        if (!this.vehid) {
            return;
        }

        if (this.gtmotiveService.isMultiGTcodes) {
            this.bsModalService.show(SagGtmotiveMultiGraphicModalComponent, {
                ignoreBackdropClick: true,
                initialState: {
                    gtLinks: this.gtmotiveService.gtLinks,
                    openGtmotiveForGtCode: (gteCodes) => this.openGtmotiveForGTcode(type, gteCodes)
                }
            });

            return;
        }

        if (this.gtmotiveService.gtmotiveData && this.gtmotiveService.gtmotiveData.equipmentItems) {
            this.gotoSearchVin(type);
        } else {
            const spinner = this.config.spinner.start('sag-in-context-articles-result-list .result', {
                containerMinHeight: 200,
                class: 'fixed-top'
            });
            this.gtmotiveService.openGtmotive()
                .pipe(
                    takeUntil(this.destroy$),
                    catchError((error) => {
                        this.handleErrorMessage(error);
                        return of(null);
                    }),
                    finalize(() => {
                        if (this.gtmotiveService.gteCodes.equipments) {
                            this.gotoSearchVin(type);
                        }
                        this.config.spinner.stop(spinner);
                    })
                )
                .subscribe();
        }
    }

    private openGtmotiveForGTcode(type: GT_TYPE, gteCodes: any) {
        if (!this.vehid) {
            return;
        }

        this.gtmotiveService.openGtmotiveForGTcode(gteCodes)
            .pipe(
                takeUntil(this.destroy$),
                catchError((error) => {
                    this.handleErrorMessage(error);
                    return of(null);
                }),
                finalize(() => {
                    if (this.gtmotiveService.gteCodes.equipments) {
                        this.gotoSearchVin(type);
                    }
                })
            ).subscribe();
    }

    private colectArticlesFromGtmotive(request) {
        this.gtmotiveService.colectArticlesFromGtmotive(request)
            .pipe(
                takeUntil(this.destroy$),
                finalize(() => this.config.spinner.stop())
            )
            .subscribe((res: any) => {
                const resArticles = get(res, 'data.articles.content', []);
                const cupiArticles = get(res, 'data.cupisResponse.articles.content', []);
                const cateIdsFromCupis = get(res, 'data.cupisResponse.cateIds', []);
                const directMatches = get(res, 'data.directMatches.content', []);

                this.gtNonMatchedOperations = get(res, 'data.nonMatchedOperations');
                this.normauto = get(res, 'data.normauto');
                this.mailContent = get(res, 'data.mailContent');

                this.categoryTreeReadyObserve = this.categoryTreeService.readyStateObservable
                    .pipe(takeUntil(this.destroy$))
                    .subscribe(ready => {
                        if (!ready) {
                            return;
                        }

                        this.articleInContextService.directMatches = directMatches;

                        const gtmArticles = uniqBy([...resArticles, ...cupiArticles], 'pimId');
                        gtmArticles.forEach(art => {
                            art.source = ARTICLE_EVENT_SOURCE.EVENT_SOURCE_GT;
                        });

                        const gtmGaids = uniq((resArticles || []).map(item => item.gaID));
                        const gtmCategories = this.categoryTreeService.getCategoriesByGaids(gtmGaids).map(c => c.id);

                        const gtmCp = uniqWith((resArticles || []).map(item => item.cupi), isEqual);
                        const cpCategories = this.categoryTreeService.getCategoriesByCupis(gtmCp);

                        if (cpCategories.length === 0) {
                            this.logVinError(gtmCp);
                        }

                        const gtmMatchedCategories = intersection(gtmCategories, cpCategories)

                        const allArticles = uniqBy([...gtmArticles, ...this.gtmotiveService.allArticles], 'pimId');

                        this.categoryTreeService.checkOnCategoryTree(
                            [...this.gtmotiveService.allSelectedCategoryIds, ...gtmMatchedCategories, ...cateIdsFromCupis],
                            true,
                            false
                        );

                        this.categoryTreeService.updateCurrentSelectedCateIds();

                        const selectedCategories = this.categoryTreeService.getCheckedCategories();

                        const incomingGroupedArticles = this.articleInContextService.groupArticle(
                            allArticles,
                            selectedCategories,
                            [],
                            this.vehid
                        );
                        const currentGroupedArticles = this.articleInContextService.groupArticle(
                            this.gtmotiveService.allArticles,
                            selectedCategories,
                            [],
                            this.vehid
                        );

                        currentGroupedArticles.forEach(group => {
                            const incomingGroup = incomingGroupedArticles.find(item => item.key === group.key);
                            if (incomingGroup) {
                                group.values.forEach(sub => {
                                    const incomingSub = incomingGroup.values.find(item => item.key === sub.key);
                                    if (incomingSub) {
                                        sub.values = incomingSub.values;
                                    }
                                });
                            }
                        });

                        const currentSelectedCategiries = this.categoryTreeService.currentSelectedCategiries || [];
                        const queuedCategoryIds = this.gtmotiveService.queuedCategoryIds || [];
                        let isEmptyCurrentGroupedArticles = currentGroupedArticles && currentGroupedArticles.length === 0;
                        let isHaveCurrentSelectedCategiries = currentSelectedCategiries.length > 0 || queuedCategoryIds.length > 0;
                        
                        if (isEmptyCurrentGroupedArticles && isHaveCurrentSelectedCategiries) {
                            this.articleInContextService.emitData(undefined);
                        } else {
                            this.articleInContextService.emitData(currentGroupedArticles);
                        }

                        if (queuedCategoryIds.length > 0) {
                            this.categoryTreeService.checkOnCategoryTree([...this.gtmotiveService.queuedCategoryIds], true, true);
                            this.gtmotiveService.queuedCategoryIds = [];
                        }

                        this.integrationService.eventSource = ARTICLE_EVENT_SOURCE.EVENT_SOURCE_GT;
                        this.integrationService.sendCategoryEventData(
                            this.categoryTreeService.categoriesArray,
                            this.categoryTreeService.getCheckedCategories(),
                            this.vehicle
                        );

                        try {
                            (this.gtNonMatchedOperations || []).forEach(item => {
                                const operation = (this.gtRequest && this.gtRequest.operations || []).find(o => o.reference === item.reference);
                                
                                this.gtmotiveService.logVinError({
                                    type: 'VIN_ERROR_NON_MATCH_OE',
                                    oeNr: item && item.reference,
                                    vin: this.vehicle.vin,
                                    cupi: operation && operation.cupi,
                                    umc: get(this.gtmotiveService, 'gteCodes.umc'),
                                    returnedData: JSON.stringify(this.gtRequest.partCodes || [])
                                }).subscribe();
                            });
                        } catch (error) { }

                        this.switchTab();
                    });
            });
    }

    switchToArticleList() {
        this.categoryTreeService.emitSearchRequest();
    }

    goBack() {
        this.router.navigate(['/home']);
    }

    sendAdsEvent() {
        this.integrationService.sendAdsEventData();
    }

    toggleSingleSelectMode() {
        this.integrationService.toggleSingleSelectMode();
    }

    onSelectFavoriteItem(item: any) {
        this.selectFavoriteItemEmit.emit(item);
    }

    private oilChecked(params: any) {
        const cateIds = params.cateIds;
        const spinner = this.config.spinner.start();
        const genArtIds = this.categoryTreeService.getSelectedGaIds(cateIds);
        const body = {
            vehIds: this.vehid,
            genArtIds,
            selectedOilIds: [],
            selectedCategoryIds: cateIds,
            size: 100,
            selectedFromQuickClick: true
        };
        this.getArticles(body).pipe(
            catchError(error => {
                this.categoryTreeService.unCheckAllOilCate();
                // show error message
                this.bsModalService.show(SagConfirmationBoxComponent, {
                    ignoreBackdropClick: true,
                    initialState: {
                        message: 'OLYSLAGER.MESSAGE.API_ERROR',
                    },
                    class: 'modal-sm'
                });
                params.error(cateIds);
                return of({} as any);
            }),
            finalize(() => {
                this.config.spinner.stop(spinner);
                this.isUpdateAvail = false;
            })
        ).subscribe(({ type, data, selectedOil, selectedCateId }) => {
            if (type === 'oilTypeIds') {
                this.showOlyslagerPopup({ data, selectedOil, selectedCateId }, params.success, params.error);
            } else if (type === 'article') {
                this.articleInContextService.emitData(data);
            }
        });
    }

    private requestArticles(request) {
        const currentSelectedCateIds = this.categoryTreeService.currentSelectedCategiries;
        const checkedCateIds = this.isUpdateAvail ? currentSelectedCateIds : difference(request.cateIds, currentSelectedCateIds);
        const uncheckedCateIds = this.isUpdateAvail ? [] : difference(currentSelectedCateIds, request.cateIds);
        const body: SagInContextSearchRequest = this.getBodyRequest(checkedCateIds);
        let requestObservable;
        if (!body.genArtIds) {
            requestObservable = of({
                type: 'article',
                data: []
            });
        } else {
            this.gtRequest = null;
            requestObservable = this.getArticles(body);
        }
        return requestObservable.pipe(
            takeUntil(this.destroy$),
            map((res: any) => {
                const selectedCates = this.categoryTreeService.getCheckedCategories();

                (res.data || []).forEach(art => {
                    art.source = this.integrationService.eventSource;
                });

                const data = this.articleInContextService.groupArticle(res.data || [], selectedCates, uncheckedCateIds, this.vehid);
                // checking artilce in shopping cart
                if (selectedCates.length === 0) {
                    if (!this.onHaynesProError) {
                        this.articleInContextService.emitData(undefined);
                    }
                } else {
                    this.articleInContextService.emitData(data);
                }
                if (body.genArtIds) {
                    this.integrationService.sendCategoryEventData(
                        this.categoryTreeService.categoriesArray,
                        selectedCates,
                        this.vehicle
                    );
                } else {
                    this.integrationService.eventSource = ARTICLE_EVENT_SOURCE.EVENT_SOURCE_USER;
                }
                this.categoryTreeService.updateCurrentSelectedCateIds();
                this.onHaynesProError = false;
                this.articleInContextService.hasPositionWithCvpValueIsNull = false;
                return true;
            }),
            catchError(error => {
                this.articleInContextService.emitData(null);
                this.handleErrorWhenCvpIsNull(error);
                return of(false);
            })
        );
    }

    private showOlyslagerPopup({ data, selectedOil, selectedCateId }, success, rollback) {
        if (!!this.olyslagerRef) {
            return;
        }
        if (data.length > 0) {
            this.olyslagerRef = this.bsModalService.show(SagArticleListOlyslagerPopupComponent, {
                ignoreBackdropClick: true,
                keyboard: false,
                initialState: {
                    data,
                    onClosed: (oils) => {
                        if (oils) {
                            if (success) {
                                success([...oils, ...selectedOil]);
                            }
                        } else {
                            rollback(selectedCateId);
                        }
                        this.olyslagerRef = null;
                    }
                }
            });
        } else {
            success(selectedOil);
        }
    }

    private getArticles(body: SagInContextSearchRequest) {
        return this.articleInContextService.getArticles(body);
    }

    private getBodyRequest(checkedCateIds: string[]) {
        const gaIds = this.categoryTreeService.getSelectedGaIds(checkedCateIds);
        const selectedOilIds = this.libStorage.selectedOils;
        return {
            genArtIds: gaIds,
            selectedOilIds: (selectedOilIds || []).map(oil => oil.value),
            selectedCategoryIds: [],
            size: 100,
            vehIds: this.vehid,
            isSelectedFromQuickClick: false
        } as SagInContextSearchRequest;
    }

    private switchTab() {
        this.router.navigate(['articles'], { relativeTo: this.activatedRoute, preserveQueryParams: true });
    }

    private setDefaultTab() {
        const isFromFavorite = this.queryParams.source === VEHICLE_SOURCE.FAVORITE;
        if (isFromFavorite) {
            return;
        }
        const classicCategoryView = this.storage.classicViewMode;
        if (classicCategoryView) {
            this.router.navigate(['classic'], { relativeTo: this.activatedRoute, preserveQueryParams: true });
        } else {
            this.router.navigate(['quick-click'], { relativeTo: this.activatedRoute, preserveQueryParams: true });
        }
    }

    private setVehicleContext(vehicleDoc?: any) {
        return this.integrationService.updateVehicleContext(vehicleDoc);
    }

    private handleErrorMessage(error): void {
        let code = error.code || error.status;

        if (!code) {
            code = error.error;
        }

        switch (code) {
            case SAG_COMMON_RESPONSE_CODE.NOT_FOUND:
            case SAG_COMMON_RESPONSE_CODE.BADREQUEST:
                this.errorMessage = 'SEARCH.ERROR_MESSAGE.VEHICLE_NOT_FOUND';
                break;
            case SAG_COMMON_RESPONSE_CODE.ACCESS_DENIED:
                this.errorMessage = 'SEARCH.ERROR_MESSAGE.PERMISSION';
                break;
            default:
                this.errorMessage = 'ERROR_500';
        }
    }

    private handleErrorWhenCvpIsNull(error: any) {
        this.articleInContextService.hasPositionWithCvpValueIsNull = false;
        if (error && error.message === CVP_VALUE_IS_MISSING_ERR_MSG) {
            this.articleInContextService.hasPositionWithCvpValueIsNull = true;
            this.categoryTreeService.updateCurrentSelectedCateIds();
        }
    }

    private logVinError(cupis: any[]) {
        try {
            if (!cupis || cupis.length === 0) {
                return;
            }
            cupis.forEach(cupi => {
                const umc = get(this.gtmotiveService, 'gteCodes.umc');
                const catesByCupi = this.categoryTreeService.categoriesArray.filter(cate => {
                    if (cate.children || !cate.genArts) {
                        return false;
                    }
        
                    return cate.genArts.some(ga =>
                        ga.cupis.some(caCp =>
                            cupi.cupi === caCp.cupi
                                && (cupi.loc || '').trim().toLowerCase() === (caCp.loc || '').trim().toLowerCase()
                        )
                    );
                });

                if (catesByCupi.length > 0) {
                    // log location not match
                    this.gtmotiveService.logVinError({
                        type: 'LOCATION_NOT_FOUND',
                        vin: this.vehicle.vin,
                        cupi: cupi.cupi,
                        umc,
                        returnedData: JSON.stringify(this.gtRequest.partCodes || [])
                    }).subscribe();
                } else {
                    // log cupi not match
                    this.gtmotiveService.logVinError({
                        type: 'CUPI_NOT_MATCH_GAID',
                        vin: this.vehicle.vin,
                        cupi: cupi.cupi,
                        umc,
                        returnedData: JSON.stringify(this.gtRequest.partCodes || [])
                    }).subscribe();
                }
            });
        } catch (error) { }
    }
}
