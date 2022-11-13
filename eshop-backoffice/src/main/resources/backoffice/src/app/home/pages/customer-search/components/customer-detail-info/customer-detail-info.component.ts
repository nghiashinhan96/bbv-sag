import { Component, OnInit, Input } from '@angular/core';

@Component({
    selector: 'backoffice-customer-detail-info',
    templateUrl: './customer-detail-info.component.html',
    styleUrls: ['./customer-detail-info.component.scss']
})
export class CustomerDetailInfoComponent implements OnInit {
    @Input() customerInfo: any;
    @Input() affiliate: string;

    isFormExpanded = false;
    // toggleCondition = true;

    public isEdit;
    constructor() { }

    ngOnInit() {
    }
}
