import { Component, OnInit, OnChanges } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, } from '@angular/forms';
import { DiscountItemType, DiscountType } from '../../enums/discount.enums';
import { OfferPosition } from '../../models/offer-position.model';
import { OfferDetail } from '../../models/offer-detail.model';
import { OfferDiscountService } from '../../services/offer-discount.service';
import { Validator } from 'src/app/core/utils/validator';
import { OFFER_ITEM_TYPE } from '../../enums/offers.enum';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-offer-discount-modal',
    templateUrl: './offer-discount-modal.component.html',
    styleUrls: ['./offer-discount-modal.component.scss']
})

export class OfferDiscountModalComponent implements OnChanges, OnInit {
    type: DiscountItemType;
    discountItem: OfferPosition | OfferDetail;
    totalAmount: number;
    totalOwnArticle: number;
    totalOwnWork: number;
    takeDiscount: any;

    discountTypeModel: DiscountType;
    reasonModel: string;
    discountTypes: Array<any>;
    discountForm: FormGroup;
    attemptedSubmitingForm: boolean;
    isDiscountByPercent: boolean;
    percentSign = '%';
    currencySign = 'CHF';

    constructor(
        public modalRef: BsModalRef,
        private offerDiscountService: OfferDiscountService,
        private formBuilder: FormBuilder) {
    }

    ngOnChanges() {
        if (!this.isFirstTimeVisit()) {
            this.getDiscountTypes(this.type, this.totalAmount - this.totalOwnWork, this.totalOwnArticle, this.totalOwnWork);
            this.resetModel();
        }
    }

    ngOnInit() {
        this.getDiscountTypes(this.type, this.totalAmount - this.totalOwnWork, this.totalOwnArticle, this.totalOwnWork);
        this.initDefaultDiscountType();
        this.isDiscountByPercent = this.isTypeDiscountByPercent();
    }

    submit() {
        this.attemptedSubmitingForm = true;
        if (this.discountForm.valid) {
            this.takeDiscount(this.repairDiscount());
            this.resetModel();
            this.modalRef.hide();
        }
    }

    selectDiscountType() {
        this.isDiscountByPercent = this.isTypeDiscountByPercent();
        this.discountForm = this.buildDiscountForm(this.discountTypeModel);
        this.attemptedSubmitingForm = false;
    }

    private isTypeDiscountByPercent() {
        return this.offerDiscountService.isDiscountOrSurChargeByPercent(this.discountTypeModel);
    }

    private isDiscountForOffer() {
        return this.type === DiscountItemType.OFFER;
    }

    private getDiscountTypes(type: DiscountItemType, totalArticle: number, totalOwnArticle: number, totalOwnWork: number) {
        this.discountTypes = this.offerDiscountService.getDiscountTypes(type, totalArticle, totalOwnArticle, totalOwnWork);
    }

    private resetModel() {
        this.initDefaultDiscountType();
        this.isDiscountByPercent = this.isTypeDiscountByPercent();
        this.attemptedSubmitingForm = false;
        this.reasonModel = null;
    }

    private isFirstTimeVisit() {
        if (!this.discountForm) {
            return true;
        }
        return false;
    }

    private initDefaultDiscountType() {
        this.discountTypeModel = this.discountTypes[0].value;
        this.discountForm = this.buildDiscountForm(this.discountTypeModel);
    }

    private buildDiscountForm(value: DiscountType) {
        if (this.offerDiscountService.isDiscountOrSurChargeByPercent(value)) {
            return this.formBuilder.group({
                percent: new FormControl(null, Validator.validatePercent)
            });
        } else {
            return this.formBuilder.group({
                amount: new FormControl(null, Validator.validateAmountForDiscount)
            });
        }
    }

    private repairDiscount(): OfferPosition {
        const discountValue = this.repairDiscountValue();
        if (this.isDiscountForOffer()) {
            return new OfferPosition({
                type: OFFER_ITEM_TYPE.REMARK.toString(),
                totalGrossPrice: this.repairTotalGrossPrice(),
                remark: this.reasonModel,
                quantity: 1,
                actionType: DiscountType[this.discountTypeModel],
                actionValue: discountValue
            });
        } else {
            const discountItem = this.discountItem as OfferPosition;
            discountItem.actionType = DiscountType[this.discountTypeModel];
            discountItem.actionValue = discountValue;
            discountItem.remark = this.reasonModel;
            return discountItem;
        }
    }

    private repairDiscountValue() {
        if (this.isTypeDiscountByPercent()) {
            return this.discountForm.controls.percent.value / 100;
        }
        return this.discountForm.controls.amount.value;
    }

    private repairTotalGrossPrice() {
        if (this.offerDiscountService.isDiscountOrSurChargeForOwnAllArticleByPercent(this.discountTypeModel)) {
            return this.totalOwnArticle;
        } else if (this.offerDiscountService.isDiscountOrSurChargeForOwnAllWorkByPercent(this.discountTypeModel)) {
            return this.totalOwnWork;
        }
        return this.totalAmount;
    }
}
