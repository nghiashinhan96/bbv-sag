import { MotorTyresAggregations } from './motor-tyre-aggregations.model';

export class MotorTyresModel {
    public content: MotorTyresAggregations = new MotorTyresAggregations();
    public totalElements: number;
    public viewArticles: boolean;

    constructor(res?: any) {
        if (!res) {
            return;
        }
        this.content = new MotorTyresAggregations(res.aggregations);
        this.totalElements = res.total_elements;
        this.viewArticles = res.view_articles;
    }
}
