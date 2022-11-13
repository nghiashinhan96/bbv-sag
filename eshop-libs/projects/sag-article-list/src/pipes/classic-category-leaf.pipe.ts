import { Pipe, PipeTransform } from '@angular/core';
import { CategoryModel } from 'sag-article-detail';

@Pipe({
    name: 'classicCategoryLeaf'
})
export class SagArticleListClassicCategoryLeafPipe implements PipeTransform {

    transform(categories: Array<CategoryModel>) {
        if (!categories) {
            return [];
        }
        return categories.map(category => {
            if (category.children) {
                category.children = this.getLeafCategories(category);
            }
            return category;
        });
    }

    getLeafCategories(category: CategoryModel) {
        let leafCategories = [];

        if (category.children) {
            category.children.forEach(item => {
                const cats = this.getLeafCategories(item);
                leafCategories = [...leafCategories, ...cats];
            });
        } else {
            leafCategories.push(category);
        }

        return leafCategories;
    }
}