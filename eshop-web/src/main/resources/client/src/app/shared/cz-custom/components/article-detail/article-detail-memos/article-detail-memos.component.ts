import { Component, Input, OnInit } from '@angular/core';
import { AffiliateUtil } from 'sag-common';
import { CZ_ARTICLE_STATUS, CZ_ARTICLE_STATUS_VALUE } from 'src/app/shared/cz-custom/enums/article.enums';
import { environment } from 'src/environments/environment';
import { ArticleModel } from 'sag-article-detail';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'connect-cz-article-detail-memos',
    templateUrl: 'article-detail-memos.component.html'
})
export class ArticleDetailMemosComponent implements OnInit {
    @Input() set memos(value: any[]) {
        let targetMemos = [CZ_ARTICLE_STATUS.NON_RETURNABLE];

        if (AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate)) {
            targetMemos = targetMemos.concat([CZ_ARTICLE_STATUS.FGAS, CZ_ARTICLE_STATUS.DEPOSIT_ITEM]);
            
            this.displayMemos = value.filter(item => {
                const statusKeyIndex = targetMemos.indexOf(item.statusKey);
                return statusKeyIndex !== -1 && item.statusValue === CZ_ARTICLE_STATUS_VALUE.YES
            }) ;
            return;
        }

        this.displayMemos = value.filter(item => {
            return item.statusKey === CZ_ARTICLE_STATUS.FGAS ||
                (item.statusKey !== CZ_ARTICLE_STATUS.DISCONTINUED && item.statusValue === CZ_ARTICLE_STATUS_VALUE.YES);
        });
    }
    @Input() article: ArticleModel;

    displayMemos: any[] = [];
    depositItemType: string;
    CZ_ARTICLE_STATUS = CZ_ARTICLE_STATUS;

    constructor(
        private translateService: TranslateService
    ) { }

    ngOnInit() {
        this.depositItemType = this.getDepositTypeTranslationText();
    }

    private getDepositTypeTranslationText() {
        if (!this.article) {
            return '';
        }
        const depositItemTypeArr: string[] = [];
        if (this.article.depositArticle) {
            depositItemTypeArr.push(this.translateService.instant('ARTICLE.DEPOSIT'));
        }
        if (this.article.vocArticle) {
            depositItemTypeArr.push(this.translateService.instant('SHOPPING_BASKET.VOC_TEXT'));
        }
        if (this.article.vrgArticle) {
            depositItemTypeArr.push(this.translateService.instant('SHOPPING_BASKET.VRG_TEXT'));
        }
        if (this.article.pfandArticle) {
            depositItemTypeArr.push(this.translateService.instant('SHOPPING_BASKET.PFAND_TEXT'));
        }
        const depositItemType = depositItemTypeArr.join(', ');
        return depositItemType;
    }
}
