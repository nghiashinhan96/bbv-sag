import { intersectionWith, isEqual, get } from 'lodash';
import { CategoryModel } from './category.model';
import { ArticleAvailabilityModel } from './article-availability.model';
import { LibUserSetting } from './lib-user-price-setting.model';
import { CustomPrice, GrossPriceType } from 'sag-custom-pricing';
import { Cupi } from './cupi.interface';
import { INFO_TYPE } from '../enums/info-type.enum';
import { NONVEHICLE } from '../consts/article-detail.const';
import { CVP_VALUE_IS_MISSING_ERR_MSG } from 'sag-common';
import { RelevanceGroupUtil } from '../utils/relevance-group.util';
import { AvailabilityUtil } from '../utils/availability.util';
interface Criteria {
    cvp: string;
    cid: string;
    cn: string;
    csort: number;
}

export class ArticleInfo {
    id: string;
    type: string;
    txt: string;
    truncatedTxt?: string;
    artnr: string;
    label: string;

    constructor (data) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.type = data.type;
        this.txt = data.txt;
        this.truncatedTxt = data.truncatedTxt;
        this.artnr = data.artnr;
        this.label = data.label;
    }
}

export class ArticleModel {
    id: string;
    name: string;
    pimId: string;
    dlnrId: string;
    artid: string;

    amountNumber: number;
    stock: any;
    gaID: string;
    combinedGenArtIds: string[];
    genArtTxts: {
        gaid: string;
        gatxtdech: string;
    }[] = [];
    genArtTxtEng: {
        gaid: string;
        gatxtdech: string;
    };
    criteria: Criteria[] = [];
    artnr: string;
    position: string;
    salesQuantity: number;
    qtyMultiple: number;
    price: any;
    prices: any[];
    isTyre: boolean;
    artDesc: string;
    supplier: string;
    productBrand: string;
    productAddon: string;
    artnrDisplay: string;
    info: ArticleInfo[];
    hasReplacement?: string;
    isReplacementFor?: string;
    oeNumbers: any;
    iamNumbers: any;
    pnrnEANs: string[];
    certifiedPartType: string;
    images: any[];

    cateId?: string;
    group?: string;
    root?: string;
    prio?: number;
    subPrio?: number;
    availabilities?: ArticleAvailabilityModel[];
    isPromotion?: boolean;
    netPrice?: number;
    grossPrice?: number;
    totalNetPrice?: number;
    totalGrossPrice?: number;
    discountInPercent?: number;
    memo?: string;
    memos?: any[];
    umsartId: string;
    idProductBrand: string;
    sagProductGroup: string;
    sagProductGroup2: string;
    sagProductGroup3: string;
    sagProductGroup4: string;
    depositArticle?: ArticleModel;
    vocArticle?: ArticleModel;
    vrgArticle?: ArticleModel;
    pfandArticle?: ArticleModel;
    allowedAddToShoppingCart?: boolean;

    oilArticle: boolean;
    oilProduct: any;
    article: any;
    glassOrBody?: boolean;
    // analytics
    source?: string;

    // Optional fields
    markAsDeleted?: boolean;
    availRequested?: boolean;
    stockRequested?: boolean;
    priceRequested?: boolean;
    autonetRequested?: boolean;
    displayedPrice?: CustomPrice;
    pnrnPccs: string[] = [];
    itemType: string;
    cupi: Cupi;
    totalAxStock: number;

    totalFinalCustomerNetPrice: number;
    totalFinalCustomerNetPriceWithVat: number;
    finalCustomerNetPrice: number;
    finalCustomerNetPriceWithVat: number;
    displayFCNetPrice: number;

    favorite: boolean;
    favoriteComment: string;

    replacementForArtId?: string;
    replacementInContextData?: {
        vehicle: any;
        category: CategoryModel;
    };

    notFoundInAx: boolean;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;
    oldQuantity?: number;

    seqNo: number;
    accessoryLists: any[];
    accessoryListsText: string;
    accessoryLinkText: string;
    hasAccessories: boolean;

    bom: boolean;
    partsListItems: any[];

    pseudo?: boolean;
    relevanceGroupType: string;
    relevanceGroupOrder: number;
    icat: string;
    icat2: string;
    icat3: string;
    icat4: string;
    icat5: string;
    deliverableStock?: number;

    constructor (art?: ArticleModel | any) {
        if (!art) {
            return;
        }
        this.id = art.id;
        this.name = art.name;
        this.artid = art.artid;
        this.pimId = art.pimId || art.id_pim;
        this.dlnrId = art.dlnrId || art.id_dlnr;
        this.gaID = art.gaID;
        this.amountNumber = art.amountNumber;
        this.stock = art.stock;
        this.combinedGenArtIds = art.combinedGenArtIds;
        this.genArtTxts = art.genArtTxts;
        this.genArtTxtEng = art.genArtTxtEng;
        this.criteria = art.criteria;
        this.artnr = art.artnr;
        this.position = this.getPosition(this.criteria) || this.getPositionFromCupi(art.cupi);
        this.salesQuantity = art.salesQuantity;
        this.qtyMultiple = art.qtyMultiple;
        this.price = art.price;
        this.prices = art.prices;
        this.displayedPrice = art.displayedPrice;
        this.setNetPrice();
        this.setGrossPrice();
        this.setTotalNetPrice();
        this.setTotalGrossPrice();
        this.setDiscount();
        this.isTyre = art.isTyre || art.tyre_article || false;
        this.artDesc = art.artDesc || art.partDesc || '';
        this.supplier = art.supplier;
        this.productAddon = art.productAddon || art.product_addon;
        this.productBrand = art.productBrand || art.product_brand;
        this.artnrDisplay = art.artnrDisplay || art.artnr_display;
        this.info = (art.info || []).map(i => {
            return this.generateInfo(art, i);
        });
        this.hasReplacement = art.hasReplacement;
        this.isReplacementFor = art.isReplacementFor;
        this.iamNumbers = art.iamNumbers || {};
        this.oeNumbers = art.oeNumbers || {};
        this.pnrnEANs = art.pnrnEANs || [];
        this.certifiedPartType = (art.certifiedPartType || '').toLowerCase();
        this.images = art.images;
        this.availabilities = art.availabilities;
        this.memo = art.memo;
        this.memos = art.memos;
        this.umsartId = art.umsartId || art.id_umsart;
        this.idProductBrand = art.id_product_brand || art.idProductBrand;
        this.sagProductGroup = art.sag_product_group || art.sagProductGroup;
        this.sagProductGroup2 = art.sag_product_group_2 || art.sagProductGroup2;
        this.sagProductGroup3 = art.sag_product_group_3 || art.sagProductGroup3;
        this.sagProductGroup4 = art.sag_product_group_4 || art.sagProductGroup4;
        if (art.depositArticle) {
            this.depositArticle = new ArticleModel(art.depositArticle);
        }
        if (art.vocArticle) {
            this.vocArticle = new ArticleModel(art.vocArticle);
        }
        if (art.vrgArticle) {
            this.vrgArticle = new ArticleModel(art.vrgArticle);
        }
        if (art.pfandArticle) {
            this.pfandArticle = new ArticleModel(art.pfandArticle);
        }
        this.allowedAddToShoppingCart = art.allowedAddToShoppingCart;
        this.oilArticle = art.oilArticle;
        this.oilProduct = art.oilProduct;
        this.article = art.article;
        this.glassOrBody = art.glassOrBody;
        this.source = art.source;
        this.totalAxStock = art.totalAxStock;
        // addtional Info
        this.markAsDeleted = art.markAsDeleted;
        this.availRequested = art.availRequested;
        this.stockRequested = art.stockRequested;
        this.priceRequested = art.priceRequested || !!art.price;
        this.autonetRequested = art.autonetRequested;
        this.pnrnPccs = art.pnrnPccs || [];
        this.itemType = art.itemType;
        this.cupi = art.cupi;
        this.finalCustomerNetPrice = art.finalCustomerNetPrice;
        this.totalFinalCustomerNetPrice = art.totalFinalCustomerNetPrice;
        this.finalCustomerNetPriceWithVat = art.finalCustomerNetPriceWithVat;
        this.totalFinalCustomerNetPriceWithVat = art.totalFinalCustomerNetPriceWithVat;
        this.favorite = art.favorite;
        this.favoriteComment = art.favoriteComment || '';
        this.replacementForArtId = art.replacementForArtId;
        this.replacementInContextData = art.replacementInContextData;

        // accessories
        this.seqNo = art.seqNo;
        this.accessoryListsText = art.accesoryListsText;
        this.accessoryLinkText = art.accessoryLinkText;
        this.accessoryLists = art.accessoryLists;
        // parts list
        this.bom = art.bom;
        this.partsListItems = (art.parts_list_items || art.partsListItems || []).filter(i => i.parts_list_item_id_art);

        this.pseudo = art.pseudo;
        this.relevanceGroupType = art.relevanceGroupType;
        this.relevanceGroupOrder = RelevanceGroupUtil.getRelevanceGroupOrder(this.relevanceGroupType);

        this.basketItemSourceId = art.basketItemSourceId;
        this.basketItemSourceDesc = art.basketItemSourceDesc;
        this.getArticleItemCategory(art);
        this.deliverableStock = art.deliverableStock;
    }

    // PDP
    getDPC(inclVAT = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { dpcPrice, dpcPriceWithVat } = this.price.price;

        return inclVAT ? dpcPriceWithVat : dpcPrice;
    }

    getNet1PriceFound () {
        if (!this.price || !this.price.price) {
            return false;
        }

        const { net1PriceFound } = this.price.price;

        return net1PriceFound || false;
    }

    netPriceLessThanNet1Price() {
        if (this.getNetPrice() < this.getNet1Price()) {
            return true;
        } else {
            return false;
        }
    }

    netPriceGreaterThanOrEqualNet1Price() {
        if (this.getNetPrice() >= this.getNet1Price()) {
            return true;
        } else {
            return false;
        }
    }

    // Gross Price
    getBrutto(currentStateVatConfirm = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { standardGrossPrice, standardGrossPriceWithVat } = this.price.price;
        return currentStateVatConfirm ? (standardGrossPriceWithVat || 0) : (standardGrossPrice || 0);
    }

    getGrossPrice(currentStateVatConfirm = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { grossPrice, grossPriceWithVat, oepPriceWithVat } = this.price.price;
        const displayedGrossPrice = this.displayedPrice && this.displayedPrice.price;
        const priceWithVat = this.displayedPrice && this.displayedPrice.priceWithVat;
        return currentStateVatConfirm ? (displayedGrossPrice && (priceWithVat || oepPriceWithVat)) || grossPriceWithVat : (displayedGrossPrice || grossPrice);
    }

    getGrossPriceWithVat() {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { grossPriceWithVat, oepPriceWithVat } = this.price.price;
        const priceType = this.displayedPrice && this.displayedPrice.type;
        const priceWithVat = this.displayedPrice && this.displayedPrice.priceWithVat;

        return priceType === GrossPriceType.OEP.toString() ? (priceWithVat || oepPriceWithVat) : grossPriceWithVat;
    }

    getDepositGrossPrice(currentStateVatConfirm = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { grossPrice, grossPriceWithVat } = this.price.price;
        return currentStateVatConfirm ? grossPriceWithVat : grossPrice;
    }

    setTotalGrossPrice(currentStateVatConfirm = false, isInCart = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        if (!isInCart && this.displayedPrice) {
            this.totalGrossPrice = this.displayedPrice.totalPrice;
        } else {
            const { totalGrossPrice, totalGrossPriceWithVat } = this.price.price;
            this.totalGrossPrice = currentStateVatConfirm ? totalGrossPriceWithVat : totalGrossPrice;
        }
    }

    // Net Price
    getNetPrice(inclVAT = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { netPrice, netPriceWithVat } = this.price.price;

        return inclVAT ? (netPriceWithVat || 0) : (netPrice || 0);
    }

    getTotalPriceWithVat(currentStateNetPriceView = false) {
        if (!this.price || !this.price.price) {
            return null;
        }

        const { totalNetPriceWithVat, totalGrossPriceWithVat } = this.price.price;
        return currentStateNetPriceView ? totalNetPriceWithVat : totalGrossPriceWithVat;
    }

    getTotalPriceInPDP(currentStateNetPriceView = false, isInCart = false) {
        if (!this.price || !this.price.price) {
            return null;
        }

        if (currentStateNetPriceView) {
            const { totalNetPrice } = this.price.price;
            return totalNetPrice;
        } else {
            if (!isInCart && this.displayedPrice) {
                return this.displayedPrice.totalPrice;
            } else {
                const { totalGrossPrice } = this.price.price;
                return totalGrossPrice;
            }
        }
    }

    // Discount
    getDiscount() {
        if (!this.price || !this.price.price) {
            return null;
        }

        const { discountInPercent } = this.price.price;

        return discountInPercent || 0;
    }

    getFinalCustomerNetPrice(priceSetting, inclVAT = false, isFinalCustomerAffiliate = false) {
        if (priceSetting.fcUserCanViewNetPrice || !isFinalCustomerAffiliate) {
            if (inclVAT) {
                return this.finalCustomerNetPriceWithVat;
            }
            return this.finalCustomerNetPrice;
        }
        else {
            this.setNetPrice(inclVAT);
            return this.netPrice;
        }
    }

    getUvpePrice(inclVAT = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { uvpePrice, uvpePriceWithVat } = this.price.price;

        return inclVAT ? uvpePriceWithVat : uvpePrice;
    }

    getOepPrice(inclVAT = false) {
        if (!this.price || !this.price.price) {
            return null;
        }

        const { oepPrice, oepPriceWithVat } = this.price.price;


        return inclVAT ? oepPriceWithVat : oepPrice;
    }

    getKbPrice(inclVAT = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { kbPrice, kbPriceWithVat } = this.price.price;

        return inclVAT ? kbPriceWithVat : kbPrice;
    }

    getUvpPrice(inclVAT = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { uvpPrice, uvpPriceWithVat } = this.price.price;

        return inclVAT ? uvpPriceWithVat : uvpPrice;
    }

    getNet1Price(inclVAT = false) {
        if (!this.price || !this.price.price) {
            return null;
        }

        let { net1Price, net1PriceWithVat } = this.price.price;

        return inclVAT ? (net1PriceWithVat || 0) : (net1Price || 0);
    }

    getTotalNet1Price(inclVAT = false) {
        if (!this.price || !this.price.price) {
            return null;
        }

        let { totalNet1Price, totalNet1PriceWithVat } = this.price.price;

        return inclVAT ? (totalNet1PriceWithVat || 0) : (totalNet1Price || 0);
    }

    getDiscountPrice() {
        if (!this.price || !this.price.price) {
            return null;
        }

        const { discountPrice } = this.price.price;

        return discountPrice;
    }

    getPromotionDiscount() {
        if (!this.price || !this.price.price) {
            return null;
        }

        let { promotionInPercent } = this.price.price;

        return promotionInPercent;
    }

    getTotalGrossPriceVat(isInCart = false) {
        if (!this.price || !this.price.price) {
            return null;
        }

        if (!isInCart && this.displayedPrice) {
            return this.displayedPrice.totalPrice;
        } else {
            const { totalGrossPriceWithVat } = this.price.price;
            return totalGrossPriceWithVat;
        }
    }

    showPromotionBanner() {
        if (this.getNetPrice() < this.getNet1Price()) {
            return true;
        }

        return false;
    }

    // PDP

    isBelongToCate(cate: CategoryModel) {
        const gaIds = (cate.belongedGaIds || '').split(',');
        if (!gaIds || gaIds.length === 0) {
            return false;
        }
        if (gaIds.indexOf(this.gaID) !== -1) {
            return true;
        }
        const combinedGenArtIds = this.combinedGenArtIds || [];
        return intersectionWith(gaIds, combinedGenArtIds, isEqual).length > 0;
    }

    isBelongDefaultGroup(cate: CategoryModel) {
        const gaIds = (cate.belongedGaIds || '').split(',');
        if (!gaIds || gaIds.length === 0) {
            return false;
        }
        return gaIds.indexOf(this.gaID) !== -1;
    }

    hasCommonCriteria(cateCriteria) {
        if (!cateCriteria || cateCriteria.length === 0 || !cateCriteria[0].cid) {
            return true;
        }
        return intersectionWith(cateCriteria, (this.criteria || []), (cateVal: Criteria, artVal: Criteria) => {
            return cateVal.cid === artVal.cid
                && cateVal.cvp && artVal.cvp
                && cateVal.cvp.toLowerCase() === artVal.cvp.toLowerCase();
        }).length > 0;
    }

    noCriteria() {
        return !this.criteria || this.criteria.length === 0;
    }

    getOfferGrossPrice() {
        if (!this.price || !this.price.price) {
            return null;
        }
        const price = this.price.price;
        return price.grossPrice || price.originalBrandPrice;
    }

    getOfferTotalGrossPrice() {
        if (!this.price || !this.price.price) {
            return null;
        }
        return this.price.price.totalGrossPrice;
    }

    getPriceType(defaultBrand) {
        if (this.displayedPrice) {
            return `${this.displayedPrice.type} ${this.displayedPrice.brand}`;
        } else {
            let priceType = get(this.price, 'price.type');
            if (priceType === GrossPriceType.OEP.toString()) {
                priceType += ` ${defaultBrand && defaultBrand.brand}`;
            }
            return priceType;
        }
    }

    // gross price
    setGrossPrice(currentStateVatConfirm = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { grossPrice, grossPriceWithVat } = this.price.price;
        const displayedGrossPrice = this.displayedPrice && this.displayedPrice.price;
        this.grossPrice = currentStateVatConfirm ? grossPriceWithVat : (displayedGrossPrice || grossPrice);
    }

    setNetPrice(currentStateVatConfirm = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { netPrice, netPriceWithVat } = this.price.price;
        this.netPrice = currentStateVatConfirm ? netPriceWithVat : netPrice;
    }

    setTotalNetPrice(currentStateVatConfirm = false) {
        if (!this.price || !this.price.price) {
            return null;
        }
        const { totalNetPrice, totalNetPriceWithVat } = this.price.price;
        this.totalNetPrice = currentStateVatConfirm ? totalNetPriceWithVat : totalNetPrice;
    }

    updatePrice(price) {
        if (!this.price || !this.price.price) {
            return null;
        }
        this.price.price = {
            ...this.price.price,
            ...price
        };
    }

    isAllInPrioLocation() {
        return !(this.availabilities || []).some(avail => avail.location && !avail.location.allInPrioLocations);
    }

    private setDiscount() {
        this.discountInPercent = this.price && this.price.price && this.price.price.discountInPercent || null;
    }

    getTotalPrice(currentStateNetPriceView: boolean) {
        if (currentStateNetPriceView) {
            return this.totalNetPrice || 0;
        } else {
            return this.totalGrossPrice || 0;
        }
    }

    getFcTotalPrice(priceSetting: LibUserSetting, currentStateNetPriceView: boolean, includeVat = false) {
        if (priceSetting.fcUserCanViewNetPrice && currentStateNetPriceView) {
            if (includeVat) {
                return this.totalFinalCustomerNetPriceWithVat;
            } else {
                return this.totalFinalCustomerNetPrice;
            }
        } else {
            if (!this.price || !this.price.price) {
                return null;
            }

            const { totalGrossPriceWithVat, totalGrossPrice } = this.price.price;

            return includeVat ? totalGrossPriceWithVat || 0 : totalGrossPrice || 0;
        }
    }

    isShownDiscount(priceSettings: LibUserSetting) {
        return this.discountInPercent && ((this.isTyre && priceSettings.showTyresDiscount) || priceSettings.showDiscount);
    }

    getStandardGrossPrice(includeVat = false) {
        if (!this.price || !this.price.price) {
            return null;
        }

        const { standardGrossPrice, standardGrossPriceWithVat } = this.price.price;

        return includeVat ? standardGrossPriceWithVat : standardGrossPrice || null;
    }

    setFCNetPrice(priceSetting: LibUserSetting, currentStateVatConfirm = false) {
        if (priceSetting.fcUserCanViewNetPrice) {
            if (currentStateVatConfirm) {
                this.displayFCNetPrice = this.finalCustomerNetPriceWithVat;
                return;
            }
            this.displayFCNetPrice = this.finalCustomerNetPrice;
        } else {
            this.setNetPrice(currentStateVatConfirm);
            this.displayFCNetPrice = this.netPrice;
        }
    }

    generateInfo(art, info) {
        if (!info) {
            return;
        }
        let label = info.label;
        let txt = info.txt || info.info_txt || '';

        if ((info.info_type === INFO_TYPE.ADD_REC) && txt) {
            const str = txt.split(',');
            label = str[0];
            str.shift();
            txt = str.join(',');
        }

        return new ArticleInfo({
            id: info.id || info.id_info,
            type: info.type || info.info_type,
            txt,
            label,
            artnr: art.artnr,
            artnrDisplay: this.artnrDisplay
        });
    }

    private hasDiscountPrice() {
        return !!this.price
            && !!this.price.price
            && !!this.price.price.discountPrice
            && !!this.price.price.discountInPercent;
    }

    private getPosition(criterias: Criteria[]) {
        const positions: Criteria[] = (criterias || []).filter(criteria => criteria.cid === '100');
        positions.sort((a, b) => {
            return a.csort - b.csort;
        });
        if (positions.length > 0) {
            const positionWithCvpValueIsNull = positions.find(criteria => criteria.cvp === null);
            if (positionWithCvpValueIsNull) {
                throw new Error(CVP_VALUE_IS_MISSING_ERR_MSG);
            }
        }
        return positions.map(pos => this.capitalizeFirstLetter(pos.cvp)).join(', ');
    }

    private getPositionFromCupi(cupi: Cupi) {
        if (!cupi) {
            return;
        }
        return cupi.loc;
    }

    private capitalizeFirstLetter(str: string) {
        if (!str) {
            return '';
        }
        return str.charAt(0).toUpperCase() + str.slice(1);
    }

    setAccessories(vehicle?: any) {
        if (vehicle && vehicle.vehid && vehicle.vehid !== NONVEHICLE) {
            this.accessoryLists = (this.accessoryLists || []).filter(a => {
                switch (a.linkType) {
                    case -1:
                        return true;
                    case 1:
                        return a.linkVal == vehicle.id_make;
                    case 2:
                        return a.linkVal == vehicle.id_model;
                    case 3:
                        return a.linkVal == vehicle.ktype;
                }
            });
        }
        this.hasAccessories = (this.accessoryLists || []).length > 0;
    }

    private getArticleItemCategory(article) {
        this.icat = article.icat;
        this.icat2 = article.icat2;
        this.icat3 = article.icat3;
        this.icat4 = article.icat4;
        this.icat5 = article.icat5;
    }

    getDisplayAvail(isInCart = false) {
        if (!this.availabilities || this.availabilities.length === 0) {
            return null;
        }
        const avails = AvailabilityUtil.sortAvailWithLatestTime(this.availabilities);
        const hasInvalidAvail = avails.some(avail => avail.backOrder);
        const hasDropShipmentFakeAvail = AvailabilityUtil.isArticleCon(avails);
        if (avails.length > 1 && (hasInvalidAvail || hasDropShipmentFakeAvail)) {
            let latestAvailIndex = 0;
            if (isInCart) {
                latestAvailIndex = (avails || []).findIndex(a => !a.backOrder);
            } else {
                latestAvailIndex = (avails || []).findIndex(a => (!a.backOrder && !a.conExternalSource));
            }
            return avails[latestAvailIndex];
        }
        return avails[0];
    }
}
