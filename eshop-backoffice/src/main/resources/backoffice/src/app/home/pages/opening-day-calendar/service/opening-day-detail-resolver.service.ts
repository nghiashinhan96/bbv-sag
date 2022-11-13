import { Injectable } from '@angular/core';
import { Resolve, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { Observable, of, forkJoin } from 'rxjs';

import { OpeningDayCalendarService } from './opening-day-calendar.service';

@Injectable()
export class OpeningDayDetailResolverService implements Resolve<any> {

    constructor(private router: Router, private openingDayCalendarService: OpeningDayCalendarService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        const id = +route.paramMap.get('id');
        let openingDay = of(null);
        if (id >= 1) {
            openingDay = this.openingDayCalendarService.getOpeningDay(id);
        }
        const countries = this.openingDayCalendarService.getAllCountries();
        const workingCodes = this.openingDayCalendarService.getWorkingDayCode();
        const affiliates = this.openingDayCalendarService.getAllAffiliates();
        const branches = this.openingDayCalendarService.getBranches();

        return forkJoin([openingDay, countries, affiliates, branches, workingCodes]);
    }
}
