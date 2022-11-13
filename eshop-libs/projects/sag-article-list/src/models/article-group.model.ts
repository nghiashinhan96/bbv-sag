import { ArticleModel, CategoryModel, CateBrand } from 'sag-article-detail';

export class ArticleGroupModel {
    key: string;
    values: ArticleModel[];
    root?: string;
    cate: CategoryModel;
    cateId?: string;
    cateBrands?: CateBrand[];
    gaIdKeyChanged?: string;
    gaId?: string;
    isForceChanged: boolean;
    pseudo?: boolean;
}
