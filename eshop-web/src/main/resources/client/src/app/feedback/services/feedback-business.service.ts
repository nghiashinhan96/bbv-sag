import { Injectable } from '@angular/core';
import { FeedbackService } from './feedback.service';
import { Observable } from 'rxjs';
import { NgOption } from '@ng-select/ng-select';
import { FeedbackData } from '../models/feedback-data.model';
import { FeedbackDataItemKey, FeedbackUserType } from '../enums/feedback.enum';
import { FeedbackDataItem } from '../models/feedback-data-item.model';
import { FeedbackUserData } from '../models/feedback-user-data.model';
import { TranslateService } from '@ngx-translate/core';
import { FeedbackSalesUserData } from '../models/feedback-sales-user-data.model';
import { PaymentSetting } from 'src/app/core/models/payment-settings.model';
import { UserSetting } from 'src/app/core/models/user-setting.model';
import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { FeedbackVehicleSearch } from '../models/feedback-vehicle-search.model';
import { FeedbackTechicalData } from '../models/feedback-techical-data.model';
import { FeedbackUserSetting } from '../models/feedback-user-setting.model';
import { FeedbackArticleResult } from '../models/feedback-article-result.model';
import { FeedbackShoppingCartItem } from '../models/feedback-shopping-cart-model';
import { FeedbackArticleSearch } from '../models/feedback-article-search.model';
import { FeedbackRecordingService } from './feedback-recording.service';
import { UserService } from 'src/app/core/services/user.service';
import { flatMap, map } from 'rxjs/operators';
import { FeedbackCustomerContact } from '../models/feedback-customer-contact.model';
import { FeedbackSource } from '../models/feedback-source.model';
import { FeedbackMessageContent } from '../models/feedback-message-content.model';
import { FeedbackSavingRequest } from '../models/feedback-saving-request.model';

@Injectable()
export abstract class FeedbackBusinessService {
    constructor(
        protected translateService: TranslateService,
        protected userService: UserService,
        protected feedbackService: FeedbackService,
        protected fbRecordingService: FeedbackRecordingService
    ) { }
    abstract getFormTitle(): string;
    abstract getGroup(): string;
    abstract getMasterData(): Observable<any>;
    abstract getTechnicalData(): FeedbackData;
    abstract getUserData(data: any): FeedbackData;
    abstract getSavingRequestModel(model: FeedbackSavingRequest): FeedbackSavingRequest;
    abstract createFeedback(formData: FormData, rawModel?: any);

    getTopicsByCode(topicCodes: string[]): NgOption[] {
        return topicCodes.map(code => ({
            value: code,
            label: `FEEDBACK.TOPICS.${code}`
        } as NgOption));
    }

    getCustomerContact(contact: string): FeedbackCustomerContact {
        return contact ? new FeedbackCustomerContact({
            title: this.translateService.instant('FEEDBACK.CUSTOMER_CONTACT'),
            contact
        }) : null;
    }

    getFeedbackMessage(message: string): FeedbackMessageContent {
        return new FeedbackMessageContent({
            title:
                this.translateService.instant('FEEDBACK.YOUR_FEEDBACK'),
            content: message
        });
    }

    getSource(source: string): FeedbackSource {
        const title = this.translateService.instant('FEEDBACK.SOURCE.TITLE');
        switch (source) {
            case 'SALES_ON_BEHALF':
                return new FeedbackSource({
                    title,
                    source: this.translateService.instant('FEEDBACK.SOURCE.SALES_ON_BEHALF'),
                    code: FeedbackUserData[FeedbackUserType.SALES_ON_BEHALF]
                });
            case 'SALES_NOT_ON_BEHALF':
                return new FeedbackSource({
                    title,
                    source: this.translateService.instant('FEEDBACK.SOURCE.SALES_NOT_ON_BEHALF'),
                    code: FeedbackUserType[FeedbackUserType.SALES_NOT_ON_BEHALF]
                });
            default:
                return null;
        }
    }

    protected getNormalUserData(data: any): FeedbackData {
        const dataItems: FeedbackDataItem[] = [];
        const userData = new FeedbackUserData({
            salesId: data.salesId,
            salesEmail: data.salesEmail,
            customerNr: data.customerNr,
            customerName: data.customerName,
            customerTown: data.customerTown,
            customerEmails: data.customerEmails,
            customerPhones: data.customerPhones,
            defaultBranch: data.defaultBranch
        });

        if (userData.hasSalesInfo()) {
            const salesInfoItem = new FeedbackDataItem({
                key: FeedbackDataItemKey.SALES_INFO.toString(),
                title: this.translateService.instant('FEEDBACK.USER_DATA.SALES'),
                value: userData.salesInfoValue
            });
            dataItems.push(salesInfoItem);
        }

        const customerInfoItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CUSTOMER_INFO.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.CUSTOMER'),
            value: userData.customerInfoValue
        });
        dataItems.push(customerInfoItem);

        const customerEmailItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CUSTOMER_EMAIL.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.EMAIL'),
            value: userData.customerEmailsValue
        });
        dataItems.push(customerEmailItem);

        const customerPhoneItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CUSTOMER_PHONE.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.PHONE'),
            value: userData.customerPhonesValue
        });
        dataItems.push(customerPhoneItem);

        const defaultBranchItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.DEFAULT_BRANCH.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.BRANCH'),
            value: userData.defaultBranchValue
        });
        dataItems.push(defaultBranchItem);

        return new FeedbackData({
            title: this.translateService.instant('FEEDBACK.YOUR_DATA'),
            items: dataItems
        });
    }

    protected getSalesUserData(data: any): FeedbackData {
        const dataItems: FeedbackDataItem[] = [];
        const userDataModel = new FeedbackSalesUserData({
            userId: data.userId,
            userEmail: data.userEmail,
            userPhone: data.userPhone
        });

        const salesInfoItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.SALES_INFO.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.SALES'),
            value: userDataModel.userInfoValue
        });
        dataItems.push(salesInfoItem);

        const userPhoneItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.USER_PHONE.toString(),
            title: this.translateService.instant('FEEDBACK.USER_DATA.PHONE'),
            value: userDataModel.userPhoneValue
        });
        dataItems.push(userPhoneItem);

        return new FeedbackData({
            title: this.translateService.instant('FEEDBACK.YOUR_DATA'),
            items: dataItems
        });
    }

    protected getNormalUserTechnicalData(model: FeedbackTechicalData): FeedbackData {
        const { userSetting, userPaymentSetting } = this.userService;
        const dataItems: FeedbackDataItem[] = [];
        dataItems.push(this.getWebsiteItem(model.websiteValue));
        dataItems.push(this.getFreetextSearchItem(model.freeTextSearchKeyValue));
        dataItems.push(this.getVehicleSearchItem(model.vehicleSearch));
        dataItems.push(this.getArticleSearchItem(model.articleSearch));
        dataItems.push(this.getOfferNrItem(model.offerNrValue));
        dataItems.push(this.getVehicleItem(model.vehicleValue));
        dataItems.push(this.getUserSettingItem(new FeedbackUserSetting().fromSettings(userSetting, userPaymentSetting)));
        dataItems.push(this.getArticleResultItem(model.articleResults));
        dataItems.push(this.getShoppingCartItems(model.cartItems));

        return new FeedbackData({
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA_TITLE'),
            items: dataItems
        });
    }

    protected getSalesUserTechnicalData(model: FeedbackTechicalData): FeedbackData {
        const dataItems: FeedbackDataItem[] = [];
        dataItems.push(this.getWebsiteItem(model.websiteValue));
        return new FeedbackData({
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA_TITLE'),
            items: dataItems
        });
    }

    private getWebsiteItem(website: string): FeedbackDataItem {
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.WEBSITE.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.WEBSITE'),
            value: website,
            isShortTechnicalData: true
        });
    }

    private getFreetextSearchItem(freeTextSearchKey: string): FeedbackDataItem {
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.FREE_TEXT_SEARCH_KEY.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.FREE_TEXT_SEARCH'),
            value: freeTextSearchKey
        });
    }

    private getVehicleSearchItem(vehicleSearch: FeedbackVehicleSearch): FeedbackDataItem {
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.VEHICLE_SEARCH.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.VEHICLE_SEARCH'),
            value: vehicleSearch ? vehicleSearch.content : NOT_AVAILABLE,
            isShortTechnicalData: true
        });
    }

    private getArticleSearchItem(articleSearch: FeedbackArticleSearch): FeedbackDataItem {
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.ARTICLE_SEARCH.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.ARTICLE_SEARCH'),
            value: articleSearch ? articleSearch.content : NOT_AVAILABLE,
            isShortTechnicalData: true
        });
    }

    private getOfferNrItem(offerNr: string): FeedbackDataItem {
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.OFFER_NR.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.OFFER_NUMBER'),
            value: offerNr
        });
    }

    private getVehicleItem(vehicle: string): FeedbackDataItem {
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.VEHICLE.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.VEHICLE'),
            value: vehicle,
            isShortTechnicalData: true
        });
    }

    private getUserSettingItem(userSetting: FeedbackUserSetting): FeedbackDataItem {
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.USER_SETTINGS.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.USER_SETTING'),
            value: !userSetting ? NOT_AVAILABLE : undefined,
            childs: userSetting ? this.buildUserSettings(userSetting) : undefined
        });
    }

    private getArticleResultItem(articleResults: FeedbackArticleResult[]): FeedbackDataItem {
        const hasResults = articleResults && articleResults.length;
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.ARTICLE_RESULTS.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.ARTICLE_RESULTS'),
            value: !hasResults ? NOT_AVAILABLE : undefined,
            childs: hasResults ? this.buildArticleResults(articleResults) : undefined
        });
    }

    private getShoppingCartItems(cartItems: FeedbackShoppingCartItem[]): FeedbackDataItem {
        const hasResults = cartItems && cartItems.length;
        return new FeedbackDataItem({
            key: FeedbackDataItemKey.SHOPPING_CART.toString(),
            title: this.translateService.instant('FEEDBACK.TECHNICAL_DATA.SHOPPING_CART'),
            value: !hasResults ? NOT_AVAILABLE : undefined,
            childs: hasResults ? this.buildShoppingCartItems(cartItems) : undefined
        });
    }

    private buildUserSettings(userSettings: FeedbackUserSetting): FeedbackDataItem[] {
        const dataItems: FeedbackDataItem[] = [];

        const allowViewBillingChangedItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.ALLOW_VIEW_BILLING_CHANGED.toString(),
            title: this.translateService.instant('SETTINGS.SETTING.BILLS'),
            value: this.getBooleanText(userSettings.viewBilling)
        });
        dataItems.push(allowViewBillingChangedItem);

        const allowNetPriceChangedItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.ALLOW_NET_PRICE_CHANGED.toString(),
            title: this.translateService.instant('SETTINGS.SETTING.NET_PRICE'),
            value: this.getBooleanText(userSettings.netPriceView)
        });
        dataItems.push(allowNetPriceChangedItem);

        const netPriceConfirmItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.NET_PRICE_CONFIRM.toString(),
            title: this.translateService.instant('SETTINGS.SETTING.NET_PRICE_EMAIL'),
            value: this.getBooleanText(userSettings.netPriceConfirm)
        });
        dataItems.push(netPriceConfirmItem);


        const categoryViewModeItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.CATEGORY_VIEW_MODE.toString(),
            title: this.translateService.instant('SETTINGS.SETTING.CATEGORY_VIEW_MODE.TEXT'),
            value: userSettings.classicCategoryView ?
                this.translateService.instant('SETTINGS.SETTING.CATEGORY_VIEW_MODE.CLASSIC') :
                this.translateService.instant('SETTINGS.SETTING.CATEGORY_VIEW_MODE.TREE')
        });
        dataItems.push(categoryViewModeItem);

        const emailNotificationOrderItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.EMAIL_NOTIFICATION_ORDER.toString(),
            title: this.translateService.instant('SETTINGS.SETTING.EMAIL_CONFIRMATION'),
            value: this.getBooleanText(userSettings.emailNotificationOrder)
        });
        dataItems.push(emailNotificationOrderItem);

        const invoiceTypeItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.INVOICE_TYPE.toString(),
            title: this.translateService.instant('CONDITION.INVOICE_TYPE.TITLE'),
            value: this.translateService.instant('CONDITION.DEFAULT_INVOICE_TYPE.' + userSettings.invoiceType)
        });
        dataItems.push(invoiceTypeItem);

        const paymentMethodItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.PAYMENT_METHOD.toString(),
            title: this.translateService.instant('CONDITION.PAYMENT_METHOD.TITLE'),
            value: this.translateService.instant('CONDITION.PAYMENT_METHOD.' + userSettings.paymentMethod)
        });
        dataItems.push(paymentMethodItem);

        const deliveryTypeItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.DELIVERY_TYPE.toString(),
            title: this.translateService.instant('CONDITION.DELIVERY_TYPE.TITLE'),
            value: this.translateService.instant('CONDITION.DELIVERY_TYPE.' + userSettings.deliveryType)
        });
        dataItems.push(deliveryTypeItem);

        const collectionDeliveryItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.COLLECTION_DELIVERY.toString(),
            title: this.translateService.instant('CONDITION.COLLECTIVE_DELIVERY.TITLE'),
            value: this.translateService.instant('CONDITION.COLLECTIVE_DELIVERY.' + userSettings.collectionDelivery)
        });
        dataItems.push(collectionDeliveryItem);

        const deliveryAddressItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.DELIVERY_ADDRESS.toString(),
            title: this.translateService.instant('CONDITION.DELIVERY_ADDRESS'),
            value: userSettings.deliveryAddress
        });
        dataItems.push(deliveryAddressItem);

        const billingAddressItem = new FeedbackDataItem({
            key: FeedbackDataItemKey.BILLING_ADDRESS.toString(),
            title: this.translateService.instant('CONDITION.BILLING_ADDRESS'),
            value: userSettings.billingAddress
        });
        dataItems.push(billingAddressItem);
        return dataItems;
    }

    private buildArticleResults(articleResults: FeedbackArticleResult[]): FeedbackDataItem[] {
        const dataItems: FeedbackDataItem[] = [];
        articleResults.forEach(item => {
            const dataItem = new FeedbackDataItem({
                key: FeedbackDataItemKey.ARTICLE_RESULTS.toString(),
                value: item.content
            });
            dataItems.push(dataItem);
        });
        return dataItems;
    }

    private buildShoppingCartItems(cartItems: FeedbackShoppingCartItem[]): FeedbackDataItem[] {
        const dataItems: FeedbackDataItem[] = [];
        cartItems.forEach(item => {
            const dataItem = new FeedbackDataItem({
                key: FeedbackDataItemKey.SHOPPING_CART_ITEM.toString(),
                value: item.content
            });
            dataItems.push(dataItem);
        });
        return dataItems;
    }

    private getBooleanText(isPresent: boolean): string {
        return isPresent ? this.translateService.instant('COMMON_LABEL.YES') : this.translateService.instant('COMMON_LABEL.NO');
    }
}
