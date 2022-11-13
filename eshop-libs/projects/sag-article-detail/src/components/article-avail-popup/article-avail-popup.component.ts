import { Component, OnInit, Input, HostListener, Renderer2, ElementRef, TemplateRef } from '@angular/core';
import { ArticleModel } from '../../models/article.model';
import { ArticleAvailabilityModel } from '../../models/article-availability.model';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';
import { ArticleDetailConfigService } from '../../services/article-detail-config.service';
import { SagNumericService } from 'sag-currency';
import { ProjectId, AffiliateUtil } from 'sag-common';
import { SagArticleDetailIntegrationService } from '../../services/article-detail-integration.service';

export const POPUP_STYLE = 'popup';
export const HORIZONTAL_STYLE = 'horizontal';

@Component({
    selector: 'sag-article-detail-avail-popup',
    templateUrl: './article-avail-popup.component.html',
    styleUrls: ['./article-avail-popup.component.scss']
})
export class SagArticleDetailAvailPopupComponent implements OnInit {
    @Input() article: ArticleModel;
    @Input() availabilities: ArticleAvailabilityModel[];
    @Input() popupRef: any;
    @Input() isCollapsible = false;
    @Input() isShownContent = true;
    @Input() userSetting: LibUserSetting;
    @Input() availTemplateRef: TemplateRef<any>;
    @Input() hidePrice = false;
    @Input() currentStateVatConfirm: boolean;

    style = POPUP_STYLE;
    isPopupStyle: boolean;
    currency: string;
    PROJECT_ID = ProjectId;
    projectId = ProjectId.CONNECT;
    basketType: any;
    private documentListener;
    isEhCz: boolean;
    isPDP: boolean;
    affiliateCode: string;
    user: any;
    title = 'ARTICLE.PRICE.PRICE_INFORMATION_AVAILIBILITY';

    constructor(
        private renderer: Renderer2,
        private ref: ElementRef,
        private numericService: SagNumericService,
        private config: ArticleDetailConfigService,
        public integrationService: SagArticleDetailIntegrationService
    ) {
        this.currency = this.numericService.getCurrency();
        this.projectId = this.config.projectId;
        this.user = this.integrationService.userDetail;
        this.basketType = this.integrationService.basketType;
        this.affiliateCode = this.config.affiliate;
    }

    @HostListener('click', ['$event'])
    onClickElement(event) {
        const checkClick = this.ref.nativeElement.contains(event.target);
        if (checkClick) {
            // do not excute document click;
            event.stopPropagation();
        }
    }

    ngOnInit() {
        this.isEhCz = AffiliateUtil.isEhCz(this.affiliateCode);
        this.isPDP = AffiliateUtil.isAffiliateApplyPDP(this.config.affiliate);
        this.style = this.isCollapsible ? HORIZONTAL_STYLE : POPUP_STYLE;

        if (this.isPopupStyle) {
            setTimeout(() => {
                this.bindDocumentEditListener();
            });
        }

        this.getTitlePopup();
    }

    getTitlePopup() {
        if (this.hidePrice) {
            this.title = 'ARTICLE.PRICE.INFORMATION_AVAILIBILITY';
        }

        if(this.article && this.article.itemType === 'VIN') {
            this.title = 'ARTICLE.PRICE.PRICE_INFORMATION';
        }
    }

    closePopup() {
        this.unbindDocumentEditListener();
        if (this.popupRef) {
            this.popupRef.hide();
        }
    }

    private bindDocumentEditListener() {
        if (!this.documentListener) {
            this.documentListener = this.renderer.listen('document', 'click', () => {
                this.closePopup();
            });
        }
    }

    private unbindDocumentEditListener() {
        if (this.documentListener) {
            this.documentListener();
            this.documentListener = null;
        }
    }
}
