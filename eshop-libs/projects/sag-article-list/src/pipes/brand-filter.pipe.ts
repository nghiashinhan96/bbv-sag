import { Pipe, PipeTransform } from '@angular/core';
import { Criteria } from 'sag-article-detail';
import { ArticleModel } from 'sag-article-detail';

@Pipe({
    name: 'sagArticleListBrandFilter'
})
export class SagArticleListBrandFilterPipe implements PipeTransform {
    transform(articles: ArticleModel[], brands: string[], criterias: Criteria[]) {
    
        let filteredBrands = articles;
        if (brands && brands.length > 0) {
            filteredBrands = (articles || []).filter(art => brands.indexOf(art.supplier) !== -1);
        }

        if (!criterias || criterias.length === 0) {
            return filteredBrands;
        }
        const filteredCriteria = (filteredBrands || []).filter(art => {
            return (art.criteria || []).some(cr => {
               return criterias.some(checkedCr => cr.cid === checkedCr.cid && cr.cvp === checkedCr.cvp);
            })
        })
        return filteredCriteria;
    }
}
