import { uniqBy } from 'lodash';

import { BrandFilterItem } from "../models/brand-filter-item.model";

export class BrandFilterUtil {
    public static getUniqCombinedBrands(items: BrandFilterItem[]) {
        const brands = (items || []).reduce((result, current) => {
            result = [...result, ...current.brands];
            return result;
        }, []);

        return uniqBy(brands, 'name');
    }
}
