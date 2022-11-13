import { Pipe, PipeTransform } from '@angular/core';
import { CategoryModel } from 'sag-article-detail';
import { ArticleSortUtil } from '../utils/article-sort.util';

@Pipe({
    name: 'classicCategoryColumn'
})
export class SagArticleListClassicCategoryColumnPipe implements PipeTransform {

    transform(categories: CategoryModel[]) {
        const groupedCategories = ArticleSortUtil.groupBy(categories, (category: CategoryModel) => [category.classicCol]);
        return groupedCategories.sort((a: CategoryModel[], b: CategoryModel[]) => Number(a[0].classicCol) - Number(b[0].classicCol));
    }
}
