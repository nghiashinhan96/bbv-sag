import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { TranslateService } from '@ngx-translate/core';
import { timer } from 'rxjs';
import { take } from 'rxjs/operators';

import { OpeningDayModel } from '../../model/opening-day.model';
import { CountryModel } from '../../model/country.model';
import { AffiliateModel } from '../../model/affiliate.model';
import { BranchModel } from '../../model/branch.model';
import { WorkingDayCodeModel } from '../../model/working-day-code.model';
import { OpeningDayCalendarService } from '../../service/opening-day-calendar.service';
import { OPENING_DAY_ROUTE } from '../../constant';

const TIME_DURATION = 2000;
@Component({
    selector: 'backoffice-opening-day-form-page',
    templateUrl: './opening-day-form-page.component.html',
    styleUrls: ['./opening-day-form-page.component.scss']
})
export class OpeningDayFormPageComponent implements OnInit {
    openingDay: OpeningDayModel;
    affiliates: AffiliateModel[];
    countries: CountryModel[];
    branches: BranchModel[];
    workingCodes: WorkingDayCodeModel[];

    noticeMessage: string;
    isCreated: boolean;
    onlyNumberHint: string;

    constructor(
        private openingDayService: OpeningDayCalendarService,
        private route: ActivatedRoute,
        private router: Router,
        private translateService: TranslateService) { }

    ngOnInit() {
        this.onlyNumberHint = this.translateService.instant('OPENING_DAY.OPENING_DAY_FORM.ONLY_NUMBER');
        this.route.data.subscribe((data: { openingDay: OpeningDayModel }) => {
            this.openingDay = data.openingDay[0] ? data.openingDay[0] : null;
            this.countries = data.openingDay[1];
            this.affiliates = data.openingDay[2];
            this.branches = data.openingDay[3];
            this.workingCodes = data.openingDay[4];
        });
    }

    save({ request, form }) {
        this.openingDayService.createOpeningDay(request).subscribe(res => {
            this.isCreated = true;
            this.noticeMessage = 'COMMON.MESSAGE.SAVE_SUCCESSFULLY';
            this.initiateTimer(TIME_DURATION);
        }, error => {
            this.handleError(error);
            form.enable();
            form.get('exceptionGroup').disable();
        });
    }

    update({ request, form }) {
        this.openingDayService.updateOpeningDay(request).subscribe(res => {
            this.isCreated = true;
            this.noticeMessage = 'COMMON.MESSAGE.SAVE_SUCCESSFULLY';
            this.initiateTimer(TIME_DURATION);
        }, error => {
            this.handleError(error);
            form.enable();
        });
    }

    goBack(path: string) {
        this.router.navigate([path]);
    }

    private initiateTimer(duration: number) {
        return timer(duration).pipe(
            take(1)
        ).subscribe(() => this.router.navigate([OPENING_DAY_ROUTE]));
    }

    private handleError(error: any): void {
        this.isCreated = false;
        if (!error) {
            this.noticeMessage = 'COMMON.MESSAGE.SAVE_UNSUCCESSFULLY';
            return;
        }
        switch (error.error_code) {
            case 'DUPLICATED_OPENING_DAYS':
                this.noticeMessage = 'OPENING_DAY.DUPLICATED_OPENING_DAYS';
                break;
            default:
                this.noticeMessage = 'COMMON.MESSAGE.SAVE_UNSUCCESSFULLY';
                break;
        }
    }

}
