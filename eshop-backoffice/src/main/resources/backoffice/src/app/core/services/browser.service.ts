import { Location } from '@angular/common';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { saveAs } from 'file-saver';

import { EDGE_BROWSER } from '../conts/app.constant';

@Injectable()
export class BrowserService {

    constructor(
        private http: HttpClient
    ) {
    }

    public backToPreviousPage(location: Location) {
        location.back();
    }

    public downloadFile(data, fileName, typeFile) {
        saveAs(new Blob([data], { type: 'application/' + typeFile + ';charset=charset=utf-8' }), fileName)
    }

    public isEdgeBrowser(appVersion: string) {
        return appVersion.toLowerCase().indexOf(EDGE_BROWSER.toLowerCase()) > 0;
    }

    public isIEBrowser(appVersion: string) {
        return appVersion.indexOf('.NET') > 0;
    }

}
