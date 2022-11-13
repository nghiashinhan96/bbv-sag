import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BulbsModel } from '../models/bulbs.model';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable()
export class BulbsService {

    public static readonly CodesImgMap = {
        // tslint:disable: object-literal-key-quotes
        'C5W': 'c5w.png',
        'D1R': 'd1r.png',
        'D1S': 'd1s.png',
        'D2R': 'd2r.png',
        'D2S': 'd2s.png',
        'D3R': 'd1r.png',
        'D3S': 'd1s.png',
        'D4R': 'd2r.png',
        'D4S': 'd2s.png',
        'H1': 'h1.png',
        'H11': 'h11.png',
        'H11B': 'h11b.png',
        'H13': 'h13.png',
        'H15': 'h15.png',
        'H21W': 'h21w.png',
        'H27/2': 'h27w2.png',
        'H3': 'h3.png',
        'H4': 'h4.png',
        'H6W': 'h6w.png',
        'H7': 'h7.png',
        'H8': 'h8.png',
        'H8B': 'h11b.png',
        'H9': 'h9.png',
        'H9B': 'h9b.png',
        'HB2': 'h4.png',
        'HB3': 'hb3.png',
        'HB4': 'hb4.png',
        'HB4A': 'hb4a.png',
        'HS1': 'hs1.png',
        'P21/4W': 'p214w.png',
        'P21/5W': 'p214w.png',
        'P21W': 'p21w.png',
        'P27/7W': 'p277w.png',
        'P27W': 'p277w.png',
        'PY21W': 'py21w.png',
        'R10W': 'r10w.png',
        'R2': 'r2.png',
        'R5W': 'r5w.png',
        'T4W': 't4w.png',
        'W16W': 'w16w.png',
        'W2,3W': '2721.png',
        'W21/5W': 'w215w.png',
        'W21W': 'w21w.png',
        'W3W': 'w3w.png',
        'W5W': 'w3w.png',
        'WY21W': 'wy21w.png',
        'WY5W': 'wy5w.png'
    };

    private readonly OSRAM = 'OSRAM';
    private readonly HELLA = 'HELLA';

    private baseUrl = environment.baseUrl;

    constructor(
        private http: HttpClient
    ) { }

    getBulbsDataFilter(request): Observable<BulbsModel> {
        const url = `${this.baseUrl}search/bulbs`;
        return this.http.post(url, request).pipe(
            map((res) => new BulbsModel(res))
        );
    }

    isSupplierOsram(item) {
        return item.toUpperCase() === this.OSRAM;
    }

    isSupplierHella(item) {
        return item.toUpperCase() === this.HELLA;
    }

    // Sort supplier #2474
    // All -> Osram -> Hella -> the rest
    sortSupplier(supplier) {
        let sortArray = [];
        // 1: Push Osram
        const osramFilter = supplier.filter(item => this.isSupplierOsram(item));
        if (osramFilter.length) {
            sortArray.push(osramFilter[0]);
        }
        // 2: Push Hella
        const hellaFilter = supplier.filter(item => this.isSupplierHella(item));
        if (hellaFilter.length) {
            sortArray.push(hellaFilter[0]);
        }
        // 4 : The rest
        sortArray = sortArray.concat(supplier.filter(item => !this.isSupplierOsram(item) && !this.isSupplierHella(item)));
        return sortArray;
    }

    totalSpecialSupplier(supplier): number {
        let specialSupplier = supplier.filter(item => this.isSupplierOsram(item) || this.isSupplierHella(item)).length;
        // plus total 'Alle'
        return specialSupplier += 1;
    }
}
