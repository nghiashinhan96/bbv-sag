import { Injectable } from '@angular/core';
import { CategoryModel } from 'sag-article-detail';
import { CLASSIC_CATEGORY_STEP } from '../enums/classic-category.enum';

@Injectable()
export class ClassicCategoryService {
    categories: CategoryModel[];
    selectedCategoriesOnStep1: CategoryModel[] = []; // step 1 selected categories
    selectedCategoriesOnStep2: CategoryModel[] = []; // step 2 selected categories

    constructor() { }

    cloneCategories(categories: CategoryModel[]) {
        return categories.map(item => new CategoryModel(item));
    }

    findCategoryById(categories: CategoryModel[], id: string, recursive = false): CategoryModel {
        if (!categories) {
            return null;
        }

        const category = categories.find(item => item.id === id);

        if (category) {
            return category;
        }

        if (recursive) {
            categories.forEach(item => {
                return this.findCategoryById(item.children || [], id, recursive);
            });
        }

        return null;
    }

    addSelectedCategory(category: CategoryModel, step, optionAll: boolean = false) {
        switch (step) {
            case CLASSIC_CATEGORY_STEP.ONE:
                if (this.findCategoryById(this.selectedCategoriesOnStep1, category.id)) {
                    return;
                }
                this.selectedCategoriesOnStep1.forEach(item => item.show = optionAll);
                category.show = true;
                this.selectedCategoriesOnStep1 = [category, ...this.selectedCategoriesOnStep1];
                break;
            case CLASSIC_CATEGORY_STEP.TWO:
                if (this.findCategoryById(this.selectedCategoriesOnStep2, category.id)) {
                    return;
                }
                this.selectedCategoriesOnStep2 = [category, ...this.selectedCategoriesOnStep2];
                break;
        }

    }

    removeSelectedCategory(categoryIds: string[], step) {
        if (!categoryIds) {
            return;
        }
        switch (step) {
            case CLASSIC_CATEGORY_STEP.ONE:
                let leafChildren = [];
                this.selectedCategoriesOnStep1.forEach(c => {
                    if (categoryIds.indexOf(c.id) >= 0) {
                        this.resetCategory(c);
                        leafChildren = [...this.getAllLeafCategories(c), ...leafChildren];
                    }
                });

                // Remove from selected categories list on step 1
                this.selectedCategoriesOnStep1 = this.selectedCategoriesOnStep1.filter(c => categoryIds.indexOf(c.id) === -1);
                // Get all leaf category of step 1, then remove it if it was selected on step 2
                this.removeSelectedCategory(leafChildren, CLASSIC_CATEGORY_STEP.TWO);
                break;
            case CLASSIC_CATEGORY_STEP.TWO:
                this.selectedCategoriesOnStep2 = categoryIds.length ?
                    this.selectedCategoriesOnStep2.filter(c => categoryIds.indexOf(c.id) === -1)
                    : this.selectedCategoriesOnStep2;
                break;
        }
    }

    removeAllSelectedCategory(step) {
        switch (step) {
            case CLASSIC_CATEGORY_STEP.ONE:
                this.selectedCategoriesOnStep1 = [];
                break;
            case CLASSIC_CATEGORY_STEP.TWO:
                this.selectedCategoriesOnStep2 = [];
                break;
        }
        this.reset();
    }

    isSelectedCategory(id: string, categories: CategoryModel[] = this.selectedCategoriesOnStep1) {
        if (categories.some(item => item.id === id)) {
            return true;
        }

        categories.forEach(item => {
            if (item.children && this.isSelectedCategory(id, item.children)) {
                return true;
            }
        });

        return false;
    }

    collapseAllSelectedCategories() {
        this.selectedCategoriesOnStep1.forEach(category => {
            category.show = false;
        });
    }

    getAllLeafCategories(category: CategoryModel): string[] {
        if (!category) {
            return;
        }

        let leafIds = [];
        if (category.children) {
            category.children.forEach(child => leafIds = [...this.getAllLeafCategories(child), ...leafIds]);
        } else {
            leafIds = [category.id];
        }

        return leafIds;
    }

    hasSelectedLeafCategory(category: CategoryModel) {
        if (!category) {
            return null;
        }

        if (category.children) {
            return category.children.find(item => this.hasSelectedLeafCategory(item));
        } else {
            return category.isChecked;
        }
    }

    reset() {
        (this.categories || []).forEach(category => {
            this.resetCategory(category);
        });
    }

    getSelectedIdsOnStep1() {
        return this.selectedCategoriesOnStep1.map(cate => cate.id);
    }

    getSelectedIdsOnStep2() {
        return this.selectedCategoriesOnStep2.map(cate => cate.id);
    }

    resetAll() {
        this.categories = null;
        this.selectedCategoriesOnStep1 = [];
        this.selectedCategoriesOnStep2 = [];
    }

    private resetCategory(category: CategoryModel) {
        category.isChecked = false;

        if (category.children) {
            category.children.forEach(item => {
                this.resetCategory(item);
            });
        }
    }
}
