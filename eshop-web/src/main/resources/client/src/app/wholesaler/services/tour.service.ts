import { Observable, of } from 'rxjs';
import { Injectable, TRANSLATIONS } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { TourModel, TourRequestModel } from '../models/tour.model';
import { DAY_IN_WEEK } from 'src/app/core/conts/app.constant';
import { environment } from 'src/environments/environment';
import { CREATE_TOUR, SEARCH_TOUR, REMOVE_TOUR, UPDATE_TOUR, GET_TOUR } from './constant';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

@Injectable()
export class TourService {
    private readonly BASE_URL = environment.baseUrl;

    private readonly weekDayTranslation = {};

    constructor(
        private translateService: TranslateService,
        private http: HttpClient) {
        this.generateWeekDayTrans();
    }

    generateWeekDayTrans() {
        DAY_IN_WEEK.forEach(day => {
            this.weekDayTranslation[day] = this.translateService.instant('MY_DATE_RANGE_PICKER.DAYS.' + day.substr(0, 3));
        })
    }

    getDurationTimeFromTour(tour: TourModel): string {
        if (!tour.wssTourTimesDtos) {
            return;
        }
        const durationList: string = tour.wssTourTimesDtos.reduce((tourTimeText, duration, index) => {
            if (duration.departureTime) {
                return `${tourTimeText} ${this.weekDayTranslation[duration.weekDay]}:${duration.departureTime},`;
            }
            return `${tourTimeText}`;
        }, '');
        return durationList.substr(0, durationList.length - 1);
    }

    getTourList(body: TourRequestModel, page?) {
        let url = page ? `${SEARCH_TOUR}?page=${page.page}&size=${page.size}` : SEARCH_TOUR;
        url = `${this.BASE_URL}${url}`;
        return this.http.post(url, body).pipe(
            catchError(() => {
                return of([]);
            })
        );
    }

    getTour(tourId) {
        let url = `${this.BASE_URL}${GET_TOUR}/${tourId}`;
        return this.http.get(url);
    }

    removeTour(id) {
        let url = `${this.BASE_URL}${REMOVE_TOUR}/${id}`;
        return this.http.delete(url);
    }

    createTour(tour: TourModel): Observable<any> {
        let url = `${this.BASE_URL}${CREATE_TOUR}`;
        return this.http.post(url, tour);
    }

    updateTour(tour: TourModel): Observable<any> {
        let url = `${this.BASE_URL}${UPDATE_TOUR}`;
        return this.http.put(url, tour);
    }
}
