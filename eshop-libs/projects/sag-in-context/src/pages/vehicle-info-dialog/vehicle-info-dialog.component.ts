import { Component, OnInit, Input } from '@angular/core';
import { AffiliateUtil } from 'sag-common';
import { SagInContextConfigService } from '../../services/articles-in-context-config.service';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-in-context-vehicle-info-dialog',
    templateUrl: './vehicle-info-dialog.component.html',
    styleUrls: ['./vehicle-info-dialog.component.scss']
})
export class SagInContextVehicleInfoDialogComponent implements OnInit {
    @Input() vehicle;

    vehicleInfoCodes: string;

    private NATIONAL_CODE_ATTRIBUTE: string;

    constructor(
        public bsModalRef: BsModalRef,
        private config: SagInContextConfigService
    ) { }

    ngOnInit() {
        if (AffiliateUtil.isBaseAT(this.config.affiliate)) {
            this.NATIONAL_CODE_ATTRIBUTE = 'at_natcode';
        } else {
            this.NATIONAL_CODE_ATTRIBUTE = 'typenschein';
        }
        this.vehicleInfoCodes = this.buildVehicleCodeValues();
    }

    private buildVehicleCodeValues() {
        if (!this.vehicle.codes) {
            return '';
        }
        return this.vehicle.codes
            .filter((item: any) => item.veh_code_attr === this.NATIONAL_CODE_ATTRIBUTE && item.veh_code_value !== '')
            .map((item: any) => item.veh_code_value)
            .join(', ');
    }
}
