import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { IAngularMyDpOptions, IMyDateModel } from 'angular-mydatepicker';
import { cloneDeep, isEmpty, isEqual } from 'lodash';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';
import { finalize } from 'rxjs/operators';
import * as moment from 'moment';
import { OfferAnalyticService } from 'src/app/analytic-logging/services/offer-analytic.service';
import { SagConfirmationBoxComponent } from 'sag-common';
import { MESSAGE_SUCCESS, SHOPPING_BASKET_PAGE } from 'src/app/core/conts/app.constant';
import { ArticleType } from 'src/app/core/enums/article.enum';
import { HAYNESPRO_LICENSE } from 'src/app/core/enums/haynes.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { UserService } from 'src/app/core/services/user.service';
import { DateUtil } from 'src/app/core/utils/date.util';
import { permissions } from 'src/app/core/utils/permission';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { SubSink } from 'subsink';
import { LabourTime } from '../../../dms/models/labour-time.model';
import { OfferArticleFormModalComponent } from '../../components/offer-article-form-modal/offer-article-form-modal.component';
import { OfferArticleModalComponent } from '../../components/offer-article-modal/offer-article-modal.component';
import { OfferDiscountModalComponent } from '../../components/offer-discount-modal/offer-discount-modal.component';
import { OfferPersonFormModalComponent } from '../../components/offer-person-form-modal/offer-person-form-modal.component';
import { OfferPersonModalComponent } from '../../components/offer-person-modal/offer-person-modal.component';
import { DiscountItemType } from '../../enums/discount.enums';
import { OFFER_ITEM_TYPE, OFFER_STATUS } from '../../enums/offers.enum';
import { OfferArticleModel } from '../../models/offer-article.model';
import { OfferUpdateRequest } from '../../models/offer-detail-request.model';
import { OfferDetail } from '../../models/offer-detail.model';
import { OfferPosition } from '../../models/offer-position.model';
import { OffersService } from '../../services/offers.services';
import { HaynesProService, LabourTimeService, HaynesProLicenseSettings } from 'sag-haynespro';
import { ArticleModel, ArticlesService } from 'sag-article-detail';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-offer-detail',
    templateUrl: 'offer-detail.component.html',
    styleUrls: ['../../offers.component.scss', 'offer-detail.component.scss']
})
export class OfferDetailComponent implements OnInit, OnDestroy {
    ITEM_TYPE = OFFER_ITEM_TYPE;
    redirectPage = new Subject();
    modalRef: BsModalRef;

    offer: OfferDetail = new OfferDetail();
    originalOffer: OfferDetail;
    discountType: DiscountItemType;
    discountItem: OfferDetail | OfferPosition;

    creatingDate: any;
    deliveryDate: any;
    dateFromSetting: IAngularMyDpOptions = {};
    dateToSetting: IAngularMyDpOptions = {};
    dateFromDisableToday = false;
    dateToDisableToday = false;

    // display vat
    vatValue: number;

    locale: string;
    isShowLabourTime: boolean;
    isNuDdatAuthed: boolean;

    spinnerSelector = '.offer-detail';

    private subs = new SubSink();
    notifier = null;
    affiliateCode = environment.affiliate;

    private addFromBasket: boolean;

    constructor(
        private location: Location,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private translateService: TranslateService,
        private modalService: BsModalService,
        private appStorageService: AppStorageService,
        private offerAnalyticService: OfferAnalyticService,
        private haynesProService: HaynesProService,
        private labourTimeService: LabourTimeService,
        private articleService: ArticlesService,
        private offersService: OffersService,
        public userService: UserService
    ) {
        this.subs.sink = this.activatedRoute.data.subscribe(({ title }) => {
            this.offersService.title.next(title);
        });
        this.subs.sink = this.userService.userDetail$.subscribe((user: UserDetail) => {
            this.isNuDdatAuthed = user.normalUser;

            if (!user || !user.permissions) {
                return;
            }

            const hasPermission = user.permissions.find(item => permissions.haynespro.name === item.permission);
            if (hasPermission) {
                this.checkHaynesproLicense();
            }
        });
    }

    ngOnInit() {
        this.subs.sink = this.activatedRoute.params.subscribe(params => {
            if (!params) {
                return;
            }
            this.addFromBasket = this.activatedRoute.snapshot.queryParams.addFromBasket === 'true';
            if (params.id) {
                this.getOffer(params.id);
            }
        });

        this.locale = this.translateService.currentLang;
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    onDateFromChanged(event: IMyDateModel) {
        if (!event || !event.singleDate.date) {
            return;
        }
        const jsFromDate = this.getDateTime(event.singleDate.date);
        let isDateValid = true;
        if (this.dateFromSetting.disableSince) {
            const jsMaxFromDate = this.getDateTime(this.dateFromSetting.disableSince);
            isDateValid = DateUtil.isDateInRange(jsFromDate, jsMaxFromDate, true);
        }
        this.dateToDisableToday = DateUtil.isDateInRange(new Date(), jsFromDate, true);

        if (isDateValid) {
            this.dateToSetting = { ...this.dateToSetting, disableUntil: event.singleDate.date };
            this.offer.offerDate = jsFromDate;
        } else {
            const date = moment(this.offer.offerDate).toDate();
            setTimeout(() => {
                this.creatingDate = DateUtil.buildDataDatePicker(date);
            });
        }
    }

    onDateToChanged(event: IMyDateModel) {
        if (!event || !event.singleDate.date) {
            return;
        }
        const jsToDate = this.getDateTime(event.singleDate.date);
        let isDateValid = true;
        if (this.dateToSetting.disableUntil) {
            const jsMinToDate = this.getDateTime(this.dateToSetting.disableUntil);
            isDateValid = DateUtil.isDateInRange(jsToDate, jsMinToDate, false);
        }
        this.dateFromDisableToday = DateUtil.isDateInRange(new Date(), jsToDate, false);

        if (isDateValid) {
            this.dateFromSetting = { ...this.dateFromSetting, disableSince: event.singleDate.date };
            this.offer.deliveryDate = this.getDateTime(event.singleDate.date);
        } else {
            const date = moment(this.offer.deliveryDate).toDate();
            setTimeout(() => {
                this.deliveryDate = DateUtil.buildDataDatePicker(date);
            });
        }
    }

    openDiscountModalForOffer() {
        this.openDiscountModal(DiscountItemType.OFFER, this.offer);
    }

    openDiscountModalForItem(item: OfferPosition) {
        this.openDiscountModal(DiscountItemType.ITEM, item);
    }

    takeDiscount(discount: OfferPosition) {
        if (discount.type === OFFER_ITEM_TYPE[OFFER_ITEM_TYPE.REMARK]) {
            this.addDiscountToOffer(discount);
        } else {
            this.updatePrice({ item: discount, relatedDom: null });
        }
    }

    bindingSelectOwnArticlesOrWork(articles) {
        for (const article of articles) {
            this.updateToListOfferPositions(OfferPosition.convertFromOfferOwnArticleOrWork(article));
        }
        this.updatePrice({ item: null, relatedDom: null });
    }

    updateOfferPerson(offerPerson) {
        SpinnerService.start(this.spinnerSelector)
        this.offersService.getEndCustomerPerson(offerPerson.id)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe(res => {
                this.offer.offerPerson = res;
            });

    }

    deleteItem(item?: OfferPosition) {
        const offerPositions = Object.assign([], this.offer.offerPositions);
        offerPositions.splice(item.index, 1);
        this.offer.offerPositions = offerPositions || [];
        this.removeEmptyRemarkItems(this.offer.offerPositions);
        this.updatePrice({ item: null, relatedDom: null });
    }

    updatePrice({ item, relatedDom }) {
        // binding item updated to offer position list
        const offerPositions = this.offer.offerPositions;
        if (item) {
            for (let i = 0; i < offerPositions.length; i++) {
                if (offerPositions[i].index === item.index) {
                    offerPositions[i] = item;
                    break;
                }
            }
        }
        this.offer.offerPositions = Object.assign([], offerPositions);

        // service to update price and update again in UI.
        const offerRequestUpdate = OfferUpdateRequest.convertToRequestUpdateOffer(this.offer);

        SpinnerService.start(this.spinnerSelector);
        this.offersService.updateOffer(offerRequestUpdate, true)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe(res => {
                // keep the offer-Person;
                const offerPerson = this.offer.offerPerson;
                this.offer = res.offer;
                this.offer.offerPerson = offerPerson;
                this.calculateAllPrice();
                this.addUniqueIndexForOfferPosition();
                if (relatedDom) {
                    try {
                        // excute next action if any
                        relatedDom.click();
                    } catch (e) { }
                }
            });
    }

    changeStatus() {
        this.offer.status = OFFER_STATUS.MANUALLY_ORDERED.toString();
        this.updateOffer();
    }

    updateOffer(calculated?: boolean, isRedirectPage?: boolean) {
        const offerRequestUpdate = OfferUpdateRequest.convertToRequestUpdateOffer(this.offer);

        SpinnerService.start(this.spinnerSelector);
        this.offersService.updateOffer(offerRequestUpdate, !!calculated)
            .pipe(finalize(() => {
                SpinnerService.stop(this.spinnerSelector);
                window.scrollTo(0, 0);
            }))
            .subscribe(res => {
                this.notifier = { messages: [MESSAGE_SUCCESS], status: 'SUCCESS' };

                this.addFromBasket = false;
                // if user click outside and show popup, not click update button
                if (isRedirectPage) {
                    this.redirectPage.next(true);
                }
                this.appStorageService.selectedOffer = res.offer;
                this.originalOffer = this.offer;
            }, err => {
                this.notifier = { messages: [err.message], status: 'ERROR' };

                if (isRedirectPage) {
                    this.redirectPage.next(true);
                }
            });
    }

    orderOffer() {
        // Get offer positions to add to current shopping basket
        if (isEmpty(this.offer.offerPositions)) {
            return;
        }
        const offerPosItems = this.offer.offerPositions.filter(offerPos => this.isVendorArticle(offerPos));
        if (isEmpty(offerPosItems)) {
            return;
        }

        SpinnerService.start(this.spinnerSelector)

        this.offersService.orderOffer(offerPosItems)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe(response => {
                this.offerAnalyticService.sendOfferEventData(offerPosItems);
                this.appStorageService.selectedOffer = this.offer;
                this.goToBasket();
            });
    }

    async refreshPrice() {
        const selectedOfferPositions = this.offer.offerPositions;

        if (isEmpty(selectedOfferPositions)) {
            return;
        }

        SpinnerService.start(this.spinnerSelector);

        await Promise.all(selectedOfferPositions.map(async (offerPos) => {
            if (!this.isVendorArticle(offerPos)) {
                return;
            }
            const updatedArticleDoc: any = await this.articleService.getArticleByUpdatedAmount({
                amount: offerPos.quantity,
                pimId: offerPos.pimId
            }).toPromise().catch(error => console.log(error));

            if (!updatedArticleDoc || updatedArticleDoc.notFoundInAx) {
                return;
            }

            const articleDoc = new ArticleModel(updatedArticleDoc);
            selectedOfferPositions[offerPos.index].grossPrice = articleDoc.getOfferGrossPrice();
            selectedOfferPositions[offerPos.index].totalGrossPrice = articleDoc.getOfferTotalGrossPrice();
        }));

        this.updatePrice({ item: null, relatedDom: null });
    }

    updateLabourTime(vehicleId: string) {
        this.labourTimeService.getLabourTime(vehicleId).subscribe(res => {
            if (isEmpty(res)) {
                return;
            }
            const labourTimes = Object.assign(new Array<LabourTime>(), res);

            const offerPositions = Object.assign([], this.offer.offerPositions);
            labourTimes.forEach(item => {
                offerPositions.push(OfferPosition.convertFromLabourTime(item, vehicleId));
            });
            this.offer.offerPositions = offerPositions;
            this.addUniqueIndexForOfferPosition();
            this.updatePrice({ item: null, relatedDom: null });
        }, err => {
            console.log('error when getting labour time ' + err);
        });
    }

    openPersonModal() {
        this.modalRef = this.modalService.show(OfferPersonModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                selectPerson: (data) => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        this.updateOfferPerson(data);
                        sub.unsubscribe();
                    });
                },
                createPerson: () => {
                    this.openCreatePersonModal();
                }
            },
            class: 'modal-lg'
        });
    }

    openCreatePersonModal() {
        this.modalRef = this.modalService.show(OfferPersonFormModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                canBack: true,
                callback: () => {
                    this.openPersonModal();
                }
            }
        });
    }

    openArticleModal() {
        this.modalRef = this.modalService.show(OfferArticleModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                type: ArticleType.ARTICLE,
                createArticle: () => {
                    this.openCreateArticleModal(ArticleType.ARTICLE);
                },
                selectArticles: (articles: OfferArticleModel[]) => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        this.bindingSelectOwnArticlesOrWork(articles);
                        sub.unsubscribe();
                    });
                }
            },
            class: 'modal-lg'
        });
    }

    openWorkModal() {
        this.modalRef = this.modalService.show(OfferArticleModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                type: ArticleType.WORK,
                createArticle: () => {
                    this.openCreateArticleModal(ArticleType.WORK);
                },
                selectArticles: (articles: OfferArticleModel[]) => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        this.bindingSelectOwnArticlesOrWork(articles);
                        sub.unsubscribe();
                    });
                }
            },
            class: 'modal-lg'
        });
    }

    openCreateArticleModal(type: ArticleType) {
        this.modalRef = this.modalService.show(OfferArticleFormModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                type,
                canBack: true,
                callback: () => {
                    if (type === ArticleType.ARTICLE) {
                        this.openArticleModal();
                    } else {
                        this.openWorkModal();
                    }
                }
            }
        });
    }

    exportPdfCurrentOffer() {
        this.offersService.exportCurrentOffer(this.offer, 'pdf');
    }

    exportWordCurrentOffer() {
        this.offersService.exportCurrentOffer(this.offer, 'word');
    }


    exportRtfCurrentOffer() {
        this.offersService.exportCurrentOffer(this.offer, 'rtf');
    }

    backToPreviousPage() {
        this.location.back();
    }

    canDeactivate() {
        if (!this.isNuDdatAuthed || !this.originalOffer) {
            return true;
        }

        if (this.isUserChangeDatePicker(this.originalOffer, this.offer) ||
            this.compareOfferDetailChanges() || this.addFromBasket
        ) {
            if (this.modalRef) {
                this.modalRef.hide();
            }
            SpinnerService.stop();
            this.modalService.show(SagConfirmationBoxComponent, {
                ignoreBackdropClick: true,
                initialState: {
                    title: '',
                    message: 'OFFERS.OFFER_DETAIL.CONFIRM_SAVE_CHANGES',
                    okButton: 'OFFERS.OFFER_CUSTOMER.YES',
                    cancelButton: 'OFFERS.OFFER_CUSTOMER.NO',
                    bodyIcon: 'fa-exclamation-triangle',
                    cancel: () => {
                        const sub = this.modalService.onHidden.subscribe(() => {
                            this.redirectPage.next(true);
                            sub.unsubscribe();
                        });
                    },
                    close: () => {
                        const sub = this.modalService.onHidden.subscribe(() => {
                            this.updateOffer(false, true);
                            sub.unsubscribe();
                        });
                    }
                }
            });
            return this.redirectPage;
        } else {
            return true;
        }
    }

    private getDateTime(date) {
        return Date.UTC(date.year, date.month - 1, date.day);
    }

    private getOffer(id) {
        SpinnerService.start(this.spinnerSelector);
        this.offersService.getOfferById(id)
            .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
            .subscribe(res => {
                if (!res) {
                    this.router.navigate(['/404']);
                    return;
                }

                if (res.offerPositions.length > 0) {
                    this.offer = res;
                } else {
                    if (this.addFromBasket) {
                        this.offer = this.appStorageService.selectedOffer || res;
                    } else {
                        this.offer = res;
                    }
                }
                this.appStorageService.selectedOffer = this.offer;

                this.prepareDataForOfferPositions();
                this.originalOffer = cloneDeep(res);
                this.setVatValue();
                this.buildDatePickerValues();
            });
    }

    private prepareDataForOfferPositions() {
        this.cloneGrossPrice();
        this.addUniqueIndexForOfferPosition();
        this.calculateAllPrice();
    }

    private cloneGrossPrice() {
        this.offer.offerPositions.forEach(item => {
            if (!item.originalGrossPrice) {
                item.originalGrossPrice = item.grossPrice;
            }
        });
    }

    private addUniqueIndexForOfferPosition() {
        // add index for each item, support to remove and update item since doent have unique id
        if (isEmpty(this.offer.offerPositions)) {
            return;
        }
        this.offer.offerPositions.forEach((item, i) => {
            item.index = i;
        });
    }

    private calculateAllPrice() {
        this.offer.totalGrossPrice = this.offer.totalIncludeVat;
    }

    private setVatValue() {
        this.vatValue = this.originalOffer.vat * 100;
    }

    private buildDatePickerValues() {
        this.creatingDate = DateUtil.buildDataDatePicker(new Date(this.offer.formattedOfferDate));
        this.deliveryDate = (this.offer.formattedDeliveryDate || '').trim() ?
            DateUtil.buildDataDatePicker(new Date(this.offer.formattedDeliveryDate)) : null;
        if (!this.creatingDate) {
            return;
        }
        // validate range date from can not be over date to
        const dateFromSetting = Object.assign({}, this.dateFromSetting, DateUtil.getCommonSetting());
        if (this.deliveryDate) {
            dateFromSetting.disableSince = this.deliveryDate.singleDate.date;
            this.dateFromDisableToday = DateUtil.isDateInRange(new Date(), this.getDateTime(this.deliveryDate.singleDate.date), false);
        }

        this.dateFromSetting = dateFromSetting;

        // validate range date to can not be before date from
        const dateToSetting = Object.assign({}, this.dateToSetting, DateUtil.getCommonSetting());
        dateToSetting.disableUntil = this.creatingDate.singleDate.date;

        this.dateToSetting = dateToSetting;

        this.dateToDisableToday = DateUtil.isDateInRange(new Date(), this.getDateTime(this.creatingDate.singleDate.date), true);
    }

    private openDiscountModal(discountType: DiscountItemType, discountItem: OfferDetail | OfferPosition) {
        this.discountType = discountType;
        this.discountItem = discountItem;

        this.modalRef = this.modalService.show(OfferDiscountModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                type: discountType,
                discountItem: this.discountItem,
                totalOwnArticle: this.offer.totalOwnArticle,
                totalOwnWork: this.offer.totalWork,
                totalAmount: this.offer.totalArticle + this.offer.totalWork,
                takeDiscount: (data) => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        this.takeDiscount(data);
                        sub.unsubscribe();
                    });
                }
            }
        });
    }

    private removeEmptyRemarkItems(items: Array<OfferPosition>) {
        this.offer.offerPositions = items.filter(item => !(item.type === OFFER_ITEM_TYPE.REMARK.toString() && item.totalGrossPrice === 0));
    }

    private addDiscountToOffer(discount: OfferPosition) {
        this.offer.offerPositions.push(discount);
        this.updatePrice({ item: null, relatedDom: null });
    }

    // handle append selected shopArticle from modal own/client work articles.
    private updateToListOfferPositions(shopArticle: OfferPosition) {
        const offerPositions = Object.assign([], this.offer.offerPositions);
        // add directly to list
        offerPositions.push(shopArticle);
        this.offer.offerPositions = offerPositions;
        this.addUniqueIndexForOfferPosition();
    }

    private goToBasket() {
        this.router.navigate([`/${SHOPPING_BASKET_PAGE}`]);
    }

    private isVendorArticle(offerPos: OfferPosition) {
        return (
            offerPos.type === OFFER_ITEM_TYPE.VENDOR_ARTICLE.toString() ||
            offerPos.type === OFFER_ITEM_TYPE.VENDOR_ARTICLE_WITHOUT_VEHICLE.toString()
        ) &&
            offerPos.pimId != null;
    }

    private compareOfferDetailChanges() {
        const originalData = Object.assign({}, this.originalOffer);
        const currentData = Object.assign({}, this.offer);

        originalData.offerDate = originalData.deliveryDate = null;
        currentData.offerDate = currentData.deliveryDate = null;

        return !isEqual(originalData, currentData);
    }

    private isUserChangeDatePicker(originalDate, currentDate) {
        const originalCreatingDate = DateUtil.formatDateInDate(originalDate.offerDate);
        const currentCreatingDate = DateUtil.formatDateInDate(currentDate.offerDate);
        const originalDeliveryDate = DateUtil.formatDateInDate(originalDate.deliveryDate);
        const currentDeliveryDate = DateUtil.formatDateInDate(currentDate.deliveryDate);
        if (originalCreatingDate !== currentCreatingDate) {
            return true;
        }
        if (originalDeliveryDate !== currentDeliveryDate) {
            return true;
        }
        return false;
    }

    private checkHaynesproLicense() {
        this.haynesProService.getHaynesProLicense().subscribe(
            (res: HaynesProLicenseSettings) => {
                const licenseType = res && res.licenseType;
                this.isShowLabourTime = (licenseType === HAYNESPRO_LICENSE.ULTIMATE.toString());
            }, (err) => {
                this.isShowLabourTime = false;
            }
        );
    }
}
