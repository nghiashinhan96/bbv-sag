import { BarFilter, GenArtBarFilter } from '../models/bar-filter-option.model';
import { CateBrand } from './cate-brand.interface';
import { CategoryTile } from './category-tile.model';
import { Criteria } from './criteria.interface';
import { Cupi } from './cupi.interface';

export class CategoryModel {
    id: string;
    description: string;
    rootDescription: string;

    classicCol: string;
    open: string;
    sort: string;
    qflag: number;
    qlevel: number;
    qsort: number;
    qshow: boolean;
    qfold: boolean;
    qfoldShow: boolean;

    ignoredOpen: boolean;
    oilCate: boolean;
    check: boolean;
    belongedGaIds?: string;
    parentId?: string;
    children?: CategoryModel[];
    genArts?: {
        gaid: string;
        criteria: Criteria[];
        brands: CateBrand[];
        cupis: Cupi[];
    }[];
    criterias?: Criteria[];
    /** For rendering UI */
    rendered?: boolean;
    ref: any;
    isChecked?: boolean;
    show?: boolean;
    link: string;
    isGlassBodyWork?: boolean;
    descriptionPath: string[];
    tiles: CategoryTile[];
    nodeName: string;
    isFolded = false;

    filterCaid: any;
    filterDefault: any;
    filterSort: any;
    filterBar: any;
    filterOpen: string;
    filters: BarFilter[] = [];

    constructor(data?: CategoryModel, isWspMode?: boolean) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.description = data.description || data.nodeName;
        this.rootDescription = data.rootDescription;

        this.classicCol = data.classicCol;
        this.open = data.open;
        this.sort = data.sort;
        this.qflag = data.qflag;
        this.qlevel = data.qlevel;
        this.qsort = data.qsort;
        this.qshow = Boolean(data.qshow);
        this.qfold = Boolean(data.qfold);
        this.qfoldShow = Boolean(data.qfoldShow);
        this.isFolded = this.qfold;

        this.ignoredOpen = data.ignoredOpen;
        this.oilCate = data.oilCate;
        this.check = data.check;
        this.isGlassBodyWork = data.check;

        this.belongedGaIds = data.belongedGaIds;
        this.parentId = data.parentId;
        this.genArts = data.genArts;
        this.criterias = data.criterias;
        if (data.children) {
            this.children = data.children.map(child => new CategoryModel(child));
            if (this.qfold) {
                this.qfold = this.children.some(c => c.qshow && c.qflag === 1 && !c.qfoldShow);
                this.isFolded = this.qfold;
            }
        }
        this.isChecked = data.isChecked;
        this.show = data.show;
        this.link = data.link;
        this.tiles = data.tiles;
        this.nodeName = data.nodeName;

        if (isWspMode) {
            return;
        }
        const { filters } = data as any;
        this.filters = (filters || []).map(filter => new BarFilter(filter));
    }

    hasCidEqual100() {
        return !!this.criterias && !!this.criterias.find(criteria => criteria.cid === '100');
    }

    getCateBrands() {
        return (this.genArts || []).reduce((brands, genArt, i) => {
            return [...brands, ...genArt.brands];
        }, []);
    }

    getDescriptionByLevel(levels: number[], separator = ' / ') {
        let descs = [];
        for (let level of levels) {
            if (this.descriptionPath && this.descriptionPath[level]) {
                descs.push(this.descriptionPath[level]);
            }
        }
        descs.push(this.description)
        return descs.join(separator);
    }
}

export class WspMatchModel {
    articleGroups: [];
    articleIds: [];
    brandIds: number[];
    genArts: number[];
    criteria: Criteria[];
    constructor(data) {
        if (!data) {
            return;
        }

        this.articleGroups = (data.artgrp || '').split(',').filter(item => !!item);
        this.articleIds = (data.artids || '').split(',').filter(item => !!item);
        this.brandIds = [];
        this.genArts = [];
        (data.idDlnr || '').split(',').map(item => {
            if (!!item) {
                this.brandIds.push(parseInt(item));
            }
        });
        (data.genarts || '').split(',').map(item => {
            if (!!item) {
                this.genArts.push(parseInt(item));
            }
        });;
        this.criteria = data.criteria;
    }
}

export class WspCategoryModel extends CategoryModel {
    include: WspMatchModel;
    exclude: WspMatchModel;
    children?: WspCategoryModel[];
    nodeExternalServiceAttribute: string;
    nodeExternalType: string;
    target: string;
    navigate: string;
    brandPrio: number;
    nodeName: string;

    filterCaid: any;
    filterDefault: any;
    filterSort: any;
    filterBar: any;
    index: number = 0;
    activeLink: boolean;
    genArtBarFilters: GenArtBarFilter[] = [];

    constructor(data, index = -1) {
        super(data, true);
        this.brandPrio = 0;
        this.genArts = this.brandPrio ? data.genArts : null;
        this.filterCaid = data.filterCaid;
        this.filterDefault = data.filterDefault;
        this.filterSort = data.filterSort;
        this.filterBar = data.filterBar;
        this.index = index + 1;

        if (data.children) {
            this.children = data.children.map(child => new WspCategoryModel(child, this.index));
        }
        this.include = new WspMatchModel(data.include);
        this.exclude = new WspMatchModel(data.exclude);
        this.nodeExternalServiceAttribute = data.nodeExternalServiceAttribute;
        this.nodeExternalType = data.nodeExternalType;
        this.target = data.target;
        this.navigate = data.navigate;
        this.nodeName = data.nodeName;
        this.activeLink = data.activeLink;
        const { filters } = data as any;
        this.genArtBarFilters = (filters || []).map(filter => new GenArtBarFilter(filter));
    }
}
