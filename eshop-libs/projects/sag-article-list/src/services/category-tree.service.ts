import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/internal/operators/map';
import { Subject } from 'rxjs/internal/Subject';
import { BehaviorSubject, of } from 'rxjs';
import { ArticleModel, CategoryModel, Cupi, WspCategoryModel } from 'sag-article-detail';
import { QuickClickService } from './quick-click.service';
import { ArticleListStorageService } from './article-list-storage.service';
import { ArticleListConfigService } from './article-list-config.service';
import { catchError } from 'rxjs/internal/operators/catchError';

@Injectable()
export class CategoryTreeService {

    rootCategory: CategoryModel;
    categories: any;
    categoriesArray: CategoryModel[];

    private selectedCategories$ = new Subject<{
        cateIds: string[],
        cate?: any,
        gaIds: string,
        target?: string;
    }>();

    private checkOilCate$ = new Subject<{
        cateIds: any[],
        emitSearchEvent: boolean,
        success: (oils) => void,
        error: (oils) => void
    }>();

    private hasSelectedCate$ = new Subject<boolean>();

    selectedCategoriesObservable = this.selectedCategories$.asObservable();
    checkOilCateObservable = this.checkOilCate$.asObservable();
    hasSelectedCateObservable = this.hasSelectedCate$.asObservable();

    currentSelectedCategiries = [];
    currentSelectedWspCategory: WspCategoryModel = null;

    private readyState$ = new BehaviorSubject(false);
    readyStateObservable = this.readyState$.asObservable();
    isCategoriesNotFound$ = new BehaviorSubject(false);

    constructor(
        private http: HttpClient,
        private quickClickService: QuickClickService,
        private libStorage: ArticleListStorageService,
        private config: ArticleListConfigService
    ) { }

    searchTree(vehicleId: string, text: string) {
        const url = `${this.config.baseUrl}categories/${vehicleId}/search`;
        return this.http.get(url, { params: { text }, observe: 'body' });
    }

    searchFreetext(body: any) {
        const url = `${this.config.baseUrl}search/free-text`;
        return this.http.post(url, body);
    }

    getCategories(vehicleId: string, isGtmotive = false, syncCategories = true) {
        if (this.categories && this.categories.length && !syncCategories) {
            return of(this.categories);
        }
        let url = `${this.config.baseUrl}categories/${vehicleId}/all`;
        if (isGtmotive) {
            url = `${this.config.baseUrl}categories/${vehicleId}/all?addCupiCode=true`
        }
        return this.http.get(url, { observe: 'body' }).pipe(map((res: any) => {
            this.categories = res.ALL.map(cate => new CategoryModel(cate));
            this.categoriesArray = [];
            this.extractTree(this.categories, this.categoriesArray);
            this.readyState$.next(true);
            this.isCategoriesNotFound$.next(false);
            return this.categories;
        }));
    }

    getUniversalCategories(treeId: string, nodeId?: string) {
        let url = `${this.config.baseUrl}unitrees/${treeId}`;
        return this.http.get(url, { observe: 'body' }).pipe(
            catchError(eror =>  of(null)),
            map((res: any) => {
            this.rootCategory = res;
            this.categories = (this.rootCategory && this.rootCategory.children || []).map(cate => new WspCategoryModel(cate));
            this.categoriesArray = [];
            this.extractTree(this.categories, this.categoriesArray);
            if(nodeId) {
                this.unCheckAllCate();
                this.checkOnCategoryTree([nodeId]);
                this.checkOnSingleCategory(nodeId, true);
            } else {
                this.selectedCategories$.next({
                    cateIds: [this.rootCategory.id],
                    gaIds: ''
                });
            }

            this.readyState$.next(true);
            return this.categories;
        }));
    }

    getRequestedTreeFromNodeId(nodeId: string) {
        const url = `${this.config.baseUrl}unitrees?leafId=${nodeId}`;
        return this.http.get(url, { observe: 'body' });
    }

    reSelectedCategorybyId(cateId?) {
        const id = cateId || this.rootCategory && this.rootCategory.id || null;

        if(id) {
            this.selectedCategories$.next({
                cateIds: [id],
                gaIds: ''
            });
        }
    }

    /** On checked / unchecked a category */
    emitSearchRequest(id?: string, isChecked?: boolean) {
        if (!this.checkOilCate(id, isChecked, true)) {
            const selectedCateIds = this.getSelectedCategoryIds();
            const selectedGaIds = this.getSelectedGaIds();
            this.selectedCategories$.next({
                cateIds: selectedCateIds,
                gaIds: selectedGaIds
            });
            this.emitHasSelectedCateEvent(selectedCateIds.length > 0);
        }
    }

    checkOilCate(id?: string | CategoryModel, isChecked?: boolean, emitSearchEvent = true) {
        if (!id) {
            return false;
        }
        // remove the selected olyslager id if uncheck this catgory
        let selectedCate: any;
        if (typeof id === 'string' || id instanceof String) {
            selectedCate = this.categoriesArray.find(cate => cate.id === id);
        } else {
            selectedCate = id;
        }
        if (selectedCate && selectedCate.oilCate) {
            this.checkOnCategoryTreeCate(selectedCate.id, isChecked);
            if (isChecked) {
                // emit check oil
                this.emitCheckOilCate(
                    [selectedCate.id],
                    emitSearchEvent,
                    oils => {
                        this.libStorage.selectedOils = oils;
                        this.oilCateChecked(emitSearchEvent);
                    },
                    oils => {
                        this.checkOnCategoryTreeCate(selectedCate.id, false);
                        this.libStorage.removeSelectedOil(selectedCate.id);
                        this.oilCateChecked(emitSearchEvent);
                    }
                );
                return true;
            }
            this.oilCateChecked(false);
            this.libStorage.removeSelectedOil(selectedCate.id);
        }
        return false;
    }

    emitCheckOilCate(cateIds, emitSearchEvent, success, error) {
        this.checkOilCate$.next({
            cateIds,
            emitSearchEvent,
            success,
            error
        });
    }

    markedTreeState(state: boolean) {
        this.readyState$.next(state);
    }

    resetCategoryTree() {
        if (this.categoriesArray) {
            this.categoriesArray.forEach(item => {
                if (item.parentId) {
                    item.rendered = false;
                    item.isChecked = false;
                }
                item.show = undefined;
            });
            this.selectedCategories$.next({
                cateIds: [],
                gaIds: ''
            });
            this.currentSelectedCategiries = [];
            this.emitHasSelectedCateEvent(false);
        }
        this.quickClickService.reset();
    }

    resetCategoryTreeInQuickClick() {
        if (this.categoriesArray) {
            this.categoriesArray.forEach(item => {
                if (item.parentId) {
                    item.rendered = false;
                    item.isChecked = false;
                }
                item.show = undefined;
            });
            this.currentSelectedCategiries = [];
        }
        this.quickClickService.reset();
    }

    destroyCategoryTree() {
        this.categories = null;
        this.categoriesArray = null;
    }

    destroyWspCategoryTree() {
        this.categories = null;
        this.categoriesArray = null;
        this.selectedCategories$.next({
            cateIds: [],
            gaIds: ''
        });
        this.currentSelectedCategiries = [];
        this.rootCategory = undefined;
        this.emitHasSelectedCateEvent(false);
    }

    getSelectedCategoryIds(): string[] {
        return this.getCheckedCategories().map(d => d.id);
    }

    checkOnCategoryTree(categoryIds, isChecked = true, emitSearchEvent = true) {
        if (categoryIds.length === 0) {
            return;
        }
        categoryIds.forEach(id => this.checkOnCategoryTreeCate(id, isChecked));

        if (emitSearchEvent) {
            this.emitSearchRequest();
        } else {
            this.emitHasSelectedCateEvent();
        }
    }

    checkOnSingleCategory(categoryId, isChecked = true, emitSelectedCate = true, target = '') {
        if (!categoryId) {
            return;
        }
        const category = this.getCateDetailById(categoryId);

        if (category && category.children && category.children.length === 0) {
            this.checkOnCategoryTreeCate(categoryId, isChecked);
        }

        if (emitSelectedCate) {
            this.selectedCategories$.next({
                cateIds: [categoryId],
                cate: category,
                gaIds: '',
                target
            });
            this.currentSelectedWspCategory = category as WspCategoryModel;
        }
    }

    unCheckAllCate(resetViewState = true) {
        if (this.categoriesArray) {
            this.categoriesArray.forEach(item => {
                if (resetViewState) {
                    item.rendered = false;
                    item.show = undefined;
                }
                item.isChecked = false;
            });
            this.currentSelectedCategiries = [];
        }
    }

    unCheckAllOilCate() {
        this.categoriesArray.forEach(item => {
            if (!item.children && item.oilCate) {
                item.isChecked = false;
            }
        });
    }

    getSelectedGaIds(cateIds?: string[]) {
        const checkedCates = this.getCheckedCategories(cateIds);
        const gaids: string = checkedCates.map(cate => cate.belongedGaIds).join(',');
        return gaids;
    }

    getCategoriesByGaids(gaids) {
        return this.categoriesArray.filter(cate => {
            if (cate.children || !cate.belongedGaIds) {
                return false;
            }

            const cateGaIds = cate.belongedGaIds.split(',');
            return cateGaIds.some(item => gaids.find(gaid => gaid === item));
        });
    }

    getCategoriesByCupis(cupis: Cupi[]) {
        return this.categoriesArray.filter(cate => {
            if (cate.children || !cate.genArts) {
                return false;
            }

            const hasCupis = cate.genArts.some(ga =>
                ga.cupis.some(caCp =>
                    cupis.some(cp => cp.cupi === caCp.cupi
                        && (cp.loc || '').trim().toLowerCase() === (caCp.loc || '').trim().toLowerCase())
                )
            );

            const matchCriteria = cate.genArts.some(ga =>
                ga.criteria.some(criteria => {
                    if (criteria.cid === '100') {
                        return cupis.some(cp => (cp.loc || '').trim().toLowerCase() === (criteria.cvp || '').trim().toLowerCase());
                    }
                    return false;
                })
            );

            const hasCidEqual100 = cate.hasCidEqual100();
            return (hasCidEqual100 && hasCupis && matchCriteria) || (!hasCidEqual100 && hasCupis);
        }).map(cate => cate.id);
    }

    // keep the current state after articles were loaded
    updateCurrentSelectedCateIds() {
        this.currentSelectedCategiries = this.getSelectedCategoryIds() || [];
        this.libStorage.selectedCateIds = [...this.currentSelectedCategiries];
        this.emitHasSelectedCateEvent(this.currentSelectedCategiries.length > 0);
    }

    getCheckedCategories(cateIds?: string[]): CategoryModel[] {
        if (!this.categoriesArray) {
            return [];
        }
        if (!!cateIds) {
            return this.categoriesArray.filter(cate => cateIds.indexOf(cate.id) !== -1);
        }
        return this.categoriesArray.filter(cate => cate.isChecked && !cate.children);
    }

    getCateDetailById(cateId: string) {
        return (this.categoriesArray || []).find(cate => cate.id === cateId);
    }

    getMatchedCategoriesForArticle(article: ArticleModel) {
        if (!article) {
            return [];
        }
        return this.categoriesArray.filter(cate => {
            if (cate.children || !cate.belongedGaIds) {
                return false;
            }

            if (article.isBelongToCate(cate) && (article.hasCommonCriteria(cate.criterias) || (article.noCriteria() && cate.hasCidEqual100()))) {
                return true;
            }

            return false;
        });
    }

    private emitHasSelectedCateEvent(status = null) {
        let hasSelectedCate = status;
        if (hasSelectedCate === null) {
            hasSelectedCate = this.getSelectedCategoryIds().length > 0;
        }
        this.hasSelectedCate$.next(hasSelectedCate);
    }

    checkOnCategoryTreeCate(id: string, isChecked: boolean) {
        const node = (this.categoriesArray || []).find(n => n.id === id);
        if (node) {
            node.rendered = true;
            node.show = true;
            node.isChecked = isChecked;
            if (node.ref) {
                node.ref.checked = isChecked;
            }
            if (node.parentId) {
                this.checkOnCategoryTreeCate(node.parentId, isChecked);
            }
        }
    }

    updateSelectedCategories(data) {
        this.selectedCategories$.next(data);
    }

    updateOilCate(data) {
        this.checkOilCate$.next(data);
    }

    private extractTree(tree: any[], result, descriptionPath: string[] = []) {
        tree.forEach(node => {
            node.descriptionPath = descriptionPath;
            result.push(node);
            if (node.children) {
                this.extractTree(node.children, result, [...node.descriptionPath, node.description]);
            }
        });
    }

    private oilCateChecked(emitSearchEvent: boolean) {
        if (emitSearchEvent) {
            this.emitSearchRequest();
        } else {
            this.emitHasSelectedCateEvent();
        }
    }
}
