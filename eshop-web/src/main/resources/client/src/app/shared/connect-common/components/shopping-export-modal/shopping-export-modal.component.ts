import * as moment from 'moment';

import { AffiliateUtil, SagMessageData } from 'sag-common';
import { Component, Input, OnInit } from '@angular/core';

import { ArticleModel } from 'sag-article-detail';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Constant } from 'src/app/core/conts/app.constant';
import { ADD_ON_ARTICLE_PREFIX, ADD_ON_ARTICLE_TYPE, EXPORT_TYPE } from '../../enums/shopping-export-type.enum';
import { SagCurrencyPipe } from 'sag-currency';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { ShoppingExportService } from '../../services/shopping-export.service';
import { TranslateService } from '@ngx-translate/core';
import { UserService } from 'src/app/core/services/user.service';
import { environment } from 'src/environments/environment';
import { ArticleUtil } from 'src/app/core/utils/article.util';

@Component({
    selector: 'connect-shopping-export-modal',
    templateUrl: './shopping-export-modal.component.html',
    styleUrls: ['./shopping-export-modal.component.scss']
})
export class ShoppingExportModalComponent implements OnInit {
    @Input() set shoppingBasket(val: ShoppingBasketModel) {
        if (val) {
            this.basket = new ShoppingBasketModel(val);
        }
    }
    private data;
    private basket: ShoppingBasketModel;
    errorMessage: SagMessageData;
    exportOptions = [
        {
            type: EXPORT_TYPE.NEW,
            text: 'SHOPPING_BASKET.EXPORT_OPTIONS.NEW'
        },
        {
            type: EXPORT_TYPE.LEGACY,
            text: 'SHOPPING_BASKET.EXPORT_OPTIONS.LEGACY'
        }
    ];

    formatTypeBasketExport = [
        {
            type: 'RTF',
            text: '.rtf',
            extension: 'rtf'
        },
        {
            type: 'WORD',
            text: '.docx',
            extension: 'docx'

        }
    ];

    isCZ = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isSB = AffiliateUtil.isSb(environment.affiliate);

    constructor (
        public modelRef: BsModalRef,
        private exportService: ShoppingExportService,
        private translateService: TranslateService,
        public userService: UserService,
        private currencyPipe: SagCurrencyPipe
    ) { }

    ngOnInit() {}

    exportCSVFile(option) {
        option.isIncludeAddOnArticle = true;
        this.buildData(this.basket.items, option);
        const fileName = `${this.getExportName()}.csv`;
        this.exportService.exportFile('CSV', fileName, this.data).subscribe((res: SagMessageData) => {
            this.errorMessage = res;

            if(this.isCZ || this.isSB) {
                option();
            }
        });
    }

    exportShortCsvFile(callback) {
        this.buildData(this.basket.items);
        const csvPrefix = this.translateService.instant('SHOPPING_BASKET.ARTICLE_LIST_EXPORT_FILE_NAME');
        const fileName = `${this.getExportName(csvPrefix)}.csv`;
        this.exportService.exportFile('SHORT_CSV', fileName, this.data).subscribe((res: SagMessageData) => {
            this.errorMessage = res;
            callback();
        });
    }

    exportExcelFile(option) {
        option.isIncludeAddOnArticle = true;
        this.buildData(this.basket.items, option);
        const fileName = `${this.getExportName()}.xlsx`;
        this.exportService.exportFile('EXCEL', fileName, this.data).subscribe((res: SagMessageData) => {
            this.errorMessage = res;

            if(this.isCZ || this.isSB) {
                option();
            }
        });
    }

    exportShortExcelFile(callback) {
        this.buildData(this.basket.items);
        const excelPrefix = this.translateService.instant('SHOPPING_BASKET.ARTICLE_LIST_EXPORT_FILE_NAME');
        const fileName = `${this.getExportName(excelPrefix)}.xlsx`;
        this.exportService.exportFile('SHORT_EXCEL', fileName, this.data).subscribe((res: SagMessageData) => {
            this.errorMessage = res;
            callback();
        });
    }

    exportMSWordFile(option) {
        option.isIncludeAddOnArticle = true;
        this.buildData(this.basket.items, option);
        const fileName = `${this.getExportName()}.docx`;
        this.exportService.exportFile('WORD', fileName, this.data).subscribe((res: SagMessageData) => {
            this.errorMessage = res;

            if(this.isCZ || this.isSB) {
                option();
            }
        });
    }

    exportBasketFile(option) {
        this.buildData(this.basket.items);
        const rtfPrefix = this.translateService.instant('SHOPPING_BASKET.BASKET_FILE');
        const fileName = `${this.getExportName(rtfPrefix)}.${option.extension}`;
        this.exportService.exportFile('BASKET_FILE', fileName, null, {
            curentStateNetPriceView: this.userService.userPrice.currentStateNetPriceView,
            formatType: option.type
        }).subscribe((res: SagMessageData) => {
            this.errorMessage = res;
        });
    }

    private buildHeader(option) {
        const header = [];
        header.push(this.translateService.instant('SHOPPING_BASKET.ARTICLE_NUMBER'));
        header.push(this.translateService.instant('SHOPPING_BASKET.ARTICLE_DESCRIPTION'));
        header.push(this.translateService.instant('ARTICLE.NUMBER'));
        if ((option && option.type === EXPORT_TYPE.NEW) || this.isCZ || this.isSB) {
            header.push(this.translateService.instant('SHOPPING_BASKET.EXPORT_HEADER.GROSS_PRICE_TYPE'));
        }
        header.push(this.translateService.instant('SHOPPING_BASKET.EXPORT_HEADER.GROSS_PRICE'));
        if (this.userService.userPrice.currentStateNetPriceView) {
            header.push(this.translateService.instant('SHOPPING_BASKET.EXPORT_HEADER.NET_PRICE'));
        }
        header.push(this.translateService.instant('SHOPPING_BASKET.TOTAL'));
        return header.join(',');
    }

    private buildData(items: ShoppingBasketItemModel[], option = null) {
        const headerTitle = this.buildHeader(option);
        const articleAddOnField = [
            ADD_ON_ARTICLE_TYPE.DEPOSIT_ARTICLE,
            ADD_ON_ARTICLE_TYPE.VRG_ARTICLE,
            ADD_ON_ARTICLE_TYPE.VOC_ARTICLE,
            ADD_ON_ARTICLE_TYPE.PFAND_ARTICLE
        ] as ADD_ON_ARTICLE_TYPE[];
        this.data = [];
        items.forEach(item => {
            const productText = item.getProductText();
            const result: any = {
                headerTitle,
                articleNumber: item.articleItem.artnrDisplay || '',
                articleDescription: productText && this.translateService.instant(productText) || '',
                quantity: item.quantity,
                grossPrice: this.currencyPipe.transform(item.grossPrice),
            } as any;
            if ((option && option.type === EXPORT_TYPE.NEW) || this.isCZ || this.isSB) {
                result.grossPriceType = this.getGrossPriceTypeAndBrand(item.articleItem);
            }
            if (this.userService.userPrice.currentStateNetPriceView) {
                let netPrice = item.netPrice;
                let totalNetPrice = item.totalNetPrice;
                if (this.userService.userDetail.isFinalUserRole) {
                    netPrice = item.finalCustomerNetPrice;
                    totalNetPrice = item.totalFinalCustomerNetPrice;
                }
                result.netPrice = this.currencyPipe.transform(netPrice);
                result.total = this.currencyPipe.transform(totalNetPrice);
            } else {
                result.total = this.currencyPipe.transform(item.totalGrossPrice);
            }
            this.data.push(result);

            // build add on article
            const articleAddOn = item.articleItem ? articleAddOnField.filter(field => item.articleItem[field]) : [];

            if (articleAddOn.length && option && option.isIncludeAddOnArticle) {
                this.buildArticleAddOnData(item, articleAddOn, headerTitle, result.grossPriceType);
            }
        });
    }

    getGrossPriceTypeAndBrand(articleDoc: ArticleModel) {
        if (articleDoc.displayedPrice) {
            return `${articleDoc.displayedPrice.type} ${articleDoc.displayedPrice.brand}`;
        }
        if (articleDoc.price && articleDoc.price.price
            && articleDoc.price.price.type && articleDoc.price.price.type !== Constant.GROSS) {
            return articleDoc.price.price.type;
        }

        return '';
    }

    getExportName(prefix = 'export') {
        return `${prefix}_${moment().format('YYYYMMDD_HHmm')}`;
    }

    buildArticleAddOnData(item: ShoppingBasketItemModel, addOnField: ADD_ON_ARTICLE_TYPE[] = [], headerTitle, grossPriceType: string = '') {
        addOnField.forEach(field => {
            const article = item.articleItem[field];
            const text = this.getDescription(field);
            const grossPrice = article.price && article.price.price ? article.price.price.grossPrice : 0;
            const prefix = this.getPrefix(field);
            const addOnItem = {
                headerTitle,
                articleNumber: `${prefix} - ${ item.articleItem.artnrDisplay}` || '',
                articleDescription: this.translateService.instant(text) || '',
                grossPriceType: grossPriceType,
                quantity: article.amountNumber || 0,
                grossPrice: this.currencyPipe.transform(grossPrice),
            } as any;
            if (this.userService.userPrice.currentStateNetPriceView && article.price && article.price.price) {
                let netPrice = article.price.price.netPrice;
                let totalNetPrice = article.price.price.totalNetPrice;
                if (this.userService.userDetail.isFinalUserRole) {
                    netPrice = article.price.price.finalCustomerNetPrice;
                    totalNetPrice = article.price.price.totalFinalCustomerNetPrice;
                }
                addOnItem.netPrice = this.currencyPipe.transform(netPrice);
                addOnItem.total = this.currencyPipe.transform(totalNetPrice);
            } else {
                addOnItem.total = this.currencyPipe.transform(item.totalGrossPrice);
            }
            this.data.push(addOnItem);
        });
    }

    private getDescription(field: ADD_ON_ARTICLE_TYPE) {
        switch (field) {
            case ADD_ON_ARTICLE_TYPE.DEPOSIT_ARTICLE:
                return 'SHOPPING_BASKET.DEPOT_TEXT';
            case ADD_ON_ARTICLE_TYPE.PFAND_ARTICLE:
                return 'SHOPPING_BASKET.PFAND_TEXT';
            case ADD_ON_ARTICLE_TYPE.VOC_ARTICLE:
                return 'SHOPPING_BASKET.VOC_TEXT';
            case ADD_ON_ARTICLE_TYPE.VRG_ARTICLE:
                return 'SHOPPING_BASKET.VRG_TEXT';
            default:
                return '';
        }
    }

    private getPrefix(field: ADD_ON_ARTICLE_TYPE) {
        switch (field) {
            case ADD_ON_ARTICLE_TYPE.DEPOSIT_ARTICLE:
                return ADD_ON_ARTICLE_PREFIX.DEPOSIT_ARTICLE;
            case ADD_ON_ARTICLE_TYPE.PFAND_ARTICLE:
                return ADD_ON_ARTICLE_PREFIX.PFAND_ARTICLE;
            case ADD_ON_ARTICLE_TYPE.VOC_ARTICLE:
                return ADD_ON_ARTICLE_PREFIX.VOC_ARTICLE;
            case ADD_ON_ARTICLE_TYPE.VRG_ARTICLE:
                return ADD_ON_ARTICLE_PREFIX.VRG_ARTICLE;
            default:
                return '';
        }
    }
}
