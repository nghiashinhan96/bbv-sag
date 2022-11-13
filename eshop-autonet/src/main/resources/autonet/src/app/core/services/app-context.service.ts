import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { AppContextEnum } from '../enums/app-context.enum';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AppContextService {
    private baseurl = environment.baseUrl;
    constructor(private http: HttpClient) { }

    initAppContext() {
        const url = `${this.baseurl}context/init`;
        return this.http.post(url, null, { observe: 'body' }).pipe(catchError(err => {
            console.log('context init fail: ', err);
            return of(null);
        }));
    }

    updateVehicleContext(vehicleDoc?: string) {
        const body = {
            contextDto: {
                vehicleDoc
            }
        };
        const url = `${this.baseurl}context/update/${AppContextEnum.VEHICLE_CONTEXT}`;
        return this.http.post(url, body, { observe: 'body' }).pipe(catchError(err => {
            console.log('update vehicle context fail: ', err);
            return of(false);
        }));
    }
}
