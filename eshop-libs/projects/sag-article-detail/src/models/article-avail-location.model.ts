import { ArticleAvailLocationItemModel } from './article-avail-location-item.model';

export class ArticleAvailLocationModel {
    state: string;
    name: string;
    allInPrioLocations: boolean;
    items: ArticleAvailLocationItemModel[] = [];

    constructor (data = null) {
        if (data) {
            this.state = data.state;
            this.name = data.name;
            this.allInPrioLocations = data.allInPrioLocations;
            if (data.items) {
                data.items.forEach(element => {
                    this.items.push(new ArticleAvailLocationItemModel(element));
                });
            }
        }
    }
}