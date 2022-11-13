import { PkwTyresAggregations } from './pkw-tyre-aggregations.model';

export class PkwTyresModel {
    public content: PkwTyresAggregations = new PkwTyresAggregations();
    public totalElements: number;
    public viewArticles: boolean;

    constructor(res?: any) {
        if (!res) {
            return;
        }
        this.content = new PkwTyresAggregations(res.aggregations);
        this.totalElements = res.total_elements;
        this.viewArticles = res.view_articles;
    }
}
