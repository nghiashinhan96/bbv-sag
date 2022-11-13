import { Injectable } from '@angular/core';
import { Resolve, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { Observable } from 'rxjs';

import { OpeningDayCalendarService } from './opening-day-calendar.service';

@Injectable()
export class OpeningDayListResolverService implements Resolve<any> {

    constructor(private router: Router, private openingDayCalendarService: OpeningDayCalendarService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        const workingCodes = this.openingDayCalendarService.getWorkingDayCode();

        return workingCodes;
    }
}
