import { BatteryAggregations } from './battery-aggregations.model';

export class BatteriesModel {
    public content: BatteryAggregations = new BatteryAggregations();
    public totalElements: number;
    public viewArticles: boolean;

    constructor(res?: any) {
        if (!res) {
            return;
        }
        this.content = new BatteryAggregations(res.aggregations);
        this.totalElements = res.total_elements;
        this.viewArticles = res.view_articles;
    }
}
