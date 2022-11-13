import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { OpeningDayModel } from '../../model/opening-day.model';

@Component({
    selector: '[backoffice-opening-day-list-item]',
    templateUrl: './opening-day-list-item.component.html',
    styleUrls: ['./opening-day-list-item.component.scss']
})
export class OpeningDayListItemComponent implements OnInit {
    @Input() openingDay: OpeningDayModel;

    @Output() delete = new EventEmitter();

    public branchCodes: string;
    public expWorkingCode: string;
    public expAddressIds: string;

    constructor() { }

    ngOnInit() {
        this.branchCodes = this.formatText(this.openingDay.expBranchInfo);
        this.expAddressIds = this.formatText(this.openingDay.expDeliveryAddressId);
        this.expWorkingCode = this.openingDay.expWorkingDayCode ? `OPENING_DAY.${this.openingDay.expWorkingDayCode}` : null;
    }

    public onDelete() {
        this.delete.next(this.openingDay);
    }

    private formatText(text: string): string {
        return text.replace(/,/g, ', ');
    }
}
