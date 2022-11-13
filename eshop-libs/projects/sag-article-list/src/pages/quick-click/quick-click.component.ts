import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { tap } from 'rxjs/internal/operators/tap';
import { Observable } from 'rxjs/internal/Observable';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs/internal/observable/of';
import { uniq } from 'lodash';
import { CategoryModel } from 'sag-article-detail';
import { ArticleListConfigService } from '../../services/article-list-config.service';
import { QuickClickService } from '../../services/quick-click.service';
import { CategoryTreeService } from '../../services/category-tree.service';
import { ArticleListStorageService } from '../../services/article-list-storage.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';

@Component({
    selector: 'sag-article-list-quick-click',
    templateUrl: './quick-click.component.html',
    styleUrls: ['./quick-click.component.scss']
})
export class SagArticleListQuickClickComponent implements OnInit, OnDestroy {
    errorMessage = '';
    msgForVehicleDoNotMatch = 'SEARCH.ERROR_MESSAGE.VEHICLE_NOT_MATCHED';
    msgForVehicleSelectCateOnLeft = 'SEARCH.ERROR_MESSAGE.SELECT_CATEGORIES_ON_LEFT';
    isCategoriesNotFound = false;
    @Input() set vehicleId(val: string) {
        this.errorMessage = '';
        this.config.spinner.start('sag-article-list-quick-click .quick-click-container');
        this.categories$ = this.quickClickService.getData(val).pipe(
            catchError(err => {
                if (err.status === 404) {
                    this.errorMessage = this.msgForVehicleSelectCateOnLeft;
                    if (this.isCategoriesNotFound) {
                        this.errorMessage = this.msgForVehicleDoNotMatch;
                    }
                }
                return of(null);
            }),
            tap((res) => {
                this.config.spinner.stop('sag-article-list-quick-click .quick-click-container');
                const selectedCateIds = this.categoryTreeService.getSelectedCategoryIds();
                if (selectedCateIds && selectedCateIds.length > 0) {
                    this.quickClickService.checkQuickClick(selectedCateIds);
                }
                this.foldState = (res || []).map(root => {
                    return root && root.some(c => c.qfold);
                });
            }));
    }
    @Input() currentStateSingleSelectMode: boolean;

    @Output() oilClicked = new EventEmitter();
    @Output() performanceSearch = new EventEmitter();

    foldState = []
    categories$: Observable<CategoryModel[][]>;
    isReady = false;
    private subs = new SubSink();
    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private quickClickService: QuickClickService,
        private config: ArticleListConfigService,
        private categoryTreeService: CategoryTreeService,
        private libStorage: ArticleListStorageService
    ) { }

    ngOnInit() {
        this.subs.sink = this.categoryTreeService.readyStateObservable.subscribe(isReady => {
            this.isReady = isReady;
        });
        this.subs.sink = this.categoryTreeService.isCategoriesNotFound$.subscribe(isErrorStatus => {
            this.isCategoriesNotFound = isErrorStatus;
            if (isErrorStatus && this.errorMessage !== '') {
                this.errorMessage = this.msgForVehicleDoNotMatch;
            }
        });
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    searchArticles() {
        this.performanceSearch.emit();
    }

    checkOnTree(element, category: CategoryModel) {
        const isChecked = element.target.checked;
        category.isChecked = isChecked;
        if (category.oilCate) {
            this.categoryTreeService.checkOilCate(category, isChecked, false);
            this.oilClicked.emit({ category, isChecked });
        } else {
            const cateIds = [];
            this.quickClickService.findCateIds(category, cateIds);
            this.categoryTreeService.checkOnCategoryTree(uniq(cateIds), isChecked, false);
        }
        // if unchecked one category -> emit list part remove redundant parts
    }

    handleSelectCategory(category: CategoryModel) {
        if (this.currentStateSingleSelectMode) {
            category.isChecked = true;
            const queryParams = { returnUrl: '../quick-click' };
            if (category.oilCate) {
                this.libStorage.removeAllSelectedOil();
                this.categoryTreeService.unCheckAllCate();
                this.router.navigate([], { relativeTo: this.activatedRoute, queryParams });
                this.categoryTreeService.checkOilCate(category, true, true);
            } else {
                const cateIds = [];
                this.quickClickService.findCateIds(category, cateIds);
                this.router.navigate(['../', 'articles'], { relativeTo: this.activatedRoute, queryParams }).then(() => {
                    this.categoryTreeService.unCheckAllCate();
                    this.categoryTreeService.checkOnCategoryTree(cateIds, true);
                });
            }
        }
    }

    toggle(category: CategoryModel) {
        category.isFolded = !category.isFolded;
    }
}
