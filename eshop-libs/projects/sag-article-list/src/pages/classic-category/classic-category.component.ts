import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subscription } from 'rxjs';
import { intersection } from 'lodash';
import { CategoryModel } from 'sag-article-detail';
import { CLASSIC_CATEGORY_STEP } from '../../enums/classic-category.enum';
import { CategoryTreeService } from '../../services/category-tree.service';
import { ClassicCategoryService } from '../../services/classic-category.service';
import { ArticleListConfigService } from '../../services/article-list-config.service';
import { ClassCategoryStorage } from './classic-category-storage';
import { ArticleListStorageService } from '../../services/article-list-storage.service';
import { SubSink } from 'subsink';

@Component({
    selector: 'sag-article-list-classic-category',
    templateUrl: 'classic-category.component.html',
    styleUrls: ['classic-category.component.scss']
})
export class SagArticleListClassicCategoryComponent implements OnInit, OnDestroy {
    @Input() vehicleId: string;

    @Output() search = new EventEmitter();

    STEP = CLASSIC_CATEGORY_STEP;

    categories: CategoryModel[] = [];
    selectedCategories: CategoryModel[] = [];

    totalStep2Selected = 0;
    totalStep2Checked = 0;

    errorMessage: string;
    private subs = new SubSink();
    oilStorage = new ClassCategoryStorage();
    constructor(
        private categoryTreeService: CategoryTreeService,
        private config: ArticleListConfigService,
        private storage: ArticleListStorageService,
        public classicCategoryService: ClassicCategoryService
    ) { }

    ngOnInit() {
        const spinner = this.config.spinner.start('sag-article-list-classic-category .classic-category');
        this.subs.sink = this.categoryTreeService.readyStateObservable.subscribe(isReady => {
            if (isReady) {
                if (!this.vehicleId) {
                    return;
                }
                this.categories = this.getClassicCategories();
                this.compareAndCheckForResetCategorySelection();
                this.calculateSelectedCategory();
                this.config.spinner.stop(spinner);
            }
        });
        this.subs.sink = this.categoryTreeService.isCategoriesNotFound$.subscribe(isErrorStatus => {
            if (isErrorStatus) {
                this.errorMessage = 'SEARCH.ERROR_MESSAGE.VEHICLE_NOT_MATCHED';
            } else {
                this.errorMessage = '';
            }
        });
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    getClassicCategories(): CategoryModel[] {
        if (this.classicCategoryService.categories) {
            return this.classicCategoryService.categories;
        }

        let categories: CategoryModel[] = [];

        if (this.categoryTreeService.categories) {
            categories = this.categoryTreeService.categories;
        } else {
            categories = [];
        }
        return this.classicCategoryService.cloneCategories(categories);
    }

    oilCateCheck({ checked, unChecked }) {
        if (checked.length > 0) {
            this.categoryTreeService.emitCheckOilCate(
                checked,
                false,
                oils => {
                    this.oilStorage.selectedOils = oils;
                },
                oils => {
                    this.oilStorage.removeSelectedOil(checked);
                    const ids = oils.map(oil => oil.id);
                    this.categoryTreeService.checkOnCategoryTree(ids, false, false);
                }
            );
        }
        if (unChecked.length > 0) {
            this.oilStorage.removeSelectedOil(unChecked);
            this.categoryTreeService.checkOnCategoryTree(unChecked, false, false);
        }
    }

    selectCategory({
        category,
        checked,
        step,
        optionAll
    }: {
        category: CategoryModel,
        checked: boolean,
        step: CLASSIC_CATEGORY_STEP,
        optionAll: boolean
    }) {

        category.isChecked = checked;
        switch (step) {
            case CLASSIC_CATEGORY_STEP.ONE:
                if (checked) {
                    if (this.classicCategoryService.isSelectedCategory(category.id)) {
                        return;
                    }
                    if (category.children) {
                        this.classicCategoryService.addSelectedCategory(category, step, optionAll);
                    }
                } else {
                    this.classicCategoryService.removeSelectedCategory([category.id], step);
                }
                break;
            case CLASSIC_CATEGORY_STEP.TWO:
                if (checked) {
                    this.classicCategoryService.addSelectedCategory(category, step);
                } else {
                    this.classicCategoryService.removeSelectedCategory([category.id], step);
                }
                break;
        }

        this.calculateSelectedCategory();
    }

    clearSelectedCategories() {
        this.categoryTreeService.getSelectedCategoryIds().forEach(item => {
            this.categoryTreeService.checkOnCategoryTree([item], false, false);
        });
        this.categories.forEach(category => {
            this.unselectAll(category);
        });
        this.classicCategoryService.reset();
        this.categoryTreeService.resetCategoryTreeInQuickClick();
        this.classicCategoryService.removeAllSelectedCategory(CLASSIC_CATEGORY_STEP.ONE);
        this.classicCategoryService.removeAllSelectedCategory(CLASSIC_CATEGORY_STEP.TWO);
        this.totalStep2Selected = 0;
        this.totalStep2Checked = 0;

    }

    onSearch() {
        this.classicCategoryService.categories = this.classicCategoryService.cloneCategories(this.categories);
        this.categoryTreeService.resetCategoryTree();
        const selectedCategoryIds = this.classicCategoryService.getSelectedIdsOnStep2();
        this.categoryTreeService.checkOnCategoryTree(selectedCategoryIds, true, false);
        this.storage.removeAllSelectedOil();
        this.storage.selectedOils = this.oilStorage.selectedOils;
        this.search.emit();
    }

    private calculateSelectedCategory() {
        const leafCategories = this.getAllSelectedLeaf(this.classicCategoryService.selectedCategoriesOnStep1) || [];
        this.totalStep2Selected = leafCategories.length;
        this.totalStep2Checked = leafCategories.filter(item => item.isChecked).length;
    }

    // Compare selected cateogries on  the category tree, if this is different from current selected classic categories then
    // reset the selected classic categories
    private compareAndCheckForResetCategorySelection() {
        const treeServiceSelectedIds = this.categoryTreeService.getSelectedCategoryIds();
        const classicServiceSelectedIds = this.classicCategoryService.getSelectedIdsOnStep2();
        const intersectionLength = intersection(treeServiceSelectedIds, classicServiceSelectedIds).length;

        if (intersectionLength !== treeServiceSelectedIds.length || intersectionLength !== classicServiceSelectedIds.length) {
            this.classicCategoryService.removeAllSelectedCategory(CLASSIC_CATEGORY_STEP.ONE);
            this.classicCategoryService.removeAllSelectedCategory(CLASSIC_CATEGORY_STEP.TWO);
            this.categories.map(cate => this.unselectAll(cate));
        }
    }

    private getStep1SelectedCategories() {
        let selectedCategories = [];
        this.categories.forEach(category => {
            const categories = (category.children || []).filter(item => item.isChecked);
            if (categories.length) {
                selectedCategories = [...selectedCategories, ...categories];
            }
        });
        return selectedCategories;
    }

    private unselectAllLeaf(category: CategoryModel) {
        if (!category.children) {
            this.categoryTreeService.checkOnCategoryTree([category.id], false, false);
            return;
        }

        category.children.forEach(item => {
            this.unselectAllLeaf(item);
        });
    }

    private unselectAll(category: CategoryModel) {
        category.isChecked = false;
        if (category.children) {
            category.children.forEach(item => {
                this.unselectAll(item);
            });
        }
    }

    private getAllSelectedLeaf(categories: CategoryModel[]): CategoryModel[] {
        let results = [];

        categories.forEach(category => {
            if (category.children) {
                results = [...results, ...this.getAllSelectedLeaf(category.children)];
            } else {
                results = [...results, category];
            }
        });

        return results;
    }
}
