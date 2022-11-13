import { Component, Input, OnChanges, OnInit, SimpleChanges } from "@angular/core";
import { BehaviorSubject, Subject } from "rxjs";
import { ANALYTICAL_CARD_PAYMENT } from "../../enums/analytical-card/analytical-card.enum";
import { AnalyticalCardFilter } from "../../models/analytical-card/analytical-card-filter.model";
import { AnalyticalCardService } from "../../services/analytical-card.service";

@Component({
    selector: 'connect-analytical-card-container',
    templateUrl: './analytical-card-container.component.html',
    styleUrls: ['analytical-card-container.component.scss']
})
export class AnalyticalCardContainerComponent implements OnInit, OnChanges {
    @Input() paymentMethod: ANALYTICAL_CARD_PAYMENT;
    @Input() selected: boolean;
    
    filterEvent = new BehaviorSubject<AnalyticalCardFilter>(null);
    initiated: boolean;
    filter: AnalyticalCardFilter;
    filterCallback: any;
    inProcessAmount = 0;
    postedBalance = 0;

    constructor(
        private analyticsCardService: AnalyticalCardService
    ) { }

    ngOnInit() {
        this.analyticsCardService.getAnalyticalCardAmount(this.paymentMethod).subscribe(data => {
            this.inProcessAmount = data && data.inProcessAmount || 0;
            this.postedBalance = data && data.postedBalance || 0;
        });
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.selected && !changes.selected.firstChange) {
            if (changes.selected.currentValue && !this.initiated) {
                this.initiated = true;
                this.filterEvent.next(this.filter);
            }
        }
    }

    onFilterChange({ request, done }: { request: AnalyticalCardFilter, done: () => void }) {
        this.filter = new AnalyticalCardFilter({
            ...request,
            paymentMethod: this.paymentMethod
        });
        if (this.selected) {
            this.initiated = true;
            this.filterEvent.next(this.filter);
        }
        this.filterCallback = done;
    }

    searchCompleted() {
        if (this.filterCallback) {
            this.filterCallback();
        }
    }
}