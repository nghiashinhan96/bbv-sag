import { Component, Input, OnInit } from '@angular/core';
import { UrlUtil } from 'sag-common';
import { INFO_TYPE_MAP } from '../../consts/article-detail.const';
import { INFO_TYPE } from '../../enums/info-type.enum';
import { ArticleInfo } from '../../models/article.model';

@Component({
    selector: 'sag-article-info-item',
    templateUrl: './article-info-item.component.html',
    styleUrls: ['./article-info-item.component.css']
})
export class SagArticleInfoItemComponent implements OnInit {

    @Input() info: ArticleInfo;

    INFO_TYPE_MAP = INFO_TYPE_MAP;
    INFO_TYPE = INFO_TYPE;
    type = INFO_TYPE_MAP.TEXT;

    constructor() { }

    ngOnInit() {
        this.generateContent(this.info);
    }

    generateContent(info: ArticleInfo) {
        if (!info) {
            return;
        }
        const url = UrlUtil.parseUrl(info.txt);
        if (!url) {
            this.type = INFO_TYPE_MAP.TEXT;
            return;
        }
        const isImageLink = info.txt.toLowerCase().includes('.jpg') || info.txt.toLowerCase().includes('.png');
        if (isImageLink) {
            this.type = INFO_TYPE_MAP.IMAGE;
            return;
        }
        this.type = INFO_TYPE_MAP.LINK;
    }

}
