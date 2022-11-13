import { Component, Input, OnInit } from '@angular/core';
import { ArticleModel } from 'sag-article-detail';
import { FGAS_TYPE } from '../../../consts/cz.consts';
import { CZ_ARTICLE_STATUS, CZ_ARTICLE_STATUS_VALUE } from 'src/app/shared/cz-custom/enums/article.enums';
import { environment } from 'src/environments/environment';
import { AffiliateUtil } from 'sag-common';
import { SagNumericService } from 'sag-currency';
import { UserService } from 'src/app/core/services/user.service';
@Component({
    selector: 'connect-cz-article-detail-special-info',
    templateUrl: 'article-detail-special-info.component.html'
})
export class ArticleDetailSpecialInfoComponent implements OnInit {
    @Input() article: ArticleModel;
    @Input() isShoppingCart: boolean;

    @Input() set memos(value: any[]) {
        this.initMemosDisplay(value);
    }

    nonReturnableMemo: any;
    fgasMemo: any;
    isFgasAndDepositNewFlow = false;
    shouldShowFGASItemInfoText = false;
    currency: string;
    isShowVat: boolean;

    constructor(
        private numericService: SagNumericService,
        private userService: UserService
        ) { }

    ngOnInit() {
        this.isFgasAndDepositNewFlow = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
        // have to initial the service setting because SagNumericService is not a root single instance
        this.numericService.updateSetting();
        this.currency = this.numericService.getCurrency();
        this.getIsShowVat();
    }

    private getIsShowVat() {
        const inclVATSettings = this.userService.userPrice && this.userService.userPrice.vatTypeDisplayConvert;
        this.isShowVat = inclVATSettings && inclVATSettings.list;
    }

    private initMemosDisplay(value) {
        const memos = value || [];

        this.nonReturnableMemo = memos.find(item => {
            return item.statusKey === CZ_ARTICLE_STATUS.NON_RETURNABLE && item.statusValue === CZ_ARTICLE_STATUS_VALUE.YES;
        });

        this.fgasMemo = memos.find(item => item.type === FGAS_TYPE && item.text);
        const allowedAddToShoppingCart = this.article.allowedAddToShoppingCart;
        const isFGASArticle = !!memos.find(item => item.statusKey === CZ_ARTICLE_STATUS.FGAS && item.statusValue === CZ_ARTICLE_STATUS_VALUE.YES);
        this.shouldShowFGASItemInfoText = isFGASArticle && !allowedAddToShoppingCart;
    }
}
