import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { AutonetAuthModel } from 'src/app/core/models/autonet-auth.model';
import { UrlUtil } from 'src/app/core/utils/url.util';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';

@Component({
    selector: 'autonet-autonet-frame-dialog',
    templateUrl: './autonet-frame-dialog.component.html',
    styleUrls: ['./autonet-frame-dialog.component.scss']
})
export class AutonetFrameDialogComponent implements OnInit {

    @Input() data: {
        autonetId: string,
        quantity: number,
        description?: string,
        dlnrId: number,
        artnr: number,
        gaID: number
    };

    url = `${UrlUtil.autonetServer(this.appStore.country)}core/addtocart/Display`;
    params = {
        esid: '',
        spr: null,
        kartnr: '',
        QTY: null,
        src: 'ext'
    };

    constructor(
        public bsModalRef: BsModalRef,
        private appStore: AppStorageService
    ) { }

    ngOnInit() {
        const autonet = this.appStore.autonet as AutonetAuthModel;
        this.params.esid = autonet.uid;
        this.params.spr = autonet.lid;
        this.params.kartnr = this.data.autonetId;
        this.params.QTY = this.data.quantity;
        const pageUrl = AppHelperUtil.objectToUrl(this.url, this.params);
        this.params['400'] = pageUrl;
    }

}
