import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { cloneDeep } from 'lodash';
import { AffiliateUtil } from 'sag-common';
import { CZ_AVAIL_STATE, ArticleModel } from 'sag-article-detail';
import { CZ_PRICE_TYPE } from 'src/app/shared/cz-custom/enums/article.enums';
import { environment } from 'src/environments/environment';
import { ArticleInContextService } from 'sag-in-context';
import { CommonModalService } from 'src/app/shared/connect-common/services/common-modal.service';
@Component({
    selector: 'connect-articles-in-context-article-list',
    templateUrl: './article-in-context-list.component.html'
})
export class ArtilesInContextListComponent implements OnInit {
    czAvailState = CZ_AVAIL_STATE;
    czPriceType = CZ_PRICE_TYPE;
    isCz = AffiliateUtil.isAffiliateCZ(environment.affiliate);
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);

    @ViewChild('specialInfoRef', { static: false }) specialInfoTemplateRef: TemplateRef<any>;
    @ViewChild('memosRef', { static: false }) memosTemplateRef: TemplateRef<any>;
    @ViewChild('availRef', { static: false }) availTemplateRef: TemplateRef<any>;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChild('priceRef', { static: false }) priceTemplateRef: TemplateRef<any>;
    @ViewChild('totalPriceRef', { static: false }) totalPriceTemplateRef: TemplateRef<any>;


    constructor(
        public articleInContextService: ArticleInContextService,
        private commonModalService: CommonModalService
    ) { }

    ngOnInit() {
    }

    emitBrandFilterData(data) {
        this.articleInContextService.emitBrandFilterData(data);
    }


    emitBarFilterData(data) {
        const newData = cloneDeep(data);
        this.articleInContextService.emitBarFilterData(newData);
    }

    onArticleNumberClick(article: ArticleModel) {
        this.commonModalService.showReplaceModal(article);
    }

    onShowAccessories(data: any) {
        this.commonModalService.showAccessoriesModal(data);
    }

    onShowPartsList(data: any) {
        this.commonModalService.showPartsListModal(data);
    }

    onShowCrossReference(data: any) {
        this.commonModalService.showCrossReferenceModal(data);
    }
}
