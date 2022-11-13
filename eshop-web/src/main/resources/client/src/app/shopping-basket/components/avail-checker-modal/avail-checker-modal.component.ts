import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'connect-avail-checker-modal',
  templateUrl: './avail-checker-modal.component.html',
  styleUrls: ['./avail-checker-modal.component.scss']
})
export class AvailCheckerModalComponent implements OnInit {
  isConfirm = false;

  constructor(
    public modelRef: BsModalRef
  ) { }

  ngOnInit() {
  }

  confirm() {
    this.isConfirm = true;
    this.modelRef.hide();
  }
}
