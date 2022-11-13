import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { HttpClient } from '@angular/common/http';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { Observable } from 'rxjs/internal/Observable';
import { PriceFile } from './price-file.model';
import { map } from 'rxjs/internal/operators/map';
import { tap } from 'rxjs/internal/operators/tap';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-price-file',
    templateUrl: './price-file.component.html',
    styleUrls: ['./price-file.component.scss']
})
export class PriceFileComponent implements OnInit {
    files$: Observable<any>;
    isLoaded = false;
    constructor(
        public bsModalRef: BsModalRef,
        public http: HttpClient
    ) { }

    ngOnInit(): void {
        SpinnerService.start('#price-file-modal');
        const url = `${environment.baseUrl}price-file/list`;
        this.files$ = this.http.get(url).pipe(
            tap(() => {
                this.isLoaded = true;
                SpinnerService.stop('#price-file-modal');
            }),
            map((data: any) => {
                return data.map(d => new PriceFile(d));
            }),
            catchError(err => {
                this.isLoaded = true;
                SpinnerService.stop('#price-file-modal');
                return of(null);
            })
        );
    }

}
