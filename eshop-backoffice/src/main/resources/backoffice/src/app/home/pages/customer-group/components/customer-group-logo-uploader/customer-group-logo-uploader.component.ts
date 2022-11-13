import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

import { NotificationModel } from 'src/app/shared/models/notification.model';

@Component({
  selector: 'backoffice-customer-group-logo-uploader',
  templateUrl: './customer-group-logo-uploader.component.html',
  styleUrls: ['./customer-group-logo-uploader.component.scss'],
})
export class CustomerGroupLogoUploaderComponent implements OnInit {
  onClose: Function;
  selectedFile: File;
  notifier: NotificationModel;
  acceptedFileTypes = 'image/png, image/jpeg, image/jpg';
  maxFileSize = 5 * 1024 * 1024;

  constructor(public bsModalRef: BsModalRef) { }

  ngOnInit() { }

  upload() {
    if (!this.selectedFile) {
      return;
    }
    this.onClose(this.selectedFile);
    this.bsModalRef.hide();
  }
}
