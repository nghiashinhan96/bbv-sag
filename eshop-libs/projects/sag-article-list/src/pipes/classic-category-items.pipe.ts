import { Pipe, PipeTransform } from '@angular/core';
import { CategoryModel } from 'sag-article-detail';
import { CLASSIC_CATEGORY_STEP } from '../enums/classic-category.enum';

@Pipe({
    name: 'classicCategoryItems',
})
export class SagArticleListClassicCategoryItemsPipe implements PipeTransform {
    transform(categories: Array<CategoryModel>, step: CLASSIC_CATEGORY_STEP) {
        return categories.map(category => {
            switch (step) {
                case CLASSIC_CATEGORY_STEP.ONE:
                    if (!category.parentId && category.children) {
                        return this.getParentCatFormat(category, step);
                    } else {
                        return this.getChildrenCatFormat(category);
                    }
                case CLASSIC_CATEGORY_STEP.TWO:
                    if (category.children) {
                        return this.getParentCatFormat(category, step);
                    } else {
                        return this.getChildrenCatFormat(category);
                    }
            }
        });
    }

    getParentCatFormat(category: CategoryModel, step: CLASSIC_CATEGORY_STEP) {
        const cat = {
            type: 'parent',
            content: category
        };

        if (category.show === undefined) {
            if (step === CLASSIC_CATEGORY_STEP.ONE) {
                category.show = this.checkHasOpenStatus(category.children) > 0;
            } else {
                category.show = true;
            }
        }

        return cat;
    }

    getChildrenCatFormat(category: CategoryModel) {
        const cat = {
            type: 'children',
            content: category,
            catCriterias: category.criterias ? JSON.stringify(category.criterias) : '',
            catBrands: category.genArts && category.genArts.length ? JSON.stringify(category.getCateBrands()) : '',
        };

        return cat;
    }

    checkHasOpenStatus(categories: Array<CategoryModel>) {
        const catsHasOpenStatus = categories.filter((category) => {
            return category.open === '1';
        });

        return catsHasOpenStatus.length;
    }
}
