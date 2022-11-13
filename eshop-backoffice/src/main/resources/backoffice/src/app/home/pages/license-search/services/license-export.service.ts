import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { X_SAG_REQUEST_ID_HEADER_NAME } from 'src/app/core/conts/app.constant';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { catchError, map } from 'rxjs/operators';
import { of } from 'rxjs';
import { uuid } from 'uuidv4';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { EXPORT_LICENSE_MODE } from '../enums/license.enum';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Injectable()
export class LicenseExportService {
    constructor(
        private http: HttpClient,
        private appStorage: AppStorageService) {}

    exportLicenses(mode, body) {
        SpinnerService.start(); 
        const url = ApiUtil.getUrl(`admin/licenses/export-${mode}`);
        const fileName = this.getLicensesExportFileName(mode);
        const headerConfigForExport: any = {};
        const accessToken = this.appStorage.appToken;
        headerConfigForExport.Authorization = `Bearer ${accessToken}`;
        headerConfigForExport[X_SAG_REQUEST_ID_HEADER_NAME] = uuid().toString();
        headerConfigForExport['Content-Type'] = 'application/json; charset=utf-8';
        headerConfigForExport.xhrFields = { responseType: 'arraybuffer' };

        return this.http.post(url, body, {
            headers: headerConfigForExport,
            responseType: 'arraybuffer',
        }).pipe(map((res: any) => {
            saveAs(new Blob([res], {type: "text/plain;charset=utf-8"}), fileName);
            SpinnerService.stop();
            return null;
        }), catchError((error) => {
            const errorMess = ApiUtil.handleErrorReponse(error);
            return of({ messages: [errorMess], status: false });
        }));
    }

    private getLicensesExportFileName(mode) {
        const now = new Date();
        const extension: string = mode === EXPORT_LICENSE_MODE.CSV ? 'csv' : 'xlsx';
        return (
            'license_list_' +
            ('0' + now.getDate()).slice(-2) +
            ('0' + (now.getMonth() + 1)).slice(-2) + now.getFullYear() +
            `.${extension}`
        );
    }
}