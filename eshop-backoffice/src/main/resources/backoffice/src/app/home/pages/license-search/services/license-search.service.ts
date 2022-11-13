import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable()
export class LicenseSearchService {
    constructor(private http: HttpClient) { }

    searchLicense(searchData: any): Observable<any> {
        const url = ApiUtil.getUrl(`admin/licenses/search`);
        return this.http.post(url, searchData).pipe(
            map((res) => {
                const licenseData: any = res;
                return licenseData;
            }),
            catchError((err) => of(null))
        ) as Observable<any>;
    }
}