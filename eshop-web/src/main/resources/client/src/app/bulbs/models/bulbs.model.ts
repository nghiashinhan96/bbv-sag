import { BulbsAggregations } from './bulb-aggregations.model';

export class BulbsModel {
    public content: BulbsAggregations = new BulbsAggregations();
    public totalElements: number;
    public viewArticles: boolean;

    constructor(res?: any) {
        if (!res) {
            return;
        }
        this.content = new BulbsAggregations(res.aggregations);
        this.totalElements = res.total_elements;
        this.viewArticles = res.view_articles;
    }
}
