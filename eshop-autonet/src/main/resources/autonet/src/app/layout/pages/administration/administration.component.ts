import { Component, OnInit } from '@angular/core';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { environment } from 'src/environments/environment';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';
import { UrlUtil } from 'src/app/core/utils/url.util';
import { SubSink } from 'subsink';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';

@Component({
    selector: 'autonet-administration',
    templateUrl: './administration.component.html',
    styleUrls: ['./administration.component.scss']
})
export class AdministrationComponent implements OnInit {
    url = `${UrlUtil.autonetServer(this.appStore.country)}portal/Select.aspx`;
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
        const params = {
            type: 1,
            uid: autonet.uid,
            14: autonet.lid,
            400: ''
        };
        this.params = {
            ...params,
            '400': AppHelperUtil.objectToUrl(this.url, params)
        };
    }
}
