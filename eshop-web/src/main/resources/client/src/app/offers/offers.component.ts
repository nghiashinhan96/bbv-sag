import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { OffersService } from './services/offers.services';

@Component({
    selector: 'connect-offers',
    templateUrl: './offers.component.html',
    styleUrls: ['./offers.component.scss']
})
export class OffersComponent implements OnInit, OnDestroy {
    title: string;
    titleSub: Subscription;

    constructor(
        private offersService: OffersService
    ) {
        this.titleSub = this.offersService.title.subscribe(title => {
            this.title = title;
        });
    }

    ngOnInit() {

    }

    ngOnDestroy() {
        this.titleSub.unsubscribe();
    }
}
