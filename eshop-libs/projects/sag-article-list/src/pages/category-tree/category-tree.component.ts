import { Component, OnInit, Input, SimpleChanges, OnChanges, Output, EventEmitter, OnDestroy, AfterViewInit, ViewChildren, QueryList } from '@angular/core';
import { CategoryTreeService } from '../../services/category-tree.service';
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/internal/operators/tap';
import { ArticleListConfigService } from '../../services/article-list-config.service';
import { ArticleListStorageService } from '../../services/article-list-storage.service';
import { catchError } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';
import { WSP_LINK_TYPES, WSP_SHOP_LINK } from './../../consts/wsp-link-type.const';
import { ActivatedRoute } from '@angular/router';
import { ArticleModel } from 'sag-article-detail';
import { SagArticleListCategoryTreeItemComponent } from '../../components/category-tree-item/category-tree-item.component';
import { SubSink } from 'subsink';
@Component({
    selector: 'sag-article-list-category-tree',
    templateUrl: './category-tree.component.html',
    styleUrls: ['./category-tree.component.scss']
})
export class SagArticleListCategoryTreeComponent implements OnInit, OnChanges, OnDestroy, AfterViewInit {
    mainLabel = 'CATEGORY.TAB.ALL_PARTS';
    errorMessage = '';

    @Input() set vehicleData(data) {
        if (data) {
            this.getVehicleCategories(data);
        }
    }

    @Input() set categoryLabel(label) {
        if (label) {
            this.mainLabel = label;
        }
    }

    @Input() treeId: string;
    @Input() userDetailSetting: any;
    @Input() searchProductCategory = false;

    @Input() isSingleLink: boolean;
    @Input() hideSearchBox: boolean;
    @Input() hasResetFilter: boolean = true;

    @Output() handleCategorySearch = new EventEmitter();
    @Output() listCategoryTreeItemRendered = new EventEmitter();

    @ViewChildren(SagArticleListCategoryTreeItemComponent) listCategoryTreeItem: QueryList<SagArticleListCategoryTreeItemComponent>;

    categories$: Observable<any>;
    isSelected = false;
    vehicle;

    subs = new SubSink();
    
    constructor(
        private categoryTreeService: CategoryTreeService,
        private config: ArticleListConfigService,
        private libStorage: ArticleListStorageService,
        private activeRouter: ActivatedRoute
    ) {
        this.categoryTreeService.hasSelectedCateObservable.subscribe(hasSelectedCate => {
            this.isSelected = hasSelectedCate;
        });
    }

    ngOnInit() {
        if (this.treeId) {
            this.getUniversalCategories(this.treeId);
        }
    }

    ngAfterViewInit(): void {
        if (this.listCategoryTreeItem && this.listCategoryTreeItemRendered) {
            this.subs.sink = this.listCategoryTreeItem.changes.subscribe(treeItems => {
                if (treeItems.length > 0) {
                    this.listCategoryTreeItemRendered.emit();
                }
            })
        }
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.treeId && changes.treeId.currentValue && !changes.treeId.firstChange) {
            this.treeId = changes.treeId.currentValue;

            this.getUniversalCategories(this.treeId);
        }
    }

    ngOnDestroy(): void {
        this.categoryTreeService.isCategoriesNotFound$.next(false);
        this.subs.unsubscribe();
    }

    resetTree() {
        this.categoryTreeService.resetCategoryTree();
    }

    private getVehicleCategories(data) {
        const val = data.vehicleId;
        const isGtmotive = data.isGtmotive;
        const syncCategories = data.syncCategories || true;

        this.vehicle = val;
        this.config.spinner.start('sag-article-list-category-tree .card-body');
        this.errorMessage = '';
        this.categories$ = this.categoryTreeService.getCategories(val, isGtmotive, syncCategories).pipe(
            catchError(err => {
                if (err.status === 404) {
                    this.categoryTreeService.isCategoriesNotFound$.next(true);
                }
                this.categoryTreeService.markedTreeState(true);
                return of(null);
            }),
            tap((res) => {
                this.config.spinner.stop('sag-article-list-category-tree .card-body');
                const accessoryArticle = this.libStorage.accessoryArticle ? new ArticleModel(this.libStorage.accessoryArticle) : null;
                const matchCategories = this.categoryTreeService.getMatchedCategoriesForArticle(accessoryArticle) || [];
                const cateids = matchCategories.map(c => c.id);
                this.libStorage.accessoryArticle = null;
                const selectedCateIds = [...this.libStorage.selectedCateIds || [], ...cateids];
                if (selectedCateIds && selectedCateIds.length > 0) {
                    const oilsCate = this.categoryTreeService
                        .getCheckedCategories(selectedCateIds)
                        .filter(cate => cate.oilCate).map(cate => cate.id);
                    this.categoryTreeService.checkOnCategoryTree(selectedCateIds, true, oilsCate.length === 0);
                    if (oilsCate.length > 0) {
                        this.categoryTreeService.emitCheckOilCate(
                            oilsCate,
                            true,
                            oils => {
                                this.libStorage.selectedOils = oils;
                                this.categoryTreeService.emitSearchRequest();
                            },
                            oils => {
                                this.libStorage.removeAllSelectedOil();
                                this.categoryTreeService.checkOnCategoryTree(oilsCate, false, true);
                            }
                        );
                    }
                }
            }),
        );
    }

    private getUniversalCategories(treeId: string) {
        this.config.spinner.start('sag-article-list-category-tree .card-body');
        this.errorMessage = '';
        const nodeId = this.activeRouter.snapshot.queryParams['nodeId'] || null;

        this.categories$ = this.categoryTreeService.getUniversalCategories(treeId, nodeId).pipe(
            catchError(err => {
                return of(null);
            }),
            tap((res) => {
                this.config.spinner.stop('sag-article-list-category-tree .card-body');
            }),
            map(data => {
                if(data) {
                    data = this.formatNode(data);
                }

                return data;
            })
        );
    }

    private formatNode(categories: any[]) {
        (categories || []).forEach(item => {
            this.handleWspNodeType(item);

            if (item.children && item.children.length) {
                this.formatNode(item.children);
            }
        });

        return categories;
    }

    private handleWspNodeType(category: any) {
        switch(category.nodeExternalType) {
            case WSP_LINK_TYPES.service:
                if(category.nodeExternalServiceAttribute === 'thule') {
                    const link = this.userDetailSetting && this.userDetailSetting.externalUrls && this.userDetailSetting.externalUrls.thule || '';
                    category.link = link;
                    category.target = '_self';
                }

                break;
            case WSP_LINK_TYPES.shop:
                category.navigate = WSP_SHOP_LINK[category.nodeExternalServiceAttribute] || '';
                category.link = category.navigate;

                break;
            case WSP_LINK_TYPES.partner:
            case WSP_LINK_TYPES.link:
                category.link = category.nodeExternalServiceAttribute || '';
                category.target = '_blank';

                break;
            default:
                break;
        }
    }
}
