import { Component } from '@angular/core';

import { BsModalRef } from 'ngx-bootstrap/modal';
import { finalize } from 'rxjs/operators';

import { ExternalVendorService } from '../../services/external-vendor.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { NotificationModel } from 'src/app/shared/models/notification.model';

@Component({
    selector: 'backoffice-app-external-vendor-import-modal',
    templateUrl: './external-vendor-import-modal.component.html',
    styleUrls: ['./external-vendor-import-modal.component.scss']
})
export class ExternalVendorImportModalComponent {
    public notifier: NotificationModel;
    public selectedFile: File;
    public acceptedFileTypes = '.csv, text/csv, application/vnd.ms-excel';

    closeModal: any;
    constructor(public bsModalRef: BsModalRef, private externalVendorService: ExternalVendorService) { }

    upload() {
        SpinnerService.start();
        this.externalVendorService.upload(this.selectedFile).pipe(
            finalize(() => SpinnerService.stop())
        ).subscribe(() => {
            this.notifier = {
                messages: ['COMMON.MESSAGE.UPLOAD_SUCCESSFULLY'],
                status: true
            };

            setTimeout(() => {
                if (this.closeModal) {
                    this.closeModal();
                }
            }, 1000);
        }, (error) => this.notifier = {
            messages: ['COMMON.MESSAGE.NOT_UPLOAD_SUCCESSFULLY'],
            status: false
        });
    }

    onClose() {
        this.bsModalRef.hide();
    }
}
