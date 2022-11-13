import { Component, EventEmitter, Input, Output, OnChanges, OnDestroy } from '@angular/core';
import { OfferPosition, OfferPositionT } from '../../models/offer-position.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { OFFER_ITEM_TYPE } from '../../enums/offers.enum';
import { uniq, isEmpty } from 'lodash';
import { DiscountType } from '../../enums/discount.enums';
import { OfferPriceService } from '../../services/offer-price.service';
import { Validator } from 'src/app/core/utils/validator';
import { UserService } from 'src/app/core/services/user.service';
import { Subscription } from 'rxjs';
import { OfferDiscountService } from '../../services/offer-discount.service';
import { HaynesLinkHandleService, SagHaynesProReturnModalComponent } from 'sag-haynespro';
import { BsModalService } from 'ngx-bootstrap/modal';
import { MAX_QUANTITY } from 'sag-currency';

@Component({
    selector: 'connect-offer-item',
    templateUrl: './offer-item.component.html',
    styleUrls: ['../../offers.component.scss', './offer-item.component.scss']
})

export class OfferItemComponent implements OnChanges, OnDestroy {
    public readonly MINIMUM_AMOUNT_NUMBER = 1;
    public HAYNESPRO_PROVIDER_WORK = OFFER_ITEM_TYPE.HAYNESPRO_PROVIDER_WORK.toString();
    public CLIENT_WORK = OFFER_ITEM_TYPE.CLIENT_WORK.toString();
    public CLIENT_ARTICLE = OFFER_ITEM_TYPE.CLIENT_ARTICLE.toString();
    public dataItems = [];
    public labourTimeItems: Array<OfferPosition>;
    @Input() set items(dataItems: Array<OfferPosition>) {
        const items = JSON.parse(JSON.stringify(dataItems));
        this.dataItems = items.filter(item => item.type !== this.HAYNESPRO_PROVIDER_WORK);
        this.labourTimeItems = items.filter(item => item.type === this.HAYNESPRO_PROVIDER_WORK);
        if (this.dataItems && this.dataItems.length) {
            this.getVehicles();
            // keep the originalGrossPrice in case user update price
            this.keepOrginalGrossPrice();
        }
        this.initDiscount(this.dataItems);
    }
    @Input() isShowLabourTime: boolean;
    @Input() hourlyRate: any;
    @Output() eventCalculatePrice = new EventEmitter();
    @Output() eventDeleteItem = new EventEmitter();
    @Output() eventShowDiscount = new EventEmitter();
    @Output() emitUpdateLabourTime = new EventEmitter();

    maxQuantity = MAX_QUANTITY;

    private user: UserDetail;

    private initHaynesProLinks = false;

    public vehicles = [];
    public isLoginOnBehalf: boolean;
    // To keep check value changed. To prevent the case without value changed still calling api
    private valuesChangedMap = {};
    userSettingSubscription: Subscription;

    constructor(
        private bsModalService: BsModalService,
        private userService: UserService,
        private offerDiscountService: OfferDiscountService,
        public haynesLinkHandleService: HaynesLinkHandleService

    ) {
        // this.store.select(getIsLoginOnBehalf).subscribe(authed => this.isLoginOnBehalf = authed);
        this.userSettingSubscription = this.userService.userDetail$
            .subscribe((user: UserDetail) => {
                if (user && user.custNr) {
                    this.user = user;
                    this.buildAndShowLabourTimeAndHaynesProLink();
                }
            });
    }

    ngOnChanges() {
        this.buildAndShowLabourTimeAndHaynesProLink();
    }

    ngOnDestroy() {
        this.userSettingSubscription.unsubscribe();
    }

    private getVehicles() {
        this.vehicles = [];
        let vehicleIds = this.dataItems.map(item => item.connectVehicleId);

        vehicleIds = uniq(vehicleIds);
        vehicleIds.sort();

        vehicleIds.forEach(vehicleId => {
            const items = this.dataItems.filter(item => item.connectVehicleId === vehicleId);
            const labourTimes = !isEmpty(this.labourTimeItems) ?
                this.labourTimeItems.filter(item => item.connectVehicleId === vehicleId) : [];

            this.vehicles.push({
                connectVehicleId: vehicleId,
                vehicleDesc: items[0].vehicleDescription,
                content: this.mapItemsByCatName(items),
                labourTimes
            });
        });
    }

    mapItemsByCatName(items) {
        const itemsByCatName = [];
        let categoryNames = items.map(item => item.vehicleBomDescription);

        categoryNames = uniq(categoryNames);
        categoryNames.forEach(catName => {
            itemsByCatName.push({
                catName,
                articles: items.filter(item => item.vehicleBomDescription === catName)
            });
        });

        return itemsByCatName;
    }

    increaseQuantity(item: OfferPosition) {
        item.quantity = +item.quantity;
        item.quantity += 1;
        item.quantity = this.getMaxValidValue(item.quantity);
        item.totalGrossPrice = OfferPriceService.getTotalGrossPriceItem(item.grossPrice, item.quantity);
        this.eventCalculatePrice.emit({ item });
    }

    decreaseQuantity(item: OfferPosition) {
        item.quantity = Math.max(item.quantity - 1, this.MINIMUM_AMOUNT_NUMBER);
        item.quantity = this.getMaxValidValue(item.quantity);
        item.totalGrossPrice = OfferPriceService.getTotalGrossPriceItem(item.grossPrice, item.quantity);
        this.eventCalculatePrice.emit({ item });
    }

    updatePrice(blurEvent, item: OfferPosition) {
        if (this.valuesChangedMap[item.id]) {
            blurEvent.preventDefault();
            blurEvent.stopPropagation();
            delete this.valuesChangedMap[item.id];
            item.quantity = Number(item.quantity) || this.MINIMUM_AMOUNT_NUMBER;
            item.quantity = this.getMaxValidValue(item.quantity);
            item.totalGrossPrice = OfferPriceService.getTotalGrossPriceItem(item.grossPrice, item.quantity);
            this.updateDisplayPrice(item);
            this.eventCalculatePrice.emit({ item, relatedDom: blurEvent.relatedTarget });
        }
    }

    private updateDisplayPrice(item: OfferPosition) {
        if (!item.displayedPrice) {
          return;
        }
        item.displayedPrice.price = item.grossPrice;
    }

    deleteItem(item: OfferPosition) {
        this.eventDeleteItem.emit(item);
    }

    allowNumber(event) {
        return Validator.allowNumber(event);
    }

    allowDecimalNumber(event, value) {
        return Validator.allowDecimalNumber(event, value);
    }

    showDiscount(item: OfferPosition) {
        this.eventShowDiscount.emit(item);
    }

    removeDiscount(item: OfferPositionT) {
        item.actionType = DiscountType[DiscountType.NONE];
        item.actionValue = null;
        item.remark = null;
        delete item.discountDescription;
        delete item.discountValue;
        this.eventCalculatePrice.emit({ item });
    }

    onValueChaged(val, attr, article) {
        article[attr] = val;
        this.valuesChangedMap[article.id] = true;
    }

    generateHaynesProLink(vehicleId, index) {
        this.haynesLinkHandleService.loginHaynesPro(this.userService.userDetail, vehicleId, index);
    }

    openModalToRetrieveDataRegenerateLink(index) {
        const vehicleId = this.vehicles[index].connectVehicleId;
        this.bsModalService.show(SagHaynesProReturnModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                index,
                vehicleId,
                callback: (selectedVehicleId) => {
                    this.emitUpdateLabourTime.emit(selectedVehicleId);
                }
            }
        });

        this.haynesLinkHandleService.loginHaynesPro(this.userService.userDetail, vehicleId, index);
    }

    private keepOrginalGrossPrice() {
        this.dataItems.forEach(item => {
            if (!item.originalGrossPrice) {
                item.originalGrossPrice = item.grossPrice;
            }
        });
    }

    private initDiscount(items: Array<OfferPosition>) {
        items.map(item => item as OfferPositionT).forEach(item => {
            if (item.actionType && item.actionType !== DiscountType[DiscountType.NONE]) {
                item.discountDescription = this.offerDiscountService.buildDiscountDescriptionAsString(item);
                item.discountValue = this.offerDiscountService.buildDiscountAmountWithSignAsString(item);
            }
        });
        this.dataItems = items;
    }

    private buildAndShowLabourTimeAndHaynesProLink() {
        // have vehicles, have license
        if (!this.initHaynesProLinks && this.vehicles.length > 0 && this.isShowLabourTime && this.user && this.user.id) {
            this.initHaynesProLinks = true;
            const groupVehiclesCategories = this.vehicles.map(v => {
                return { vehicleId: v.connectVehicleId };
            });
            this.haynesLinkHandleService.getLinkHaynesPro(this.userService.userDetail, groupVehiclesCategories);
        }
    }

    private getMaxValidValue(quantity: number) {
        return quantity < this.maxQuantity ? quantity : this.maxQuantity;
    }

}
