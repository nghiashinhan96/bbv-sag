import { Component, OnInit, Input } from '@angular/core';
import { map } from 'rxjs/operators';
import { BsModalService } from 'ngx-bootstrap/modal';
import {
    SagArticleDetailSpecVehicleUsageComponent
} from '../article-detail-spec-vehicle-usage/article-detail-spec-vehicle-usage.component';
import { of } from 'rxjs/internal/observable/of';
import { ArticleInfo, ArticleModel } from '../../models/article.model';
import { CategoryModel } from '../../models/category.model';
import { INFO_TYPE } from '../../enums/info-type.enum';
import { ArticlesService } from '../../services/articles.service';
import { BroadcastService, UrlUtil } from 'sag-common';
import { ArticleBroadcastKey } from '../../enums/article-broadcast-key.enum';
import { TranslateService } from '@ngx-translate/core';


@Component({
    selector: 'sag-article-detail-spec',
    templateUrl: './article-detail-spec.component.html',
    styleUrls: ['./article-detail-spec.component.scss']
})
export class SagArticleDetailSpecComponent implements OnInit {
    @Input() article: ArticleModel;
    @Input() cate: CategoryModel;
    @Input() isShownMoreInfo = false;


    vehicleUsages$;

    allVehicleUsages: any = [];
    firstVehicleUsages: any;

    INFO_TYPE = INFO_TYPE;
    constructor(
        private articleService: ArticlesService,
        private modalService: BsModalService,
        private broadcaster: BroadcastService,
        private translate: TranslateService
    ) { }

    ngOnInit() {

    }

    getArticleVehicleUsages() {
        if (this.allVehicleUsages.length > 0) {
            this.vehicleUsages$ = of(this.allVehicleUsages[0]);
        } else {
            this.vehicleUsages$ = this.articleService.getArticleVehicleUsages(this.article.artid).pipe(map((res: any) => {
                this.allVehicleUsages = res;
                if (res && res.length > 0) {
                    return res[0];
                }
                return {};
            }));
        }
    }

    naviagteTo(event, vehId) {
        event.preventDefault();
        event.stopPropagation();
        this.broadcaster.broadcast(ArticleBroadcastKey.VEHICLE_ID_CHANGE, vehId);
    }

    showAllVehicleUsage() {
        this.modalService.show(SagArticleDetailSpecVehicleUsageComponent, {
            ignoreBackdropClick: true,
            initialState: {
                allVehicleUsages: this.allVehicleUsages
            }
        });
    }

    buildInfoHtml(info: ArticleInfo) {
        if (!info) {
            return '';
        }
        const title = this.translate.instant('ARTICLE.' + this.INFO_TYPE[info.type])
        let content = '<span class="font-title">' + title + '</span><br>';
        const url = UrlUtil.parseUrl(info.txt);
        if (!url) {
            return content + `<p>${info.txt}</p>`;
        }
        if (UrlUtil.isImageUrl(info.txt)) {
            return content + `<img src=${url} class="info-image" /><br>`;
        }
        return content + '<p>' + UrlUtil.parseUrlStr(info.txt) + '</p>';
    }

    printInfo() {
        let printContent = '';
        (this.article.info || []).map(info => {
            printContent += this.buildInfoHtml(info);
        })
        const printStr = `<html><head><title>SAG</title>
        <style> body { padding: 20px; font-family: "Roboto", sans-serif; } .font-title { font-weight: bold; } .info-image{ width: 70% } </style></head>
        <body onload="window.print();window.close()">${printContent}</body></html>`;
        const screenHeight = screen.height || 100;
        const screenwidth = screen.width || 100;
        const popupWin = window.open('', '_blank', `scrollbars=yes,top=0,left=0,height=${screenHeight},width=${screenwidth},resizable=yes`);
        popupWin.document.open();
        popupWin.document.write(printStr);
    }
}
