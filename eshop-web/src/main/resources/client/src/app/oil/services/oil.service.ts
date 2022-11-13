import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OilModel } from '../models/oil.model';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { OilFilterRequest } from '../models/oil-filter-request.model';

@Injectable()
export class OilService {

    private baseUrl = environment.baseUrl;

    constructor(
        private http: HttpClient
    ) { }

    getOilDataFilter(request: OilFilterRequest): Observable<OilModel> {
        const url = `${this.baseUrl}search/oil`;
        return this.http.post(url, request).pipe(
            map((res) => new OilModel(res))
        );
    }
}
