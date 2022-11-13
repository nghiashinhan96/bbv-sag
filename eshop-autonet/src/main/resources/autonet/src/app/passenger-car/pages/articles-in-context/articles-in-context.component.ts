import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs/internal/Subject';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/core/services/user.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { map } from 'rxjs/internal/operators/map';
import { tap } from 'rxjs/internal/operators/tap';
import { debounceTime } from 'rxjs/internal/operators/debounceTime';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { takeUntil } from 'rxjs/internal/operators/takeUntil';
import { ArticleInContextSearchRequest } from './article-in-context-search-request.interface';
import { difference } from 'lodash';
import { of } from 'rxjs/internal/observable/of';
import { catchError, finalize } from 'rxjs/operators';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { AutonetCommonService } from 'src/app/shared/autonet-common/autonet-common.service';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AppMessageDialogComponent } from 'src/app/shared/autonet-common/dialogs/app-message-dialog/app-message-dialog.component';
import { ArticleInContextService } from './services/articles-in-context.service';
import { ArticleListStorageService, ARTICLE_LIST_TYPE, CategoryTreeService, OlyslagerModel, SagArticleListOlyslagerPopupComponent, SEARCH_MODE } from 'sag-article-list';
import { BroadcastService } from 'sag-common';
import { ArticleBasketModel, ArticleBroadcastKey } from 'sag-article-detail';
import { UrlUtil } from 'src/app/core/utils/url.util';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';
import { Subscription } from 'rxjs';

@Component({
    selector: 'autonet-articles-in-context',
    templateUrl: './articles-in-context.component.html',
    styleUrls: ['./articles-in-context.component.scss']
})
export class ArticleInContextComponent implements OnInit, OnDestroy {
    articleListType = ARTICLE_LIST_TYPE.IN_CONTEXT;
    vehicleId$;
    vehicle;

    isRequestOfferMode = false;
    requestOfferUrl = `${UrlUtil.autonetServer(this.appStorage.country)}portal/OFERTAADD.aspx`;
    requestOfferParams: any;
    isSyncCategories = false;

    private vehicleId: string;
    private destroy$ = new Subject();
    private olyslagerRef: BsModalRef;
    private selectedCateSub: Subscription;
    constructor(
        private categoryTreeService: CategoryTreeService,
        private router: Router,
        private articleInContextService: ArticleInContextService,
        private activatedRoute: ActivatedRoute,
        private autonetCommonService: AutonetCommonService,
        private articleListBroadcastService: BroadcastService,
        private userService: UserService,
        private libStorage: ArticleListStorageService,
        private appContext: AppContextService,
        private modalService: BsModalService,
        private appStorage: AppStorageService
    ) {
        this.setDefaultTab();
    }

    ngOnInit() {
        this.autonetCommonService.isInArticleListPage = true;
        this.vehicleId = this.appStorage.vehicleId;
        // reset
        this.vehicleId$ = this.activatedRoute.params.pipe(
            takeUntil(this.destroy$),
            map(({ vehicleId }) => {
                if (!!this.vehicleId && this.vehicleId !== vehicleId) {
                    if (this.selectedCateSub) {
                        this.selectedCateSub.unsubscribe();
                    }
                    this.isRequestOfferMode = false;
                    this.resetTree(true);
                    this.isSyncCategories = true;
                    this.onSelectedCate();
                } else {
                    this.isSyncCategories = false;
                }
                this.vehicleId = vehicleId;
                this.appStorage.vehicleId = this.vehicleId;
                // set search context
                this.articleInContextService.getVehiclesByVehId(vehicleId).pipe(switchMap((vehicle: any) => {
                    this.vehicle = vehicle;
                    const autonet = this.appStorage.autonet as AutonetAuthModel;
                    this.requestOfferParams = {
                        sid: '',
                        14: autonet.lid,
                        uid: autonet.uid,
                        khernr: vehicle.id_make,
                        manufacturer: vehicle.vehicle_brand,
                        kmodnr: vehicle.id_model,
                        model: vehicle.vehicle_model,
                        ktypnr: vehicle.ktype,
                        typ: vehicle.vehTypeDesc,
                        enginecode: vehicle.vehicle_engine_code,
                        capacity: vehicle.vehicle_capacity_cc_tech,
                        cyl: vehicle.vehicle_zylinder,
                        enginemode: '',
                        constryear: vehicle.vehicle_built_year_from,
                        KW: vehicle.vehicle_power_kw,
                        HP: vehicle.vehicle_power_hp,
                        motNr: '',
                        FT: vehicle.vehicle_fuel_type,
                        vehid: vehicleId
                    };
                    const pageUrl = AppHelperUtil.objectToUrl(this.requestOfferUrl, this.requestOfferParams);
                    this.requestOfferParams['400'] = pageUrl;
                    return this.setVehicleContext(vehicle);
                })).subscribe(status => {
                    // console.log(status);
                });
                return { vehicleId: this.vehicleId, syncCategories: this.isSyncCategories };
            }));

        this.onSelectedCate();

        this.categoryTreeService.checkOilCateObservable.pipe(
            takeUntil(this.destroy$),
        ).subscribe((data: any) => {
            this.oilChecked(data);
        });

        this.articleListBroadcastService.on(ArticleBroadcastKey.TOGGLE_PRICE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((libUserSetting: any) => {
                this.userService.updatePriceSetting(libUserSetting);
            });

        this.articleListBroadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((code: string) => {
                this.router.navigate(['../../../', 'article'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code },
                    relativeTo: this.activatedRoute
                });
            });

        this.articleListBroadcastService.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((vehicleId: string) => {
                this.router.navigate(['../', vehicleId], { relativeTo: this.activatedRoute });
                this.articleInContextService.resetResultList();
                this.articleInContextService.emitData([]);
            });

        this.articleListBroadcastService.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .pipe(takeUntil(this.destroy$))
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'ADD':
                        this.autonetCommonService.addToCart(data.article);
                        break;
                }
            });

        setTimeout(() => {
            this.autonetCommonService.enableFreetextSearch(true);
        });
    }

    async ngOnDestroy() {
        this.autonetCommonService.isInArticleListPage = false;
        this.destroy$.next(true);
        this.destroy$.complete();
        this.resetTree();
        this.appStorage.vehicleId = null;
        this.autonetCommonService.enableFreetextSearch(false);
        await this.setVehicleContext().toPromise();
    }

    onSelectedCate() {
        this.selectedCateSub = this.categoryTreeService.selectedCategoriesObservable.pipe(
            takeUntil(this.destroy$),
            tap(() => {
                this.switchTab();
            }),
            debounceTime(600),
            switchMap(request => {
                SpinnerService.start('autonet-articles-in-context .result');
                return this.requestArticles(request);
            })
        ).subscribe(status => {
            SpinnerService.stop('autonet-articles-in-context .result');
        });
    }

    private resetTree(stateTree = false) {
        this.libStorage['olyslagerId'] = null;
        this.libStorage.selectedCateIds = null;
        this.categoryTreeService.resetCategoryTree();
        this.categoryTreeService.markedTreeState(stateTree);
        this.articleInContextService.resetResultList();
    }

    goBack() {
        this.router.navigate(['/cars']);
    }

    onOpenRequestPriceOffer() {
        this.isRequestOfferMode = true;
    }

    onSelectFavoriteItem(item: any) {
        if (item.type === 'ARTICLE') {
            const queryParams: any = {
                type: SEARCH_MODE.FREE_TEXT,
                articleId: item.articleId
            };
            this.router.navigate(['../../../', 'article'], {
                queryParams: queryParams,
                relativeTo: this.activatedRoute
            });
        }
    }

    private oilChecked(data: any) {
        const cateId = data.cate.id;
        if (!this.libStorage['olyslagerId']) {
            SpinnerService.start();
            const body: ArticleInContextSearchRequest = this.getBodyRequest([cateId]);
            this.getArticles(body).pipe(
                catchError(error => {
                    SpinnerService.stop();
                    data.cate.isChecked = false;
                    this.categoryTreeService.unCheckAllOilCate();
                    // show error message
                    this.modalService.show(AppMessageDialogComponent, {
                        initialState: {
                            messageCode: 'OLYSLAGER.ERROR_MESSAGE',
                            titleCode: ''
                        },
                        class: 'modal-sm'
                    });
                    return of(null);
                })
            ).subscribe(res => {
                SpinnerService.stop();
                if (res && res.type === 'olyslager') {
                    this.showOlyslagerPopup(res.data, (id: string) => {
                        if (id) {
                            this.libStorage['olyslagerId'] = id;
                            if (data.emitSearchEvent) {
                                this.categoryTreeService.emitSearchRequest();
                            }
                        } else {
                            // uncheck oilCate on quickClick
                            data.cate.isChecked = false;
                            this.categoryTreeService.unCheckAllOilCate();
                        }
                    });
                } else if (res && res.type === 'article') {
                    if (data.emitSearchEvent) {
                        this.categoryTreeService.emitSearchRequest();
                    }
                }
            });
        }
    }

    private requestArticles(request) {
        const currentSelectedCateIds = this.categoryTreeService.currentSelectedCategiries;
        const checkedCateIds = difference(request.cateIds, currentSelectedCateIds);
        const uncheckedCateIds = difference(currentSelectedCateIds, request.cateIds);
        const body: ArticleInContextSearchRequest = this.getBodyRequest(checkedCateIds);
        let requestObservable;
        if (!body.genArtIds) {
            requestObservable = of({
                type: 'article',
                data: []
            });
        } else {
            requestObservable = this.getArticles(body);
        }
        return requestObservable.pipe(
            takeUntil(this.destroy$),
            map((res: any) => {
                const selectedCates = this.categoryTreeService.getCheckedCategories();
                const data = this.articleInContextService.groupArticle(res.data || [], selectedCates, uncheckedCateIds);
                if (selectedCates.length === 0) {
                    this.articleInContextService.emitData(undefined);
                } else {
                    this.articleInContextService.emitData(data);
                }
                return true;
            }),
            finalize(() => this.categoryTreeService.updateCurrentSelectedCateIds()),
            catchError(error => {
                this.articleInContextService.emitData(null);
                return of(false);
            })
        );
    }

    private showOlyslagerPopup(data: OlyslagerModel[], callback) {
        if (!!this.olyslagerRef) {
            return;
        }
        if (!!data && data.length === 1) {
            callback(data[0].id);
        } else {
            this.olyslagerRef = this.modalService.show(SagArticleListOlyslagerPopupComponent, {
                ignoreBackdropClick: true,
                initialState: {
                    olyslagerTypeIds: data
                }
            });
            this.olyslagerRef.content.closing = (olyslagerId: string) => {
                callback(olyslagerId);
                this.olyslagerRef = null;
            };
        }
    }

    private getArticles(body: ArticleInContextSearchRequest) {
        return this.articleInContextService.getArticles(body);
    }

    private getBodyRequest(checkedCateIds: string[]) {
        const gaIds = this.categoryTreeService.getSelectedGaIds(checkedCateIds);
        return {
            genArtIds: gaIds,
            selectedTypeId: this.libStorage['olyslagerId'],
            size: 100,
            vehIds: this.vehicleId
        } as ArticleInContextSearchRequest;
    }

    private switchTab() {
        this.router.navigate(['articles'], { relativeTo: this.activatedRoute });
    }

    private setDefaultTab() {
        this.router.navigate(['quick-click'], { relativeTo: this.activatedRoute });
    }

    private setVehicleContext(vehicleDoc?: any) {
        return this.appContext.updateVehicleContext(vehicleDoc);
    }
}
