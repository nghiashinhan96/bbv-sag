import { Component, OnInit, Input, ChangeDetectionStrategy, OnDestroy, ViewChild } from '@angular/core';
import { ArticleModel } from '../../models/article.model';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SagCommonImgSliderModalComponent, getImages, getImageThumb } from 'sag-common';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PopoverDirective } from 'ngx-bootstrap/popover';

@Component({
    selector: 'sag-article-detail-image',
    templateUrl: './article-detail-image.component.html',
    styleUrls: ['./article-detail-image.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SagArticleDetailImageComponent implements OnInit, OnDestroy {
    @ViewChild('magnifierPop', { static: true }) popover: PopoverDirective;

    @Input() article: ArticleModel;
    @Input() vinType: string;
    @Input() showHoverImg = true;
    @Input() popoverDelay: number = 0;
    @Input() useImgTag = false;

    private images = [];
    thumb: any;
    thumbnail: any;
    noImage = false;

    private modal: BsModalRef;

    constructor(
        private modalService: BsModalService
    ) { }

    ngOnInit() {
        if (!this.vinType) {
            this.getImagesSource();
        } else {
            this.vinType = this.vinType.trim().toLowerCase() === 'vin-10' ? 'vin-10' : 'vin-20';
        }
    }

    ngOnDestroy() {
        if(this.modal) {
            this.modal.hide();
        }
    }

    getImagesSource() {
        const source = this.article.images || [];
        this.images = getImages(source);
        if (this.images.length) {
            let thumb = getImageThumb(source);
            if (!thumb) {
                thumb = this.images[0].ref;
            }
            if (this.useImgTag) {
                this.thumb = thumb;
            } else {
                this.thumbnail = {
                    'background-image': `url('${thumb}')`
                };
            }
        } else {
            this.noImage = true;  // no images
        }
    }

    showImagesInfo() {
        this.modal = this.modalService.show(SagCommonImgSliderModalComponent, {
            ignoreBackdropClick: true,
            class: 'slick-slider-modal',
            initialState: {
                images: this.images
            }
        });
    }
}
