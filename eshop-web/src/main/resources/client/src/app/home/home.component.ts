import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../core/services/user.service';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AffiliateEnum } from 'sag-common';
import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';

@Component({
    selector: 'connect-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

    vehicleHistorySearch$ = new BehaviorSubject<[]>([]);
    vehicleHistoryTitle: string;
    activeSection = 'connect';
    MATIK_CH = AffiliateEnum.MATIK_CH;
    sb = AffiliateEnum.SB;
    subs = new SubSink();
    autoFilledVinValue = '';
    constructor(
        public userService: UserService,
        private activatedRoute: ActivatedRoute
    ) { }

    ngOnInit() {
        this.subs.sink = this.activatedRoute.queryParams.subscribe(queryParams => {
            const vinCode = queryParams.vin;
            if (vinCode) {
                this.autoFilledVinValue = vinCode;
            }
        });
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }
}
