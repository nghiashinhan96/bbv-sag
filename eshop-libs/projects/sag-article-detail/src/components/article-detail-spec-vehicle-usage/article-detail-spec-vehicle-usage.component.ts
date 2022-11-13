import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BroadcastService } from 'sag-common';
import { INFO_TYPE } from '../../enums/info-type.enum';
import { ArticleBroadcastKey } from '../../enums/article-broadcast-key.enum';

@Component({
    selector: 'sag-article-detail-spec-vehicle-usage',
    templateUrl: './article-detail-spec-vehicle-usage.component.html',
    styleUrls: ['./article-detail-spec-vehicle-usage.component.scss']
})
export class SagArticleDetailSpecVehicleUsageComponent implements OnInit {

    @Input() allVehicleUsages: any[];

    INFO_TYPE = INFO_TYPE;

    constructor(
        public bsModalRef: BsModalRef,
        private broadcaster: BroadcastService
    ) { }

    ngOnInit() {

    }

    navigateTo(event, vehId: any) {
        event.preventDefault();
        event.stopPropagation();
        this.bsModalRef.hide();
        this.broadcaster.broadcast(ArticleBroadcastKey.VEHICLE_ID_CHANGE, vehId);
    }
}
