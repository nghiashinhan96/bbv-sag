import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { BehaviorSubject, Observable, ReplaySubject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { uuid } from 'uuidv4';

import { User } from 'src/app/authentication/models/user.model';
import { AppStorageService } from './custom-local-storage.service';
import AffUtils from '../utils/aff-utils';
import { UserExportRequest } from 'src/app/home/pages/user-management/models/user-export-request.model';
import { environment } from 'src/environments/environment';
import { BrowserService } from './browser.service';
import { SpinnerService } from '../utils/spinner';
import { X_SAG_REQUEST_ID_HEADER_NAME } from '../conts/app.constant';
import { ChangePasswordModel } from 'src/app/admin/model/change-password.model';
import { ApiUtil } from '../utils/api.util';
// import { getToken, resetLocalStorage } from "./local-storage.util";
// import { ChangePasswordModel } from 'src/app/admin/model/change-password.model';

@Injectable()
export class UserService {
    private curUserSubj = new BehaviorSubject<User>(new User());
    public curUser = this.curUserSubj.asObservable();
    private isAuthedSubj = new ReplaySubject<boolean>(1);
    public isAuthed = this.isAuthedSubj.asObservable();

    constructor(
        private http: HttpClient,
        private browserService: BrowserService,
        private appStorage: AppStorageService // private jwtService: JwtService
    ) { }

    initialize() {
    }

    searchUser(body) {
        const url = `${environment.baseUrl}admin/users/search`;
        return this.http.post(url, body);
    }

    createUser(user) {
        const url = `${environment.baseUrl}admin/users/profile/create`;
        return this.http.post(url, user);
    }

    getUserDefaultInfo(affiliateShortName) {
        const url = `${environment.baseUrl}admin/users/profile/new?affiliateShortName=${affiliateShortName}`;
        return this.http.get(url);
    }

    showUserDetail(id) {
        const url = `${environment.baseUrl}admin/users/${id}/settings`;
        return this.http.get(url);
    }

    deleteUser(id) {
        const url = `${environment.baseUrl}admin/users/delete/${id}`;
        return this.http.get(url);
    }

    deleteDvseExternalUser(id) {
        const url = `${environment.baseUrl}admin/users/dvse/delete/${id}`;
        return this.http.get(url);
    }

    updatePassword(object) {
        const url = `${environment.baseUrl}admin/users/password/update`;
        return this.http.post(url, object);
    }

    updateUserSetting(userSetting) {
        const url = `${environment.baseUrl}admin/users/settings/update`;
        return this.http.post(url, userSetting);
    }

    setAuth(user: User) {
        this.curUserSubj.next(user);
        this.isAuthedSubj.next(true);
    }

    resetAuth() {
        this.appStorage.resetLocalStorage();
        this.curUserSubj.next(new User());
        this.isAuthedSubj.next(false);
    }

    login(): Observable<User> {
        const url = `${environment.baseUrl}admin/users/user/detail`
        return this.http.get(url)
            .pipe(
                map(
                    (data) => {
                        const res: any = data;
                        this.setAuth(res);
                        return res;
                    },
                    (error) => this.resetAuth()
                )
            );
    }

    getCurrentUser(): User {
        return this.curUserSubj.value;
    }

    logout() {
        this.resetAuth();
    }

    exportUsersByCriteria(request: UserExportRequest, callback?) {
        const path = `admin/users/export`;
        const now = new Date();
        now.getDate();
        const fileName = this.getUserExportFileName();
        // UserList_DDMMYYYY.CSV
        this.executeDownload(
            path,
            fileName,
            'vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            request,
            callback
        );
    }



    executeDownload(path: string, filename, extension, data, callback?) {
        // Change config to export file
        const headerConfigForExport: any = {};
        const accessToken = this.appStorage.appToken;
        headerConfigForExport.Authorization = `Bearer ${accessToken}`;
        headerConfigForExport[X_SAG_REQUEST_ID_HEADER_NAME] = uuid().toString();
        headerConfigForExport['Content-Type'] = 'application/json; charset=utf-8';
        headerConfigForExport.xhrFields = {
            responseType: 'arraybuffer',
        };

        const handleDownloadData = (response) => {
            const resData = response;
            if (
                this.browserService.isIEBrowser(navigator.appVersion) ||
                this.browserService.isEdgeBrowser(navigator.appVersion)
            ) {
                window.navigator.msSaveBlob(new Blob([resData]), filename);
            } else {
                this.browserService.downloadFile(resData, filename, extension);
            }
            SpinnerService.stop();
            return;
        };

        this.http
            .post(`${environment.baseUrl}${path}`, JSON.stringify(data), {
                headers: headerConfigForExport,
                responseType: 'arraybuffer'
            }).toPromise()
            .then(res => {
                handleDownloadData(res);
                if (callback) {
                    callback(res);
                }
            })
            .catch(er => {
                SpinnerService.stop();
                if (callback) {
                    callback(er);
                }
            });
    }

    private getUserExportFileName() {
        if (AffUtils.isCH()) {
            const now = new Date();
            return (
                'UserList_' +
                ('0' + now.getDate()).slice(-2) +
                ('0' + (now.getMonth() + 1)).slice(-2) +
                now.getFullYear() +
                '.CSV'
            );
        }
        return 'users.xls';
    }

    updateSystemAdminPassword(model: ChangePasswordModel) {
        let url = 'admin/users/system-admin/password/update';
        url = ApiUtil.getUrl(url);
        return this.http.post(url, model, { responseType: 'text' });
    }
}
