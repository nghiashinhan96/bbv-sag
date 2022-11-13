import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BatteriesModel } from '../models/batteries.model';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable()
export class BatteriesService {

    public static readonly InterconnectionImgMap = [
        'Schema 0',
        'Schema 1',
        'Schema 2',
        'Schema 6',
        'Schema 9'
    ];

    public static readonly PolartImgMap = [
        'Polart 1',
        'Polart 19',
        'Polart 1+19',
        'Polart 3',
        'Polart 5'
    ];

    constructor(
        private http: HttpClient
    ) { }

    private baseUrl = environment.baseUrl;

    getBatteriesDataFilter(request): Observable<BatteriesModel> {
        const url = `${this.baseUrl}search/battery`;
        return this.http.post(url, request).pipe(
            map((res) => new BatteriesModel(res))
        );
    }
}
