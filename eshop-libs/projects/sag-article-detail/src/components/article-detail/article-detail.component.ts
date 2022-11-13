import {
    AfterViewInit,
    ChangeDetectorRef,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    Output,
    QueryList,
    SimpleChanges,
    TemplateRef,
    ViewChild,
    ViewChildren,
} from '@angular/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { BroadcastService, AffiliateUtil, ProjectId, SagAvailDisplaySettingModel, SAG_AVAIL_DISPLAY_STATES, MarkedHtmlPipe, SAG_AVAIL_DISPLAY_OPTIONS } from 'sag-common';
import { ArticleAction, ArticleBroadcastKey } from '../../enums/article-broadcast-key.enum';
import { CustomPrice, CustomPriceUpdate, GrossPriceType, SagCustomPricingService } from 'sag-custom-pricing';
import { ARTICLE_MODE } from '../../enums/article-mode.enum';
import { ArticleDetailConfigService } from '../../services/article-detail-config.service';
import { ArticleModel } from '../../models/article.model';
import { ArticlesService } from '../../services/articles.service';
import { AvailabilityUtil } from '../../utils/availability.util';
import { CategoryModel } from '../../models/category.model';
import { NONVEHICLE, OTHER_SUPPLIERS_NAME } from '../../consts/article-detail.const';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';
import { ArticleAvailabilityModel } from '../../models/article-availability.model';
import { ArticleBasketModel } from '../../models/article-basket.model';
import { ARTICLE_PARENT } from '../../enums/article-parent.enums';
import { SagArticleWarningAvailPopupComponent } from '../article-warning-avail-popup/article-warning-avail-popup.component';
import { SubSink } from 'subsink';
import { FAVORITE_BROADCAST_EVENT } from '../../consts/article-detail.const';
import { PartDetailDescriptionConst } from '../article-detail-description/article-detail-description.component';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { CurrencyUtil, MAX_QUANTITY, SAG_CURRENCY_INPUT_HORIZONTAL_MODE } from 'sag-currency';
import { SagArticleDetailIntegrationService } from '../../services/article-detail-integration.service';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { get, uniqBy, sumBy } from 'lodash';
import uuid from 'uuid/v4';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'sag-article-detail',
    templateUrl: './article-detail.component.html',
    styleUrls: ['./article-detail.component.scss']
})
export class SagArticleDetailComponent implements OnInit, OnDestroy, OnChanges, AfterViewInit {
    @Input() index: number;
    @Input() article: ArticleModel;
    // Article belong to a list which have to get newset avail by a bash[5,10...] request to ERP
    // Not by itself;
    @Input() set availabilities(val: any[]) {
        if (!val || val.length === 0) {
            if (this.projectId === ProjectId.CONNECT) {
                this.artAvailabilities = null;
            }
            return;
        }
        const artAvail = val.find(avail => avail.key === this.article.pimId);
        if (!!artAvail) {
            this.article.availabilities = artAvail.value;
            this.initalAvail(this.article.availabilities);
        }
        this.checkAllowedAddToShoppingCart();
        this.checkCustomerHaveFGASCertificateOrNot();
    }
    @Input() set price(val: any) {
        if (this.article && val) {
            this.article.price = val;
            this.initialPrice();
            if (!this.disabled && !this.article.bom) {
                this.isCartItem();
            }
        }
    }
    @Input() articleMode: string;

    @Input() removable = false;
    @Input() selectable = false;
    @Input() disabled = false;

    @Input() enableShoppingCart = true;
    @Input() enableDetail = true;
    @Input() enableAvail = true;

    @Output() removeArticleEmitter = new EventEmitter();
    @Output() checkArticleEmitter = new EventEmitter();
    @Output() onArticleNumberClickEmitter = new EventEmitter();
    @Output() onShowAccessoriesEmitter = new EventEmitter();
    @Output() onShowPartsListEmitter = new EventEmitter();
    @Output() onShowCrossReferenceEmitter = new EventEmitter();

    @Input() userSetting: LibUserSetting;
    @Input() category: CategoryModel;
    @Input() vehicle: any;
    @Input() cartKey = null;
    @Input() quantity = null;
    @Input() isShownMoreInfo = false;
    @Input() productText;
    @Input() currentStateVatConfirm = false;
    @Input() parent: ARTICLE_PARENT;
    @Input() isSBMode: boolean;

    @Input() isSubBasket = false;

    @Input() set vinType(val: string) {
        if (val) {
            this.vinTypeDisplay = val.toLocaleLowerCase();
            this.enableAvail = false;
            this.enableShoppingCart = false;
            this.enableDetail = false;
        }
    }

    @Input() specialInfoTemplateRef: TemplateRef<any>;
    @Input() memosTemplateRef: TemplateRef<any>;
    @Input() customAvailTemplateRef: TemplateRef<any>;
    @Input() customAvailPopoverContentTemplateRef: TemplateRef<any>;
    @Input() priceTemplateRef: TemplateRef<any>;
    @Input() totalPriceTemplateRef: TemplateRef<any>;

    @Output() popoversChanged = new EventEmitter();
    @Output() syncArticleEvent = new EventEmitter();

    @Input() isAccessoryItem = false;
    @Input() isPartsItem = false;
    @Input() hideArticleInfo = false;
    @Input() amountFromFileImported: number;
    @Input() rootModalName: string = '';

    isSimpleMode: boolean;
    descriptionType = PartDetailDescriptionConst;
    isShownSpecDetail: boolean;
    isArticle24h: boolean;
    isSofort: boolean;
    artAvailabilities: ArticleAvailabilityModel[];
    grossPrice: number;
    nonAvailableSymbol = 'fa-exclamation';
    inputHorizontalMode = SAG_CURRENCY_INPUT_HORIZONTAL_MODE;
    availStatePath: string;
    projectId = ProjectId.CONNECT;
    PROJECT_ID = ProjectId;
    vinTypeDisplay: string;
    priceType: string;
    customPriceBrand: any = null;
    displayedPrice: any = null;
    // currently only use for cz
    allowedAddToShoppingCart = true;

    uuid = uuid();
    isEhCh = false;
    isEhCz = false;
    popoverDelay = 0;
    isShowNetPrice = false;
    isNotExistsInAx = false;

    actionFavorite = FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_ARTICLE;
    isSb: boolean;
    isCz: boolean;

    private bsModalRef: BsModalRef;
    subs = new SubSink();
    affiliateCode: string;
    availDisplaySettings: SagAvailDisplaySettingModel[];
    availDisplayStates = SAG_AVAIL_DISPLAY_STATES;
    customerHaveFGASCertificate = true;
    hasInvalidAvail: boolean;
    hasDropShipmentFakeAvail: boolean;
    availableQuantity: number;
    latestAvailIndex: number;
    showCrossReference: boolean;

    partPosMaxLength = 40;
    maxQuantity: number = MAX_QUANTITY;

    articleRef: ArticleModel;

    @ViewChildren(PopoverDirective) popovers: QueryList<PopoverDirective>;
    @ViewChild('pop', { static: false }) pop: any;
    private cartEvent$: Subscription;
    private updateArticleEvent$: Subscription;
    private updateIdenticalArticleEvent$: Subscription;
    private addAllPartItemsToCartEvent$: Subscription;
    private popoversEvent$: Subscription;

    constructor(
        private config: ArticleDetailConfigService,
        private broadcaster: BroadcastService,
        private integrationService: SagArticleDetailIntegrationService,
        private customPricingService: SagCustomPricingService,
        private articlesService: ArticlesService,
        private ref: ElementRef,
        private cdr: ChangeDetectorRef,
        private modalService: BsModalService,
        private markedHtmlPipe: MarkedHtmlPipe,
        private translateService: TranslateService
    ) {
        this.isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate);
        this.isSb = AffiliateUtil.isSb(this.config.affiliate);
        this.affiliateCode = this.config.affiliate;
        if (this.config.projectId === ProjectId.AUTONET) {
            this.partPosMaxLength = 200;
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.article && !changes.article.firstChange) {
            this.initialAvail();
            this.initialPrice();
            this.checkAllowedAddToShoppingCart();
            this.checkCustomerHaveFGASCertificateOrNot();
            this.article.setAccessories(this.vehicle);
        }
        if (changes.articleMode && !changes.articleMode.firstChange) {
            this.isSimpleMode = this.articleMode === ARTICLE_MODE.SIMPLE_MODE;
        }
        if (changes.userSetting && !changes.userSetting.firstChange) {
            this.initialPrice();
            this.setVisibleOfNetPrice();
        }
        if (changes.currentStateVatConfirm && !changes.currentStateVatConfirm.firstChange) {
            this.initialPrice();
        }
    }

    ngOnInit() {
        const replacementVehicle = get(this.article, 'replacementInContextData.vehicle');
        const replacementCategory = get(this.article, 'replacementInContextData.category');
        if (replacementVehicle) {
            this.vehicle = replacementVehicle;
        }
        if (replacementCategory) {
            this.category = replacementCategory;
        }
        this.isEhCh = AffiliateUtil.isEhCh(this.config.affiliate);
        this.isEhCz = AffiliateUtil.isEhCz(this.config.affiliate);
        this.isSimpleMode = this.articleMode === ARTICLE_MODE.SIMPLE_MODE;

        this.showCrossReference = this.checkShowCrossReference();

        this.projectId = this.config.projectId || ProjectId.CONNECT;
        if (this.vehicle && this.vehicle.vehid && this.vehicle.vehid !== NONVEHICLE) {
            this.customPriceBrand = {
                brand: this.vehicle.vehicle_brand,
                brandId: this.vehicle.id_make
            };
        }

        this.initialPrice();
        this.initialAvail();

        this.nonAvailableSymbol = AffiliateUtil.isAffiliateCH(this.config.affiliate) || this.isEhCz ? 'fa-exclamation' : 'fa-info-circle';
        this.checkAllowedAddToShoppingCart();
        this.registerUpdateCartItemListener();
        this.registerUpdateIdenticalArticleListener();
        this.registerAddAllPartItemsToCart();

        this.popoverDelay = this.userSetting && this.userSetting.mouseOverFlyoutDelay || 0;
        this.setVisibleOfNetPrice();
        this.availDisplaySettings = this.userSetting && this.userSetting.availDisplaySettings || [];
        this.checkCustomerHaveFGASCertificateOrNot();

        this.article.setAccessories(this.vehicle);
        this.articleRef = this.article;
    }

    ngAfterViewInit() {
        if (this.article.priceRequested && !this.disabled && !this.article.bom) {
            // this for checking if the article is added in cart
            this.isCartItem();
        }
        if (this.popovers && this.popoversChanged) {
            this.popoversChanged.emit(this.popovers);
            this.popoversEvent$ = this.popovers.changes.subscribe(pops => {
                this.popoversChanged.emit(pops);
            })
        }
    }

    ngOnDestroy(): void {
        if (this.cartEvent$) {
            this.cartEvent$.unsubscribe();
        }

        if (this.bsModalRef) {
            this.bsModalRef.hide();
        }

        this.subs.unsubscribe();
        if (this.updateArticleEvent$) {
            this.updateArticleEvent$.unsubscribe();
        }

        if (this.updateIdenticalArticleEvent$) {
            this.updateIdenticalArticleEvent$.unsubscribe();
        }

        if (this.popoversEvent$) {
            this.popoversEvent$.unsubscribe();
        }

        if (this.addAllPartItemsToCartEvent$) {
            this.addAllPartItemsToCartEvent$.unsubscribe();
        }
    }

    get isPartialDisplayText() {
        if (!this.availDisplaySettings || AffiliateUtil.isAffiliateCZ9(this.config.affiliate)) {
            return false;
        }

        const avaiSetting = AvailabilityUtil.getAvailSettingByAvailState(this.availDisplaySettings, this.availDisplayStates.PARTIALLY_AVAILABLE);
        return avaiSetting && avaiSetting.displayOption === SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT;
    }

    getAvailDisplayText(availability, state) {
        const text = AvailabilityUtil.getAvailTextDisplayWhenHaveTime(this.availDisplaySettings, availability, this.config.appLangCode, this.config.affiliate, state);
        if (text) {
            return this.markedHtmlPipe.transform(text);
        }

        return '';
    }

    getAvailDisplayTextCaseSofort() {
        const availSetting = AvailabilityUtil.getAvailSettingByAvailState(this.availDisplaySettings, this.availDisplayStates.PARTIALLY_AVAILABLE);
        if (availSetting && availSetting.displayOption === SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT) {
            const text = AvailabilityUtil.getAvailContentByLanguageCode(availSetting.listAvailText, this.config.appLangCode);
            if (text) {
                return this.markedHtmlPipe.transform(text);
            }

            return '';
        }

        return this.translateService.instant('ARTICLE.DELIVERY_IMMEDIATE');
    }

    getAvailColor(state: string) {
        return AvailabilityUtil.getAvailColorByAvailState(this.availDisplaySettings, this.config.affiliate, state);
    }

    getSpecialNotesWithDeliveryTour(availabilities) {
        const text = AvailabilityUtil.initInStockNoteForTour(this.availDisplaySettings, availabilities, this.article.stock, this.config.affiliate, this.config.appLangCode);
        if (text) {
            return this.markedHtmlPipe.transform(text);
        }

        return '';
    }

    setVisibleOfNetPrice() {
        this.isShowNetPrice = this.userSetting.currentStateNetPriceView;
        if (this.isEhCh || this.isEhCz) {
            this.isShowNetPrice = this.userSetting.netPriceView && this.userSetting.fcUserCanViewNetPrice;
        }
    }

    async addToShoppingCart(amountNumber?, done?, isCalledFromTemplateEvent?: boolean) {
        // case click icon, if already in cart dont need to do
        // if (!amountNumber && this.article.cartKey) {
        //     return;
        // }
        if (this.disabled) {
            return;
        }

        if (this.isSb) {
            this.config.spinner.start(`#part-detail-${this.uuid}`,
                { containerMinHeight: 0 }
            );

            const article = await this.articlesService.getArticleAvailabilityForCheckPrimaryLocation({
                pimId: this.article.pimId,
                amount: amountNumber || this.quantity || this.article.amountNumber,
                stock: this.article.stock
            });

            this.config.spinner.stop(`#part-detail-${this.uuid}`);

            if (article) {
                const newArticle = new ArticleModel(article);
                if (!newArticle.isAllInPrioLocation()) {
                    this.quantity = amountNumber || this.quantity || this.article.amountNumber;

                    this.showWarningNoQuantityMsg(newArticle);

                    return;
                }
            }
        }

        if (this.article.bom) {
            this.handleAddPseudoArticle(amountNumber);
            return;
        }
        this.article.oldQuantity = this.quantity;
        amountNumber = this.overrideAmountFromShoppingList(isCalledFromTemplateEvent, amountNumber);
        this.broadcaster.broadcast(ArticleBroadcastKey.SHOPPING_BASKET_EVENT, {
            index: this.index,
            uuid: this.uuid,
            action: 'ADD',
            amount: amountNumber || this.quantity || this.article.amountNumber,
            pimId: this.article.pimId,
            artnr: this.article.artnr,
            stock: this.article.stock,
            category: this.category,
            vehicle: this.vehicle,
            cartKey: this.cartKey,
            article: this.article,
            isAccessoryItem: this.isAccessoryItem,
            isPartsItem: this.isPartsItem,
            rootModalName: this.rootModalName,
            callback: (data) => {
                if (done) {
                    done();
                }
                if (!data) {
                    return;
                }
                if (data.notFoundInAx) {
                    this.updateNotExistsInAxArticle();
                    return;
                }
                this.updateArticleInfo(data);
                this.broadcaster.broadcast(ArticleBroadcastKey.UPDATE_IDENTICAL_ARTICLE, {
                    action: ArticleAction.ADD,
                    uuid: this.uuid,
                    vehicle: this.vehicle,
                    ...data
                });
            }
        } as ArticleBasketModel);
    }

    removeArticle() {
        this.removeArticleEmitter.emit(this.article);
        if (this.cartKey) {
            this.broadcaster.broadcast(ArticleBroadcastKey.SHOPPING_BASKET_EVENT, {
                index: this.index,
                uuid: this.uuid,
                action: 'REMOVE',
                cartKey: this.cartKey,
                article: this.article,
                callback: ({ amount, article, cartKey, enableShoppingCart, removable }) => {
                    if (enableShoppingCart !== undefined) {
                        this.enableShoppingCart = enableShoppingCart;
                    }
                    if (removable !== undefined) {
                        this.removable = removable;
                    }
                    this.broadcaster.broadcast(this.cartKey);
                }
            } as ArticleBasketModel);
        }

    }

    onMarkAsDeleted() {
        this.checkArticleEmitter.emit(this.article);
    }

    showSpecialDetail() {
        this.isShownSpecDetail = !this.isShownSpecDetail;
        this.broadcaster.broadcast(ArticleBroadcastKey.TOGGLE_SPECIAL_DETAIL, {
            article: {
                ...this.article,
                index: this.index,
                isShownSpecDetail: this.isShownSpecDetail,
            },
            rootModalName: this.rootModalName,
        });
    }

    showSNPInfo(event) {

    }

    showTyrePromotionInfo(event) {

    }

    onCustomPriceChange(displayedPrice: CustomPrice) {
        const vehid = this.vehicle && this.vehicle.id;
        if (this.cartKey) {
            const body = [{
                cartKey: this.cartKey,
                displayedPrice
            } as CustomPriceUpdate];
            const spinner = this.config.spinner.start(`#part-detail-${this.uuid}`, {
                containerMinHeight: 0
            });
            this.customPricingService.updateCustomPriceInBasket(body)
                .pipe(finalize(() => this.config.spinner.stop(spinner)))
                .subscribe(res => {
                    const data: any = {
                        action: ArticleAction.CUSTOM_PRICE_CHANGE,
                        uuid: this.uuid,
                        article: this.article,
                        vehicle: this.vehicle,
                        displayedPrice,
                        response: res
                    };
                    this.updateArticleCustomPrice(data);
                    this.broadcaster.broadcast(ArticleBroadcastKey.UPDATE_IDENTICAL_ARTICLE, data);
                    this.broadcaster.broadcast(ArticleBroadcastKey.SHOPPING_BASKET_EVENT, {
                        action: 'CUSTOM_PRICE_CHANGE',
                        article: this.article,
                        basket: res
                    });

                    if (this.parent === ARTICLE_PARENT.SHOPPING_BASKET) {
                        this.sendShoppingBasketEvent(res && res.items || []);
                    } else {
                        this.sendArticleListEventData(displayedPrice, vehid);
                    }
                }, error => {
                    console.error('Update displayed price fail: ', error);
                });
        } else {
            this.article.displayedPrice = displayedPrice;
            this.initialPrice();
            this.sendArticleListEventData(displayedPrice, vehid);
        }
    }

    onArticleNumberClick(article: ArticleModel) {
        if (!article) {
            return;
        }
        const replacementInContextData = {
            vehicle: this.vehicle,
            category: this.category
        };
        this.onArticleNumberClickEmitter.emit({ ...article, replacementInContextData });
    }

    onShowAccessories() {
        this.onShowAccessoriesEmitter.emit({ article: this.article, vehicle: this.vehicle, category: this.category });
    }

    onShowPartsList() {
        this.onShowPartsListEmitter.emit({ article: this.article, vehicle: this.vehicle, category: this.category });
    }

    onShowCrossReference() {
        this.onShowCrossReferenceEmitter.emit({ article: this.article, vehicle: this.vehicle, category: this.category });
    }

    private sendArticleListEventData(displayedPrice: CustomPrice, vehid: string) {
        this.integrationService.sendArticleListEventData({
            article: {
                artid: this.article.artid,
                artlistPriceType: displayedPrice && displayedPrice.type || GrossPriceType.UVPE.toString(),
                artlistPriceVehBrand: (displayedPrice && displayedPrice.brand)
                    || (this.customPriceBrand && this.customPriceBrand.brand),
                artlistVehicleId: vehid
            }
        });
    }

    private sendShoppingBasketEvent(cartItems = []) {
        const cartItem = cartItems.find(item => item.cartKey === this.cartKey);
        this.integrationService.sendShoppingBasketEvent(
            cartItem,
            {
                type: GrossPriceType.UVPE.toString(),
                brand: this.vehicle && this.vehicle.vehicle_brand
            }
        );
    }

    private initialPrice() {
        if (!this.article.price || !this.article.price.price) {
            return;
        }

        const isInCart = !!this.cartKey;

        this.priceType = this.article.getPriceType(this.customPriceBrand);

        this.article.setGrossPrice(this.currentStateVatConfirm);
        this.article.setNetPrice(this.currentStateVatConfirm);
        this.article.setTotalGrossPrice(this.currentStateVatConfirm, isInCart);
        this.article.setTotalNetPrice(this.currentStateVatConfirm);
        this.reCalculateGrossPriceForDepositArticle();
    }

    private initialAvail() {
        if (this.article.availabilities) {
            this.initalAvail(this.article.availabilities);
        }
    }

    private initalAvail(validAvails: ArticleAvailabilityModel[]) {
        if (this.enableAvail) {
            if (this.projectId === ProjectId.AUTONET) {
                this.artAvailabilities = validAvails || [];
                if (this.artAvailabilities && this.artAvailabilities.length > 0) {
                    this.availStatePath = this.artAvailabilities[0].presentPath;
                }
            } else {
                const unsortedAvailabilities = validAvails; // as ErpAvailability[];
                this.artAvailabilities = AvailabilityUtil.sortAvailWithLatestTime(unsortedAvailabilities);
                this.hasInvalidAvail = this.artAvailabilities.some(avail => avail.backOrder);
                if (this.cartKey) {
                    this.availableQuantity = sumBy(this.artAvailabilities, a => a.backOrder ? 0 : a.quantity);
                    this.latestAvailIndex = (this.artAvailabilities || []).findIndex(a => !a.backOrder);
                    this.hasDropShipmentFakeAvail = false;
                } else {
                    this.getAvailInCaseOfArticleNotInBasket();
                }
                this.isArticle24h = this.isEhCz ? AvailabilityUtil.isArticle24hCz(this.artAvailabilities) : AvailabilityUtil.isArticle24h(this.artAvailabilities);
                if (this.artAvailabilities && this.artAvailabilities.length > 0) {
                    this.isSofort = this.artAvailabilities[0].sofort;
                }
                this.article.availabilities = [...this.artAvailabilities];
            }

            if (this.isSb) {
                this.checkAllowedAddToShoppingCart();
            }
        }
    }

    private getAvailInCaseOfArticleNotInBasket() {
        this.hasDropShipmentFakeAvail = AvailabilityUtil.isArticleCon(this.artAvailabilities);
        if (this.hasInvalidAvail || this.hasDropShipmentFakeAvail) {
            this.availableQuantity = sumBy(this.artAvailabilities, a => (a.backOrder || a.conExternalSource) ? 0 : a.quantity);
            this.latestAvailIndex = (this.artAvailabilities || []).findIndex(a => (!a.backOrder && !a.conExternalSource));
        }
    }

    private isCartItem() {
        if (this.isPartsItem && this.hideArticleInfo) {
            return;
        }
        this.broadcaster.broadcast(ArticleBroadcastKey.SHOPPING_BASKET_EVENT, {
            action: 'LOADED',
            pimId: this.article.pimId,
            vehicle: this.vehicle,
            article: this.article,
            uuid: this.uuid,
            isAccessoryItem: this.isAccessoryItem,
            isPartsItem: this.isPartsItem,
            callback: ({ amount, cartKey, enableShoppingCart, removable, price, displayedPrice }) => {
                this.displayedPrice = displayedPrice;
                if (enableShoppingCart !== undefined) {
                    this.enableShoppingCart = enableShoppingCart;
                }
                if (removable !== undefined) {
                    this.removable = removable;
                }
                this.quantity = amount;
                this.cartKey = cartKey;
                if (amount !== this.article.salesQuantity) {
                    const spinner = this.config.spinner.start(`#part-detail-${this.uuid}`, {
                        containerMinHeight: 0
                    });
                    const oilArticle = this.article.oilArticle;
                    const oilProduct = this.article.oilProduct;
                    const favorite = this.article.favorite;
                    const favoriteComment = this.article.favoriteComment;
                    const salesQuantity = this.article.salesQuantity;
                    // update avail and price by new amount
                    this.articlesService
                        .syncArticle({ amount, pimId: this.article.pimId })
                        .subscribe((art: ArticleModel) => {
                            if (!art) {
                                return;
                            }
                            if (art.notFoundInAx) {
                                this.updateNotExistsInAxArticle();
                                return;
                            }
                            // To sync data to article list
                            this.articleRef.amountNumber = amount;
                            this.article = new ArticleModel(art);
                            this.article.displayedPrice = this.displayedPrice;
                            this.article.updatePrice(price);
                            this.article.oilArticle = oilArticle;
                            this.article.oilProduct = oilProduct;
                            this.article.favorite = favorite;
                            this.article.favoriteComment = favoriteComment;
                            if (this.isAccessoryItem || this.isPartsItem) {
                                this.article.salesQuantity = salesQuantity;
                            }
                            this.initialPrice();
                            this.initialAvail();
                            this.article.setAccessories(this.vehicle);
                            this.config.spinner.stop(spinner);
                            this.syncArticleEvent.emit(this.article);
                        });
                } else {
                    // use the already calculated value
                    this.cartKey = cartKey;
                    this.article.displayedPrice = this.displayedPrice;
                    this.article.updatePrice(price);
                    this.initialPrice();
                    this.initialAvail();
                    this.syncArticleEvent.emit(this.article);
                }
                this.registerRemoveCartItemListener(this.cartKey);

                // apply changes
                setTimeout(() => {
                    this.cdr.detectChanges();
                });
            },
            addToCart: (amount?: number) => {
                this.addToShoppingCart(amount);
            }
        } as ArticleBasketModel);
    }

    private registerRemoveCartItemListener(cartKey) {
        if (this.cartEvent$) {
            this.cartEvent$.unsubscribe();
        }
        this.cartEvent$ = this.broadcaster.on(cartKey).subscribe(() => {
            const spinner = this.config.spinner.start(`#part-detail-${this.uuid}`, {
                containerMinHeight: 0
            });
            const defaultAmount = this.article.salesQuantity;
            const oilArticle = this.article.oilArticle;
            const oilProduct = this.article.oilProduct;
            const favorite = this.article.favorite;
            const favoriteComment = this.article.favoriteComment;
            const salesQuantity = this.article.salesQuantity;
            this.articlesService
                .syncArticle({ amount: defaultAmount, pimId: this.article.pimId })
                .subscribe((art: ArticleModel) => {
                    if (!art) {
                        return;
                    }
                    if (art.notFoundInAx) {
                        this.updateNotExistsInAxArticle();
                        return;
                    }
                    // To sync data to article list
                    this.articleRef.amountNumber = defaultAmount;
                    this.article = new ArticleModel(art);
                    this.article.oilArticle = oilArticle;
                    this.article.oilProduct = oilProduct;
                    this.article.favorite = favorite;
                    this.article.favoriteComment = favoriteComment;
                    if (this.isAccessoryItem || this.isPartsItem) {
                        this.article.salesQuantity = salesQuantity;
                    }
                    this.quantity = null;
                    this.cartKey = null;
                    this.initialPrice();
                    this.initialAvail();
                    this.article.setAccessories(this.vehicle);
                    this.config.spinner.stop(spinner);
                });
            this.cartEvent$.unsubscribe();
        });
    }

    private registerUpdateCartItemListener() {
        if (this.updateArticleEvent$) {
            this.updateArticleEvent$.unsubscribe();
        }

        this.updateArticleEvent$ = this.broadcaster.on(ArticleBroadcastKey.UPDATE_ARTICLE_BY_CART_KEYS).subscribe((shoppingBasketItems: any[]) => {
            const shoppingItem = (shoppingBasketItems || []).find(item => {
                return this.article.artid === (item.articleItem && item.articleItem.artid || '') &&
                    item.vehicleId === (this.vehicle && this.vehicle.id || '');
            });
            if (shoppingItem && shoppingItem.quantity !== this.quantity) {
                const spinner = this.config.spinner.start(`#part-detail-${this.uuid}`, {
                    containerMinHeight: 0
                });

                const defaultAmount = shoppingItem.quantity;
                const oilArticle = this.article.oilArticle;
                const oilProduct = this.article.oilProduct;
                const displayedPrice = shoppingItem.articleItem.displayedPrice;
                const favorite = this.article.favorite;
                const favoriteComment = this.article.favoriteComment;
                const salesQuantity = this.article.salesQuantity;
                this.articlesService
                    .syncArticle({ amount: defaultAmount, pimId: this.article.pimId })
                    .subscribe((art: ArticleModel) => {
                        if (!art) {
                            return;
                        }
                        if (art.notFoundInAx) {
                            this.updateNotExistsInAxArticle();
                            return;
                        }
                        // To sync data to article list
                        this.articleRef.amountNumber = defaultAmount;
                        this.article = new ArticleModel(art);
                        this.article.oilArticle = oilArticle;
                        this.article.oilProduct = oilProduct;
                        this.article.displayedPrice = displayedPrice;
                        this.article.favorite = favorite;
                        this.article.favoriteComment = favoriteComment;
                        if (this.isAccessoryItem || this.isPartsItem) {
                            this.article.salesQuantity = salesQuantity;
                        }
                        this.quantity = defaultAmount;
                        this.cartKey = shoppingItem.cartKey;
                        this.registerRemoveCartItemListener(this.cartKey);
                        this.initialPrice();
                        this.initialAvail();
                        this.article.setAccessories(this.vehicle);
                        this.config.spinner.stop(spinner);
                    });
            }
        });

    }

    private registerUpdateIdenticalArticleListener() {
        if (this.updateIdenticalArticleEvent$) {
            this.updateIdenticalArticleEvent$.unsubscribe();
        }

        this.updateIdenticalArticleEvent$ = this.broadcaster.on(ArticleBroadcastKey.UPDATE_IDENTICAL_ARTICLE).subscribe((data: any) => {
            if (!data || !data.article) {
                return;
            }
            const { article, vehicle } = data;
            if (article.artid === this.article.artid && data.uuid !== this.uuid && (vehicle && vehicle.vehicleId || '') === (this.vehicle && this.vehicle.vehid || '')) {
                switch (data.action) {
                    case ArticleAction.ADD:
                        this.updateArticleInfo(data);
                        break;
                    case ArticleAction.CUSTOM_PRICE_CHANGE:
                        this.updateArticleCustomPrice(data);
                        break;
                }

            }
        });

    }

    private registerAddAllPartItemsToCart() {
        if (this.addAllPartItemsToCartEvent$) {
            this.addAllPartItemsToCartEvent$.unsubscribe();
        }

        this.addAllPartItemsToCartEvent$ = this.broadcaster.on(ArticleBroadcastKey.ADD_ALL_PART_ITEMS).subscribe((data: ArticleBasketModel) => {
            const partItemIds = (data && data.article && data.article.partsListItems || []).map(item => String(item.parts_list_item_id_art || '')).filter(x => x);
            if (partItemIds.some(artid => artid === this.article.artid)) {
                if (this.article.pseudo) {
                    data.confirm(String(this.article.artid));
                    data.done(String(this.article.artid));
                    return;
                }
                this.addToShoppingCart(null, () => {
                    if (data && data.done) {
                        data.done(String(this.article.artid));
                    }
                });
                if (data && data.confirm) {
                    data.confirm(String(this.article.artid));
                }
            }
        });
    }

    private checkAllowedAddToShoppingCart() {
        if ((AffiliateUtil.isAffiliateCZ(this.config.affiliate) || AffiliateUtil.isAffiliateApplyFgasAndDeposit(this.config.affiliate)) && this.article.allowedAddToShoppingCart === false) {
            this.allowedAddToShoppingCart = false;
        }
        if (this.article.allowedAddToShoppingCart) {
            this.allowedAddToShoppingCart = true;
        }

        if (this.isSb) {
            if (this.artAvailabilities && this.artAvailabilities.length > 0) {
                this.allowedAddToShoppingCart = this.artAvailabilities[0].location && this.artAvailabilities[0].location.allInPrioLocations;
            } else {
                this.allowedAddToShoppingCart = false;
            }
        }
    }

    private showWarningNoQuantityMsg(article: ArticleModel) {
        const locations = article.availabilities && article.availabilities[0] && article.availabilities[0].location.items || [];

        this.bsModalRef = this.modalService.show(SagArticleWarningAvailPopupComponent, {
            ignoreBackdropClick: true,
            initialState: {
                locations: locations,
                userSetting: this.userSetting
            },
            class: 'warning-avail-primary-modal-content'
        });

        this.subs.sink = this.modalService.onHidden.subscribe(data => {
            this.quantity = this.cartKey ? this.article.amountNumber : null;
        });
    }

    private updateArticleInfo({ amount, article, cartKey, enableShoppingCart, removable, price, displayedPrice }) {
        const favorite = this.article.favorite;
        const favoriteComment = this.article.favoriteComment;
        const salesQuantity = this.article.salesQuantity;
        // To sync data to article list
        this.articleRef.amountNumber = amount;
        this.article = new ArticleModel(article);
        this.article.favorite = favorite;
        this.article.favoriteComment = favoriteComment;
        this.article.displayedPrice = displayedPrice;
        if (enableShoppingCart !== undefined) {
            this.enableShoppingCart = enableShoppingCart;
        }
        if (removable !== undefined) {
            this.removable = removable;
        }
        if (this.isAccessoryItem || this.isPartsItem) {
            this.article.salesQuantity = salesQuantity;
        }
        this.cartKey = cartKey;
        this.quantity = amount;
        this.article.updatePrice(price);
        this.initialPrice();
        this.initialAvail();
        this.article.setAccessories(this.vehicle);
        this.config.spinner.stop(`#part-detail-${this.uuid}`);
        this.registerRemoveCartItemListener(this.cartKey);

        if (!!this.pop && !this.pop.isOpen
            && this.article.salesQuantity > 1
            && this.artAvailabilities.length > 1) {
            this.ref.nativeElement.click();
            this.cdr.detectChanges();
            this.pop.show();
        }

        // apply changes
        setTimeout(() => {
            this.cdr.detectChanges();
        });
    }

    private updateArticleCustomPrice({ displayedPrice, response }) {
        const vehid = this.vehicle && this.vehicle.id;
        this.article.displayedPrice = displayedPrice;
        const cartItem = (response && response.items || []).find(item => item.articleItem.id_pim === this.article.pimId &&
            ((!item.vehicleId && !vehid) || item.vehicleId === vehid));
        if (cartItem) {
            this.displayedPrice = cartItem.articleItem.displayedPrice;
            this.article.updatePrice({
                grossPrice: cartItem.grossPrice,
                totalGrossPrice: cartItem.totalGrossPrice,
                totalNetPrice: cartItem.totalNetPrice,
                netPriceWithVat: cartItem.netPriceWithVat,
                grossPriceWithVat: cartItem.grossPriceWithVat,
                totalGrossPriceWithVat: cartItem.totalGrossPriceWithVat,
                totalNetPriceWithVat: cartItem.totalNetPriceWithVat,
                discountPriceWithVat: cartItem.discountPriceWithVat
            });
        }
        this.initialPrice();
    }

    private updateNotExistsInAxArticle() {
        this.isNotExistsInAx = true;
        this.disabled = true;
        this.config.spinner.stop(`#part-detail-${this.uuid}`);
    }

    private handleAddPseudoArticle(amountNumber?) {
        const articles = [];
        let partItems = (this.article.partsListItems || []).filter(item => item.parts_list_item_id_art);
        partItems = uniqBy(partItems, 'parts_list_item_id_art');
        if (partItems.length === 0) {
            return;
        }
        const spinner = this.config.spinner.start(`#part-detail-${this.uuid}`, {
            containerMinHeight: 0
        });

        this.broadcaster.broadcast(ArticleBroadcastKey.ADD_ALL_PART_ITEMS, {
            amount: amountNumber || this.quantity || this.article.amountNumber,
            category: this.category,
            vehicle: this.vehicle,
            article: this.article,
            confirm: (artid) => {
                if (!articles.some(art => art.artid === artid)) {
                    articles.push({ artid });
                }
            },
            done: (artid) => {
                const article = articles.find(art => art.artid === artid);
                if (article) {
                    article.done = true;
                }
                if (articles.length >= partItems.length && articles.every(art => art.done)) {
                    this.config.spinner.stop(spinner);
                }
            }
        } as ArticleBasketModel);

        setTimeout(() => {
            const invisibleArtIds = partItems
                .filter(item => !articles.some(art => art.artid === String(item.parts_list_item_id_art || '')))
                .map(item => String(item.parts_list_item_id_art || ''));
            for (let artid of invisibleArtIds) {
                articles.push({ artid, done: false });
                this.articlesService
                    .syncArticle({ amount: amountNumber, pimId: artid })
                    .subscribe((art: ArticleModel) => {
                        if (!art || art.pseudo || art.notFoundInAx) {
                            const article = articles.find(item => item.artid === artid);
                            if (article) {
                                article.done = true;
                            }
                            if (articles.length >= partItems.length && articles.every(art => art.done)) {
                                this.config.spinner.stop(spinner);
                            }
                            return;
                        }
                        const article = new ArticleModel(art);
                        this.broadcaster.broadcast(ArticleBroadcastKey.SHOPPING_BASKET_EVENT, {
                            action: 'ADD',
                            amount: amountNumber || this.quantity || article.amountNumber,
                            pimId: article.pimId,
                            artnr: article.artnr,
                            stock: article.stock,
                            category: this.category,
                            vehicle: this.vehicle,
                            article: article,
                            isPartsItem: true,
                            callback: () => {
                                const article = articles.find(item => item.artid === art.artid);
                                if (article) {
                                    article.done = true;
                                }
                                if (articles.length >= partItems.length && articles.every(item => item.done)) {
                                    this.config.spinner.stop(spinner);
                                }
                            }
                        } as ArticleBasketModel);
                    });
            }
        }, 100);
    }

    private checkCustomerHaveFGASCertificateOrNot() {
        if (!this.allowedAddToShoppingCart && AffiliateUtil.isAffiliateApplyFgasAndDeposit(this.config.affiliate)) {
            this.customerHaveFGASCertificate = false;
        }
        if (this.allowedAddToShoppingCart) {
            this.customerHaveFGASCertificate = true;
        }
    }

    getAvailDisplayTextByState(state: string) {
        const availSetting = AvailabilityUtil.getAvailSettingByAvailState(this.availDisplaySettings, state);
        if (availSetting && availSetting.displayOption === SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT) {
            const text = AvailabilityUtil.getAvailContentByLanguageCode(availSetting.listAvailText, this.config.appLangCode);
            if (text) {
                return this.markedHtmlPipe.transform(text);
            }
            return '';
        }
        return '';
    }

    reCalculateGrossPriceForDepositArticle() {
        if (this.article.depositArticle) {
            this.article.depositArticle.setGrossPrice(this.currentStateVatConfirm);
        }
        if (this.article.vocArticle) {
            this.article.vocArticle.setGrossPrice(this.currentStateVatConfirm);
        }
        if (this.article.vrgArticle) {
            this.article.vrgArticle.setGrossPrice(this.currentStateVatConfirm);
        }
        if (this.article.pfandArticle) {
            this.article.pfandArticle.setGrossPrice(this.currentStateVatConfirm);
        }
    }

    private overrideAmountFromShoppingList(isCalledFromTemplateEvent: boolean, amountNumber: number) {
        if (isCalledFromTemplateEvent) {
            if (this.amountFromFileImported && this.amountFromFileImported > 0 && !this.quantity) {
                let amount = this.amountFromFileImported;
                const qtyMultiple = this.article.qtyMultiple;
                if (amount % qtyMultiple !== 0) {
                    amount = Math.ceil(amount / qtyMultiple) * qtyMultiple;
                }
                amountNumber = CurrencyUtil.getMaxQuantityValid(amount, qtyMultiple);
            }
        }
        return amountNumber;
    }

    private checkShowCrossReference() {
        const iamNumbers = Object.keys(this.article.iamNumbers || {});
        const pnrnPccs = this.article.pnrnPccs || [];

        if (iamNumbers.length === 0 && pnrnPccs.length === 0) {
            return false;
        }

        if (iamNumbers.length === 1) {
            if (iamNumbers[0] === OTHER_SUPPLIERS_NAME) {
                return false;
            }
        }

        return true;
    }
}
