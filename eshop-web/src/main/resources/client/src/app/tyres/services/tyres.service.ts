import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PkwTyresModel } from '../models/pkw-tyres.model';
import { MotorTyresModel } from '../models/motor-tyres.model';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable()
export class TyresService {

    constructor(
        private http: HttpClient
    ) { }

    public static readonly EXAMPLE_MATCH_CODES = [
        'PW2055516NTNGOODYEAR',
        'PW20555*GOODYEAR',
        'PW2055516*',
        'PW2055516NTN*'
    ];

    private baseUrl = environment.baseUrl;

    private readonly SUMMER_END_DATE = 14;
    private readonly SUMMER_START_MONTH = 3;
    private readonly SUMMER_END_MONTH = 8;

    getPkwTyreDataFilter(request): Observable<PkwTyresModel> {
        const url = `${this.baseUrl}search/tyre`;
        return this.http.post(url, request).pipe(
            map((res) => new PkwTyresModel(res))
        );
    }

    getMotorTyreDataFilter(request): Observable<MotorTyresModel> {
        const url = `${this.baseUrl}search/tyre/motor`;
        return this.http.post(url, request).pipe(
            map((res) => new MotorTyresModel(res))
        );
    }

    // Summer time is from 1-3 to 14-8 . The rest is Winter
    public isSummerTime(dateOfMonth, month) {
        // special month is 8
        if (month === this.SUMMER_END_MONTH && dateOfMonth <= this.SUMMER_END_DATE) {
            return true;
        }
        // other month 3 -> 7
        if (month >= this.SUMMER_START_MONTH && month < this.SUMMER_END_MONTH) {
            return true;
        }
        return false;
    }
}
