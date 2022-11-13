import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

import { environment } from 'src/environments/environment';


@Injectable({
    providedIn: 'root'
})
export class UnicatService {
    private baseUrl = environment.baseUrl;

    constructor(
        private http: HttpClient
    ) { }

    getUnicatCatalogUri(): Observable<any> {
        const url = `${this.baseUrl}unicat/open-cataloge-uri`;
        return this.http.get(url);
    }
}
