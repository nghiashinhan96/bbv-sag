import { Component, OnInit } from '@angular/core';

import { BsModalRef } from 'ngx-bootstrap/modal';
import { finalize } from 'rxjs/operators';

import { DeliveryProfileService } from '../../services/delivery-profile.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { NotificationModel } from 'src/app/shared/models/notification.model';

@Component({
    selector: 'backoffice-delivery-profile-importer',
    templateUrl: './delivery-profile-importer.component.html',
    styleUrls: ['./delivery-profile-importer.component.scss']
})
export class DeliveryProfileImporterComponent implements OnInit {
    onClose: any;
    selectedFile: File;
    acceptedFileTypes = '.csv, text/csv, application/vnd.ms-excel';
    notifier: NotificationModel;

    constructor(public bsModalRef: BsModalRef, private deliveryProfileService: DeliveryProfileService) { }

    ngOnInit() { }

    upload() {
        if (!this.selectedFile) {
            return;
        }
        SpinnerService.start('app-delivery-profile-importer .modal-body');
        const body = new FormData();
        body.append('file', this.selectedFile, this.selectedFile.name);
        this.deliveryProfileService
            .importDeliveryProfile(body)
            .pipe(
                finalize(() => SpinnerService.stop())
            )
            .subscribe(res => {
                this.notifier = { messages: ['DELIVERY_PROFILE.IMPORT_DELIVERY_PROFILE_SUCCESS'], status: true };
                setTimeout(() => {
                    this.onClose();
                    this.bsModalRef.hide();
                }, 600);
            }, err => {
                this.notifier = { messages: ['DELIVERY_PROFILE.IMPORT_DELIVERY_PROFILE_FAIL'], status: false };
            });
    }
}
