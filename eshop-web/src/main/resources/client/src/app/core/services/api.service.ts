import { Injectable, Injector } from '@angular/core';
import { APP_CONFIGURATION } from '../conts/app.constant';
import { LocalStorage } from 'ngx-webstorage';
import { AppConfigurationModel } from '../models/app-config.model';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

export class ApiService {

    private baseUrl = environment.baseUrl;

    constructor() { }

    // postForToken(path: string, body: Object = {}): Observable<any> {
    //     return this.http.post(`${environment.tokenUrl}${path}`,
    //         body,
    //         {
    //             headers: this.tokenApiHeaders()
    //         })
    //         .timeoutWith(apiCallTimeout, this.throwTimeoutError())
    //         .catch(error => this.handleError(error))
    //         .map(this.toJSONResponse);
    // }
}
