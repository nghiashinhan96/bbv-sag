import { Component, OnDestroy, OnInit } from '@angular/core';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { environment } from 'src/environments/environment';
import { DomSanitizer } from '@angular/platform-browser';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';
import { UrlUtil } from 'src/app/core/utils/url.util';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';
import { SubSink } from 'subsink';

@Component({
    selector: 'autonet-catelogues',
    templateUrl: './catelogues.component.html',
    styleUrls: ['./catelogues.component.scss']
})
export class CateloguesComponent implements OnInit, OnDestroy {
    url = `${UrlUtil.autonetServer(this.appStore.country)}portal/SelectNew.aspx`;
    params: any;
    private subs = new SubSink();

    constructor(
        private appStore: AppStorageService,
        private router: Router
    ) { }

    ngOnInit() {
        this.loadIframe();
        this.subs.sink = this.router.events.subscribe(evt => {
            if (evt instanceof NavigationStart) {
                this.params = null;
            } else if (evt instanceof NavigationEnd) {
                setTimeout(() => {
                    this.loadIframe();
                })
            }
        });
        
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    loadIframe() {
        const autonet = this.appStore.autonet as AutonetAuthModel;
        this.params = {
            type: 2,
            uid: autonet.uid,
            14: autonet.lid
        }
    }
}
