import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { map } from 'rxjs/internal/operators/map';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { SagMessageData } from 'sag-common';

@Injectable({
    providedIn: 'root'
})
export class ShoppingExportService {
    private baseUrl = environment.baseUrl;
    constructor(private http: HttpClient) { }

    exportFile(type: 'CSV' | 'SHORT_CSV' | 'EXCEL' | 'SHORT_EXCEL' | 'WORD' | 'BASKET_FILE', fileName: string, rows, params = {}) {
        const url = this.getUrl(type, fileName);
        return this.http.post(url, rows, {
            responseType: 'arraybuffer',
            params
        }).pipe(map(res => {
            saveAs(new Blob([res]), fileName);
            return null;
        }), catchError(err => {
            return of({
                type: 'ERROR',
                message: 'MESSAGES.GENERAL_ERROR'
            } as SagMessageData);
        }));
    }

    private getUrl(type: 'CSV' | 'SHORT_CSV' | 'EXCEL' | 'SHORT_EXCEL' | 'WORD' | 'BASKET_FILE', fileName: String) {
        switch (type) {
            case 'CSV':
                return `${this.baseUrl}cart/exportcsv`;
            case 'EXCEL':
                return `${this.baseUrl}cart/exportexcel`;
            case 'WORD':
                return `${this.baseUrl}cart/exportword`;
            case 'BASKET_FILE':
                return `${this.baseUrl}cart/export-basket`;
            case 'SHORT_CSV':
                return `${this.baseUrl}cart/export-short-csv`;
            case 'SHORT_EXCEL':
                return `${this.baseUrl}cart/export-short-excel?sheetName=${fileName.split(/_(.+)/)[0]}`;
        }
    }
}
