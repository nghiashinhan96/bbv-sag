import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ReleaseService {
    constructor(private http: HttpClient) { }

    getRelease(): Observable<any> {
        const timestamp = new Date().getTime();
        const url = `${environment.baseUrl}release?ver=${timestamp}`;
        return this.http.get(url);
    }
}
