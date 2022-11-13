import { Component, OnInit, ElementRef, ViewChild, Input, AfterViewInit, ChangeDetectionStrategy, EventEmitter, Output } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { MAX_INFO_LENGTH } from '../../consts/article-detail.const';
import { INFO_TYPE } from '../../enums/info-type.enum';
import { ArticleModel } from '../../models/article.model';
import { SagArticleDetailRefrencesComponent } from '../article-detail-refrences/article-detail-refrences.component';
import { AffiliateUtil } from 'sag-common';

export const PartDetailDescriptionConst = {
    FULL: 'FULL',
    HEADER: 'HEADER',
    CONTENT: 'CONTENT',
    INFO: 'INFO',
    REPLACEMENT: 'REPLACEMENT',
    ORDER_HISTORY: 'ORDER_HISTORY'
};

@Component({
    selector: 'sag-article-detail-description',
    templateUrl: './article-detail-description.component.html',
    styleUrls: ['./article-detail-description.component.scss']
})
export class SagArticleDetailDescriptionComponent implements OnInit, AfterViewInit {
    @ViewChild('descHeader', { static: false }) descHeader: ElementRef;
    @ViewChild('numberRef', { static: false }) numberRef: ElementRef;
    @ViewChild('descriptionTitle', { static: false }) descriptionTitle: ElementRef;

    @Input() article: ArticleModel;
    @Input() searchArticleNr: any;
    @Input() type = PartDetailDescriptionConst.FULL;
    @Input() popoverDelay: number = 0;

    genArtDescription: string;
    TYPE = PartDetailDescriptionConst;
    INFO_TYPE = INFO_TYPE;

    @Input() isSimpleMode: boolean;

    @Output() articleNumberClick = new EventEmitter();

    @Input() enableRefNrChange = true;

    @Input() enableDetail = true;

    @Input() affiliateCode: string;

    info = [];
    isPDP: boolean;

    constructor(
        private modalService: BsModalService
    ) { }

    ngOnInit() {
        this.isPDP = AffiliateUtil.isAffiliateApplyPDP(this.affiliateCode);
        this.updateGenArtInfo();
        this.info = this.article.info.filter(item => this.isDocTypeAdditional(item))
            .map(item => {
                if (this.isTruncated(item)) {
                    item.truncatedTxt = item.txt.slice(0, 60) + '...';
                } else {
                    item.truncatedTxt = item.txt;
                }
                return item;
            });
    }

    private updateGenArtInfo() {
        this.article.genArtTxts = this.article.genArtTxts != null ? this.article.genArtTxts : [];
        this.genArtDescription = this.article.genArtTxts.length > 0 ? this.article.genArtTxts[0].gatxtdech : '';
    }

    ngAfterViewInit(): void {
        setTimeout(() => {
            if (this.type === PartDetailDescriptionConst.ORDER_HISTORY) {
                return;
            }
            if (this.numberRef && this.numberRef.nativeElement && this.descriptionTitle && this.descriptionTitle.nativeElement && this.descHeader && this.descHeader.nativeElement) {
                const parentW = this.descHeader.nativeElement.offsetWidth;
                const descriptionW = this.descriptionTitle.nativeElement.scrollWidth;
                const numberW = this.numberRef.nativeElement.scrollWidth;
                if (descriptionW + numberW > parentW) {
                    this.descriptionTitle.nativeElement.classList.add('text-ellipsis');
                    if (numberW > parentW / 2) {
                        this.numberRef.nativeElement.classList.add('text-ellipsis');
                        if (descriptionW < parentW / 2) {
                            const percent = (parentW - descriptionW) / parentW * 100;
                            this.numberRef.nativeElement.parentElement.style.maxWidth = `${percent}%`;
                        } else {
                            this.numberRef.nativeElement.parentElement.style.maxWidth = '50%';
                        }
                    }
                }
            }
        });
    }

    showRef() {
        const hasOeNumbers = Object.keys(this.article.oeNumbers || {}).length > 0;
        const hasIamNumbers = Object.keys(this.article.iamNumbers || {}).length > 0;
        this.modalService.show(SagArticleDetailRefrencesComponent, {
            ignoreBackdropClick: true,
            initialState: {
                article: this.article,
                enableRefNrChange: this.enableRefNrChange
            },
            class: 'oe-cross-reference-modal-content ' + (hasOeNumbers && hasIamNumbers ? 'double-column' : 'single-column')
        });
    }

    openReplacedByArticle() {
        this.articleNumberClick.emit(this.article);
    }

    private isDocTypeAdditional(item) {
        return item.type == this.INFO_TYPE.BRIEF_INFORMATION || item.type == this.INFO_TYPE.ADD_REC;
    }

    private isTruncated(item) {
        return item.txt && item.txt.length > MAX_INFO_LENGTH;
    }


}
