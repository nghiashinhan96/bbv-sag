import { Injectable, Injector } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AffiliateService {
    private baseUrl = environment.baseUrl;
    constructor(private http: HttpClient) {
    }

    public getAffiliateSettings(affiliate) {
        const url = `${this.baseUrl}settings/` + affiliate;
        return this.http.get(url, { observe: 'body' });
    }
}
