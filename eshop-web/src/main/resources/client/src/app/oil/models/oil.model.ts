import { OilAggregations } from './oil-aggregations.model';

export class OilModel {
    public content: OilAggregations = new OilAggregations();
    public totalElements: number;
    public viewArticles: boolean;

    constructor(res?: any) {
        if (!res) {
            return;
        }
        this.content = new OilAggregations(res.aggregations);
        this.totalElements = res.total_elements;
        this.viewArticles = res.view_articles;
    }
}
