import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';
import { UrlUtil } from 'src/app/core/utils/url.util';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'autonet-showpage',
    templateUrl: './showpage.component.html',
    styleUrls: ['./showpage.component.scss']
})
export class ShowpageComponent implements OnInit {
    url: string;
    params = {};
    constructor(
        private activatedRoute: ActivatedRoute,
        private appStore: AppStorageService
    ) { }

    ngOnInit() {
        this.url = decodeURIComponent(this.activatedRoute.snapshot.queryParams['400']);
    }
}
