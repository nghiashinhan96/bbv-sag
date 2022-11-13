import { CateBrand } from "sag-article-detail";

export class GenArtTile {
    gaId: number;
    gaText: string;
    brands: Brand[];

    constructor(data?) {
        this.gaId = parseInt(data.gaId) || 0;
        this.gaText = data.gaText;
        this.brands = data.brands.map(brand => new Brand(brand));
    }
}

export class Brand {
    brandId: number;
    brandName: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.brandId = parseInt(data.dlnrid) || data.brandId || 0;
        this.brandName = data.suppname || data.brandName;
    }
}

export class BrandPrio {
    gaid: string;
    sorts: [];
    brands: CateBrand[];

    constructor(data?) {
        if (!data) {
            return;
        }
        this.gaid = data.gaid;
        this.sorts = data.sorts;
        this.brands = data.brands;
    }
}

export interface BrandData {
    gaId: number;
    gaText: string;
    suppliers: string[];
  }