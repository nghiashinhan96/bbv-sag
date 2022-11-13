import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { map } from 'rxjs/internal/operators/map';
import { environment } from 'src/environments/environment';
import { User } from 'src/app/core/models/user.model';
import { X_SAG_REQUEST_ID_HEADER_NAME } from 'src/app/core/conts/app.constant';
import uuid from 'uuid/v4';
import { LoginInfo } from '../models/login-info.model';

@Injectable()
export class AuthService {
    private userSub$ = new BehaviorSubject<User>(null);
    private isAuthSub$ = new BehaviorSubject<boolean>(false);

    constructor(
        private http: HttpClient,
        private appStorage: AppStorageService,
    ) { }

    get user$() {
        return this.userSub$.asObservable();
    }

    get isAuth$() {
        return this.isAuthSub$.asObservable();
    }

    login(credentials: LoginInfo): Observable<any> {
        const url = `${environment.tokenUrl}oauth/token`;
        const headers = {
            'Accept-Language': this.appStorage.appLangCode || 'de',
            Authorization: `Basic ZXNob3AtYWRtaW46ZXNob3AtYWRtaW4tV0ZwR2hBdHp5`,
            'Content-Type': 'application/x-www-form-urlencoded',
            [X_SAG_REQUEST_ID_HEADER_NAME]: uuid(),
        };
        const formData = credentials.formData;

        return this.http.post(url, formData, { headers })
            .pipe(
                map(
                    (data: any) => {
                        this.appStorage.appToken = data.access_token;
                        return data;
                    }
                )
            );
    }

    resetSystemAdminPassword(body) {
        const url = `${environment.baseUrl}admin/users/system-admin/password/reset`;
        return this.http.post(url, body, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
    }

    logout() {
        this.resetAuth();
    }

    getUserDetail() {
        return this.http.get(`${environment.baseUrl}admin/users/user/detail`)
            .pipe(map(
                (data: User) => {
                    this.userSub$.next(data);
                    this.isAuthSub$.next(true);
                    return data;
                },
                error => this.resetAuth()
            ));
    }

    resetAuth() {
        this.appStorage.removeAll();
        this.userSub$.next(null);
    }
}
