import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { map } from 'rxjs/internal/operators/map';

@Injectable({
    providedIn: 'root'
})
export class HeaderSettingsService {

    incentiveLink: any;
    private incentiveLinkSub$ = new BehaviorSubject<any>(null);
    private baseUrl = environment.baseUrl;
    constructor(private http: HttpClient) { }

    saveAcceptTerm() {
        const url = `${this.baseUrl}incentive/save-term`;
        return this.http.post(url, {});
    }

    getIncentiveLink() {
        const url = `${this.baseUrl}incentive/link`;
        return this.http.get(url, { observe: 'body' }).pipe(map(body => {
            this.incentiveLink = body;
            this.incentiveLinkSub$.next(this.incentiveLink);
            return this.incentiveLink;
        }));
    }

    get incentiveLink$() {
        return this.incentiveLinkSub$.asObservable();
    }

    resetIncentiveLink() {
        this.incentiveLink = null;
        this.incentiveLinkSub$.next(this.incentiveLink);
    }
}
