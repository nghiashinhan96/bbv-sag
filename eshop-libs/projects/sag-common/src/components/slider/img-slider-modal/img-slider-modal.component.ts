import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-common-img-slider-modal',
    templateUrl: './img-slider-modal.component.html',
    styleUrls: ['./img-slider-modal.component.scss']
})
export class SagCommonImgSliderModalComponent implements OnInit {

    @Input() images;

    constructor(public bsModalRef: BsModalRef) { }

    ngOnInit() { }

}
