import { DOCUMENT } from '@angular/common';
import { AfterViewInit, ElementRef, Renderer2, ViewChild, Inject } from '@angular/core';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { SubSink } from 'subsink';
import { get } from 'lodash';

import { UserDetailOwnSetting } from '../core/models/user-detail-own-setting.mode';
import { UserService } from '../core/services/user.service';
import { CatalogIframeUtil } from '../core/utils/catalog-iframe.util';
import { ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-stg-info-catalog',
    templateUrl: './stg-info.component.html',
    styleUrls: ['./stg-info.component.scss']
})
export class STGInfoComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('stginfoIframe', { static: true }) stginfoIframe: ElementRef;
    @ViewChild('stginfoSection', { static: true }) stginfoSection: ElementRef;

    userSettings: UserDetailOwnSetting;
    isMaxSize = false;
    footerIsShowed = false;
    mainMenuIsShowed = true;

    currentTotalCartItems: number;
    subs = new SubSink();
    pollSubject: Observable<any>;
    intervalUpdateTop: any;
    initHeight = 170;
    params: any;
    isSalesOnBeHalf = false;

    constructor(
        private renderer: Renderer2,
        @Inject(DOCUMENT) private document: any,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {
        this.subs.sink = this.activatedRoute.queryParams.subscribe(params => {
            this.params = params;
        });
        this.isSalesOnBeHalf = this.userService.userDetail && this.userService.userDetail.isSalesOnBeHalf;
    }

    ngOnInit() {
        this.updateSTGInfoLayout();
        this.loadSTGInfoUrl();
    }

    ngAfterViewInit() {
        this.intervalUpdateTop = setInterval(() => {
            CatalogIframeUtil.updateCatalogIframeTop(this.renderer, this.document, '.stginfo-section', this.initHeight);
        }, 1000);
    }

    ngOnDestroy() {
        this.destroyCustomLayout();
        this.subs.unsubscribe();
        clearInterval(this.intervalUpdateTop);
    }

    toogleFooter() {
        this.footerIsShowed = !this.footerIsShowed;
        document.body.classList.toggle('footer-hide', !this.footerIsShowed);
    }

    private loadSTGInfoUrl() {
        if (this.stginfoIframe) {
            const user = this.userService.userDetail;
            let link;
            const laximo = get(environment, 'laximo_src');
            const src = this.isSalesOnBeHalf ? laximo.sales : laximo.normal;
            if (this.params && this.params.vin) {
                link = `${get(user, 'settings.externalUrls.laximo', '')}&10=&vin=${this.params.vin}`;
            } else {
                link = `${get(user, 'settings.externalUrls.stg_info', '')}`;
            }
            link = `${link}&src=${src}`
            this.renderer.setAttribute(this.stginfoIframe.nativeElement, 'src', link);
        }
    }

    private updateSTGInfoLayout() {
        document.body.classList.add('stginfo-openned');
        document.body.classList.toggle('menu-hide', !this.mainMenuIsShowed);
        document.body.classList.toggle('footer-hide', !this.footerIsShowed);
        this.renderer.removeClass(this.stginfoSection.nativeElement, 'container-lg');
    }

    private destroyCustomLayout() {
        document.body.classList.remove('menu-hide', 'footer-hide', 'stginfo-openned');
    }
}
