import { UserDetail } from 'src/app/core/models/user-detail.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { MetadataLogging } from './analytic-metadata.model';

export class ArticleCategoryEvent extends MetadataLogging {
    eventType = AnalyticEventType.ARTICLE_CATEGORY_EVENT.toString();
    categoryVehicleId: string;
    selectedCategories: any;
    categoryEventSource: string;

    constructor(metadata: MetadataLogging | any, userDetail: UserDetail, data: any) {
        super(metadata, userDetail);
        if (data) {
            this.categoryVehicleId = data.vehicleId;
            this.categoryEventSource = data.eventSource;
            this.selectedCategories = data.selectedCategories.map(item => {
                return this.buildSelectedCategoriesPath(data.categories, item);
            });
        }
    }

    toArticleCategoryRequest() {
        const request = super.toRequest();

        return {
            ...request,
            category_vehicle_id: this.categoryVehicleId,
            category_source: this.categoryEventSource,
            category_gaid: this.selectedCategories
        };
    }

    private buildSelectedCategoriesPath(categories: any[], category: any) {
        let path = [];
        let cate = { ...category };
        path.push(cate.description);
        while (cate.parentId) {
            cate = categories.find(item => item.id === cate.parentId);
            if (cate) {
                path = [cate.description, ...path];
            }
        }
        return path.map((item, index) => ({ subcat: item, level: index }));
    }
}
