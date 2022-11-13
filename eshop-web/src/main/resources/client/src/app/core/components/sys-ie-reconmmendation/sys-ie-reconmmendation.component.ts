import { Component, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-sys-ie-recommendation',
    templateUrl: './sys-ie-reconmmendation.component.html',
    styleUrls: ['./sys-ie-reconmmendation.component.scss']
})
export class SysIERecommendationComponent {
    @Input() title = '';
    @Input() firstDescription = '';
    @Input() secondDescription = '';

    constructor(private bsModalRef: BsModalRef) { }

    confirm() {
        this.bsModalRef.hide();
    }

}
